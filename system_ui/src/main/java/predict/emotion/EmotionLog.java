package predict.emotion;

import java.util.Date;

public class EmotionLog implements Comparable<EmotionLog>{
    private Date logTime;
    private Emotion emotion;
    public EmotionLog(Date date, Emotion emotion) {
        logTime = (Date)date.clone();
        this.emotion = emotion;
    }

    public Date getLogTime() {
        return logTime;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public String toString() {
        return logTime.toString() + " " + emotion.toString();
    }

    @Override
    public int compareTo(EmotionLog o) {
        return logTime.compareTo(o.logTime);
    }
}
