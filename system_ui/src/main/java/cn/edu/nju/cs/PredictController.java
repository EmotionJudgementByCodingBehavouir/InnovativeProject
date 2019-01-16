package cn.edu.nju.cs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import predict.emotion.*;
import predict.model.*;

public class PredictController implements Initializable {
    @FXML Button chooseModel;
    @FXML Button chooseGitLog;
    @FXML Button chooseCompileLog;
    @FXML Button display;
    @FXML Canvas displayCanvas;
    @FXML GridPane panel;
    private Image background = new Image("defaultCanvas.png");
    private String modelPath = null;
    private String gitLogPath = null;
    private String compileLogPath = null;

    @FXML private void chooseModelHandler() {
        FileChooser modelChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("请选择模型文件","*.model");
        modelChooser.getExtensionFilters().add(filter);
        Stage stage = (Stage)panel.getScene().getWindow();
        File file = modelChooser.showOpenDialog(stage);
        if(file != null) {
            modelPath = file.getPath();
        }
        System.out.println(modelPath);
        if(modelPath!=null && gitLogPath != null && compileLogPath != null) {
            display.setDisable(false);
        }
    }

    @FXML private void chooseGitLogHandler() {
        FileChooser gitLogChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("选择Git记录文件","*.txt");
        gitLogChooser.getExtensionFilters().add(filter);
        Stage stage = (Stage)panel.getScene().getWindow();
        File file = gitLogChooser.showOpenDialog(stage);
        if(file != null) {
            gitLogPath = file.getPath();
        }
        if(modelPath!=null && gitLogPath != null && compileLogPath != null) {
            display.setDisable(false);
        }
    }

    @FXML private void chooseCompileLogHandler() {
        FileChooser compileLogChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("选择编译记录文件","*.txt");
        compileLogChooser.getExtensionFilters().add(filter);
        Stage stage = (Stage)panel.getScene().getWindow();
        File file = compileLogChooser.showOpenDialog(stage);
        if(file != null) {
            compileLogPath = file.getPath();
        }
        if(modelPath!=null && gitLogPath != null && compileLogPath != null) {
            display.setDisable(false);
        }
    }

    @FXML private void displayHandler() {

    }
    //初始化窗口：设置文本，填充默认背景
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseModel.setText("选择模型");
        chooseGitLog.setText("选择Git日志");
        chooseCompileLog.setText("选择编译日志");
        display.setText("生成结果");
        display.setDisable(true);
        GraphicsContext gc = displayCanvas.getGraphicsContext2D();
        gc.drawImage(background,0,0,displayCanvas.getWidth(),displayCanvas.getHeight());
    }
}
