package mainpackage;

import operation.Operation;
import register.RegisterTable;
import symbol.SymbolTable;

public class ObjectCodeGenerator {

    private int n = 0, i=0, x = 0, b = 0, p = 0, e = 0;
    private String opcode = "";
    private int operationFormat = 0;
    private String instructionCode = "", r1 = "", r2 = "", flags = "", disp = "", address = "";
    private int baseRegisterOperand = -1;
    private String instructionObjectCode = "";
    private Line line;
    private boolean addressExpression = false;
    String binaryAddress = "";


    public ObjectCodeGenerator() {
    }

    public void generateObjectCode(Line line) {
        this.line = line;
        instructionCode = "";
        instructionObjectCode = "";
        binaryAddress = "";


        //if line is a comment, skip
        if (!line.getComment().equals(""))
            return;

        Operation operation = line.getOperation();
        String operandField = line.getOperandField();


        //print operation
        if (operation != null) {
            System.out.println(operation.toString());
            operationFormat = operation.getFormat();
        } else {
            System.out.println("operation is null");
            return;
        }
        int valueOfString;

        if(Postfix.containsOperator(line.getOperandField().trim()))
        {
            if(Postfix.infixToPostfix(line.getOperandField().trim()))
            {
                //look for symbols in symboltable
                for(String string : Postfix.listOfOperands)
                {
                    System.out.println("postfix string: " + string);
                    try{
                        valueOfString = Integer.parseInt(string);
                        System.out.println("postfix: " + valueOfString);
                    }catch (NumberFormatException e) {
                        if(SymbolTable.getInstance().getSymbol(string) == null)
                        {
                            line.getErrorIndexList().add(28);
                            System.out.println("POSTFIXERROR ON STRING " + string);
                            return;
                        }
                    }

                }
                addressExpression = true;
                int postfixResult = Postfix.evaluatePostfixExpression();
                n = 1;
                i = 1;
                x = 0;
                opcode = convertHexToBin(operation.getBinaryCode()).substring(0, 6);
                if(operationFormat==3)
                    e=0;
                else if(operationFormat==4)
                    e=1;

                setBPFlags(postfixResult, line);

                instructionCode = instructionCode + opcode + n + i + x + b + p + e + binaryAddress;
                System.out.println("POSTFIX CODE: " + instructionCode);
                instructionObjectCode = convertBinaryToHex(instructionCode);
                line.setObjectCode(instructionObjectCode);
//                this.instructionObjectCode = leftPad(convertBinaryToHex(instructionCode), 6);

//                formInstructionCode();
//                createObjectCode();
                return;
            }



        }

        if (operation.getOperationMnemonic().equals("base") && line.isBaseRegisterSet()) {
            String operand = line.getOperandField().trim();
            if (operand.charAt(0) == '#' || operand.charAt(0) == '@') {
                operand = operand.substring(1);
            }
            if (operand.charAt(operand.length() - 2) == ',') {
                operand = operand.substring(0, operand.length() - 1);
            }
            if (Character.isDigit(operand.charAt(0)))
                baseRegisterOperand = Integer.valueOf(operand);
            else
                baseRegisterOperand = SymbolTable.getInstance().getSymbol(operand).getValue();
        }


        switch (operationFormat) {
            case -1:
                //directive, no opcode
                opcode = "";
                r1 = "";
                r2 = "";
                n = i = x = b = p = e = -1;


                if (operation.getOperationMnemonic().equals("byte")) {
                    for (int i = 2; i < operandField.trim().length() - 1; i++) {
                        if (operandField.charAt(0) == 'c') {
                            int asciiValue = operandField.charAt(i);
                            opcode = opcode + convertDecToHex(asciiValue);
                            System.out.println("opcode: " + opcode);
                        } else {
                            opcode = opcode + operandField.charAt(i);
                        }
                    }
                    n = -2;
                } else if (operation.getOperationMnemonic().equals("word")) {
                    String[] splittedOperand = operandField.trim().split(",");
                    for (String s : splittedOperand) {
                        int value = Integer.valueOf(s);
                        opcode = opcode + leftPad(convertDecToHex(value), 6);
                    }
                    n = -2;
                }

                break;
            case 2:
                opcode = convertHexToBin(operation.getBinaryCode());
                System.out.println("opcode = " + opcode);

                //get r1 and r2
                r1 = leftPad(convertDecToBin(RegisterTable.getInstance().getRegTable()
                        .get(operandField.substring(0, 1)).getAddress()), 4);
                if (operandField.trim().length() > 1)
                    r2 = leftPad(convertDecToBin(RegisterTable.getInstance().getRegTable()
                            .get(operandField.substring(2, 3)).getAddress()), 4);
                else {
                    r2 = "0000";
                }

                System.out.println("r1: " + r1);
                System.out.println("r2: " + r2);

                break;


            case 3:
                r1 = "";
                r2 = "";
                opcode = convertHexToBin(operation.getBinaryCode()).substring(0, 6);
                System.out.println("opcode = " + opcode);
                e = 0;

                setNIXFlags(operandField);

                binaryAddress = objectCodeHelper(operandField, 3);

                System.out.println("KITTY B = " + b + "   P = " + p);
                break;


            case 4:
                r1 = "";
                r2 = "";
                opcode = convertHexToBin(operation.getBinaryCode()).substring(0, 6);
                System.out.println("opcode = " + opcode);
                e = 1;
                b = 0;
                p = 0;
                setNIXFlags(operandField);
                binaryAddress = objectCodeHelper(operandField, 4);
                break;
        }

        formInstructionCode();
//        instructionCode = instructionCode + opcode + r1 + r2 + n + i + x + b + p + e + binaryAddress;
//        System.out.println("INSTRUCTION CODE: " + instructionCode);
//        createObjectCode();
//        line.setObjectCode(instructionObjectCode);
//        System.out.println("line object code: " + line.getObjectCode());
//        System.out.println("---------new line--------------");
    }

