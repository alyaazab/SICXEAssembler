import javax.xml.transform.Source;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        OperationTable.fillOpTable();
        Error.fillErrorArray();
        Utils utils = new Utils();
        RegisterTable.getInstance().fillRegisterTable();

        ArrayList<String> sourceProgram = SourceFile.readSourceProgramFromFile();
        Line currentLine;

        for(int i=0; i<sourceProgram.size(); i++)
        {
            currentLine = utils.extractFields(sourceProgram.get(i));
            //if this line is a comment, skip over it
            if(currentLine.getComment().length() != 0){
                System.out.println("comm");
            }
            System.out.println("not comment");
        }
        utils.extractFields("");


    }

}
