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

    public Game(Saver saver){
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
    public void initialize(int epidemicCards){
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

    /**
     * Called when the main loop shall be started
     */
    void start(){
        while(true){
            try{
                this.nextRound();
                this.finishRound();
            }
            catch(EndOfGame endOfGame){
                endGame();
            }
        }
    }

    public void setPlayers(Player[] players){
        this.players = players;
    }

    /**
     * Prepares the next round for the next player
     * @throws EndOfGame
     */
    public void nextRound() throws EndOfGame{
        shallInfect = true;
        save();
        players[player].round();
    }

    /**
     * Copies the propriate properties of a Game in an other (previous) status
     * @param previousStatus
     */
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

    /**
     * Saves the current status of the game
     */
    private void save(){
        saver.save();
    }

    /**
     * Chooses a random city, and infects it with the given amount of virus
     * @param amount
     * @throws EndOfGame
     */
    private void infect(int amount)throws EndOfGame{
        int breakOuts = 0;
        CityCard infection = (CityCard) infectionDeck.draw();
        Field city = infection.getCity();
        for (int i = 0; i < amount; i++) breakOuts = city.infect();
        infectionTrash.add(infection);
        players[player].showInfection(infection, infectionStatus <= 3 ? 2 : infectionStatus <= 5 ? 3 : 4);
        addBreakOuts(breakOuts);
    }

    /**
     * Draws a card from the main deck and gives it to the player on round
     * @throws EndOfGame
     */
    private void draw() throws EndOfGame{
        Card c = this.mainDeck.draw();
        c.draw(players[player]);
    }

    /**
     * Modifies the amount of breakouts
     * @param breakOuts
     * @throws EndOfGame
     */
    private void addBreakOuts(int breakOuts) throws EndOfGame {
        this.breakOuts += breakOuts;
        if(breakOuts > 7) throw new EndOfGame("Too many breakouts");
    }

    /**
     * Adds the given card to the main trash
     * @param c
     */
    @Override
    public void drop(Card c) {
        mainTrash.add(c);
    }

    /**
     * @param v
     * @returns true if the antidote is made for the given type of Virus
     */
    @Override
    public boolean isAntidoteMade(Virus v) {
        for (Virus antidote: antidotes) {
            if(antidote == v) return true;
        }
        return false;
    }

    /**
     * Draws cards for the player on round
     * @throws EndOfGame
     */
    @Override
    public void endRound() throws EndOfGame{
        draw();
        draw();

        players[player].finish();
    }

    /**
     * Sets the new player and infects increases the infection level of the board
     * @throws EndOfGame
     */
    @Override
    public void finishRound() throws EndOfGame {
        if(shallInfect){
            int numToInfect = infectionStatus <= 3 ? 2 : infectionStatus <= 5 ? 3 : 4;
            for (int i = 0; i < numToInfect; i++) {
                infect(1);
            }
        }

        player = player == players.length - 1 ? 0 : player + 1;
    }

    /**
     * When called, the antidote from the sepcified color will be made
     * @param color
     * @throws UnnecessaryAction if it's already made
     */
    @Override
    public void createAntidote(Virus color) throws UnnecessaryAction {
        if(!antidotes.add(color))
            throw new UnnecessaryAction("Antidote is already made");
    }

    @Override
    public Field getField(String cityName) {
        return table.getField(cityName);
    }

    /**
     * @returns the events from the maintrash
     */
    @Override
    public List<EventCard> getEvents() {
        return mainTrash.getEvents();
    }

    /**
     * draws an eventCard from the mainTrash
     * @param eventCard
     */
    @Override
    public void getEvent(EventCard eventCard) {
        mainTrash.drawEvent(eventCard);
    }

    /**
     * draws an eventCard from the mainTrash
     * @param eventCard
     */
    @Override
    public void drawEvent(EventCard eventCard) {
        mainTrash.drawEvent(eventCard);
    }

    /**
     * returns a Map of the board with the Fields name as keys
     * @return
     */
    @Override
    public Map<String, Field> getFields() {
        return table.getFields();
    }

    /**
     * Loads the previously saved status of the game
     */
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

    @Override
    public Set<Virus> getAntidotes() {
        return antidotes;
    }

    /**
     * When called, the infection level of the board will not be increased this round
     */
    @Override
    public void dontInfect() {
        shallInfect = false;
    }

    /**
     * Executes forecast event
     * @return
     */
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

    /**
     * After forecast, the reordered cards shall be replaced to the infectiondeck
     * @param cards
     */
    @Override
    public void replaceCards(List<CityCard> cards) {
        for (Card card: cards)
            infectionDeck.add(card);
    }

    @Override
    public InfectionTrash getTrash() {
        return infectionTrash;
    }

    /**
     * Called when an epidemic card is drawn
     * @throws EndOfGame
     */
    @Override
    public void epidemic() throws EndOfGame {
        infectLastCity();
        infectionStatus++;
        infectionDeck.concat(infectionTrash);
        infectionTrash.empty();
    }

    /**
     * Draws the last city of the infection deck and infects it with 3 new virus
     * @throws EndOfGame
     */
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
