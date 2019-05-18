package mainpackage;

import operation.Operation;
import operation.OperationTable;
import register.RegisterTable;
import symbol.SymbolTable;

import java.util.ArrayList;


public class Parser {

    private ArrayList<Integer> errorIndexList;
    private String label = "", operation = "", operand = "";
    private int instructionLength = 0;
    private boolean endStatementFound = false;
    private boolean statementAfterEndFound = false;
    private boolean startStatementFound = false;
    private Operation operationObject = null;

    private int baseRegisterAvailable = -1;
    private String baseRegisterValue = "";
    private int orgInstructionAddress = 0;

    public Parser() {
        this.errorIndexList = new ArrayList<>();
    }

    public Line extractFields(String line) {

        errorIndexList = new ArrayList<>();
        String operandField;
        String operationField;
        String labelField;
        Line lineObj;

        System.out.println("line length = " + line.length());

        if(line.length() == 0)
        {
            errorIndexList.add(22);
            return new Line(0, "", "", "", "",
                    errorIndexList, null, baseRegisterAvailable==1);
        }

        if(isComment(line))
        {
            System.out.println("this line is a comment");
            return new Line(0, "", "", "", line,
                    errorIndexList, null, baseRegisterAvailable==1);
        }

        if(line.length() < 10)
        {
            //no operation, no operand
            labelField = line;
            errorIndexList.add(22);
            return new Line(0, labelField, "", "", "",
                    errorIndexList, null, baseRegisterAvailable==1);
        }
        else if(line.length()<=15)
        {
            //we have an operation but no operand
            labelField = line.substring(0,8);
            operationField = line.substring(9, line.length());
            operandField = "";
        }
        else if(line.length() < 18)
        {
            labelField = line.substring(0,8);
            operationField = line.substring(9, 15);
            operandField = "";
        }
        else
        {
            labelField = line.substring(0,8);
            operationField = line.substring(9, 15);
            operandField = line.substring(17, line.length());
        }


        System.out.println("label: " + labelField);
        System.out.println("operation: " + operationField);
        System.out.println("operand: " + operandField);

        this.operand = operandField.toLowerCase();

        System.out.println("TESTOPERANDFIELD: " + operandField + operandField.length());
        System.out.println("TESTOPERAND: " + this.operand + this.operand.length());



        lineObj = new Line(0, labelField, operationField, operandField, "",
                errorIndexList, this.operationObject, baseRegisterAvailable==1);


        validateFixedFormat(labelField.toLowerCase(), operationField.toLowerCase(), operandField.toLowerCase());

        if(!this.operation.equals("equ"))
            addLabelToSymbolTable();

        System.out.println("errors: " + errorIndexList.size());

        if(this.operation.equals("org"))
            lineObj.setAddress(orgInstructionAddress);
        else
            lineObj.setAddress(LocationCounter.LC - this.instructionLength);
        System.out.println(errorIndexList);
        lineObj.setOperation(this.operationObject);

        checkBaseRelativeAddressing();

        lineObj.setBaseRegisterSet(baseRegisterAvailable == 1);

        System.out.println("KITKAT: baseRegisterValue = " + baseRegisterAvailable);
        return lineObj;
    }

    private void checkBaseRelativeAddressing() {
        if(errorIndexList.size() == 0)
        {
            if(baseRegisterAvailable == 1)
            {
                if(operation.equals("nobase"))
                    baseRegisterAvailable = -1;
            } else if(baseRegisterAvailable == 0)
            {
                //CHECK IF SAME OPERAND
                if(operation.equals("base") && operand.equals(baseRegisterValue))
                    baseRegisterAvailable = 1;
                else
                    baseRegisterAvailable = -1;
            }
            else if(baseRegisterAvailable == -1)
            {
                if(operation.equals("ldb"))
                {
                    baseRegisterAvailable = 0;
                    baseRegisterValue = operand;
                }
            }

        }
    }

    private void addLabelToSymbolTable() {
        if (this.label.length() == 0)
            return;

        if (errorIndexList.size() > 0)
        {
            System.out.println("Errors found, label not inserted in symbol table.");
            return;
        }
        if (SymbolTable.getInstance().getSymbol(this.label) != null){
            System.out.println("mainpackage.Error, duplicate label");
            errorIndexList.add(3);
            return;
        }

        SymbolTable.getInstance().addToSymTab(this.label, LocationCounter.LC - this.instructionLength,
                this.instructionLength, true);

        System.out.println("symbol.Symbol: " + SymbolTable.getInstance().getSymbol(this.label).toString());
    }


