package visualize;
import predict.model.*;
import predict.emotion.*;
import preprocess.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ImageGenerator {

    public ArrayList<String> getImagesPaths() {

        return null;
    }

    public ImageGenerator(String modelPath, String gitLogPath, String compileLogPath) {
        System.out.println(modelPath + " "+gitLogPath+" "+compileLogPath);
        try {
            IntermediateLog log = new GitLogReader(gitLogPath).getIntermediateLog();
            PatternedLog patternedLog = new PatternedLog(log);
            CompileLog compileLog = new CompileLog(new BufferedReader(new FileReader(compileLogPath)));
            ArrayList<Slice> slices = new LogSlicer().getLogSlice(patternedLog,compileLog,15);
            PredictModel model = new PredictModel(modelPath);
            for(Slice slice : slices) {
                System.out.println(model.predict(slice));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generate() {

    }
}
