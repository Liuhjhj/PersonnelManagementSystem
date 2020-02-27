package com.controller;

import javafx.scene.control.Alert;

public class AlertDiaog {

    public static void alert(int alertType, String title, String headerText, String contentText){
        Alert alert;
        Alert.AlertType type;
        switch (alertType){
            case 0: type = Alert.AlertType.ERROR;break;
            case 1: type = Alert.AlertType.WARNING;break;
            case 2: type = Alert.AlertType.CONFIRMATION;break;
            case 3: type = Alert.AlertType.INFORMATION;break;
            default: type = Alert.AlertType.NONE;break;
        }
        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void alert(int alertType, String title, String headerText){
        alert(alertType, title, headerText, "");
    }

    public static void alert(String title, String headerText){
        alert(4, title, headerText);
    }

    public static void alert(String title, String headerText, String contentText){
        alert(4, title, headerText, contentText);
    }
}
