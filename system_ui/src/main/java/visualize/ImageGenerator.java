package visualize;
import javafx.scene.image.Image;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import predict.model.*;
import predict.emotion.*;
import preprocess.*;


import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.Font;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class ImageGenerator {
    private ArrayList<EmotionLog> emotionLogs = new ArrayList<>();
    private String id  = null;
    private static String basePath = "C:/Users/99752/Desktop/DataSet/";
    private static String targetPath = "C:/Users/99752/Desktop/Images/";
    public static void setBasePath(String basePath) {ImageGenerator.basePath = basePath;}
    public static void setTargetPath(String targetPath) {ImageGenerator.targetPath = targetPath; }
    private ArrayList<Image> images = new ArrayList<>();
    //是否需要更新
    private boolean isDirty(String id){
        File path = new File(targetPath + id);
        if(!path.exists()) {
            path.mkdir();
        }
        else {
            File flag = new File(targetPath + id + "/flag.txt");
            if(!flag.exists()) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(flag));
                    String gitPath = basePath + "git/" + id + "/detail";
                    int count = new File(gitPath).listFiles().length;
                    bufferedWriter.write(""+count);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Scanner scanner = new Scanner(new FileInputStream(flag));
                    String gitPath = basePath + "git/" + id + "/detail";
                    int count = new File(gitPath).listFiles().length;
                    if(count == scanner.nextInt()) {
                        return false;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    //更新
    private void update() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(targetPath + id + "/flag.txt")));
            String gitPath = basePath + "git/" + id + "/detail";
            int count = new File(gitPath).listFiles().length;
            bufferedWriter.write(""+count);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ImageGenerator(String modelPath, String id) {
        this.id = id;
        images.add(new Image("defaultCanvas.jpeg"));
        images.add(new Image("defaultCanvas.jpeg"));
        //进行判断，是否需要更新
        //不需要更新
        if(!isDirty(id)) {
            System.out.println("不需要更新");
            File file = new File(targetPath +id+"/proportion.jpeg");
            if(file.exists()) {
                images.set(0,new Image("file:"+targetPath +id+"/proportion.jpeg"));
            }
            file = new File(targetPath +id+"/Trend.jpeg");
            if(file.exists()) {
                images.set(1,new Image("file:"+targetPath +id+"/Trend.jpeg"));
            }
            return;
        }
        //需要更新
        try {
            String gitPath = basePath + "git/" + id + "/detail";
            String compilePath = basePath + "compile/" + id + "/data.txt";
            File[] gitLogs = new File(gitPath).listFiles();
            PredictModel model = new PredictModel(modelPath);
            CompileLog compileLog = new CompileLog(new BufferedReader(new FileReader(compilePath)));
            for(File gitLog : gitLogs) {
                if(gitLog.length() != 0) {
                    System.out.println(gitLog);
                    IntermediateLog log = new GitLogReader(gitLog.getPath()).getIntermediateLog();
                    PatternedLog patternedLog = new PatternedLog(log);
                    ArrayList<Slice> slices = new LogSlicer().getLogSlice(patternedLog,compileLog,15);
                    for(Slice slice : slices) {
                        EmotionLog emotionRecord = model.predict(slice);
                        emotionLogs.add(emotionRecord);
                    }
                }
            }
            Collections.sort(emotionLogs);
            for(EmotionLog emotionLog : emotionLogs) {
                System.out.println(emotionLog);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        generateTrend();
        generateProportion();
        update();
    }
    public ArrayList<Image> getImages() {
        return images;
    }


    private void generateProportion() {
        try {
            int numOfFlow = 0;
            int sum = emotionLogs.size();
            for(EmotionLog emotionLog : emotionLogs) {
                if(emotionLog.getEmotion() == Emotion.FLOW) {
                    numOfFlow ++;
                }
            }
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("积极情绪", numOfFlow);
            dataset.setValue("消极情绪",sum - numOfFlow);

            //设置图表标题等
            JFreeChart chart = ChartFactory.createPieChart("情绪比例图",dataset,true,false,false);
            chart.setTitle(new TextTitle("情绪比例分布",new Font("黑体",Font.BOLD,20)));
            LegendTitle legendTitle = chart.getLegend(0);
            legendTitle.setItemFont(new Font("黑体",Font.TYPE1_FONT,16));
            PiePlot plot = (PiePlot)chart.getPlot();
            plot.setLabelFont(new Font("黑体", Font.HANGING_BASELINE, 12));
            plot.setNoDataMessage("无数据显示");
            //设置百分比显示
            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            StandardPieSectionLabelGenerator sp = new StandardPieSectionLabelGenerator("{0} {2}",numberFormat,decimalFormat);
            plot.setLabelGenerator(sp);
            //导出图片
            OutputStream os = new FileOutputStream(targetPath +id+"/proportion.jpeg");
            ChartUtils.writeChartAsJPEG(os,chart,1000,800);
            images.set(0,new Image("file:"+targetPath +id+"/proportion.jpeg"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateTrend() {
        try {
            if(emotionLogs.size() != 0) {
                TimeSeries flowTrend = new TimeSeries("Flow");
                TimeSeries notFlowTrend = new TimeSeries("Notflow");
                int flowCount = 0;
                int notFlowCount = 0;
                Date oldDate = (Date)emotionLogs.get(0).getLogTime().clone();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                for(EmotionLog emotionRecord : emotionLogs) {
                    String key = format.format(emotionRecord.getLogTime());
                    if(key.equals(format.format(oldDate))) {
                        if(emotionRecord.getEmotion() == Emotion.FLOW) {
                            flowCount ++;
                        }
                        else {
                            notFlowCount ++;
                        }
                    }
                    else {
                        String temp = format.format(oldDate);
                        int year = Integer.parseInt(temp.substring(0,4));
                        int month = Integer.parseInt(temp.substring(4,6));
                        int day = Integer.parseInt(temp.substring(6,8));
                        flowTrend.add(new Day(day,month,year),flowCount);
                        notFlowTrend.add(new Day(day,month,year),notFlowCount);
                        flowCount = 0;
                        notFlowCount = 0;
                        oldDate = (Date)emotionRecord.getLogTime().clone();
                    }
                }
                TimeSeriesCollection trend = new TimeSeriesCollection();
                trend.addSeries(flowTrend);
                trend.addSeries(notFlowTrend);
                XYDataset xyDataset = trend;
                JFreeChart chart = ChartFactory.createTimeSeriesChart("情绪变化趋势","Date","Frequency",xyDataset,true,true,true);
                XYPlot plot = (XYPlot)chart.getPlot();
                DateAxis dateAxis = (DateAxis)plot.getDomainAxis();
                plot.setNoDataMessage("无数据显示");
                dateAxis.setLabelFont(new Font("黑体",Font.BOLD,16));
                dateAxis.setTickLabelFont(new Font("仿体",Font.BOLD,12));  //垂直标题
                chart.getTitle().setFont(new Font("黑体",Font.BOLD,20));
                chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
                OutputStream os = new FileOutputStream(targetPath +id+"/Trend.jpeg");
                ChartUtils.writeChartAsJPEG(os,chart,1000,800);
                //imagesPaths.add(targetPath+id+"/Trend.jpeg");
                images.set(1, new Image("file:"+targetPath+id+"/Trend.jpeg"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<String> ids = new ArrayList<>();
        File[] files = new File(basePath+"git").listFiles();
        for (File file : files) {
            ids.add(file.getName());
        }
        System.out.println(ids);
        //ImageGenerator imageGenerator = new ImageGenerator("C:\\Users\\99752\\Desktop\\model.model","171860518");
        for(String id : ids) {
            ImageGenerator imageGenerator = new ImageGenerator("C:\\Users\\99752\\Desktop\\model.model",id);
        }
    }
}
