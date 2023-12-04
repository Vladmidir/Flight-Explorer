package views;
import FlightModel.APIs.WebAPIs.RealTimeFlightAPI;
import FlightModel.FlightExplorer;
import FlightModel.*;
import FlightModel.Airports.*;
import FlightModel.Flights.Flight;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Modality;
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
import netscape.javascript.JSObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import views.Bridge;


/**
 * Class FlightExplorerView.
 * This class is responsible for the UI of the Flight Explorer.
 */
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

    ArrayList<Flight> flightList;

    HashMap<String, HashMap<String, HashMap<String, String>>> mapFlightList;

    String mapFlightListToString;

    static HashMap<Integer, Boolean> isPinned = new HashMap<>();


    /**
     *
     */
    public FlightExplorerView(FlightExplorer explorer, Stage stage) { // contructor for view, this is mainly javafx
        this.explorer = explorer;
        this.stage = stage;

        this.flightList = callAPI(); // #TODO
        this.mapFlightList = populateFlightInfo(flightList);
        for (int i = 0; i < flightList.size(); i++) {
            isPinned.put(i, false);
        }
        initUI();
    }

    /**
     * converts flightList to JSON
     *
     * @param  flightList List of all flights
     * @return A HashMap of all flights converted
     */
    public HashMap<String, HashMap<String, HashMap<String, String>>> populateFlightInfo(ArrayList<Flight> flightList){

        HashMap<String, HashMap<String, HashMap<String, String>>> mapFlightList = new HashMap<>();
        for (int i = 0; i < flightList.size(); i++) {
            HashMap<String, HashMap<String, String>> flightInfo = new HashMap<>();

            HashMap<String, String> flightShortInfo = new HashMap<>();
            flightShortInfo.put("'date'","'"+flightList.get(i).getShortDetails().get("date")+"'");
            flightShortInfo.put("'status'","'"+flightList.get(i).getShortDetails().get("status")+"'");
            flightShortInfo.put("'depAirport'","'"+flightList.get(i).getShortDetails().get("depAirport")+"'");
            flightShortInfo.put("'arrAirport'","'"+flightList.get(i).getShortDetails().get("arrAirport")+"'");

            flightInfo.put("'ShortDetails'",flightShortInfo);

            HashMap<String, String> flightLongInfo = new HashMap<>();
            flightLongInfo.put("'location'","'"+flightList.get(i).getLongDetails().get("location")+"'");
            flightLongInfo.put("'altitude'",flightList.get(i).getLongDetails().get("altitude"));
            flightLongInfo.put("'isGround'","'"+flightList.get(i).getLongDetails().get("isGround")+"'");
            flightLongInfo.put("'direction'",flightList.get(i).getLongDetails().get("direction"));

            flightInfo.put("'LongDetails'",flightLongInfo);


            HashMap<String, String> flightStrInfo = new HashMap<>();
            flightStrInfo.put("'flightDetail'", "'"+flightList.get(i).toString().substring(7)+"'");
            flightInfo.put("'flightDetail'", flightStrInfo);
            Airport arrAirport = flightList.get(i).getArrAirport();
            Airport depAirport = flightList.get(i).getDepAirport();
            HashMap<String, String> arrAirportDetail = new HashMap<>();
            arrAirportDetail.put("'arrAirportDetail'", "'"+arrAirport.getDetails()+"'");
            arrAirportDetail.put("'arrAirportId'", "'"+arrAirport.getId()+"'");
            arrAirportDetail.put("'arrAirportLocation'", arrAirport.getLocation().toString());
            flightInfo.put("'arrAirport'", arrAirportDetail);
            HashMap<String, String> depAirportDetail = new HashMap<>();
            depAirportDetail.put("'depAirportDetail'", "'"+depAirport.getDetails()+"'");
            depAirportDetail.put("'depAirportId'", "'"+depAirport.getId()+"'");
            depAirportDetail.put("'depAirportLocation'", depAirport.getLocation().toString());
            flightInfo.put("'depAirport'", depAirportDetail);
            mapFlightList.put("" + i, flightInfo);

        }
        mapFlightListToString = mapFlightList.toString().replace("=", ":");
        return mapFlightList;
    }

    /**
     * Initialize UI
     *
     * @return void
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

    /**
     * calls RealTimeFlightAPI search to populate flightList
     * which can then be used to plot flights and be visualized
     *
     * @return An arraylist of all the flights called from the API
     */
    private ArrayList<Flight> callAPI() {
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.getPropValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> responseBody = new HashMap<>();
        responseBody.put("flight_status", "active");

        RealTimeFlightAPI realTimeFlightAPI = new RealTimeFlightAPI(System.getProperty("AVIATIONSTACK_KEY"));
        String search = realTimeFlightAPI.search(responseBody); // #TODO

        return this.explorer.getRealTimeFlights(responseBody);
    }

    /**
     * UI, shows more info on a flight in the flightList
     *
     * @param  flightNumber to indicate which flight is being shown
     *                      in the info window
     * @return void
     */
    private void showInfoWindowPinned(int flightNumber) {



        // Create a new stage (window) // #TODO add pin button
        Stage infoStage = new Stage();
        infoStage.setTitle("Info Window");


        // Set modality to APPLICATION_MODAL to make it block user interaction with other windows
        infoStage.initModality(Modality.APPLICATION_MODAL);

        // Create a layout for the info window
        StackPane infoLayout = new StackPane();
        HashMap<String, String> flightInfo = this.flightList.get(flightNumber).getShortDetails();
        flightInfo.putAll(this.flightList.get(flightNumber).getLongDetails());

        String info = "";
        for (String key : flightInfo.keySet()) {
            if (key.equals("depAirport")){
                info += "Departure Airport: " + flightInfo.get(key) + "\n";
                Airport depAirport = this.flightList.get(flightNumber).getDepAirport();
                info += "\t\tDeparture Airport Details: " + depAirport.getDetails() + "\n\t\tID: " + depAirport.getId()+ "\n\t\tLocation: " + depAirport.getLocation() + "\n";
            }
            else if (key.equals("arrAirport")){
                info += "Arrival Airport: " + flightInfo.get(key) + "\n";
                Airport arrAirport = this.flightList.get(flightNumber).getArrAirport();
                info += "\t\tArrival Airport Details: " + arrAirport.getDetails() + "\n\t\tID: " + arrAirport.getId()+ "\n\t\tLocation: " + arrAirport.getLocation() + "\n";
            }
            else if (key.equals("isGround")){
                info += "Grounded: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("location")){
                info += "Plane Current Location: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("altitude")){
                info += "Altitude: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("direction")){
                info += "Direction: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("date")){
                info += "Date: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("status")){
                info += "Status: " + flightInfo.get(key) + "\n";
            }

        }

        Button pinButton = new Button("Unpin this flight");
        Label infoLabel = new javafx.scene.control.Label(info);
        Font customFont = new Font("Arial", 24);
        infoLabel.setFont(customFont);
        pinButton.setStyle("-fx-font-size: 24px;");


        infoLayout.getChildren().add(infoLabel);

        VBox pinCenter = new VBox(pinButton);
        pinCenter.setAlignment(Pos.CENTER);
        pinButton.setOnAction(e -> {
            isPinned.put(Integer.valueOf(flightNumber), false);
            removeFromDashboard(flightList.get(flightNumber).toString());
            infoStage.close();

        });


        VBox hold = new VBox(infoLabel, pinCenter);



        // Set the layout for the scene
        Scene infoScene = new Scene(hold, 1200, 800);
        infoStage.setScene(infoScene);

        // Show the info window
        infoStage.show();
    }

    /**
     * this removes the button from the dashboard
     * @param  pinButton a string of the button's info
     * @return void
     */
    private void removeFromDashboard (String pinButton){

//        pinned.getChildren().remove(pinButton);
        pinned.getChildren().removeIf(node -> {
            if (node instanceof Button) {
            Button button = (Button) node;
            return pinButton.equals(button.getText());
        }
        return false;
    });
    }


    /**
     * this removes the button from the dashboard
     * @param  flightNumber
     * @return void
     */
    private void showInfoWindowUnpinned(int flightNumber) {



        // Create a new stage (window) // #TODO add pin button
        Stage infoStage = new Stage();
        infoStage.setTitle("Info Window");


        // Set modality to APPLICATION_MODAL to make it block user interaction with other windows
        infoStage.initModality(Modality.APPLICATION_MODAL);

        // Create a layout for the info window
        StackPane infoLayout = new StackPane();
        HashMap<String, String> flightInfo = this.flightList.get(flightNumber).getShortDetails();
        flightInfo.putAll(this.flightList.get(flightNumber).getLongDetails());

        String info = "";
        for (String key : flightInfo.keySet()) {
            if (key.equals("depAirport")){
                info += "Departure Airport: " + flightInfo.get(key) + "\n";
                Airport depAirport = this.flightList.get(flightNumber).getDepAirport();
                info += "\t\tDeparture Airport Details: " + depAirport.getDetails() + "\n\t\tID: " + depAirport.getId()+ "\n\t\tLocation: " + depAirport.getLocation() + "\n";
            }
            else if (key.equals("arrAirport")){
                info += "Arrival Airport: " + flightInfo.get(key) + "\n";
                Airport arrAirport = this.flightList.get(flightNumber).getArrAirport();
                info += "\t\tArrival Airport Details: " + arrAirport.getDetails() + "\n\t\tID: " + arrAirport.getId()+ "\n\t\tLocation: " + arrAirport.getLocation() + "\n";
            }
            else if (key.equals("isGround")){
                info += "Grounded: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("location")){
                info += "Plane Current Location: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("altitude")){
                info += "Altitude: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("direction")){
                info += "Direction: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("date")){
                info += "Date: " + flightInfo.get(key) + "\n";
            }
            else if (key.equals("status")){
                info += "Status: " + flightInfo.get(key) + "\n";
            }


        }


        Label infoLabel = new javafx.scene.control.Label(info);
        Font customFont = new Font("Arial", 24);
        infoLabel.setFont(customFont);



        infoLayout.getChildren().add(infoLabel);
        Button pinButton = new Button("Pin this flight");
        pinButton.setStyle("-fx-font-size: 24px;");
        VBox pinCenter = new VBox(pinButton);
        pinCenter.setAlignment(Pos.CENTER);
        pinButton.setOnAction(e -> {
            addToDashboard(flightNumber);
            infoStage.close();

        });
        VBox hold = new VBox(infoLabel);
        if (!isPinned.get(flightNumber)){
            hold.getChildren().add(pinCenter);
        }
        else{
            Label aPinned = new Label("This flight is already pinned");
            aPinned.setStyle("-fx-font-size: 24px;");
            VBox temp = new VBox(aPinned);
            temp.setAlignment(Pos.CENTER);
            hold.getChildren().add(temp);
        }








        // Set the layout for the scene
        Scene infoScene = new Scene(hold, 1200, 800);
        infoStage.setScene(infoScene);

        // Show the info window
        infoStage.show();
    }

    /**
     *
     */
    private void addToDashboard(int flightNumber){
        // add to dashboard
        Button pinnedFlight = new Button(this.flightList.get(flightNumber).toString());


        pinnedFlight.setOnAction(e -> showInfoWindowPinned(flightNumber));
        this.pinned.getChildren().add(pinnedFlight);
        isPinned.put(Integer.valueOf(flightNumber), true);




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
        for (int i = 0; i < this.flightList.size(); i++) { // add labels for flight. #TODO
            Button addFlight = new Button(this.flightList.get(i).toString());
            int finalI = i;
            addFlight.setOnAction(e -> showInfoWindowUnpinned(finalI));
            contentBox.getChildren().add(addFlight); // #TODO
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
                "airline name",
                "flight status",
                "flight number",
                "flight iata",
                "flight icao",
                "departure iata",
                "arrival iata",
                "limit",
                "offset"
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

                } else if (newValue == Worker.State.FAILED) {
                    System.err.println( this.webEngine.getLoadWorker().getMessage());
                } });

            this.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("java", new Bridge());
                    webEngine.executeScript("console.log = function(message) { java.log(message); }");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
