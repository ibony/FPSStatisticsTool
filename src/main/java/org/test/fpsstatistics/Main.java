package org.test.fpsstatistics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setTitle("fps-statistics-tool");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.setScene(new Scene(root, 1600, 500));
        stage.setMinHeight(500);
        stage.setMaxHeight(500);
        stage.setMinWidth(1020);
        stage.setMaxWidth(1800);
        stage.show();


        stage.setOnCloseRequest(
                event -> {
                    event.consume();
                    System.exit(0);
                    stage.close();
                }
        );
    }


    public static void main(String[] args) {
        launch(args);
    }
}
