package cn.edu.nju.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PredictWindow extends Application {
    private Stage stage = new Stage();

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("predict.fxml"));
        primaryStage.setTitle("情绪预测");
        primaryStage.setScene(new Scene(root, 1120, 739));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void show() throws  Exception {
        start(stage);
    }

}
