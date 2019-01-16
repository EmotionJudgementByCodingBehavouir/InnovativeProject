package cn.edu.nju.cs;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //预测按钮
    @FXML private Button predictButton;

    @FXML public void predictHandler() throws Exception{
        System.out.println("进行预测，选择模型，选择文件");
        PredictWindow predictWindow = new PredictWindow();
        predictWindow.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        predictButton.setText("预测");
    }
}
