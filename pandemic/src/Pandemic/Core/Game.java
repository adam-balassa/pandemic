package Pandemic.Core;

import Pandemic.Characters.Character;
import Pandemic.Characters.CharacterType;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Players.*;
import Pandemic.Table.*;
import Pandemic.Cards.*;

import java.io.Serializable;
import java.util.*;

public class Game implements IGame, Serializable{
    private Table table;
    private Deck mainDeck;
    private InfectionDeck infectionDeck;
    private MainTrash mainTrash;
    private InfectionTrash infectionTrash;
    private Set<Virus> antidotes;
    private Player[] players;

    private int infectionStatus;
    private int breakOuts;
    private int player;
    private boolean shallInfect;

    transient private Saver saver;

    Game(Saver saver){
        player = 0;
        breakOuts = 0;
        infectionStatus = 1;
        shallInfect = true;
        this.antidotes = new HashSet<>();
        this.saver = saver;
    }


    /*******************************
     **** GAME INITIALIZATION *****
     ******************************/
    void initialize(int epidemicCards){
        table = new Table();
        Field atlanta = table.getField("Atlanta");

        List<Card> cards = table.getCityCards();
        Collections.shuffle(cards);
        infectionDeck = new InfectionDeck(cards);
        infectionTrash = new InfectionTrash(new ArrayList<>());
        mainTrash = new MainTrash(new ArrayList<>());

        this.createCharacters(atlanta);
        try {
            atlanta.build();
        } catch (UnnecessaryAction unnecessaryAction) { }

        createMainDeck(epidemicCards);
        setInfection();
    }

    void start(){
        while(true){
            try{
                shallInfect = true;
                save();
                this.nextRound();
                player = player == players.length - 1 ? 0 : player + 1;
            }
            catch(EndOfGame endOfGame){
                endGame();
            }
        }
    }

    void setPlayers(Player[] players){
        this.players = players;
    }

    private void nextRound() throws EndOfGame{
        players[player].round();
    }

    public void reconstruct(Game previousStatus){
        table = previousStatus.table;
        mainDeck = previousStatus.mainDeck;
        infectionDeck = previousStatus.infectionDeck;
        mainTrash = previousStatus.mainTrash;
        infectionTrash = previousStatus.infectionTrash;
        antidotes = previousStatus.antidotes;
        for(int i = 0; i < players.length; i++)
            players[i].reconstruct(previousStatus.players[i]);

        infectionStatus = previousStatus.infectionStatus;
        breakOuts = previousStatus.breakOuts;
        player = previousStatus.player;
        shallInfect = previousStatus.shallInfect;
    }

    /**
     * creates main deck and deals cards to players
     * @param epidemicCards how many epidemic cards shall be added
     */
    private void createMainDeck(int epidemicCards){
        List<Card> cards = table.getCityCards();
        cards.addAll(EventCard.EventTypes.getAllEventCards());

        Collections.shuffle(cards);

        dealCards(cards);

        int sizeOfMidDeck = cards.size() / epidemicCards;
        List<Card> subDeck;

        for (int i = 0; i < epidemicCards; i++) {
            subDeck = cards.subList(i * sizeOfMidDeck, (i + 1) * sizeOfMidDeck);
            subDeck.add(new EpidemicCard());
            Collections.shuffle(subDeck);
        }

        mainDeck = new Deck(cards);
    }

    /**
     * deals cards from the given List to game's players
     */
    private void dealCards(List<Card> cards){
        for (Player p: players)
            for (int i = 0; i < 6 - players.length; i++)
                p.add(cards.remove(i));
    }

    /**
     * randomly selects characters, and associates them to players
     * @param atlanta must be passed to place the characters
     */
    private void createCharacters(Field atlanta){
        List<CharacterType> characterTypes = Arrays.asList(CharacterType.values());
        Collections.shuffle(characterTypes);
        for (int i = 0; i < players.length; i++) {
            players[i].setCharacter(characterTypes.get(i).createCharacter(players[i], atlanta));
        }
    }

