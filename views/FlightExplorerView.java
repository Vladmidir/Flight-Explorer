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


    Label title = new Label("Flight Explorer");
    TextField searchBar = new TextField();


    HBox searchBox = new HBox(this.searchBar);

    Scene scene;

    ImageView imageView;

    VBox hold;


    VBox titleSearch;

    WebView webView = new WebView();

    VBox contentBox= new VBox();

    WebEngine webEngine = this.webView.getEngine();

    ComboBox<String> comboBox = new ComboBox<>();

    Button searchButton = new Button("Search");

    HBox searchButtonBox = new HBox(this.comboBox, this.searchBox, this.searchButton);

    Label flightTitle = new Label("Flights");

    Label dashboardTitle = new Label("Dashboard");

    ScrollPane scrollPane;

    ScrollPane pinnedPane;

    VBox pinned = new VBox();

    VBox scrolls;

    HBox titleSearchHold;

    /*
     *
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

    /*
     *
     */
    public void initUI() {
        createMap();
        createSearchBar();
        createComboBox();
        createSearchButton();

        this.searchBox.setAlignment(Pos.TOP_LEFT);
        this.title.setStyle("-fx-font-size: 28;");

        this.searchButtonBox.setSpacing(15);
        this.searchButtonBox.setPadding(new Insets(10));

        createTitleSearch(this.title, this.searchButtonBox);

        //Create flight results Box
        createContentBox();
        createScrollPane(this.contentBox);

        //Create Pinned Box
        this.pinned.setSpacing(15);
        createPinnedPane(this.pinned);

        //set styles for the flight and dashboard titles
        this.flightTitle.setStyle("-fx-font-size: 28;");
        this.dashboardTitle.setStyle("-fx-font-size: 28;");

        //create vbox scrolls and set styles
        createScrolls(this.flightTitle, this.scrollPane, this.dashboardTitle, this.pinnedPane);
        //create vbox to display
        createTitleSearchHold(new VBox(this.titleSearch ,this.webView), this.scrolls );
        //Set the scene
        this.scene = new Scene(titleSearchHold, 1440, 1000);
        this.stage.setScene(this.scene);
        this.stage.show();

    }

    /*
     *
     */
    private void createTitleSearchHold(VBox map, VBox scrolls) {

        this.titleSearchHold = new HBox( map, scrolls);
        this.titleSearchHold.setAlignment(Pos.TOP_LEFT);
        this.titleSearchHold.setSpacing(10);
        this.titleSearchHold.setPadding(new Insets(10));
    }

    /*
     *
     */
    private void createTitleSearch(Label title, HBox searchButtonBox) {

        this.titleSearch = new VBox(title, searchButtonBox); // ADD MAP HERE
        this.titleSearch.setAlignment(Pos.TOP_LEFT);
        this.titleSearch.setPadding(new Insets(25));
        this.titleSearch.setSpacing(25);
    }

    /*
     *
     */
    private void performSearch(TextField searchField) {
        String search = searchField.getText();
        System.out.println(search);
        // searched term, search in database
    }

    /*
     *
     */
    private void createScrolls(Label flightTitle, ScrollPane scrollPane, Label dashboardTitle, ScrollPane pinnedPane) {
        this.scrolls = new VBox(flightTitle, scrollPane, dashboardTitle, pinnedPane);
        this.scrolls.setSpacing(40);
        this.scrolls.setPadding(new Insets(10));
        this.scrolls.setAlignment(Pos.CENTER);
    }

    /*
     *
     */
    private void createPinnedPane(VBox pinned) {
        this.pinnedPane = new ScrollPane(pinned);
        this.pinnedPane.setPrefViewportHeight(300); // Set the same desired pixel amount
        this.pinnedPane.setMaxHeight(300);
        this.pinnedPane.setPrefViewportWidth(500);
        this.pinnedPane.setMaxWidth(500);
    }

    /*
     *
     */
    private void createScrollPane(VBox contentBox) {
        this.scrollPane = new ScrollPane(contentBox);
        this.scrollPane.setPrefViewportHeight(300); // Set the desired pixel amount
        this.scrollPane.setMaxHeight(300);
        this.scrollPane.setPrefViewportWidth(500);
        this.scrollPane.setMaxWidth(500);
    }

    /*
     *
     */
    private void createContentBox() {
        this.contentBox.setSpacing(15);
        for (int i = 1; i <= 400; i++) { // add labels for flight. #TODO
            Label label = new Label("Item " + i);
            this.contentBox.getChildren().add(label);
        }
    }

    /*
     *
     */
    private void createSearchButton() {
        this.searchButton.getStyleClass().add("button");
        this.searchButton.setStyle("-fx-right: 40px;");
        this.searchButton.setStyle(" -fx-font-size: 35px;");
        this.searchButton.setOnAction(e -> performSearch(searchBar));
    }

    /*
     *
     */
    private void createSearchBar() {
        this.searchBar.setPromptText("Search for a flight");
        this.searchBar.setPrefWidth(600);
        this.searchBar.setPrefHeight(75);
        this.searchBar.setStyle("-fx-font-size: 28;");

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch(searchBar);
            }
        });
    }

    /*
     *
     */
    private void createComboBox() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "Flight Number",
                "Arrival",
                "Departure",
                "Free search"
        );
        this.comboBox.setItems(items);
        this.comboBox.setValue("Select an item");
        this.comboBox.setOnAction(e -> {
            String selectedValue = this.comboBox.getValue();
            // return to search to find by type #TODO
        });
        this.comboBox.setStyle("-fx-font-size: 26px;");
    }

    /*
     *
     */
    private void createMap() {
        try {
            // Load the HTML file into the WebView
            String basePath = System.getProperty("user.dir");
            String finalPath = basePath + "/views/html/map.html";
            File htmlFile = new File(finalPath);
            URI uri = htmlFile.toURI();
            this.webEngine.load(uri.toURL().toString());
            this.webEngine.setJavaScriptEnabled(true);

            // Add a listener to wait for the page to finish loading
            this.webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {


                    String var = "'1'";
                    this.webEngine.executeScript("const number = " + var + ";");
                    this.webEngine.executeScript("showMessage();");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
