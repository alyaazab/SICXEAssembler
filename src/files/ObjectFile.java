package files;

import mainpackage.Line;
import records.EndRecord;
import records.HeaderRecord;
import records.Record;
import records.TextRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ObjectFile {


    private ArrayList<Record> records;
    private boolean isHeaderCreated;
    private Record textRecord, headerRecord, endRecord;
    private String firstExecAddress;
    private boolean isFirstExecFound;
    private String startAddress;

    public ObjectFile() {
        records = new ArrayList<>();
        textRecord = new TextRecord();
        headerRecord = new HeaderRecord();
        endRecord = new EndRecord();
    }

    public void addToRecords(Line line){

        System.out.println("ADD TO RECORDS");
        if (!line.getComment().equals("")) {
            System.out.println("COMMENT");
            return;
        }
        if (!isHeaderCreated && (line.getOperation().getOperationMnemonic().equals("start")
                || line.getOperation().getFormat() != -1)){
            isHeaderCreated = true;
            headerRecord.setName(line.getLabelField().trim());
            headerRecord.setAddress(leftPad(Integer.toHexString(line.getAddress())));
            records.add(headerRecord);
            startAddress = leftPad(Integer.toHexString(line.getAddress()));
            if (line.getOperation().getFormat() != -1) {
                firstExecAddress = headerRecord.getAddress();
                isFirstExecFound = true;
            }
        } else if (line.getOperation().getOperationMnemonic().equals("end")) {
            endRecord.setAddress(firstExecAddress);
            int end = line.getAddress();
            int start = Integer.parseInt(startAddress, 16);
            String endAddress = leftPad(Integer.toHexString(end-start));
            headerRecord.setProgramLength(endAddress);

            start = Integer.parseInt(textRecord.getAddress(),16);
            textRecord.setTextLength(Integer.toHexString(end-start));
            records.add(textRecord);
            records.add(endRecord);
        } else {
            if (line.getOperation().getFormat() == -1
                    && !line.getOperation().getOperationMnemonic().equals("byte")
                    && !line.getOperation().getOperationMnemonic().equals("word")
                    && !line.getOperation().getOperationMnemonic().equals("org")) {
                System.out.println("DIRECTIVE");
                return;
            }

            if (!line.getOperation().getOperationMnemonic().equals("org")){
                if (textRecord.getAddress().equals("")){
                    textRecord.setAddress(leftPad(Integer.toHexString(line.getAddress())));
                } if (textRecord.getObjectCodesStringLength() + line.getOperation().getFormat() <= 60){
                    textRecord.addToObjectCodesString(line.getObjectCode());
                } else {
                    recordHelper(line);
                }
            } else {
                recordHelper(line);
            }
        }
        if (!isFirstExecFound && line.getOperation().getFormat() != -1) {
            firstExecAddress = leftPad(Integer.toHexString(line.getAddress()));
            isFirstExecFound = true;
        }

        System.out.println("header record: " + headerRecord.toString());
        System.out.println("text record: " + textRecord.toString());
        System.out.println("end record: " + endRecord.toString());

    }

    private void recordHelper(Line line) {
        int end = line.getAddress();
        int start = Integer.parseInt(textRecord.getAddress(),16);
        textRecord.setTextLength(Integer.toHexString(end-start));
        records.add(textRecord);
        textRecord = new TextRecord();
        textRecord.setAddress(leftPad(Integer.toHexString(line.getAddress())));
        textRecord.addToObjectCodesString(line.getObjectCode());
    }

    public void writeToObjectFile() {
        try {
            FileWriter fileWriter = new FileWriter("objfile");
            for (Record record : records){
                fileWriter.write(record + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String leftPad(String str){

        String padString = "000000";

        if(str.length() < 6 )
            return padString.substring(str.length()) + str;
        else
            return str;
    }

}
