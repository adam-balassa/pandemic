package Pandemic;

import java.io.Serializable;

public enum Virus implements Serializable {
    RED("f00", "red"),
    YELLOW("ff0", "yellow"),
    BLACK("000", "black"),
    BLUE("33F", "blue")
    ;

    private final String color;
    private final String name;

    Virus(String s, String n){
        color = s;
        name = n;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public static Virus getVirus(String name){
        for (Virus v: Virus.values()) {
            if(v.getName().equals(name))
                return v;
        }
        return null;
    }
}
