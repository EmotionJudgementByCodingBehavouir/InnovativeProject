package predict.model;

import preprocess.PatternedCommit;
import preprocess.Slice;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class InstanceCreator {
    private static ArrayList<String> attributeName = new ArrayList<>(Arrays.asList(
            "insertion", "deletion", "insertion_per", "deletion_per", "coding_frequency", "conmpile", "file_changed",
            "d_time", "d_compile", "add_break", "add_comment", "add_continue", "add_else", "add_for", "add_if", "add_return",
            "add_while", "add_printf", "edit_distance"
    ));
    private ArrayList<String> classVal = new ArrayList<String>(Arrays.asList("flow", "noflow"));
    private static ArrayList<Attribute> attributes = null;
    private static Instances instances = null;

    public InstanceCreator() {
        if (attributes == null) {
            attributes = new ArrayList<>();
            for (String s : attributeName) {
                attributes.add(new Attribute(s));
            }
            attributes.add(new Attribute("state", classVal));
        }
        if (instances == null) {
            instances = new Instances("emotionPredict", attributes, 0);
            instances.setClassIndex(instances.numAttributes() - 1);
        }
    }

    public Instances createInstance(Slice slice) {
        Instance instance = new DenseInstance(attributes.size());
        instance.setValue(0, slice.getInsertLinesNumber());
        instance.setValue(1, slice.getDeleteLinesNumber());
        instance.setValue(2, slice.getInsertLinesPerCommit());
        instance.setValue(3, slice.getDeleteLinesPerCommit());
        instance.setValue(4, slice.getCommitNumber());
        instance.setValue(5, slice.getCompileNumber());
        instance.setValue(6, slice.getChangedFilesNumber());
        instance.setValue(7, slice.getVarOfCommitTime());
        instance.setValue(8, slice.getVarOfCompileTime());
        int k = 9;
        for (String word : PatternedCommit.ReservedWord) {
            instance.setValue(k, slice.getReservedWordNumber(word));
            k++;
        }
//        instance.setValue(19, -1);
        instance.setDataset(instances);
        instances.add(instance);
        return instances;
    }


}
