package preprocess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import info.debatty.java.stringsimilarity.*;

public class PatternedCommit implements Comparable<PatternedCommit>{
    public final static ArrayList<String> ReservedWord = new ArrayList<>(Arrays.asList(
            "break", "comment", "continue", "else", "for", "if", "return", "while", "printf"));

    private Date logTime;
    private int changedFilesNumber;
    private int insertLinesNumber;
    private int deleteLinesNumber;
    private int editDistance;
    private HashMap<String, Integer> addReservedWordNumber;

    public PatternedCommit() {
        logTime = null;
        changedFilesNumber = 0;
        insertLinesNumber = 0;
        deleteLinesNumber = 0;
        editDistance = 0;
        addReservedWordNumber = new HashMap<>();
    }

    public PatternedCommit(IntermediateCommit intermediateCommit) {
        addReservedWordNumber = new HashMap<>();
        logTime = extractLogTime(intermediateCommit.getLogTime());
        changedFilesNumber = extractEditInformation(intermediateCommit.getEditInformation(), "file");
        insertLinesNumber = extractEditInformation(intermediateCommit.getEditInformation(), "insertion");
        deleteLinesNumber = extractEditInformation(intermediateCommit.getEditInformation(), "deletion");
        editDistance = extractEditDistance(intermediateCommit.getAddCode(), intermediateCommit.getDeleteCode());
        countReservedWord(intermediateCommit.getAddCode());
    }

    public String toString() {
        String r = "Log Time: " + logTime.toString() + "\n"
                + "Changed Files: " + changedFilesNumber + "\n"
                + "Insertion Lines: " + insertLinesNumber + "\n"
                + "Delete Lines: " + deleteLinesNumber + "\n"
                + "Edit Distance: " + editDistance + "\n";
        for (String word : ReservedWord) {
            r += word + " :" + getReservedWordNumber(word) + "\n";
        }
        return r;
    }

    public int compareTo(PatternedCommit p) {
        return (int)(this.logTime.getTime() - p.getLogTime().getTime());
    }

    public Date getLogTime() {
        return logTime;
    }

    public int getChangedFilesNumber() {
        return changedFilesNumber;
    }

    public int getDeleteLinesNumber() {
        return deleteLinesNumber;
    }

    public int getEditDistance() {
        return editDistance;
    }

    public int getInsertLinesNumber() {
        return insertLinesNumber;
    }

    private void countReservedWord(String text) {
        for (String word : ReservedWord) {
            addReservedWordNumber.put(word, countWord(text, word));
        }
        addReservedWordNumber.replace("comment", countWord(text, "//") + countWord(text, "/*") + countWord(text, "*/"));
    }

    private int countWord(String text, String word) {
        int count = 0;
        int index = 0;
        /*
         * indexOf()的用法：返回字符中indexOf（String）中子串String在父串中首次出现的位置，从0开始！没有返回-1
         * 方便判断和截取字符串！
         */
        while ((index = text.indexOf(word, index)) != -1) {// 如果key在str中存在
            index = index + word.length();
            count++;// 找到一次统计一次
        }
        return count;
    }

    private int extractEditDistance(String s1, String s2) {
        Levenshtein l = new Levenshtein();
        return (int)l.distance(s1, s2);
    }

    private int extractEditInformation(String editInformation, String type) {
        int index = editInformation.indexOf(type);
        if (index < 0) {
            return 0;
        } else {
            int end = index;
            while (index >= 0 && editInformation.charAt(index) != ',') {
                index--;
            }
            return  Integer.parseInt(editInformation.substring(index + 2, end - 1));
        }
    }

    private Date extractLogTime(String s) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.ENGLISH);
        try {
            date = df.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getReservedWordNumber(String word) {
        if (addReservedWordNumber.containsKey(word)) {
            return addReservedWordNumber.get(word);
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.replace("a", 10);
        System.out.println(map.get("a"));
    }
}
