package preprocess;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class LogSlicer {
    public ArrayList<Slice> getLogSlice(PatternedLog pLog, CompileLog cLog, int interval) {
        ArrayList<Slice> seriesOfSlices = new ArrayList<>();
        Iterator<Date> cIt= cLog.getCompileLogIterator();
        Iterator<PatternedCommit> pIt = pLog.getPatternedLogIterator();

        Date oldTime;
        if(pIt.hasNext()) {
            oldTime = pIt.next().getLogTime();
        }
        else {
            oldTime = new Date();
        }
        pIt = pLog.getPatternedLogIterator();
        ArrayList<Date> compileSlice = new ArrayList<>();
        ArrayList<PatternedCommit> pCommitSlice = new ArrayList<>();
        while (cIt.hasNext()) {
            if (cIt.next().getTime() >= oldTime.getTime()) {
                break;
            }
        }
        //System.out.println(cIt.next());
        while (pIt.hasNext()) {
            PatternedCommit pCommit = pIt.next();
            if (pCommit.getLogTime().getTime() - oldTime.getTime() < interval * 60000 && pCommit.getLogTime().getTime() - oldTime.getTime() >= 0) {
                pCommitSlice.add(pCommit);
            } else {
                while (cIt.hasNext()) {
                    Date cDate = cIt.next();
                    if (cDate.getTime() - oldTime.getTime() >= 0 && cDate.getTime() - oldTime.getTime() < interval * 60000) {
                        compileSlice.add(cDate);
                    } else if (cDate.getTime() - oldTime.getTime() >= interval * 60000){
                        break;
                    }
                }

                oldTime = pCommit.getLogTime();
               seriesOfSlices.add(new Slice(pCommitSlice, compileSlice, interval));
                pCommitSlice.clear();
                pCommitSlice.add(pCommit);
                compileSlice.clear();
            }
        }
        return seriesOfSlices;
    }
}
