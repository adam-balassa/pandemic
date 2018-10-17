package Pandemic;

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

    transient private Saver<Game> saver;

    Game(){
        player = 0;
        breakOuts = 0;
        infectionStatus = 1;
        shallInfect = true;
        this.antidotes = new HashSet<>();
    }


    /*******************************
     **** GAME INITIALIZATION *****
     ******************************/
    void start(Player[] players, int epidemicCards){
        this.players = players;
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
        save();
        players[player].round();
    }

    void setSaver(Saver saver){
        this.saver = saver;
    }

    private void nextRound(){
        save();
        player = player == players.length - 1 ? 0 : player + 1;
        players[player].round();
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
        List<Card> subDeck = null;

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
     * @param atlanta
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
     * @param e
     */
    private void endGame(String e){
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
        try {
            CityCard infection = (CityCard) infectionDeck.draw();
            Field city = infection.getCity();
            for (int i = 0; i < amount; i++) breakOuts = city.infect();
            infectionTrash.add(infection);

        } catch (EndOfGame endOfGame) { }
        addBreakOuts(breakOuts);
    }

    private void draw() throws EndOfGame{
        Card c = this.mainDeck.draw();
        c.draw(players[player]);

        c = this.mainDeck.draw();
        c.draw(players[player]);
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
    public void endRound() {
        try{
            draw();
            draw();

            players[player].finish();

            int numToInfect = infectionStatus <= 3 ? 2 : infectionStatus <= 5 ? 3 : 4;
            for (int i = 0; i < numToInfect; i++) {
                infect(1);
            }
            nextRound();
        }catch (EndOfGame e){
            endGame(e.getMessage());
        }
    }

    @Override
    public Character getCharacter(String name) {
        for (Player p : players) {
            if(p.character.equals(name)) return p.character;
        }
        return null;
    }

    @Override
    public void createAntidote(Virus color) throws UnnecessaryAction {
        if(!antidotes.add(color))
            throw new UnnecessaryAction("Antidote is already made");
    }

    @Override
    public void addBreakOuts(int breakOuts) throws EndOfGame {
        this.breakOuts += breakOuts;
        if(breakOuts > 7) throw new EndOfGame("Too many breakouts");
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
        Game loaded = saver.load();
        loaded.setSaver(saver);
        loaded.nextRound();
    }


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
    }

    private void infectLastCity() throws EndOfGame{
        CityCard c = infectionDeck.drawLast();
        infectionTrash.add(c);
        Field city = c.getCity();

        int breakOuts = 0;
        for (int i = 0; i < 3; i++) breakOuts += city.infect();
        addBreakOuts(breakOuts);
    }
}
