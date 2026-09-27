package org.ap.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ap/view/AdopcionView.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Sistema de Adopción de Animales");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}