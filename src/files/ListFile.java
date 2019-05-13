package files;

import mainpackage.Line;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ListFile {

    private ArrayList<Line> lineArrayList;

    public ListFile(ArrayList<Line> lineArrayList) {
        this.lineArrayList = lineArrayList;
    }

    public void writeToListFile(){
        try {
            FileWriter fileWriter = new FileWriter("copyfile", true);
            fileWriter.append("\n\n\n----Pass 2-----\n\n");
            for (Line line : lineArrayList){
                fileWriter.write(line + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
