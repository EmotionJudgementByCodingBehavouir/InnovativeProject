package preprocess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Slice {
    private Date logTime;
    private int insertLinesNumber;
    private int deleteLinesNumber;
    private double insertLinesPerCommit;
    private double deleteLinesPerCommit;
    private int commitNumber;
    private int compileNumber;
    private int changedFilesNumber;
    private double varOfCommitTime;
    private double varOfCompileTime;
    private HashMap<String, Integer> addReservedWordNumber;
    private int editDistance;

    public Slice() {
        logTime = new Date();
        insertLinesNumber = 0;
        deleteLinesNumber = 0;
        insertLinesPerCommit = 0;
        deleteLinesPerCommit = 0;
        commitNumber = 0;
        compileNumber = 0;
        changedFilesNumber = 0;
        varOfCommitTime = 0;
        varOfCompileTime = 0;
        addReservedWordNumber = new HashMap<>();
        editDistance = 0;
    }

    public String toString() {
        String r = logTime.toString() + "\n";
        r += "InsertLinesNumber: " + insertLinesNumber + "\n";
        r += "DeleteLinesNumber: " + deleteLinesNumber + "\n";
        r += "InsertLinesPerCommit: " + insertLinesPerCommit + "\n";
        r += "DeleteLinesPerCommit: " + deleteLinesPerCommit + "\n";
        r += "CommitNumber: " + commitNumber + "\n";
        r += "CompileNumber: " + compileNumber + "\n";
        r += "ChangedFilesNumber: " + changedFilesNumber + "\n";
        r += "VarOfCommitTime: " + varOfCommitTime + "\n";
        r += "VarOfCompileTime: " + varOfCompileTime + "\n";
        for (String word : PatternedCommit.ReservedWord) {
            r += "Add " + word + ": " + getReservedWordNumber(word) + "\n";
        }
        r += "EditDistance: " + editDistance + "\n";
        return r;
    }

    public Slice(ArrayList<PatternedCommit> pCommits, ArrayList<Date> compiles, int interval) {
        logTime = new Date(pCommits.get(pCommits.size() - 1).getLogTime().getTime());
        insertLinesNumber = 0;
        for (PatternedCommit p : pCommits) {
            insertLinesNumber += p.getInsertLinesNumber();
        }
        deleteLinesNumber = 0;
        for (PatternedCommit p : pCommits) {
            deleteLinesNumber += p.getDeleteLinesNumber();
        }
        insertLinesPerCommit = insertLinesNumber / pCommits.size();
        deleteLinesPerCommit = deleteLinesNumber / pCommits.size();
        commitNumber = pCommits.size();
        compileNumber = compiles.size();
        changedFilesNumber = 0;
        for (PatternedCommit p : pCommits) {
            changedFilesNumber += p.getChangedFilesNumber();
        }
        varOfCommitTime = calculateVarOfCommit(pCommits);
        varOfCompileTime = calculateVarOfCompile(compiles);
        addReservedWordNumber = new HashMap<>();
        for (String word : PatternedCommit.ReservedWord) {
            addReservedWordNumber.put(word, 0);
            for (PatternedCommit p : pCommits) {
                addReservedWordNumber.replace(word, addReservedWordNumber.get(word) + p.getReservedWordNumber(word));
            }
        }
        editDistance = 0;
        for (PatternedCommit p : pCommits) {
            editDistance += p.getEditDistance();
        }
    }

    private double calculateVarOfCommit(ArrayList<PatternedCommit> pCommits) {
        double sum = 0;
        for (PatternedCommit p : pCommits) {
            sum += p.getLogTime().getTime() / 60000;
        }
        double avg = sum / pCommits.size();
        double var = 0;
        for (PatternedCommit p : pCommits) {
            var += Math.pow(p.getLogTime().getTime() / 60000 - avg, 2) / pCommits.size();
        }
        return var;
    }

    private double calculateVarOfCompile(ArrayList<Date> compiles) {
        double sum = 0;
        for (Date p : compiles) {
            sum += p.getTime() / 60000;
        }
        double avg = sum / compiles.size();
        double var = 0;
        for (Date p : compiles) {
            var += Math.pow(p.getTime() / 60000 - avg, 2) / compiles.size();
        }
        return var;
    }

    public int getReservedWordNumber(String word) {
        if (addReservedWordNumber.containsKey(word)) {
            return addReservedWordNumber.get(word);
        } else {
            return 0;
        }
    }

    public void setChangedFilesNumber(int changedFilesNumber) {
        this.changedFilesNumber = changedFilesNumber;
    }

    public void setCommitNumber(int commitNumber) {
        this.commitNumber = commitNumber;
    }

    public void setCompileNumber(int compileNumber) {
        this.compileNumber = compileNumber;
    }

    public void setDeleteLinesNumber(int deleteLinesNumber) {
        this.deleteLinesNumber = deleteLinesNumber;
    }

    public void setDeleteLinesPerCommit(int deleteLinesPerCommit) {
        this.deleteLinesPerCommit = deleteLinesPerCommit;
    }

    public void setEditDistance(int editDistance) {
        this.editDistance = editDistance;
    }

    public void setInsertLinesNumber(int insertLinesNumber) {
        this.insertLinesNumber = insertLinesNumber;
    }

    public void setInsertLinesPerCommit(int insertLinesPerCommit) {
        this.insertLinesPerCommit = insertLinesPerCommit;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public void setVarOfCommitTime(double varOfCommitTime) {
        this.varOfCommitTime = varOfCommitTime;
    }

    public void setVarOfCompileTime(double varOfCompileTime) {
        this.varOfCompileTime = varOfCompileTime;
    }

    public void setAddReservedWordNumber(String word, int count) {
        if (addReservedWordNumber.containsKey(word)) {
            addReservedWordNumber.replace(word, count);
        } else {
            addReservedWordNumber.put(word, count);
        }
    }

    public int getInsertLinesNumber() {
        return insertLinesNumber;
    }

    public int getDeleteLinesNumber() {
        return deleteLinesNumber;
    }

    public int getChangedFilesNumber() {
        return changedFilesNumber;
    }

    public int getEditDistance() {
        return editDistance;
    }

    public Date getLogTime() {
        return logTime;
    }

    public double getVarOfCommitTime() {
        return varOfCommitTime;
    }

    public double getVarOfCompileTime() {
        return varOfCompileTime;
    }

    public int getCommitNumber() {
        return commitNumber;
    }

    public int getCompileNumber() {
        return compileNumber;
    }

    public double getDeleteLinesPerCommit() {
        return deleteLinesPerCommit;
    }

    public double getInsertLinesPerCommit() {
        return insertLinesPerCommit;
    }
}
