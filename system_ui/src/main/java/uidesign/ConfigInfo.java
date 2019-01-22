package uidesign;

import java.io.Serializable;
import java.util.Set;

public class ConfigInfo implements Serializable {
    String modelPath;
    String dataPath;
    String targetPath;
    Set<String> stuid;
}
