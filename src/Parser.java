import java.util.ArrayList;


public class Parser {

    private ArrayList<Integer> errorIndexList;
    private String commentField = "";

    private String label = "", operation = "", operand = "";
    private int instructionLength = 0;
    private boolean endStatementFound = false;
    private boolean statementAfterEndFound = false;
    private boolean startStatementFound = false;

    public Parser() {
        this.errorIndexList = new ArrayList<>();
    }

    public Line extractFields(String line) {

        errorIndexList = new ArrayList<>();
        String operandField = "";
        String operationField = "";
        String labelField = "";
        Line lineObj;

        System.out.println("line length = " + line.length());

        if(line.length() == 0)
        {
            errorIndexList.add(22);
            return new Line(0, "", "", "", "", errorIndexList);
        }

        if(isComment(line))
        {
            System.out.println("this line is a comment");
            return new Line(0, "", "", "", line, errorIndexList);
        }

        if(line.length() < 10)
        {
            //no operation, no operand
            labelField = line;
            errorIndexList.add(22);
            return new Line(0, labelField, "", "", "", errorIndexList);
        }
        else if(line.length()<=15)
        {
            //we have an operation but no operand
            labelField = line.substring(0,8);
            operationField = line.substring(9, line.length());
        }
        else
        {
            labelField = line.substring(0,8);
            operationField = line.substring(9, 15);
            operandField = line.substring(17, line.length());
        }


//        this.commentField = line.substring(35, 66);

        System.out.println("label: " + labelField);
        System.out.println("operation: " + operationField);
        System.out.println("operand: " + operandField);


        lineObj = new Line(0, labelField, operationField, operandField, null, errorIndexList);


        validateFixedFormat(labelField.toLowerCase(), operationField.toLowerCase(), operandField.toLowerCase());
        addLabelToSymbolTable();

        System.out.println("errors: " + errorIndexList.size());

        lineObj.setAddress(LocationCounter.LC - this.instructionLength);
        System.out.println(errorIndexList);

        return lineObj;
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
            System.out.println("Error, duplicate label");
            errorIndexList.add(3);
            return;
        }

        SymbolTable.getInstance().addToSymTab(this.label, LocationCounter.LC - this.instructionLength,
                this.instructionLength, true);

        System.out.println("Symbol: " + SymbolTable.getInstance().getSymbol(this.label).toString());
    }


    private boolean isComment(String line) {
        return line.charAt(0) == '.';
    }


    private void validateFixedFormat(String labelField, String operationField, String operandField) {
        validateLabel(labelField);
        validateOperationField(operationField);
        validateOperandField(operandField);
        validateDirective();
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
            }
        }

        Operation operation = OperationTable.getOptable().get(this.operation);

        if(operation == null)
        {
            errorIndexList.add(7);
            System.out.println("operation doesn't exist in optable");
        }

    }

    private void validateOperandField(String operandField) {

        this.operand = operandField;

        System.out.println("VALIDATING OPERAND FIELD...");

        Operation operation = OperationTable.getOptable().get(this.operation);

        //if our operation doesn't need an operand and we don't have an operand, return
        if(operation != null && (operation.isHasOperand() == 0 || operation.isHasOperand() == -1)
                && this.operand == "")
            return;


        //our operation needs an operand, but we don't have one
        if(this.operand == "")
        {
            System.out.println("no operand");
            errorIndexList.add(2);

            operation = OperationTable.getOptable().get(this.operation);

            if (operation == null)
                return;

            int operationFormat = operation.getFormat();

            //if operation is a directive
            if (operationFormat == -1)
                return;

            incrementLocationCounter(operation.getFormat());
            this.instructionLength = operation.getLengthOfInstruction();
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

        operation = OperationTable.getOptable().get(this.operation);

        if (operation == null)
            return;

        int operationFormat = operation.getFormat();

        //if operation is a directive
        if (operationFormat == -1)
            return;

        incrementLocationCounter(operation.getFormat());
        this.instructionLength = operation.getLengthOfInstruction();

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

                if(this.operand.length() != 3)
                    errorIndexList.add(18);
                if(this.operand.charAt(1) != ',')
                    errorIndexList.add(17);

                if(RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(0))) == null ||
                        RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(2))) == null )
                {
                    errorIndexList.add(11);
                    System.out.println("invalid register");
                }

                break;

            case 3:
            case 4:
                System.out.println("format 3/4");
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
                        if(!Character.isDigit(this.operand.charAt(i)))
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


                for (int i = 1; i < this.operand.length(); i++){
                    if (!Character.isLetterOrDigit(this.operand.charAt(i)))
                    {
                        if(this.operand.charAt(i) == ',' && i == this.operand.length()-2 && this.operand.charAt(i+1) == 'x')
                        {
                            System.out.println("indexed addressing");
                        }
                        else
                        {
                            errorIndexList.add(8);
                            System.out.println("undefined symbol in operand");
                        }
                    }
                }
                break;
        }

    }

    private void validateDirective() {
        System.out.println("VALIDATING DIRECTIVE...");
        Operation operation = OperationTable.getOperation(this.operation);

        if (operation == null || operation.getFormat() != -1)
            return;


        System.out.println("DIRECTIVE: " + this.operation);
        this.instructionLength = operation.getLengthOfInstruction();


        switch (this.operation){
            case "equ":
                if (this.label.length() == 0)
                    errorIndexList.add(0); // missing or misplaced label

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

                break;

            case "base":
                if (this.label.length() != 0)
                    errorIndexList.add(4); // this statement can't have a label
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
                //if it doesnt start with c or x, or if it doesn't contain 2 apostrophes, error
                if((this.operand.charAt(0) != 'c' && this.operand.charAt(0) != 'x') ||
                        this.operand.charAt(1) != '\'' || this.operand.charAt(this.operand.length()-1) != '\'')
                {
                    System.out.println("undefined symbol in operand");
                    errorIndexList.add(8);
                } else if (this.operand.charAt(0) == 'c')
                {
                    if(this.operand.length() > 18)
                        System.out.println("operand too long");

                    for(int i=2; i<this.operand.length()-1; i++)
                    {
                        if(!Character.isLetterOrDigit(this.operand.charAt(i)))
                        {
                            System.out.println("undefined symbol in operand");
                            errorIndexList.add(8);
                        }
                    }
                } else if(this.operand.charAt(0) == 'x')
                {
                    if(this.operand.length() > 17)
                    {
                        System.out.println("operand too long");
                    }
                    for(int i=2; i<this.operand.length()-1; i++)
                    {
                        if(!isHexadecimal(this.operand.charAt(i)))
                        {
                            System.out.println("undef symbol in operand");
                            errorIndexList.add(8);
                        }
                    }
                }

                if(errorIndexList.size() == 0) {
                    incrementLocationCounter(this.operand.length() - 3);
                    this.instructionLength = this.operand.length() - 3;
                }
                break;

            case "word":
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
                if(this.label.length() != 0)
                {
                    System.out.println("org statement can't have a label");
                    errorIndexList.add(4);
                }

                if(SymbolTable.getInstance().getSymbol(this.operand) == null)
                {
                    System.out.println("undefined symbol in operand");
                    errorIndexList.add(8);
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
                if(this.operand.length()!=0 && SymbolTable.getInstance().getSymbol(this.operand) == null)
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
                for (int i =0; i < this.operand.length(); i++){
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
}
