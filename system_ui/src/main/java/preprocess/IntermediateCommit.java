package preprocess;

public class IntermediateCommit {
    private String logTime;
    private String editInformation;
    private String addCode;
    private String deleteCode;

    public IntermediateCommit(String logTime, String editInformation, String addCode, String deleteCode) {
        this.logTime = logTime;
        this.editInformation = editInformation;
        this.addCode = addCode;
        this.deleteCode = deleteCode;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getEditInformation() {
        return editInformation;
    }

    public String getAddCode() {
        return addCode;
    }

    public String getDeleteCode() {
        return deleteCode;
    }

    @Override
    public String toString() {
        return logTime + "\n" + editInformation + "\n" + addCode + "\n" + deleteCode;
    }
}
