package Pandemic.View.Components;

import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.PandemicException;
import Pandemic.View.Effect;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.awt.event.ActionEvent;

public class AlertBoxComponent extends StackPane {
    private TitledPane alertBoxTitle;
    private Label alertBoxText;
    private Label alertBoxNote;
    private Button alertBoxButton;

    public AlertBoxComponent(){
        this.setVisible(false);
        this.setPrefHeight(150);
        this.setPrefWidth(200);
            StackPane s1 = new StackPane();
            s1.setMaxHeight(250); s1.setMaxWidth(450); s1.setStyle("-fx-border-radius: 20px;");
                alertBoxTitle = new TitledPane();
                alertBoxTitle.setAlignment(Pos.CENTER);
                alertBoxTitle.setCollapsible(false);
                alertBoxTitle.setContentDisplay(ContentDisplay.CENTER);
                alertBoxTitle.setPrefWidth(200);
                alertBoxTitle.setStyle("-fx-border-radius: 20px;");
                    BorderPane b1 = new BorderPane();
                    b1.setPrefHeight(200); b1.setPrefWidth(200); b1.setPadding(new Insets(0, 0, -1, 0));
                        ToolBar t1 = new ToolBar();
                        t1.setPrefHeight(40); t1.setPrefWidth(200);
                            AnchorPane a1 = new AnchorPane();
                            a1.setPrefHeight(30); a1.setPrefWidth(431);
                                alertBoxButton = new Button("OK");
                                alertBoxButton.setOnAction(e -> { Transition t = Effect.fadeOut(this); t.play();});
                            a1.getChildren().add(alertBoxButton);
                        t1.getItems().add(a1);
                    b1.setBottom(t1);
                        VBox v1 = new VBox();
                        v1.setAlignment(Pos.CENTER_LEFT);
                        v1.setPrefHeight(200); v1.setPrefWidth(100); v1.setPadding(new Insets(1));
                            alertBoxText = new Label();
                            alertBoxText.setPrefHeight(80); alertBoxText.setPrefWidth(452); alertBoxText.setWrapText(true);
                            alertBoxText.setFont(Font.font(18)); alertBoxText.setPadding(new Insets(20));
                            Separator s2 = new Separator();
                            s2.setPrefWidth(200);
                            alertBoxNote = new Label();
                            alertBoxNote.setPrefHeight(77); alertBoxNote.setPrefWidth(448); alertBoxNote.setWrapText(true);
                            alertBoxNote.setPadding(new Insets(20));
                        v1.getChildren().addAll(alertBoxText, s2, alertBoxNote);
                    b1.setCenter(v1);
                alertBoxTitle.setContent(b1);
            s1.getChildren().add(alertBoxTitle);
        this.getChildren().add(s1);
    }

    public Button getButton(){
        return alertBoxButton;
    }

    public void alert(PandemicException e, String title){
        alertBoxTitle.setText(title);
        alertBoxText.setText(e.getMessage());
        alertBoxNote.setText(e.getHelp());
        Effect.fadeIn(this);
    }
}
