package views;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

import FlightModel.FlightExplorer;



/**
 * Responsible for displaying the Flight Explorer UI.
 */
public class FlightExplorerView {
    FlightExplorer explorer;
    Stage stage;
    //add buttons
    GridPane grid = new GridPane();
    //possible labels
    // create vbox
    Label title;
    TextField searchBar = new TextField();
    HBox searchBox;
    Scene scene;
    ImageView imageView;
    VBox hold;
    VBox titleSearch;
    /**
     * Constructor
     */
    public FlightExplorerView(FlightExplorer explorer, Stage stage) { // contructor for view, this is mainly javafx
        this.explorer = explorer;
        this.stage = stage;

        //add buttons
        //add labels
        //add textfield
        //add vbox
        //add grid
        //add scene
        //add stage
        initUI();
    }

    public void initUI() {

        this.searchBar.setPromptText("Search for a flight");
        this.searchBox = new HBox(this.searchBar);
        this.searchBox.setAlignment(Pos.TOP_LEFT);
//        this.searchBox.setPadding(new Insets(25));
//        this.searchBox.setSpacing(25);

        this.searchBar.setPrefWidth(800);
        this.searchBar.setPrefHeight(75);
        this.searchBar.setStyle("-fx-font-size: 28;");

        this.title = new Label("Flight Explorer");
        this.title.setStyle("-fx-font-size: 28;");

        this.stage.setTitle("FlightExplorer");
        this.stage.setScene(new Scene(this.grid, 1440, 1000));
        this.stage.setResizable(true);
//
//        this.imageView = new ImageView(new Image("map.jpeg")); #TODO
//        this.imageView.setFitWidth(800);
//        imageView.setPreserveRatio(true);


        this.titleSearch = new VBox(this.title, this.searchBox);
        this.titleSearch.setAlignment(Pos.TOP_LEFT);
        this.titleSearch.setPadding(new Insets(25));
        this.titleSearch.setSpacing(25);

//        this.hold = new VBox(this.searchBox,this.imageView); #TODO
        this.hold = new VBox(this.titleSearch);
        this.hold.setAlignment(Pos.TOP_LEFT);
        this.hold.setSpacing(25);
        this.hold.setPadding(new Insets(25));

        this.scene = new Scene(hold, 1440, 1000);
        this.stage.setScene(this.scene);
//        grid.setBackground(new Background(new BackgroundFill(
//                Color.valueOf("#000000"),
//                new CornerRadii(0),
//                new Insets(0)
//        )));
        this.stage.show();

    }
}
