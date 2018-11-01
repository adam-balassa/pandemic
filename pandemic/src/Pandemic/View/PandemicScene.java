package Pandemic.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

abstract public class PandemicScene {

    protected Pane root;
    protected Scene scene;
    protected PandemicScene(String filename) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene(){
        return scene;
    }
}
