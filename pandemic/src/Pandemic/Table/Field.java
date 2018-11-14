package Pandemic.Table;

import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Core.Virus;

import java.io.Serializable;
import java.util.*;

import Pandemic.Characters.Character;
import javafx.geometry.Point2D;

public class Field implements Serializable {
    private final String name;
    private boolean brokeOut;
    private boolean station;
    private boolean quarantine;
    private List<Character> characters;
    private List<Virus> infection;
    private List<Field> neighbours;
    private Virus color;
    private Coordinates position;

    public Field(String name, Virus color, Coordinates pos){
        this.name = name;
        this.color = color;
        this.position = pos;
        brokeOut = false;
        station = false;
        quarantine = false;
        characters = new ArrayList<>();
        infection = new ArrayList<>();
        neighbours = new ArrayList<>();
    }

    public Field connect(Field field){
        connectToMe(field);
        field.connectToMe(this);
        return this;
    }

    private void connectToMe(Field field){
        neighbours.add(field);
    }

    public void step(Character c){
        this.characters.add(c);
    }

    public void stepFrom(Character c){
        this.characters.remove(c);
    }

    public int infect(){
        int breakOuts = infect(this.color);
        this.endPhase();
        return breakOuts;
    }

    private int infect(Virus v){
        if(this.quarantine) return 0;

        int num = count(v);
        if(num == 3)
            return breakOut(v);
        infection.add(v);

        return 0;
    }

    private int breakOut(Virus v){
        if(this.brokeOut) return 0;
        this.brokeOut = true;
        int outBreaks = 1;
        for (Field f : neighbours) {
            outBreaks += f.infect(v);
        }
        return outBreaks;
    }

    private void endPhase(){
        if(!this.brokeOut) return;
        this.brokeOut = false;
        for(Field neighbour : neighbours)
            neighbour.endPhase();
    }

    public void clear() throws UnnecessaryAction {
        clear(this.color);
    }

    public void clear(Virus v) throws UnnecessaryAction {
        if(!infection.remove(v))
            throw new UnnecessaryAction("There is no " + v.getName() + " virus in " + name);
    }

    public void clearAll(Virus v) throws UnnecessaryAction{
        boolean found = false;
        for(Iterator<Virus> virus = infection.iterator(); virus.hasNext();)
            if(virus.next() == v) {
                virus.remove();
                found = true;
            }
        if(!found) throw new UnnecessaryAction("There is no " + v.getName() + " virus in " + name);
    }

    public void build()throws UnnecessaryAction{
        if(station) throw new UnnecessaryAction("A station is already built in " + this.name);
        station = true;
    }

    public Field[] getNeighbours(){
        Field[] fields = new Field[neighbours.size()];
        return neighbours.toArray(fields);
    }

    public void setQuarantine(){
        this.quarantine = true;
    }

    public void removeQuarantine(){
        this.quarantine = false;
    }

    private int count(Virus v){
        int i = 0;
        for (Virus virus : infection)
            if(v == virus) i++;
        return i;
    }

    public String getName(){
        return name;
    }

    public boolean hasNeighbour(Field f){
        for (Field field: this.neighbours) {
            if(f == field) return true;
        }
        return false;
    }

    public boolean hasStation(){
        return this.station;
    }

    public Virus getColor(){
        return this.color;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Map<Virus, Integer> getInfection(){
        Map<Virus, Integer> infections = new HashMap<>();
        infections.put(Virus.BLUE, this.count(Virus.BLUE));
        infections.put(Virus.RED, this.count(Virus.RED));
        infections.put(Virus.BLACK, this.count(Virus.BLACK));
        infections.put(Virus.YELLOW, this.count(Virus.YELLOW));
        return infections;
    }

    public Coordinates getPosition(){
        return this.position;
    }

    public static class Coordinates implements Serializable{
        public double x;
        public double y;
        public Coordinates(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
}
