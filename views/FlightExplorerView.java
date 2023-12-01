package views;
import FlightModel.FlightExplorer;
import FlightModel.*;
import FlightModel.Airports.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
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

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;



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

    WebView webView = new WebView();

    VBox contentBox;

    VBox map;

    WebEngine webEngine = this.webView.getEngine();
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


        // Load the HTML file into the WebView
        try {
            String basePath = System.getProperty("user.dir");
            String finalPath = basePath + "/views/html/map.html";
            File htmlFile = new File(finalPath);
            URI uri = htmlFile.toURI();
            this.webEngine.load(uri.toURL().toString());
            this.webEngine.setJavaScriptEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Add a listener to wait for the page to finish loading
        this.webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {


                String var = "'1'";
                this.webEngine.executeScript("const number = " + var + ";");
                this.webEngine.executeScript("showMessage();");
            }
        });



        this.searchBar.setPromptText("Search for a flight");
        this.searchBox = new HBox(this.searchBar);
        this.searchBox.setAlignment(Pos.TOP_LEFT);

        this.searchBar.setPrefWidth(600);
        this.searchBar.setPrefHeight(75);
        this.searchBar.setStyle("-fx-font-size: 28;");

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch(searchBar);
            }
        });

        this.title = new Label("Flight Explorer");
        this.title.setStyle("-fx-font-size: 28;");

        this.stage.setTitle("FlightExplorer");
        this.stage.setScene(new Scene(this.grid, 1440, 1000));
        this.stage.setResizable(true);

        ComboBox<String> comboBox = new ComboBox<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Flight Number",
                "Arrival",
                "Departure",
                "Free search"
        );
        comboBox.setItems(items);
        comboBox.setValue("Select an item");
        comboBox.setOnAction(e -> {
            String selectedValue = comboBox.getValue();
            // return to search to find by type #TODO
        });
        comboBox.setStyle("-fx-font-size: 26px;");


        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button");
        searchButton.setStyle("-fx-right: 40px;");
        searchButton.setStyle(" -fx-font-size: 35px;");
        searchButton.setOnAction(e -> performSearch(searchBar));

        HBox searchButtonBox = new HBox(comboBox, this.searchBox, searchButton);
        searchButtonBox.setSpacing(15);
        searchButtonBox.setPadding(new Insets(10));


        this.titleSearch = new VBox(this.title, searchButtonBox); // ADD MAP HERE
        this.titleSearch.setAlignment(Pos.TOP_LEFT);
        this.titleSearch.setPadding(new Insets(25));
        this.titleSearch.setSpacing(25);

        this.hold = new VBox(this.titleSearch);
        this.hold.setAlignment(Pos.TOP_LEFT);
        this.hold.setSpacing(25);
        this.hold.setPadding(new Insets(25));


        this.contentBox = new VBox();
        this.contentBox.setSpacing(15);
        for (int i = 1; i <= 400; i++) { // add labels for flight. #TODO
            Label label = new Label("Item " + i);
            contentBox.getChildren().add(label);
        }
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setPrefViewportHeight(300); // Set the desired pixel amount
        scrollPane.setMaxHeight(300);
        scrollPane.setPrefViewportWidth(500);
        scrollPane.setMaxWidth(500);

        VBox pinned = new VBox();
        pinned.setSpacing(15);
        ScrollPane pinnedPane = new ScrollPane(pinned);

        pinnedPane.setPrefViewportHeight(300); // Set the same desired pixel amount
        pinnedPane.setMaxHeight(300);
        pinnedPane.setPrefViewportWidth(500);
        pinnedPane.setMaxWidth(500);


        Label flightTitle = new Label("Flights");
        flightTitle.setStyle("-fx-font-size: 28;");
        Label dashboardTitle = new Label("Dashboard");
        dashboardTitle.setStyle("-fx-font-size: 28;");

        VBox scrolls = new VBox(flightTitle, scrollPane, dashboardTitle, pinnedPane);
        scrolls.setSpacing(40);
        scrolls.setPadding(new Insets(10));
        scrolls.setAlignment(Pos.CENTER);



        this.map = new VBox(this.titleSearch ,this.webView);
        HBox titleSearchHold = new HBox( this.map, scrolls);
        titleSearchHold.setAlignment(Pos.TOP_LEFT);
        titleSearchHold.setSpacing(10);
        titleSearchHold.setPadding(new Insets(10));


        this.scene = new Scene(titleSearchHold, 1440, 1000);
        this.stage.setScene(this.scene);
        this.stage.show();

    }

    private void performSearch(TextField searchField) {
        String search = searchField.getText();
        System.out.println(search);
        // searched term, search in database
    }




}