    private boolean isComment(String line) {
        return line.charAt(0) == '.';
    }


    private void validateFixedFormat(String labelField, String operationField, String operandField) {
        validateLabel(labelField);
        validateOperationField(operationField);

        if(this.operationObject != null)
        {
            if(this.operationObject.getFormat() == -1)
                validateDirective();
            else if(needsOperand())
                validateOperandField(operandField);
        }

    }



    private void validateLabel(String labelField) {
        System.out.println("VALIDATING LABEL...");

        this.label = labelField;

        //if label is empty, return
        if (this.label.trim().length() == 0){
            this.label = this.label.trim();
            return;
        }

        //if label starts with whitespace, misplaced label error
        if (Character.isWhitespace(this.label.charAt(0)))
        {
            errorIndexList.add(0);
            System.out.println("label starts with whitespace");
        }

        //if label doesn't start with letter, error
        if (!Character.isLetter(this.label.charAt(0)))
        {
            errorIndexList.add(13);
            System.out.println("label starts with undefined symbol");
        }

        this.label = this.label.trim();

        //if operation mnemonic is used as label, error
        if(OperationTable.getOptable().get(this.label) != null)
        {
            errorIndexList.add(19);
            System.out.println("mnemonic used as label");
        }

        //if label contains symbol other than letter or digit, error
        for (int i = 1; i < this.label.length(); i++){
            if (!Character.isLetterOrDigit(labelField.charAt(i)))
            {
                errorIndexList.add(15);
                System.out.println("label contains undefined symbol");
                return;
            }
        }

    }




    private void validateOperationField(String operationField) {

        System.out.println("VALIDATING OPERATION...");

        this.operation = operationField;

        //if operation starts with whitespace
        if(Character.isWhitespace(operationField.charAt(0)))
        {
            errorIndexList.add(1);
            System.out.println("operation starts with whitespace");
        }

        this.operation = this.operation.trim();

        //if operation contains spaces
        for (int i = 0; i < this.operation.length(); i++){
            if (this.operation.charAt(i) == ' ')
            {
                errorIndexList.add(16);
                System.out.println("operation contains space in the middle");
                return;
            }
        }

        this.operationObject = OperationTable.getOptable().get(this.operation);

        if(this.operationObject == null)
        {
            errorIndexList.add(7);
            System.out.println("operation doesn't exist in optable");
        }




    }

    private boolean needsOperand() {
//        this.operand = operandField;

        System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

        //if our operation doesn't need an operand and we don't have an operand, return

        if(this.operationObject != null && (this.operationObject.isHasOperand() == 0 || this.operationObject.isHasOperand() == -1)
                && this.operand.trim().equals(""))
        {
            this.operand = this.operand.trim();
            System.out.println("ginger");
            System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

            return false;

        }
        System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());


