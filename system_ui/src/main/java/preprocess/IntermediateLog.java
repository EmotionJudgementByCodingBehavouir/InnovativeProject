package preprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class IntermediateLog {
    private ArrayList<IntermediateCommit> intermediateLog;
    public IntermediateLog() {
        intermediateLog = new ArrayList<>();
    }
    public IntermediateLog(BufferedReader br) throws IOException {
        intermediateLog = new ArrayList<>();
        readGitLog(br);
    }

    public void readGitLog(BufferedReader br) throws IOException {
        String s = null;
        Boolean flag = false;
        String logTime = null;
        String editInfomation = null;
        String addCode = "";
        String deleteCode = "";

        while ((s = br.readLine()) != null) {
            //System.out.println(s);
            if (s.indexOf("+0800 : monitor---") >= 0) {
                if (flag == true) {
                    flag = false;
                    intermediateLog.add(new IntermediateCommit(logTime, editInfomation, addCode, deleteCode));
                }
                logTime = extractLogTime(s);
            } else if (s.indexOf(" file changed, ") >= 0 || s.indexOf(" files changed, ") >= 0) {
                editInfomation = s;
            } else if (s.startsWith("diff --git")) {
                if (s.substring(s.length() - 2, s.length()).equals(".h") || s.substring(s.length() - 2, s.length()).equals(".c")) {
                    flag = true;
                    addCode = "";
                    deleteCode = "";
                }
            } else if (flag == true && s.startsWith("+") && !s.startsWith("+++ ")) {
                addCode += s.substring(1, s.length());
            } else if (flag == true && s.startsWith("-") && !s.startsWith("--- ")) {
                deleteCode += s.substring(1, s.length());
            }
        }
        if (flag == true) {
            intermediateLog.add(new IntermediateCommit(logTime, editInfomation, addCode, deleteCode));
        }
        br.close();
    }

    private String extractLogTime(String s) {
        int index = s.indexOf("+0800 : monitor---");
        String logTime = s.substring(0, index - 1);
        //System.out.println(logTime);
        return logTime;
    }

    public void display() {
        for (IntermediateCommit c : intermediateLog) {
            System.out.println(c);
        }
    }

    public Iterator<IntermediateCommit> getIntermediateLogIterator() {
        return intermediateLog.iterator();
    }
}