    /**
     * sets the start infection level on table
     */
    private void setInfection(){
        try{
            infect(3);
            infect(3);
            infect(3);

            infect(2);
            infect(2);
            infect(2);

            infect(1);
            infect(1);
            infect(1);
        }catch (EndOfGame e){}
    }

    /**
     * Called when players lost
     */
    private void endGame(){
        System.out.println("End of game");
        System.exit(0);
    }

    private void save(){
        saver.save();
    }


    /*******************************
     ***** GAME RESPONIBILITIES *****
     ******************************/
    private void infect(int amount)throws EndOfGame{
        int breakOuts = 0;
        CityCard infection = (CityCard) infectionDeck.draw();
        Field city = infection.getCity();
        for (int i = 0; i < amount; i++) breakOuts = city.infect();
        infectionTrash.add(infection);
        players[player].showInfection(infection);
        addBreakOuts(breakOuts);
    }

    private void draw() throws EndOfGame{
        Card c = this.mainDeck.draw();
        c.draw(players[player]);
    }

    private void addBreakOuts(int breakOuts) throws EndOfGame {
        this.breakOuts += breakOuts;
        if(breakOuts > 7) throw new EndOfGame("Too many breakouts");
    }

    /**********************
     ******* IGAME *******
     *********************/
    @Override
    public void drop(Card c) {
        mainTrash.add(c);
    }

    @Override
    public boolean isAntidoteMade(Virus v) {
        for (Virus antidote: antidotes) {
            if(antidote == v) return true;
        }
        return false;
    }

    @Override
    public void endRound() throws EndOfGame{
        draw();
        draw();

        players[player].finish();

        if(shallInfect){
            int numToInfect = infectionStatus <= 3 ? 2 : infectionStatus <= 5 ? 3 : 4;
            for (int i = 0; i < numToInfect; i++) {
                infect(1);
            }
        }
    }

    @Override
    public void createAntidote(Virus color) throws UnnecessaryAction {
        if(!antidotes.add(color))
            throw new UnnecessaryAction("Antidote is already made");
    }

    @Override
    public Field getField(String cityName) {
        return table.getField(cityName);
    }

    @Override
    public List<EventCard> getEvents() {
        return mainTrash.getEvents();
    }

    @Override
    public void getEvent(EventCard eventCard) {
        mainTrash.drawEvent(eventCard);
    }

    @Override
    public Map<String, Field> getFields() {
        return table.getFields();
    }

    @Override
    public void undo() {
        Game previousStatus = saver.load();
        this.reconstruct(previousStatus);
    }

    @Override
    public Player[] getPlayers() {
        return players;
    }

    @Override
    public int getInfectionStatus() {
        return infectionStatus;
    }

    @Override
    public int getBreakOuts() {
        return breakOuts;
    }

    @Override
    public Player getCurrentPlayer(){ return players[player]; }


    /**********************
     ******* EVENTS *******
     *********************/
    @Override
    public void dontInfect() {
        shallInfect = false;
    }

    @Override
    public List<CityCard> forecast() {
        List<CityCard> cards = new ArrayList<>(6);
        try{
            for (int i = 0; i < 6; i++)
                cards.add((CityCard) infectionDeck.draw());
        }
        catch (EndOfGame endOfGame) {}
        finally {
            return cards;
        }
    }

    @Override
    public void replaceCards(List<CityCard> cards) {
        for (Card card: cards)
            infectionDeck.add(card);
    }

    @Override
    public List<CityCard> getTrash() {
        return infectionTrash.getCards();
    }

    @Override
    public void epidemic() throws EndOfGame {
        infectLastCity();
        infectionStatus++;
        infectionDeck.concat(infectionTrash);
        infectionTrash.empty();
    }

    private void infectLastCity() throws EndOfGame{
        CityCard c = infectionDeck.drawLast();
        infectionTrash.add(c);
        Field city = c.getCity();

        int breakOuts = 0;
        for (int i = 0; i < 3; i++) breakOuts += city.infect();
        addBreakOuts(breakOuts);
    }

    int getNumberOfPlayers(){
        return players.length;
    }
}
