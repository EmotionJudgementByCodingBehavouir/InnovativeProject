package uidesign;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import visualize.ImageGenerator;

import java.io.File;
import java.util.*;

public class Controller{
    public Canvas ground;
    public TableView ShowId;
    public TableColumn ID;
    private ObservableList<StudentInfo> studentInfos;

    private String modelPath;
    private String dataPath;
    private String targetPath;
    private Set<String> stuid;

    public Controller(){
        ArrayList<StudentInfo> origin = new ArrayList<>();
        studentInfos = FXCollections.observableArrayList(origin);

        stuid = new HashSet<>();
        modelPath = "";
        dataPath = "";
        targetPath = "";
    }

    public void toSetId(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("171180");
        dialog.setTitle("Text Input Dialog");
        //dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter student id:");
        Optional<String> result = dialog.showAndWait();
        System.out.println(result.get());
    }

    public void toShow(ActionEvent actionEvent) {
        GraphicsContext gc = ground.getGraphicsContext2D();
        Image image = new Image("/171860507.png");
        gc.drawImage(image, 0, 50);
    }

    public void toInitialize(ActionEvent actionEvent) {
        ID.setCellValueFactory(new PropertyValueFactory<StudentInfo,String>("SID"));
        ShowId.setEditable(true);
        ShowId.setItems(studentInfos);

        if(modelPath == ""|| dataPath == "" || targetPath == "") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("please set modelPath,dataPath and targetPath");
            alert.setContentText("Careful with the next step!");
            alert.showAndWait();
        }
        else{
            File file= new File(dataPath);
            getStudentId(file);
            System.out.println("the number of students is:"+stuid.size());
            List<String> ids= new ArrayList<>(stuid);
            Collections.sort(ids);
            for(String s:ids){
                studentInfos.add(new StudentInfo(s));
            }
        }

    }

    public void getStudentId(File file){
        File[] fs = file.listFiles();
        for(File f:fs){
            File[] files1 = f.listFiles();
            for(File m:files1){
                stuid.add(m.getName());
            }
        }
    }
    public void toAdd(ActionEvent actionEvent) {
        for(int i = 0;i < 100; i++) {
            studentInfos.add(new StudentInfo("000"+Integer.toString(i)));
        }
    }

    public void toShowPicture(MouseEvent mouseEvent) {
        int row = ShowId.getSelectionModel().getFocusedIndex();
        String s = studentInfos.get(row).getSID();
        System.out.println(s);
        GraphicsContext gc = ground.getGraphicsContext2D();
        //Image image = new Image("/171860507.png");
        Image image = new ImageGenerator(modelPath,s).getImages().get(0);
        gc.drawImage(image, 0, 50);
    }

    public void toSetModel(ActionEvent actionEvent) {
        FileChooser modelChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("请选择模型文件","*.model");
        modelChooser.getExtensionFilters().add(filter);
        File file = modelChooser.showOpenDialog(null);
        if(file != null) {
            modelPath = file.getPath();
        }
        System.out.println(modelPath);
    }

    public void toSetDataset(ActionEvent actionEvent) {
        DirectoryChooser DataPathChooser=new DirectoryChooser();
        File file = DataPathChooser.showDialog(null);
        if(file != null) {
            dataPath = file.getPath();
            ImageGenerator.setBasePath(dataPath+"/");
        }
        System.out.println(dataPath);
    }

    public void toSetTarget(ActionEvent actionEvent) {
        DirectoryChooser TargetPathChooser=new DirectoryChooser();
        File file = TargetPathChooser.showDialog(null);
        if(file != null) {
            targetPath = file.getPath();
            ImageGenerator.setTargetPath(targetPath+"/");
        }
        System.out.println(targetPath);
    }
}
