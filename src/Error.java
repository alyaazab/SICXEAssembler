import java.util.ArrayList;

public class Error {

    public static String[] errors;

    public static void printErrors(ArrayList<Integer> errorIndexList) {
        for(int i=0; i<errorIndexList.size(); i++)
        {
            System.out.println(errorIndexList.get(i));
        }
    }
    public static void fillErrorArray() {
        errors = new String[30];

        errors[3] = "***ERROR: duplicate label definition***";
        errors[4] = "***ERROR: this statement can't have a label***";//
        errors[13] = "***ERROR: label cannot start with a digit***";//
        errors[15] = "***ERROR: labels cannot have spaces in between***";//
        errors[0] = "***ERROR: missing or misplaced label***"; //
        errors[19] = "***ERROR: operation mnemonics cannot be used as labels***"; //



        errors[5] = "***ERROR: this statement can't have an operand***";
        errors[6] = "***ERROR: wrong operation prefix***";
        errors[9] = "***ERROR: not a hexadecimal string***";
        errors[10] = "***ERROR: can't be format 4 instruction***";
        errors[20] = "***ERROR: EQU should have a previously defined operand***";//
        errors[21] = "***ERROR: this statement must have an operand***";//


        errors[14] = "***ERROR: wrong comment format***";//
        errors[16] = "***ERROR: operation mnemonic cannot have spaces in between***";//
        errors[17] = "***ERROR: missing or misplaced comma in format 2 operation***";//
        errors[7] = "***ERROR: unrecognized operation code***"; //
        errors[8] = "***ERROR: undefined symbol in operand***"; //
        errors[1] = "***ERROR: missing or misplaced operation mnemonic***"; //
        errors[2] = "***ERROR: missing or misplaced operand field***";//
        errors[11] = "***ERROR: illegal address for a register***";//
        errors[18] = "***ERROR: extra characters at end of statement***";//

        errors[12] = "***ERROR: missing END statement***";

    }
}
