package files;

import mainpackage.Error;
import mainpackage.Line;
import symbol.Symbol;
import symbol.SymbolTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CopyFile {

    private ArrayList<Line> lineArrayList;
    private boolean errorFound;

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
                    errorFound = true;
                }
                fileWriter.write(line + "\n");
            }
            fileWriter.write("\n\n\n-------------------------------------------------------------------------------");
            fileWriter.write("\n\n\t\t\t\t\t\t*****SYMBOL TABLE*****\n");
            fileWriter.write("\nLabel Name\t\t\tAddress\t\t\t\tInstruction Length\t\tRelocatable\n");

            Iterator<Symbol> iterator = SymbolTable.getInstance().getSymTab().values().iterator();

            while(iterator.hasNext())
            {
                fileWriter.write(iterator.next().toString());
            }


            if (errorFound){
                fileWriter.write("\n\n\n" + "errors found, no pass 2");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Line> getLineArrayList() {
        return lineArrayList;
    }

    public boolean isErrorFound() {
        return errorFound;
    }
}
