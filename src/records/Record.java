package records;

public abstract class Record {

    int address = -1;
    String name = "";
    int textLength;
    int programLength;
    String startOfLine = "";
    String objectCodesString = "";

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public int getProgramLength() {
        return programLength;
    }

    public void setProgramLength(int programLength) {
        this.programLength = programLength;
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

     String leftPad(String str){

        String padString = "000000";

        if(str.length() < 6 )
            return padString.substring(str.length()) + str;
        else
            return str;
    }

    @Override
    public abstract String toString();

}