        return true;
    }


    private void validateOperandField(String operandField) {
       // this.operand = operandField;

        System.out.println("VALIDATING OPERAND FIELD...");


        //our operation needs an operand, but we don't have one
        if(this.operand.trim().equals(""))
        {
            errorIndexList.add(2);

            if (this.operationObject == null) {
                return;
            }

//            incrementLocationCounter(this.operationObject.getFormat());
//            this.instructionLength = this.operationObject.getLengthOfInstruction();
            this.instructionLength = 0;
            return;
        }

        //if operand starts with whitespace
        if(Character.isWhitespace(this.operand.charAt(0)))
        {
            errorIndexList.add(2);
            System.out.println("space before operand");
        }

        this.operand = this.operand.trim();


        //if operand contains whitespaces in between
        for (int i = 0; i < this.operand.length(); i++){
            if (this.operand.charAt(i) == ' ')
            {
                errorIndexList.add(8);
                System.out.println("operand field contains spaces in between");
            }
        }


        if (this.operationObject == null)
            return;



        int operationFormat = this.operationObject.getFormat();



        incrementLocationCounter(this.operationObject.getFormat());
        this.instructionLength = this.operationObject.getLengthOfInstruction();

        if(Postfix.containsOperator(this.operand))
        {
            if(Postfix.infixToPostfix(this.operand.trim()))
                return;
            else
            {
                errorIndexList.add(30);
                return;
            }
        }

        switch(operationFormat)
        {
            case 2:
                System.out.println("operation is format 2");
                //CLEAR and TIXR instructions can have 1 register operand only
                //SHIFTL and SHIFTR r1,n

                if (this.operation.equals("tixr") || this.operation.equals("clear")){
                    System.out.println("one register operand only");
                    if (this.operand.length() > 1) {
                        errorIndexList.add(8);
                        return;
                    }
                    if (RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(0))) == null){
                        errorIndexList.add(11);
                        System.out.println("invalid register");
                        return;
                    }
                    return;
                }

                if(this.operand.length() > 3)
                {
                    errorIndexList.add(18);
                    return;
                }
                else if(this.operand.length() < 3)
                {
                    errorIndexList.add(25);
                    return;
                }
                if(this.operand.charAt(1) != ',') {
                    errorIndexList.add(17);
                    return;
                }
                if(RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(0))) == null ||
                        RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(2))) == null )
                {
                    errorIndexList.add(11);
                    System.out.println("invalid register");
                }

                break;

            case 3:
            case 4:
                System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

                System.out.println("format 3/4");
                validationHelper();
                break;
        }

    }

    private void validationHelper() {
        System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

        if(Character.isDigit(this.operand.charAt(0)))
        {
            System.out.println("operand cant start with digit, undefined symbol");
            errorIndexList.add(8);
            return;
        }

        if(this.operand.charAt(0) == '#')
        {
            //make sure all following characters are digits
            for(int i=1; i<this.operand.length(); i++)
            {
                if(!Character.isDigit(this.operand.charAt(i)) && !Character.isLetter(this.operand.charAt(i)))
                {
                    errorIndexList.add(8);
                    return;
                }
            }
        }
        else if(this.operand.charAt(0) == '@')
        {
            //if second character is digit, make sure the rest are also digits
            if(Character.isDigit(this.operand.charAt(1)))
            {
                for(int i=2; i<this.operand.length(); i++)
                {
                    if(!Character.isDigit(this.operand.charAt(i)))
                    {
                        errorIndexList.add(8);
                        return;
                    }
                }
            }else if(Character.isLetter(this.operand.charAt(1)))
            {
                for(int i=2; i<this.operand.length(); i++)
                {
                    if(!Character.isLetterOrDigit(this.operand.charAt(i)))
                    {
                        errorIndexList.add(8);
                        return;
                    }
                }
            }
        }
        else if(!Character.isLetter(this.operand.charAt(0)))
        {
            System.out.println("undefined symbol in operand at index 0");
            errorIndexList.add(8);
            return;
        }


        System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

        for (int i = 1; i < this.operand.length(); i++){
            if (!Character.isLetterOrDigit(this.operand.charAt(i)))
            {
                if(this.operand.charAt(i) == ',' && i == this.operand.length()-2 && this.operand.charAt(i+1) == 'x')
                {
                    System.out.println("indexed addressing");
                }
                else
                {
                    System.out.println("TESTOPERAND: " + this.operand+ this.operand.length());

                    errorIndexList.add(8);
                    System.out.println("undefined symbol in operand");
                    System.out.println("boooooo");
                }
            }
        }
    }

    private void validateDirective() {
        System.out.println("VALIDATING DIRECTIVE...");
        Operation operation = OperationTable.getOperation(this.operation);
        operationObject = operation;

        if (operation == null || operation.getFormat() != -1)
            return;


        System.out.println("DIRECTIVE: " + this.operation);
        this.instructionLength = operation.getLengthOfInstruction();


        if(Postfix.containsOperator(this.operand))
        {
            if(Postfix.infixToPostfix(this.operand.trim()))
               return;
            else
            {
                errorIndexList.add(30);
                return;
            }
        }

        switch (this.operation){
            case "equ":
                if (this.label.length() == 0)
                {
                    errorIndexList.add(0); // missing or misplaced label
                }

                if(this.operand.trim().length() == 0)
                {
                    return;
                }
                if(this.operand.charAt(0) == '#')
                {
                    for(int i=1; i<this.operand.length(); i++)
                    {
                        if(!Character.isDigit(this.operand.charAt(i)))
                        {
                            errorIndexList.add(8);
                            System.out.println("ONLY DIGITS ALLOWED");
                            return;
                        }
                    }
                    int value = Integer.valueOf(this.operand.substring(1,this.operand.length()));
                }
                else if (SymbolTable.getInstance().getSymbol(this.operand) == null)
                    errorIndexList.add(20); // equ should have previously defined operands
                else
                {
                    int operandAddress = SymbolTable.getInstance().getSymbol(this.operand.trim()).getValue();
                    SymbolTable.getInstance().addToSymTab(this.label, operandAddress, this.instructionLength, true);
                }

                break;

            case "base":

                //checkImmediate();

                if (this.label.length() != 0)
                    errorIndexList.add(4); // this statement can't have a label

                if(this.operand.trim().length() == 0)
                {
                    errorIndexList.add(2);
                    return;
                }
                validationHelper();

                break;

            case "nobase":

                if(this.label.length() != 0)
                {
                    System.out.println("nobase statement cant have a label");
                    errorIndexList.add(4);
                }
                if(this.operand.length() != 0)
                {
                    System.out.println("nobase statement cant have an operand");
                    errorIndexList.add(5);
                }

                break;

            case "resb":
                if (this.operand.length() > 4) {
                    errorIndexList.add(8); // undefined symbol in operand
                    return;
                }

                for(int i=0; i<this.operand.length(); i++)
                    if(!Character.isDigit(this.operand.charAt(i)))
                    {
                        System.out.println("undef symbol in operand");
                        errorIndexList.add(8);
                    }


                this.instructionLength = Integer.valueOf(this.operand);
                incrementLocationCounter(this.instructionLength);
                break;

            case "resw":
                if (this.operand.length() > 4) {
                    errorIndexList.add(8); // undefined symbol in operand
                    return;
                }

                for(int i=0; i<this.operand.length(); i++)
                    if(!Character.isDigit(this.operand.charAt(i)))
                    {
                        System.out.println("undef symbol in operand");
                        errorIndexList.add(8);
                    }

                this.instructionLength = 3 * Integer.valueOf(this.operand);
                incrementLocationCounter(this.instructionLength);
                break;

            case "byte":
                int numberOfBytes = 0;

                //if it doesnt start with c or x, or if it doesn't contain 2 apostrophes, error
                if((this.operand.charAt(0) != 'c' && this.operand.charAt(0) != 'x') ||
                        this.operand.charAt(1) != '\'' || this.operand.charAt(this.operand.length()-1) != '\'')
                {
                    System.out.println("undefined symbol in operand");
                    errorIndexList.add(8);
                } else if (this.operand.charAt(0) == 'c')
                {
                    if(this.operand.length() > 18){
                        System.out.println("operand too long");
                        errorIndexList.add(18);
                    }

                    for(int i=2; i<this.operand.length()-1; i++)
                    {
                        if(!Character.isLetterOrDigit(this.operand.charAt(i)))
                        {
                            System.out.println("undefined symbol in operand");
                            errorIndexList.add(8);
                            break;
                        }
                    }

                    numberOfBytes = this.operand.length()-3;
                } else if(this.operand.charAt(0) == 'x')
                {
                    if(this.operand.length() > 17)
                    {
                        System.out.println("operand too long");
                        errorIndexList.add(18);
                    } else if(this.operand.length()%2 == 0)
                    {
                        errorIndexList.add(29);
                    }
                    for(int i=2; i<this.operand.length()-1; i++)
                    {
                        if(!isHexadecimal(this.operand.charAt(i)))
                        {
                            System.out.println("undef symbol in operand");
                            errorIndexList.add(8);
                            break;
                        }
                    }
                    numberOfBytes = (this.operand.length()-3) / 2;
                    System.out.println("number of bytes = " + numberOfBytes);
                }

                if(errorIndexList.size() == 0) {
                    incrementLocationCounter(numberOfBytes);
                    this.instructionLength = numberOfBytes;
                }
                break;

            case "word":
                if(this.operand.trim().equals(""))
                {
                    errorIndexList.add(2);
                    this.instructionLength = 0;
                    return;
                }
                if (!Character.isDigit(this.operand.charAt(0))) {
                    errorIndexList.add(8); // undefined symbol in operand
                    return;
                }
                this.instructionLength = 3;
                for (int i = 1; i < this.operand.length(); i++) {
                    if (i != operand.length()-1 && this.operand.charAt(i) == ',' && Character.isDigit(this.operand.charAt(i + 1))) {
                        this.instructionLength += 3;
                    } else if (i == operand.length()-1 && this.operand.charAt(i) == ',') {
                        errorIndexList.add(8);
                        break;
                    } else if (this.operand.charAt(i) == ',' && !Character.isDigit(this.operand.charAt(i+1))){
                        errorIndexList.add(8);
                        break;
                    }
                }
                incrementLocationCounter(this.instructionLength);
                break;

            case "org":
                this.instructionLength = 0;

                //checkImmediate();

                if(this.label.length() != 0)
                {
                    System.out.println("org statement can't have a label");
                    errorIndexList.add(4);
                }

                if(this.operand.trim().equals(""))
                {
                    errorIndexList.add(2);
                    return;
                }

                String s = this.operand;
                if (this.operand.charAt(0) == '#')
                    s = s.substring(1);

                if(SymbolTable.getInstance().getSymbol(s) == null)
                {
                    System.out.println("undefined symbol in operand");
                    errorIndexList.add(8);
                    return;
                }

                int newAddress = SymbolTable.getInstance().getSymbol(s).getValue();


                if(errorIndexList.size() == 0)
                {
                    System.out.println("i want to go to address " + newAddress);
                    System.out.println("i am at address " + LocationCounter.LC);
                    System.out.println("increment by " + -(LocationCounter.LC - newAddress));
                    orgInstructionAddress = LocationCounter.LC;
                    incrementLocationCounter(newAddress - LocationCounter.LC);
                }
                break;
            case "end":
                endStatementFound = true;
                System.out.println("end label is '" + this.label + "'");

                if(this.label.length() != 0)
                {
                    System.out.println("end statement can't have a label");
                    errorIndexList.add(4);
                }
                if(this.operand.trim().length()!=0 && SymbolTable.getInstance().getSymbol(this.operand) == null)
                {
                    System.out.println("undefined symbol in operand");
                    errorIndexList.add(8);
                }
                break;
            case "start":
                if(startStatementFound)
                {
                    errorIndexList.add(24);
                    return;
                }
                startStatementFound = true;

                if (this.operand.trim().length() == 0){
                    errorIndexList.add(21);
                    return;
                }

                if(!Character.isDigit(this.operand.charAt(0)))
                {
                    errorIndexList.add(26);
                    return;
                }
                for (int i=0; i < this.operand.length(); i++){
                    if (!isHexadecimal(this.operand.charAt(i))){
                        errorIndexList.add(9);
                        return;
                    }
                }

                //convert hex to int first
                LocationCounter.setLC(convertHexToDecimal(this.operand));
                break;

        }
    }

    private void checkImmediate() {
        if(this.operand.charAt(0) == '#')
        {
            //make sure all following characters are digits
            for(int i=1; i<this.operand.length(); i++)
            {
                if(!Character.isDigit(this.operand.charAt(i)) && !Character.isLetter(this.operand.charAt(i)))
                {
                    errorIndexList.add(8);
                    return;
                }
            }
        }
    }


    private int convertHexToDecimal(String hex){
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++)
        {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public boolean isEndStatementFound() {
        return endStatementFound;
    }

    private boolean isHexadecimal(char c) {
        return Character.isDigit(c) || c=='a' || c=='b' || c=='c' || c=='d' || c=='e' || c=='f';
    }

    private void incrementLocationCounter(int numberOfBytes) {
        LocationCounter.setLC(LocationCounter.LC + numberOfBytes);
    }

    public boolean isStatementAfterEndFound() {
        return statementAfterEndFound;
    }

    public void setStatementAfterEndFound(boolean statementAfterEndFound) {
        this.statementAfterEndFound = statementAfterEndFound;
    }

    public int getBaseRegisterValue() {
        return baseRegisterAvailable;
    }

    public void setBaseRegisterValue(int baseRegisterValue) {
        this.baseRegisterAvailable = baseRegisterValue;
    }
}
