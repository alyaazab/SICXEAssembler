package records;

public abstract class Record {

    String address = "";
    String name = "";
    String textLength;
    String programLength;
    String startOfLine = "";
    String objectCodesString = "";


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextLength() {
        return textLength;
    }

    public void setTextLength(String textLength) {
        this.textLength = textLength;
    }

    public String getProgramLength() {
        return programLength;
    }

    public void setProgramLength(String programLength) {
        this.programLength = programLength;
    }

    public String getStartOfLine() {
        return startOfLine;
    }

    public void setStartOfLine(String startOfLine) {
        this.startOfLine = startOfLine;
    }

    public String getObjectCodesString() {
        return objectCodesString;
    }

    public void setObjectCodesString(String objectCodesString) {
        this.objectCodesString = objectCodesString;
    }

    public void addToObjectCodesString(String objectCode){
        this.objectCodesString += objectCode;
    }

    public int getObjectCodesStringLength(){
        return objectCodesString.length();
    }

    @Override
    public abstract String toString();

}
