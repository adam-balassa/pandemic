package Pandemic.Players;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Characters.Character;
import Pandemic.Exceptions.*;
import Pandemic.Core.IGame;
import Pandemic.Table.Field;
import Pandemic.Core.Virus;

import java.io.*;
import java.util.*;

public class ConsolePlayer extends Player implements Serializable  {
    transient private BufferedReader inputStreamReader;
    transient private PrintWriter outputStreamWriter;

    public ConsolePlayer(IGame g, InputStream is, PrintStream os) {
        super(g);
        inputStreamReader = new BufferedReader(new InputStreamReader(is));
        outputStreamWriter = new PrintWriter(new OutputStreamWriter(os));
    }


    /*******************************
     ***** USER INTERACTION *****
     ******************************/

    /**
     * Manages reading commands till user has actions
     */
    private void readCommands() throws EndOfGame{
        while(true)
            try{
                alert("Remaining actions: " + this.getRemainingActions());
                alert("Type in your command");
                List<String> cmdStr = readCommand();
                executeCommands(getCommand(cmdStr), cmdStr);
            }catch (EndRound e){
                endRound();
                break;
            }catch (InvalidCommand e){
                alert(e.getMessage());
            }
    }

    /**
     * The only function that reads user input
     * @returns the command formatted to a List
     */
    private List<String> readCommand(){
        try {
            String str = inputStreamReader.readLine();
            int endCommand = !str.contains(" ") ? str.length() : str.indexOf(" ");
            String command = str.substring(0, endCommand);
            List<String> cmdArr = new ArrayList<>();
            cmdArr.add(command);

            String parametersString = "";
            try{ parametersString = str.substring(endCommand + 1); } catch (Exception e){}

            String[] parameters = parametersString.split(", ");
            if(!parameters[0].equals("")) cmdArr.addAll(Arrays.asList(parameters));

            return cmdArr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gives meaning to each command
     * Manages to translate commands into character actions, and manages errors
     * @param command
     * @param commandString a formatted list of the users command string
     * @throws InvalidParameter when the command was correct, but was given uninterpretable parameters
     * @throws EndRound
     */
    private void executeCommands(Command command, List<String> commandString) throws InvalidCommand, EndRound {
        int usedActions = 0;
        try{
            switch (command){
                case MOVE:
                    Field whereTo = game.getField(commandString.get(1));
                    if(whereTo == null) throw new InvalidParameter(commandString.get(1) + " is not a city");
                    usedActions += character.move(whereTo);
                    break;
                case MOVECHARACTER:
                    Field where = game.getField(commandString.get(1));
                    Character whom = this.getCharacter(commandString.get(2));
                    if(where == null) throw new InvalidParameter(commandString.get(1) + " is not a city");
                    if(whom == null) throw new InvalidParameter(commandString.get(2) + " is not a character in game");
                    usedActions += character.move(where, whom);
                    break;
                case BUILD:
                    usedActions += character.build();
                    break;
                case CLEAN:
                    usedActions += character.clean();
                    break;
                case CLEANCOLOR:
                    Virus color = Virus.getVirus(commandString.get(1));
                    if(color == null) throw new InvalidParameter(commandString.get(1) + " is not a virus color");
                    usedActions += character.clean(color);
                    break;
                case GIVECARD:
                    CityCard cardToGive = (CityCard)hasCard(game.getField(commandString.get(1)));
                    if(cardToGive == null) throw new InvalidParameter(commandString.get(1) + " is not in yours to give");
                    Character toWhom = this.getCharacter(commandString.get(2));
                    if(toWhom == null) throw new InvalidParameter(commandString.get(2) + " is not a character in game");
                    usedActions += character.giveCard(toWhom, cardToGive);
                    break;
                case GETCARD:
                    CityCard cardToGet = (CityCard)hasCard(game.getField(commandString.get(1)));
                    if(cardToGet == null) throw new InvalidParameter(commandString.get(1) + " is not in yours to give");
                    Character fromWho = this.getCharacter(commandString.get(2));
                    if(fromWho == null) throw new InvalidParameter(commandString.get(2) + " is not a character in game");
                    usedActions += fromWho.giveCard(this.character, cardToGet);
                case DRAWEVENT:
                    List<EventCard> eventCards = game.getEvents();
                    alert("Choose one from the following cards: ");
                    showCards(eventCards);
                    showCards();
                    List<String> choice = readCommand();
                    if(getCommand(choice) != Command.CHOOSE)
                        throw new InvalidCommand("Please use the 'choose' command.");
                    boolean found = false;
                    for (EventCard c: eventCards) if(c.getName().equals(choice)){
                        game.getEvent(c);
                        usedActions += character.getEvent(c);
                        found = true;
                        break;
                    }
                    if(!found) throw new InvalidParameter("The chosen card wasn't from the following");
                    break;
                case ANTIDOTE:
                    usedActions += character.antidote();
                    break;
                case EVENT:
                    EventCard event = (EventCard) hasCard(commandString.get(1));
                    if(event == null) throw new InvalidParameter("You don't have a card called" + commandString.get(1));
                    character.playEvent(event);
                    break;
                case HELP:
                    alert(Command.getHelp());
                    break;
                case PASS:
                    throw new EndRound();
                case STATUS:
                    alert("You are " + this.getName());
                    alert("Your character is " + this.character.getName());
                    alert("You are in " + this.character.getField().getName() + ", which is infected by: ");
                    for(Map.Entry<Virus, Integer> pair: this.character.getField().getInfection().entrySet())
                        alert("\t" + pair.getValue() + " " + pair.getKey().getName());
                    alert("Your neighbours are ");
                    for (Field f :this.character.getField().getNeighbours()) alert("\t" + f.getName());

                    alert("Your cards are ");
                    for (Card c: this.cards)
                        alert("\t" + c.getName());
                    break;
                case TABLE:
                    for(Field field: game.getFields().values()){
                        StringBuilder str = new StringBuilder();
                        str.append(field.getName() + (field.hasStation() ? "*" : "") + "\t\t");
                        for(Map.Entry<Virus, Integer> pair: field.getInfection().entrySet())
                            if(pair.getValue() > 0) str.append("\t" + pair.getValue() + " " + pair.getKey().getName());
                        str.append("\t\t(");
                        for (Field f : field.getNeighbours())
                            str.append(" " + f.getName());
                        str.append(")");
                        alert(str.toString());
                    }
                    break;
                case UNDO:
                    game.undo();
                    return;
                default: return;
            }
        }
        catch (AmbigousAction e){
            chooseCard();
            executeCommands(command, commandString);
            chosenCard = null;
        }
        catch(CannotPerformAction e){
            alert(e.getMessage());
        }

        useAction(usedActions);
    }

    /**
     * Translates the first element of a command list to a Command Enumeration
     * @param commandString
     * @return
     * @throws InvalidCommand
     */
    private Command getCommand(List<String> commandString) throws InvalidCommand{
        boolean found = false;
        for (Command command : Command.values()) {
            if(command.commandName.equals(commandString.get(0))){
                if(command.parameters == commandString.size() - 1)
                    return command;
                found = true;
            }
        }
        if(found) throw new InvalidParameter("The command you typed expects a different amount of parameters");
        throw new InvalidCommand("The command you typed is syntactically wrong");
    }

    /**
     * Called when an ambiguous action error occured, makes the user to specify their command
     */
    private void chooseCard(){
        alert("Choose one from the following cards: ");
        showCards();
        List<String> command = readCommand();
        try {
            if(getCommand(command) != Command.CHOOSE)
                throw new InvalidCommand("Please use the 'choose' command.");
            Card choice = hasCard(command.get(1));
            if(choice == null)
                throw new InvalidParameter("The selected card doesn't belong to the following");
            chosenCard = choice;
        } catch (InvalidCommand invalidCommand) {
            alert(invalidCommand.getMessage());
            chooseCard();
        }
    }
    
    private Character getCharacter(String name){
        Player[] players = game.getPlayers();
        for(Player p: players)
            if(p.character.getName().equals(name)) return p.character;
        return null;
    }


    /*******************************
     ***** USER COMMUNICATION *****
     ******************************/
    private void showCards(){
        for (Card card: this.cards ) {
            alert(card.getName());
        }
    }

    private void showCards(List<? extends Card> cards){
        for (Card card: cards ) {
            alert(card.getName());
        }
    }

    private void alert(String str){
        this.outputStreamWriter.println(str);
        outputStreamWriter.flush();
    }

    /*******************************
     ****** BASIC OVERLOADS *******
     ******************************/
    @Override
    public void round() throws EndOfGame {
        super.round();
        alert("New round started for " + this.getName());
        readCommands();
    }

    @Override
    public void endRound() throws EndOfGame {
        alert("Round ended for " + getName());
        super.endRound();
    }

    @Override
    public void setCharacter(Character c) {
        super.setCharacter(c);
        alert(this.getName() + " is " + c.getName());
    }

    @Override
    public void add(Card c) {
        super.add(c);
        alert(this.getName() + " got " + c.getName());
    }

    /**
     * called when player holds too many cards in hand
     */
    @Override
    protected void hasToDrop() {
        while(true){
            alert("You must drop a card, or play an event.");
            List<String> str = readCommand();
            try{
                Command cmd = getCommand(str);
                switch (cmd){
                    case TABLE:
                    case EVENT:
                    case STATUS:
                    case HELP:
                        executeCommands(cmd, str);
                        return;
                    case CHOOSE:
                        Card chosen = this.hasCard(str.get(1));
                        this.drop(chosen);
                        return;
                    default: break;
                }
            }catch (PandemicException e){
                alert(e.getMessage());
            }
        }
    }


    /*******************************
     ****** EVENT OVERLOADS *******
     ******************************/

    @Override
    public void replaceCards(List<CityCard> c) {
        game.replaceCards(c);
    }

    @Override
    public List<CityCard> forecast() {
        List<CityCard> cards = game.forecast();
        List<CityCard> newOrder = new ArrayList<>();
        List<String> cmd = null;
        while(cards.size() > 1){
            alert("Select the next card in the order");
            showCards(cards);
            cmd = readCommand();
            try{
                if(getCommand(cmd) != Command.CHOOSE) continue;
                for(CityCard card : cards) if(card.getName().equals(cmd.get(1))){
                    newOrder.add(card);
                    cards.remove(card);
                    break;
                }
            }
            catch(PandemicException e){}
        }
        newOrder.add(cards.remove(0));
        return newOrder;
    }

    @Override
    public List<CityCard> getTrash() {
        List<CityCard> cards = game.getTrash();
        List<String> cmd = null;
        Card chosen = null;
        while(true){
            alert("Select the card to discard");
            showCards(cards);
            cmd = readCommand();
            try{
                if(getCommand(cmd) != Command.CHOOSE) continue;
                for(Iterator<CityCard> card = cards.iterator(); card.hasNext();)
                    if(card.next().getName().equals(cmd.get(1))){
                        card.remove();
                        return cards;
                    }
            }
            catch(PandemicException e){}
        }
    }

    @Override
    public void epidemic() throws EndOfGame {
        alert("Epidemic!!!");
        super.epidemic();
    }

    @Override
    public void playEvent(EventCard c) throws CannotPerformAction{
        alert(c.getDescription());
        playEventCard(c, new EventCard.EventOptions.Builder().setPlayer(this));
    }

    private void playEventCard(EventCard card, EventCard.EventOptions.Builder options) throws CannotPerformAction{
        try {
            card.play(options.build());
            this.drop(card);
        } catch (CannotPerformAction cannotPerformAction) {
            alert("Please specify this event");
            alert(cannotPerformAction.getMessage());

            List<String> newOptions = readCommand();
            Character c = this.getCharacter(newOptions.get(0));
            if(c != null) {
                playEventCard(card, options.setCharacter(c));
                return;
            }

            Field f = game.getField(newOptions.get(0));
            if(f != null) {
                playEventCard(card, options.setField(f));
                return;
            }
            throw cannotPerformAction;
        }
    }



    /*******************************
     ********* COMMAND ENUM *******
     ******************************/
    private enum Command{
        MOVE("move", 1),
        MOVECHARACTER("move", 2),
        EVENT("play", 1),
        BUILD("build", 0),
        ANTIDOTE("antidote", 0),
        CLEAN("clean", 0),
        CLEANCOLOR("clean", 1),
        DRAWEVENT("drawevent", 0),
        GIVECARD("give", 2),
        GETCARD("get", 2),
        CHOOSE("choose", 1),
        HELP("help", 0),
        PASS("pass", 0),
        STATUS("status", 0),
        TABLE("table", 0),
        UNDO("undo", 0);

        public final String commandName;
        public final int parameters;
        Command(String name, int params){
            commandName = name;
            parameters = params;
        }

        public static String getHelp(){
            StringBuilder str = new StringBuilder("");
            for (Command c: Command.values()) {
                str.append(c.commandName + ": " + c.parameters +  " parameter(s)\n");
            }
            return str.toString();
        }
    }
}