    private void formInstructionCode() {
        instructionCode = instructionCode + opcode + r1 + r2 + n + i + x + b + p + e + binaryAddress;
        System.out.println("INSTRUCTION CODE: " + instructionCode);
        createObjectCode();
        line.setObjectCode(instructionObjectCode);
        System.out.println("line object code: " + line.getObjectCode());
        System.out.println("---------new line--------------");
    }

    private String objectCodeHelper(String operandField, int format) {
        int address = -1;
        int flag = 0;
        String subOperand = "";
        String binaryAddress = "";

        if (n == 1 && i == 1 && x == 0) // direct, without indexing
        {
            subOperand = operandField.trim();
        } else if (n == 1 && i == 1 && x == 1) { // direct, with indexing
            System.out.println("direct, with indexing");
            subOperand = operandField.trim().substring(0, operandField.trim().length() - 2);
        } else if (n == 1 && i == 0) { // indirect
            subOperand = operandField.trim().substring(1);
        } else if (n == 0 && i == 1) {
            subOperand = operandField.trim().substring(1);

            if (format == 3) {
                try {
                    address = Integer.parseInt(subOperand);
                    p = 0;
                    b = 0;
                    binaryAddress = leftPad(convertDecToBin(address), 12);
                    System.out.println("binary value: " + binaryAddress);
                    flag = 1;
                } catch (NumberFormatException e) {
                    flag = 0;
                }
            } else {
                try {
                    address = Integer.parseInt(subOperand);
                    flag = 1;
                } catch (NumberFormatException e) {
                    flag = 0;
                }
            }
        }
        if (flag == 0 && SymbolTable.getInstance().getSymbol(subOperand) == null) {
            line.getErrorIndexList().add(28);
        } else if (SymbolTable.getInstance().getSymbol(subOperand) != null){
            if (flag == 0)
                address = SymbolTable.getInstance().getSymbol(subOperand).getValue();

            if (format == 3)
                binaryAddress = setBPFlags(SymbolTable.getInstance().getSymbol(subOperand).getValue(), line);
            else
                binaryAddress = leftPad(convertDecToBin(address), 20);
        }

        System.out.println("KITTY B = " + b + "   P = " + p);
        return binaryAddress;
    }

