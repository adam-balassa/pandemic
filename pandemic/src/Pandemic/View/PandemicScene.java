package Pandemic.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;

import java.io.IOException;

abstract public class PandemicScene {

    protected Pane root;
    protected Scene scene;

    public PandemicScene(){
    }


    protected PandemicScene(String filename) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add("file:res/main_style.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene(){
        return scene;
    }

    protected Pane readElement(String filename){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pane();
    }
}
