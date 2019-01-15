package predict.model;

import predict.emotion.Emotion;
import predict.emotion.EmotionLog;
import preprocess.Slice;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.*;
import weka.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class PredictModel {
    private RandomForest randomForest;
    private Instances data;
    public PredictModel(String modelPath) {
        try {
            randomForest = (RandomForest) weka.core.SerializationHelper.read(modelPath);
            //data = new Instances( new BufferedReader(new FileReader("data_add.arff")));
            //data.setClassIndex(data.numAttributes() - 1);
            /*randomForest.buildClassifier(data);
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(randomForest, data, 10, new Random(1));
            System.out.println(eval.toSummaryString("\nResult", false));
            System.out.println(eval.toClassDetailsString());*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public EmotionLog predict(Slice slice) {
        try {
            //data.add(new InstanceCreator().createInstance(slice));
            //System.out.println(randomForest.classifyInstance(new InstanceCreator().createInstance(slice).lastInstance()));
            double classify = randomForest.classifyInstance(new InstanceCreator().createInstance(slice).lastInstance());
            return new EmotionLog(slice.getLogTime(), Emotion.values()[(int)classify]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        return randomForest.toString();
    }

    public static void main(String[] args) {
        PredictModel m = new PredictModel("model.model");
        System.out.println(m);
    }
}