    private void createObjectCode() {
        if (line.getErrorIndexList().size() > 0){
            this.instructionObjectCode = "";
        }
        else if (line.getOperation().getFormat() == 2){
            instructionCode = opcode + r1 + r2;
            this. instructionObjectCode = convertBinaryToHex(instructionCode);
        }
        else if (n != -1 && n != -2) {
            this.instructionObjectCode = leftPad(convertBinaryToHex(instructionCode), 6);
        }
        else if (n == -2)
            this.instructionObjectCode = opcode;

        System.out.println("object code: " + this.instructionObjectCode);
    }

    private void printOutOfRangeError() {
        System.out.println("DISPLACEMENT OUT OF RANGE, CANNOT USE PC OR BASE RELATIVE ADDRESSING");
        p = 0;
        b = 0;
        line.getErrorIndexList().add(27);
    }

    private String setBPFlags(int targetAddress, Line line) {
        //check for PC relative or Base relative to set b and p flags
        System.out.println("STR " + targetAddress);
        int displacement = targetAddress - line.getAddress() - line.getOperation().getLengthOfInstruction();
        System.out.println("displacement: " + displacement);
        System.out.println("disp: " + displacement);
        binaryAddress = "";
        if (displacement >= -2048 && displacement <= 2047) {
            p = 1;
            b = 0;
        } else if (line.isBaseRegisterSet()) {
            displacement = targetAddress - baseRegisterOperand;
            System.out.println("target address = " + targetAddress);
            System.out.println("base reg operand = " + baseRegisterOperand);
            if (displacement >= 0 && displacement < 4096) {
                b = 1;
                p = 0;
            } else {
                printOutOfRangeError();
            }
        } else {
            printOutOfRangeError();
        }

        if (p != 0 || b != 0)
            binaryAddress = leftPad(setBinaryAddress(displacement), 12);

        return binaryAddress;
    }

    private void setNIXFlags(String operandField) {
        if (operandField.charAt(0) == '#') {
            i = 1;
            x = 0;
            n = 0;
            System.out.println("addressing mode: immediate");
        } else if (operandField.charAt(0) == '@') {
            n = 1;
            i = 0;
            x = 0;
            System.out.println("addressing mode: indirect");
        } else {
            String trimmedOperand = operandField.trim();
            if (trimmedOperand.charAt(trimmedOperand.length() - 2) == ',') {
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

    private String setBinaryAddress(int displacement) {
        String hexAddress = convertDecToHex(displacement);
        System.out.println("HEX ADDRESS: " + hexAddress);
        hexAddress = truncateHexAddress(hexAddress);
        System.out.println("HEX ADDRESS: " + hexAddress);
        return convertHexToBin(hexAddress);
    }

    private String truncateHexAddress(String hexAddress) {
        if (hexAddress.length() > 3)
            return hexAddress.substring(hexAddress.length() - 3);
        else if (hexAddress.length() == 1)
            return "00" + hexAddress;
        return "0" + hexAddress;
    }

    private String convertHexToBin(String hexadecimal) {

        int decimal = Integer.parseInt(hexadecimal, 16);
        String binary = Integer.toBinaryString(decimal);


        return leftPad(binary, 8);
    }

    private String convertBinaryToHex(String instructionCode) {
        // convert to decimal first
        int decimal = Integer.parseInt(instructionCode, 2);
        // then convert to hex
        return Integer.toString(decimal, 16);
    }

    private String convertDecToBin(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    private String convertDecToHex(int decimal) {
        return Integer.toHexString(decimal);
    }

    private static String leftPad(String str, int n) {

        String padString;

        if (n == 8) {
            padString = "00000000";
            if (str.length() < 8)
                return padString.substring(str.length()) + str;
        } else if (n == 4) {
            padString = "0000";

            if (str.length() < 4)
                return padString.substring(str.length()) + str;
        } else if (n == 12) {

            padString = "000000000000";

            if (str.length() < 12)
                return padString.substring(str.length()) + str;

        } else if (n == 20) {
            padString = "00000000000000000000";

            if (str.length() < 20)
                return padString.substring(str.length()) + str;
        } else if (n == 6) {
            padString = "000000";

            if (str.length() < 6)
                return padString.substring(str.length()) + str;
        }
        return str;
    }

    public String getInstructionObjectCode() {
        return instructionObjectCode;
    }
}
