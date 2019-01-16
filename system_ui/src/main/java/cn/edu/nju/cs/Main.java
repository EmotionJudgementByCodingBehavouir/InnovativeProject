package cn.edu.nju.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("surface.fxml"));
        primaryStage.setTitle("...");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
