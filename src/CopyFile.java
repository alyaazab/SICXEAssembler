import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CopyFile {


    private ArrayList<Line> lineArrayList;

    public CopyFile() {
        this.lineArrayList = new ArrayList<>();
    }

    public void addLineToList(Line line) {
        lineArrayList.add(line);
    }

    public void writeToCopyFile() {
        try {
            FileWriter fileWriter = new FileWriter("copyfile");
            for (Line line : lineArrayList) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
