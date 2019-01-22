package uidesign;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import visualize.ImageGenerator;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    public Canvas ground;
    public TableView ShowId;
    public TableColumn ID;
    public RadioButton scale;
    public RadioButton tendency;
    private ObservableList<StudentInfo> studentInfos;
    private ConfigInfo config;
    private boolean DatasetChange;
    private boolean ScaleMap;
    private boolean Tendency;
    private String currentId;

    final ToggleGroup group = new ToggleGroup();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<StudentInfo> origin = new ArrayList<>();
        studentInfos = FXCollections.observableArrayList(origin);
        config = new ConfigInfo();
        config.stuid = new HashSet<>();
        config.modelPath = "";
        config.dataPath = "";
        config.targetPath = "";
        currentId = "";
        DatasetChange = false;
        ScaleMap = true;
        Tendency = false; //默认比例图
        scale.setToggleGroup(group);
        scale.setSelected(true);
        tendency.setToggleGroup(group);
        tendency.setSelected(false);
    }

    public void toInitialize(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        ID.setCellValueFactory(new PropertyValueFactory<StudentInfo,String>("SID"));
        ShowId.setEditable(true);
        ShowId.setItems(studentInfos);

        //判断是否设置好了
        if(config.modelPath == ""|| config.dataPath == "" || config.targetPath == "") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("please set modelPath,dataPath and targetPath");
            alert.setContentText("Careful with the next step!");
            alert.showAndWait();
        }
        else{
            toLoadTableView();
        }
    }
    public void toLoadTableView() throws IOException {
        File file= new File(config.dataPath);
        getStudentId(file);
        System.out.println("the number of students is:"+config.stuid.size());
        if(!studentInfos.isEmpty()){
            studentInfos.removeAll();
        }
        List<String> ids= new ArrayList<>(config.stuid);
        Collections.sort(ids);
        for(String s:ids){
            studentInfos.add(new StudentInfo(s));
        }
        toSaveConfig();
        DatasetChange = false;
    }

    public void toSaveConfig() throws IOException {
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File("/configuration.ser")));
        oo.writeObject(config);
    }

    public void getStudentId(File file){
        File[] fs = file.listFiles();
        for(File f:fs){
            File[] files1 = f.listFiles();
            for(File m:files1){
                config.stuid.add(m.getName());
            }
        }
    }
    public void toUpdate(ActionEvent actionEvent) throws IOException {
        if(DatasetChange) {
            toLoadTableView();
        }
        toSaveConfig();
        DatasetChange = false;
    }

    public void toShowPicture(MouseEvent mouseEvent) throws InterruptedException{
        GraphicsContext gc = ground.getGraphicsContext2D();
        Image image = new Image("/loading.jpg");
        gc.drawImage(image, 0, 100,952,542);
        System.out.println("success");
        TimeUnit.MILLISECONDS.sleep(20);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int row = ShowId.getSelectionModel().getFocusedIndex();
                String s = studentInfos.get(row).getSID();
                currentId = s;
                System.out.println(s);
                showPicture();
            }
        });
    }

    public void showPicture(){
        int index = (ScaleMap == true)?0:1;
        Image image = new ImageGenerator(config.modelPath,currentId).getImages().get(index);
        GraphicsContext gc = ground.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
    }

    public void toSetModel(ActionEvent actionEvent) throws IOException {
        FileChooser modelChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("请选择模型文件","*.model");
        modelChooser.getExtensionFilters().add(filter);
        File file = modelChooser.showOpenDialog(null);
        if(file != null) {
            config.modelPath = file.getPath();
        }
        System.out.println(config.modelPath);
    }

    public void toSetDataset(ActionEvent actionEvent) throws IOException {
        DirectoryChooser DataPathChooser=new DirectoryChooser();
        File file = DataPathChooser.showDialog(null);
        if(file != null) {
            config.dataPath = file.getPath();
            ImageGenerator.setBasePath(config.dataPath+"/");
        }
        System.out.println(config.dataPath);
        DatasetChange = true;
    }

    public void toSetTarget(ActionEvent actionEvent) throws IOException {
        DirectoryChooser TargetPathChooser=new DirectoryChooser();
        File file = TargetPathChooser.showDialog(null);
        if(file != null) {
            config.targetPath = file.getPath();
            ImageGenerator.setTargetPath(config.targetPath+"/");
        }
        System.out.println(config.targetPath);
    }

    public void chooseScaleMap(ActionEvent actionEvent) {
        ScaleMap = true;
        Tendency = false;
        showPicture();
    }

    public void chooseTendency(ActionEvent actionEvent) {
        ScaleMap = false;
        Tendency = true;
        showPicture();
    }

    public void toLoadConfig(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        FileChooser configChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("请选择配置文件","*.ser");
        configChooser.getExtensionFilters().add(filter);
        File f = configChooser.showOpenDialog(null);

        if(f.exists()){
            config = new ConfigInfo();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            config = (ConfigInfo)ois.readObject();
            ImageGenerator.setBasePath(config.dataPath+"/");
            ImageGenerator.setTargetPath(config.targetPath+"/");
        }
    }
}
