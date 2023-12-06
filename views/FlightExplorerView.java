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

import javax.swing.plaf.synth.SynthTextAreaUI;
import javax.swing.text.Style;
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

    Scene scene;

    WebView webView = new WebView();

    VBox contentBox= new VBox();

    WebEngine webEngine = this.webView.getEngine();

    ComboBox<String> comboBox = new ComboBox<>();

    Button searchButton = new Button("Search");

    ScrollPane scrollPane;

    ScrollPane pinnedPane;
    VBox pinned = new VBox();

    ArrayList<Flight> flightList;

    String mapFlightListToString;

    static HashMap<Integer, Boolean> isPinned = new HashMap<>();


    /**
     * constructor for view, this is mainly javafx
     */
    public FlightExplorerView(FlightExplorer explorer, Stage stage) { // contructor for view, this is mainly javafx
        this.explorer = explorer;
        this.stage = stage;

        this.flightList = callAPI(); // #TODO
        this.mapFlightListToString = populateFlightInfo(flightList);
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
    public String populateFlightInfo(ArrayList<Flight> flightList){
        ConvertToJSON convertToJSON = new ConvertToJSON(flightList);
        return convertToJSON.getFlightListJSON();
    }

    /**
     * Initialize UI
     *
     * @return void
     */
    public void initUI() {
        createMap(new ConvertToJSON(this.flightList).getFlightListJSON());

        //Create search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a flight");
        searchBar.setPrefWidth(600);
        searchBar.setPrefHeight(75);
        searchBar.setStyle("-fx-font-size: 28;");

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch(searchBar);
            }
        });

        //Create search box and button
        createComboBox();
        this.searchButton.getStyleClass().add("button");
        this.searchButton.setStyle("-fx-right: 40px;");
        this.searchButton.setStyle(" -fx-font-size: 35px;");
        this.searchButton.setOnAction(e -> performSearch(searchBar));

        HBox searchBox = new HBox(searchBar);
        searchBox.setAlignment(Pos.TOP_LEFT);
        Label title = new Label("Flight Explorer");
        title.setStyle("-fx-font-size: 28;");

        HBox searchButtonBox = new HBox(this.comboBox, searchBox, this.searchButton);
        searchButtonBox.setSpacing(15);
        searchButtonBox.setPadding(new Insets(10));

        //Create title and search box VBox and set styles
        VBox titleSearch = new VBox(title, searchButtonBox);
        titleSearch.setAlignment(Pos.TOP_LEFT);
        titleSearch.setPadding(new Insets(25));
        titleSearch.setSpacing(25);

        //Create flight results Box that is scrollable
        createContentBox();

        this.scrollPane = new ScrollPane(this.contentBox);
        this.scrollPane.setPrefViewportHeight(300); // Set the desired pixel amount
        this.scrollPane.setMaxHeight(300);
        this.scrollPane.setPrefViewportWidth(500);
        this.scrollPane.setMaxWidth(500);

        //Create Pinned Box
        this.pinned.setSpacing(15);
        this.pinnedPane = new ScrollPane(this.pinned);
        this.pinnedPane.setPrefViewportHeight(300); // Set the same desired pixel amount
        this.pinnedPane.setMaxHeight(300);
        this.pinnedPane.setPrefViewportWidth(500);
        this.pinnedPane.setMaxWidth(500);


        //set styles for the flight and dashboard titles
        Label flightTitle = new Label("Flights");
        Label dashboardTitle = new Label("Dashboard");
        flightTitle.setStyle("-fx-font-size: 28;");
        dashboardTitle.setStyle("-fx-font-size: 28;");

        //create vbox scrolls and set styles
        VBox scrolls = new VBox(flightTitle, this.scrollPane, dashboardTitle, this.pinnedPane);
        scrolls.setSpacing(40);
        scrolls.setPadding(new Insets(10));
        scrolls.setAlignment(Pos.CENTER);

        //create vbox to display the map and the scrollable on the right side of the app
        HBox titleSearchHold = new HBox( new VBox(titleSearch ,this.webView), scrolls);
        titleSearchHold.setAlignment(Pos.TOP_LEFT);
        titleSearchHold.setSpacing(10);
        titleSearchHold.setPadding(new Insets(10));

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
        String search = realTimeFlightAPI.search(responseBody);

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



        // Create a new stage (window)
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
     * @param  flightNumber the flight number of the flight to be added
     * @return void
     */
    private void showInfoWindowUnpinned(int flightNumber, ArrayList<Flight> flightList) {




        Stage infoStage = new Stage();
        infoStage.setTitle("Info Window");


        // Set modality to APPLICATION_MODAL to make it block user interaction with other windows
        infoStage.initModality(Modality.APPLICATION_MODAL);

        // Create a layout for the info window
        StackPane infoLayout = new StackPane();
        HashMap<String, String> flightInfo = flightList.get(flightNumber).getShortDetails();
        flightInfo.putAll(flightList.get(flightNumber).getLongDetails());

        String info = "";
        for (String key : flightInfo.keySet()) {
            if (key.equals("depAirport")){
                info += "Departure Airport: " + flightInfo.get(key) + "\n";
                Airport depAirport = flightList.get(flightNumber).getDepAirport();
                info += "\t\tDeparture Airport Details: " + depAirport.getDetails() + "\n\t\tID: " + depAirport.getId()+ "\n\t\tLocation: " + depAirport.getLocation() + "\n";
            }
            else if (key.equals("arrAirport")){
                info += "Arrival Airport: " + flightInfo.get(key) + "\n";
                Airport arrAirport = flightList.get(flightNumber).getArrAirport();
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
     * Adds a flight that was pinned to the dashboard and updates the hashmap
     * @param flightNumber the flight number of the flight to be added
     * @return void
     *
     */
    private void addToDashboard(int flightNumber){
        // add to dashboard
        Button pinnedFlight = new Button(this.flightList.get(flightNumber).toString());


        pinnedFlight.setOnAction(e -> showInfoWindowPinned(flightNumber));
        this.pinned.getChildren().add(pinnedFlight);
        isPinned.put(Integer.valueOf(flightNumber), true);




    }

    /**
     *
     * Searches and filters the resulting flights and then updating those results on the dashboard and map
     * @param  searchField the text field to search by
     * @return void
     *
     */
    private void performSearch(TextField searchField) {
        String search = searchField.getText();
        String filterType = this.comboBox.getValue();
        SearchFlights searchFlights = new SearchFlights(this.flightList);
        ArrayList<Flight> searchResult = searchFlights.search(filterType, search);
        if (filterType.equals("select an item") && search.isEmpty()){
            this.contentBox.getChildren().clear();
            for (int i = 0; i < this.flightList.size(); i++) {
                Button addFlight = new Button(this.flightList.get(i).toString());
                int finalI = i;
                addFlight.setOnAction(e -> showInfoWindowUnpinned(finalI, this.flightList));
                this.contentBox.getChildren().add(addFlight);
            }

            ConvertToJSON converting = new ConvertToJSON(this.flightList);
            String dict = converting.getFlightListJSON();
            createMap(dict);
        }
        else if (search.isEmpty()){
            Stage infoStage = new Stage();
            infoStage.setTitle("Info Window");


            // Set modality to APPLICATION_MODAL to make it block user interaction with other windows
            infoStage.initModality(Modality.APPLICATION_MODAL);

            // Create a layout for the info window
            StackPane infoLayout = new StackPane();
            String info = "Please input a search term, to reset and show all flights,\n please leave this search term empty\n and leave filter type to \"select an item\"";
            Label infoLabel = new javafx.scene.control.Label(info);
            Font customFont = new Font("Arial", 24);
            infoLabel.setFont(customFont);



            infoLayout.getChildren().add(infoLabel);
            Scene infoScene = new Scene(infoLayout, 700, 700);
            infoStage.setScene(infoScene);

            // Show the info window
            infoStage.show();
        }
        else{
            this.contentBox.getChildren().clear();
            for (int i = 0; i < searchResult.size(); i++) {
                Button addFlight = new Button(searchResult.get(i).toString());
                int finalI = i;
                addFlight.setOnAction(e -> showInfoWindowUnpinned(finalI, searchResult));
                this.contentBox.getChildren().add(addFlight);
            }

            ConvertToJSON converting = new ConvertToJSON(searchResult);
            String dict = converting.getFlightListJSON();
            if (dict.equals("}")){
                dict = "{}";
            }
            createMap(dict);

        }




    }

    /**
     * Creates the Box to store the flight results and display on the scrollable dashboard
     * @return void
     */
    private void createContentBox() {
        this.contentBox.setSpacing(15);
        for (int i = 0; i < this.flightList.size(); i++) {
            Button addFlight = new Button(this.flightList.get(i).toString());
            int finalI = i;
            addFlight.setOnAction(e -> showInfoWindowUnpinned(finalI, this.flightList));
            this.contentBox.getChildren().add(addFlight);
        }
    }

    /**
     *
     * Creates a combo box to select a filter type
     * @return void
     *
     */
    private void createComboBox() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "select an item",
                "date",
                "flight status",
                "altitude",
                "flight name",
                "grounded",
                "status",
                "direction",
                "arrival airport",
                "arrival airport ID",
                "arrival airport details",
                "arrival airport location",
                "departure airport",
                "departure airport ID",
                "departure airport details",
                "departure airport location",
                "location of flight"


        );
        this.comboBox.setItems(items);
        this.comboBox.setValue("select an item");
        this.comboBox.setOnAction(e -> {
            String selectedValue = this.comboBox.getValue();

        });
        this.comboBox.setStyle("-fx-font-size: 26px;");
    }

    /**
     *
     * Creates a map UI to Visualize flights on a map through a html file
     * @param  dict a string of the flightList in JSON format
     * @return void
     */
    private void createMap(String dict) {
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
                    this.webEngine.executeScript("createFlights(" + dict + "  );");


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
