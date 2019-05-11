import java.util.ArrayList;

public class ObjectCodeGenerator {

    ArrayList<Line> lineArrayList;
    private int n=0, i=0, x=0, b=0, p=0, e=0;
    private String opcode = "";
    private int operationFormat=0;
    private String instructionCode = "", r1="", r2="", flags="",  disp="", address="";

    public ObjectCodeGenerator(ArrayList<Line> lineArrayList) {
        this.lineArrayList = lineArrayList;
    }

    public void generateObjectCode() {
        for(Line line : lineArrayList)
        {
            instructionCode = "";
            //if line is a comment, skip
            if(!line.getComment().equals(""))
                continue;

            Operation operation = line.getOperation();
            String operandField = line.getOperandField();


            //print operation
            if(operation!=null)
            {
                System.out.println(operation.toString());
                operationFormat = operation.getFormat();
            }
            else
            {
                System.out.println("operation is null");
                continue;
            }




            switch(operationFormat)
            {
                case -1:
                    //directive, no opcode

                    System.out.println("KITTY B = " + b + "   P = " + p);

                    break;
                case 2:
                    opcode = convertHexToBin(operation.getBinaryCode());
                    System.out.println("opcode = " + opcode);

                    //get r1 and r2
                    r1 = leftPad(convertDecToBin(RegisterTable.getInstance().getRegTable()
                            .get(operandField.substring(0, 1)).getAddress()), 4);
                    if(operandField.trim().length() > 1)
                        r2 = leftPad(convertDecToBin(RegisterTable.getInstance().getRegTable()
                                .get(operandField.substring(2, 3)).getAddress()),4);

                    break;


                case 3:
                    opcode = convertHexToBin(operation.getBinaryCode()).substring(0,6);
                    System.out.println("opcode = " + opcode);
                    e = 0;

                    setNIXFlags(operandField);

                    if(n == 1 && i == 1 && x == 0) // direct, without indexing
                    {
                        setBPFlags(operandField.trim(), line);
                    }else if (n == 1 && i == 1 && x == 1){ // direct, with indexing
                        String subOperand = operandField.trim().substring(0, operandField.trim().length() - 1);
                        setBPFlags(subOperand, line);
                    }else if (n == 1 && i == 0){ // indirect
                        String subOperand = operandField.trim().substring(1);
                        setBPFlags(subOperand, line);
                    }else if(n == 0 && i == 1){
                        String subOperand = operandField.trim().substring(1);
                        try {
                            int imm = Integer.parseInt(subOperand);
                            p = 0;
                            b = 0;
                            String paddedString = leftPad(convertDecToBin(imm), 12);
                            System.out.println("binary value: " + paddedString);
                        }catch (NumberFormatException e){
                            setBPFlags(subOperand, line);
                        }
                    }
                    System.out.println("KITTY B = " + b + "   P = " + p);
                    break;


                case 4:
                    opcode = convertHexToBin(operation.getBinaryCode()).substring(0,6);
                    System.out.println("opcode = " + opcode);
                    e = 1;

                    break;
            }

            instructionCode = instructionCode + opcode + r1 + r2;
            System.out.println("INSTRUCTION CODE: " + instructionCode);

        }
    }

    private void setBPFlags(String str, Line line) {
        //check for PC relative or Base relative to set b and p flags
        int targetAddress = SymbolTable.getInstance().getSymbol(str).getValue();
        int displacement = targetAddress - line.getAddress();

        if(displacement >= -2048 && displacement <= 2047)
        {
            p=1;
            b=0;
        }
        else if(line.isBaseRegisterSet())
        {
            p=0;
            b=1;
        }
        else
        {
            System.out.println("disp is = " + displacement);
            System.out.println("DISPLACEMENT OUT OF RANGE, CANNOT USE PC OR BASE RELATIVE ADDRESSING");
            p=0;
            b=0;
        }
    }

    private String convertHexToBin(String hexadecimal) {

        int decimal = Integer.parseInt(hexadecimal, 16);
        String binary = Integer.toBinaryString(decimal);


        return leftPad(binary, 8);
    }

    private void setNIXFlags(String operandField) {
        if (operandField.charAt(0) == '#') {
            i = 1;
            x = 0;
            n = 0;
            System.out.println("addressing mode: immediate" );
        } else if(operandField.charAt(0) == '@'){
            n = 1;
            i = 0;
            x = 0;
            System.out.println("addressing mode: indirect");
        } else {
            String trimmedOperand = operandField.trim();
            if (trimmedOperand.charAt(trimmedOperand.length() - 2) == ','){
                n = 1;
                i = 1;
                x = 1;
                System.out.println("addressing mode: direct, with indexing");
            } else {
                n = 1;
                i = 1;
                x = 0;
                System.out.println("addressing mode: direct, without indexing");
            }
        }
    }

    private String convertDecToBin(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    private static String leftPad(String str, int n){

        String padString;

        if(n==8)
        {
            padString = "00000000";
            if(str.length() < 8 )
                return padString.substring(str.length()) + str;
        }
        else if(n==4)
        {
            padString = "0000";

            if(str.length() < 4)
                return padString.substring(str.length()) + str;
        }else if (n == 12) {

            padString = "000000000000";

            if(str.length() < 12)
                return padString.substring(str.length()) + str;

        }


        return str;
    }

}
