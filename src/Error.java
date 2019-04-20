public class Error {

    public static String[] errors;

    public static void fillErrorArray() {
        errors = new String[16];
        errors[0] = "***ERROR: misplaced label***";
        errors[1] = "***ERROR: missing or misplaced operation mnemonic***";
        errors[2] = "***ERROR: missing or misplaced operand field***";
        errors[3] = "***ERROR: duplicate label definition***";
        errors[4] = "***ERROR: this statement can't have a label***";
        errors[5] = "***ERROR: this statement can't have an operand***";
        errors[6] = "***ERROR: wrong operation prefix***";
        errors[7] = "***ERROR: unrecognized operation code***";
        errors[8] = "***ERROR: undefined symbol in operand***";
        errors[9] = "***ERROR: not a hexadecimal string***";
        errors[10] = "***ERROR: can't be format 4 instruction***";
        errors[11] = "***ERROR: illegal address for a register***";
        errors[12] = "***ERROR: missing END statement***";

        errors[13] = "***ERROR: label cannot start with a digit***";
        errors[14] = "***ERROR: wrong comment format***";
        errors[15] = "***ERROR: labels cannot have spaces in between***";
        errors[16] = "***ERROR: operation mnemonic cannot have spaces in between***";
    }
}
