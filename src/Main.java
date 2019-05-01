import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        OperationTable.fillOpTable();
        Error.fillErrorArray();
        Parser parser = new Parser();
        RegisterTable.getInstance().fillRegisterTable();


        CopyFile copyFile = new CopyFile();
        ArrayList<String> sourceProgram = SourceFile.readSourceProgramFromFile();
        Line currentLine = null;

        System.out.println("ARRAYLIST SIZE IS " + sourceProgram.size());
        for(int i=0; i<sourceProgram.size(); i++)
        {
            System.out.println("\n\nLC = " + LocationCounter.LC);
            currentLine = parser.extractFields(sourceProgram.get(i));
            //if this line is a comment, skip over it
            if(currentLine.getComment() != null){
                System.out.println("comm");
            }

            if(parser.isEndStatementFound() && i != sourceProgram.size() -1){
                parser.setStatementAfterEndFound(true);
            }

            System.out.println("SIZE IN MAIN: " + currentLine.getErrorIndexList().size());
            if(!parser.isEndStatementFound() && i == sourceProgram.size()-1)
            {
                currentLine.getErrorIndexList().add(12);
            }
            copyFile.addLineToList(currentLine);
        }

        if (parser.isStatementAfterEndFound()){
            Objects.requireNonNull(currentLine).getErrorIndexList().add(23);
        }

        copyFile.writeToCopyFile();

    }

}
