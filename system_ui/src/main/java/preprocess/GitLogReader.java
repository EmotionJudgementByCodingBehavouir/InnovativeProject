package preprocess;

import predict.model.PredictModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GitLogReader {
    private String fileName;
    public GitLogReader(String fileName) {
        this.fileName = fileName;
    }
    public IntermediateLog getIntermediateLog() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        return new IntermediateLog(br);
    }

    public static void main(String[] args) {
        try {
            IntermediateLog log = new GitLogReader("E:\\PA_project\\getlog\\Log\\151242017\\detail\\Week5.log").getIntermediateLog();
            PatternedLog pLog = new PatternedLog(log);
            CompileLog cLog = new CompileLog(new BufferedReader(new FileReader("E:\\PA_project\\Compile_data\\151242017\\data.txt")));
            ArrayList<Slice> slices = new LogSlicer().getLogSlice(pLog, cLog, 15);
            PredictModel model = new PredictModel("model/model.model");
            for (Slice s : slices) {
                System.out.println(model.predict(s));
                //System.out.println(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
