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
        System.out.println("SIZE IN CF: " + lineArrayList.get(lineArrayList.size()-1).getErrorIndexList().size());
    }

    public void writeToCopyFile() {
        try {
            FileWriter fileWriter = new FileWriter("copyfile");
            for (Line line : lineArrayList) {
                System.out.println(line);
                System.out.println("SIZE: " + line.getErrorIndexList().size());
                if (line.getErrorIndexList().size() > 0) {
                    for (Integer integer : line.getErrorIndexList()) {
                        String error = Error.getError(integer);
                        System.out.println("ERROR: " + error);
                    }
                }
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
