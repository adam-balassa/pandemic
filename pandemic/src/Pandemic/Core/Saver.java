package Pandemic.Core;

import java.io.*;

public class Saver <Type extends Serializable> {
    private String filename;
    private ObjectOutputStream saver;
    private ObjectInputStream loader;
    private Type object;
    Saver(Type object, String filename){
        try {
            this.object = object;
            this.filename = filename;
            saver = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            loader = new ObjectInputStream(new FileInputStream(new File(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save(){
        try {
            new FileWriter(this.filename).close();
            saver.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Type load(){
        try {
            object = (Type)loader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            return object;
        }
    }
}
