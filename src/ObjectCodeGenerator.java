public class ObjectCodeGenerator {

    private int n = 0, i = 0, x = 0, b = 0, p = 0, e = 0;
    private String opcode = "";
    private int operationFormat = 0;
    private String instructionCode = "", r1 = "", r2 = "", flags = "", disp = "", address = "";
    private int baseRegisterOperand = -1;
    private String instructionObjectCode = "";

    public ObjectCodeGenerator() {
    }

    public void generateObjectCode(Line line) {
        String binaryAddress = "";
        instructionCode = "";
        instructionObjectCode = "";
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
                n = i = x = b = p = e = -1;
                System.out.println("KITTY B = " + b + "   P = " + p);

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
                        opcode = opcode + leftPad(convertDecToHex(value), 6) + "\n";
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

                break;


            case 3:
                opcode = convertHexToBin(operation.getBinaryCode()).substring(0, 6);
                System.out.println("opcode = " + opcode);
                e = 0;

                setNIXFlags(operandField);
                String subOperand = "";
                int address;
                int flag = 0;


                if (n == 1 && i == 1 && x == 0) // direct, without indexing
                {
                    subOperand = operandField.trim();
                } else if (n == 1 && i == 1 && x == 1) { // direct, with indexing
                    subOperand = operandField.trim().substring(0, operandField.trim().length() - 2);
                } else if (n == 1 && i == 0) { // indirect
                    subOperand = operandField.trim().substring(1);
                } else if (n == 0 && i == 1) {
                    subOperand = operandField.trim().substring(1);
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
                }
                if (flag == 0) {
                    binaryAddress = setBPFlags(subOperand, line);
                }
                System.out.println("KITTY B = " + b + "   P = " + p);
                break;


            case 4:
                opcode = convertHexToBin(operation.getBinaryCode()).substring(0, 6);
                System.out.println("opcode = " + opcode);
                e = 1;
                b = 0;
                p = 0;
                address = -1;
                setNIXFlags(operandField);
                if (n == 1 && i == 1 && x == 0) {
                    address = SymbolTable.getInstance().getSymbol(operandField.trim()).getValue();
                } else if (n == 1 && i == 1 && x == 1) { // direct, with indexing
                    subOperand = operandField.trim().substring(0, operandField.trim().length() - 2);
                    address = SymbolTable.getInstance().getSymbol(subOperand).getValue();
                } else if (n == 1 && i == 0) { // indirect
                    subOperand = operandField.trim().substring(1);
                    address = SymbolTable.getInstance().getSymbol(subOperand).getValue();
                } else if (n == 0 && i == 1) {
                    subOperand = operandField.trim().substring(1);
                    try {
                        address = Integer.parseInt(subOperand);
                    } catch (NumberFormatException e) {
                        address = SymbolTable.getInstance().getSymbol(subOperand).getValue();
                    }
                }
                binaryAddress = leftPad(convertDecToBin(address), 20);
                break;
        }

        instructionCode = instructionCode + opcode + r1 + r2 + n + i + x + b + p + e + binaryAddress;
        System.out.println("INSTRUCTION CODE: " + instructionCode);
        createOnjectCode();
        line.setObjectCode(instructionObjectCode);
        System.out.println("line object code: " + line.getObjectCode());
        System.out.println("---------new line--------------");
    }

    private void createOnjectCode() {
        if (n != -1 && n != -2) {
            this.instructionObjectCode = leftPad(convertBinaryToHex(instructionCode), 6);
        } else if (n == -2)
            this.instructionObjectCode = opcode;
        System.out.println("object code: " + this.instructionObjectCode);
    }

    private void printOutOfRangeError() {
        System.out.println("DISPLACEMENT OUT OF RANGE, CANNOT USE PC OR BASE RELATIVE ADDRESSING");
        p = 0;
        b = 0;
    }

    private String setBPFlags(String str, Line line) {
        //check for PC relative or Base relative to set b and p flags
        int targetAddress = SymbolTable.getInstance().getSymbol(str).getValue();
        int displacement = targetAddress - line.getAddress();
        System.out.println("disp: " + displacement);
        String binaryAddress = null;
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
