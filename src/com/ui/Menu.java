package com.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {
    public Stage primaryStage;
    public BorderPane rootLayout;
    public Label userLabel;
    public Button exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void exitAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("/com/ui/Login.fxml"));
            stage.setScene(new Scene(parent));
            stage.setTitle("人事管理系统");
            stage.show();
            if (primaryStage == null)
                primaryStage = (Stage) rootLayout.getScene().getWindow();
            primaryStage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
