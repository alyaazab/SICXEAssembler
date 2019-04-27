import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        SymbolTable symbolTable = SymbolTable.getInstance();
        OperationTable.fillOpTable();
        Error.fillErrorArray();
        Parser parser = new Parser();
        RegisterTable.getInstance().fillRegisterTable();

        ArrayList<String> sourceProgram = SourceFile.readSourceProgramFromFile();
        Line currentLine;

        System.out.println("ARRAYLIST SIZE IS " + sourceProgram.size());
        for(int i=0; i<sourceProgram.size(); i++)
        {
            System.out.println("\n\nLC = " + LocationCounter.LC);
            currentLine = parser.extractFields(sourceProgram.get(i));
            //if this line is a comment, skip over it
            if(currentLine.getComment() != null){
                System.out.println("comm");
                continue;
            }

        }

    }

}
