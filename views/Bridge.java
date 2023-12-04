package views;

import javafx.application.Platform;

public class Bridge {

    public void exit() {
        Platform.exit();
    }

    public void log(String text) {
        System.out.println(text);
}}