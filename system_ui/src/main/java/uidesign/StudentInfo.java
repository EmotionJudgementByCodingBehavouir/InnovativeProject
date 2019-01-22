package uidesign;

public class StudentInfo {
    String SID;
    StudentInfo(){
        SID = "";
    }
    StudentInfo(String s) {
        SID = s;
    }
    public void setSID(String s){
        SID = s;
    }
    public String getSID(){
        return SID;
    }
}
