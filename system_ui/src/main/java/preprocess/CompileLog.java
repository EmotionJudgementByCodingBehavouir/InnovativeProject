package preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class CompileLog {
    private ArrayList<Date> log;

    public CompileLog(BufferedReader br) {
        log = new ArrayList<>();
        String s = null;
        try {
            while ((s = br.readLine()) != null) {
                log.add(extractLogTime(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(log);
    }

    private Date extractLogTime(String s) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.ENGLISH);
        try {
            date = df.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(date);
        return date;
    }

    public Iterator<Date> getCompileLogIterator() {
        return log.iterator();
    }

    public void display() {
        for (Date d : log) {
            System.out.println(d);
        }
    }

    public static void main(String[] args) {
        String fileName = "E:\\PA_project\\Compile_data\\151242017\\data.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            CompileLog cLog = new CompileLog(br);
            cLog.display();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
