package preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PatternedLog {
    private ArrayList<PatternedCommit> log;

    public PatternedLog(IntermediateLog intermediateLog) {
        log = new ArrayList<>();
        Iterator<IntermediateCommit> it = intermediateLog.getIntermediateLogIterator();
        while (it.hasNext()) {
            log.add(new PatternedCommit(it.next()));
        }
        Collections.sort(log);
    }

    public Iterator<PatternedCommit> getPatternedLogIterator() {
        return log.iterator();
    }

    public void display() {
        for (PatternedCommit p : log) {
            System.out.println(p);
        }
    }
}
