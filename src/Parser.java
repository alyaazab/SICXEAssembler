import java.util.ArrayList;


public class Parser {

    private ArrayList<Integer> errorIndexList = new ArrayList<>();
    private String commentField;

    private String label, operation, operand;
    private int length;
    private int lenOfInst;


    public Line extractFields(String line) {

        String operandField;
        System.out.println("line length = " + line.length());

        Line lineObj;
        if(isComment(line)) {
            System.out.println("this line is a comment");
            lineObj = new Line(0, null, null, null, line, null);
            return lineObj;
        }

        if(line.length() == 0)
        {
            errorIndexList.add(22);
            return lineObj = new Line(0, null, null, null, null, errorIndexList);
        }
        else if(line.length() < 10)
        {
            //we do not have an operation
            errorIndexList.add(22);
            return lineObj = new Line(0, line.substring(0,8), null, null, null, errorIndexList);
        }
        else
        {
            operandField = line.substring(17, line.length());
        }


        String labelField = line.substring(0, 8);
        String operationField = line.substring(9, 15);
//        this.commentField = line.substring(35, 66);

        System.out.println("label: " + labelField);
        System.out.println("operation: " + operationField);
        System.out.println("operand: " + operandField);
//        System.out.println(commentField);


        lineObj = new Line(0, labelField, operationField, operandField, null, errorIndexList);


        validateFixedFormat(labelField, operationField, operandField);
        addLabelToSymbolTable();
        System.out.println("errors: " + errorIndexList.size());
//        for(int i=0; i<errorIndexList.size(); i++)
//            System.out.println(errorIndexList.get(i));

//        Error.printErrors(errorIndexList);
        return lineObj;


    }

    private void addLabelToSymbolTable() {
        if (this.label.trim().length() == 0)
            return;

        if (errorIndexList.size() >0)
        {
            System.out.println("Errors found, label not inserted in symbol table.");
            return;
        }
        if (SymbolTable.getInstance().getSymbol(this.label) != null){
            System.out.println("Error, duplicate label");
            errorIndexList.add(3);
            return;
        }
        SymbolTable.getInstance().addToSymTab(this.label, LocationCounter.LC, this.lenOfInst, true);
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

    private void validateDirective() {
        System.out.println("VALIDATING DIRECTIVE...");
        Operation operation = OperationTable.getOperation(this.operation);

        if (operation == null || operation.getFormat() != -1)
            return;


        System.out.println("DIRECTIVE: " + this.operation);


        switch (this.operation){
            case "equ":
                if (this.label.length() == 0)
                    errorIndexList.add(0); // missing or misplaced label

                if (this.operand.charAt(0) != '#')
                    for (int i =1; i < this.operand.length(); i++)
                        if (!Character.isDigit(this.operand.charAt(i)))
                            errorIndexList.add(8);

                else if (SymbolTable.getInstance().getSymbol(this.operand) == null)
                    errorIndexList.add(20); // equ should have previously defined operands

                LocationCounter.setLC(Integer.valueOf(this.operand));
                this.lenOfInst = 0;
                break;

            case "base":
                if (this.label.length() != 0)
                    errorIndexList.add(4); // this statement can't have a label
                this.lenOfInst = 0;
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
                this.lenOfInst = 0;
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


                this.length = Integer.valueOf(this.operand);
                System.out.println("LEN: " + length);
                this.lenOfInst = this.length;
                incrementLocationCounter(this.length);
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

                this.length = 3 * Integer.valueOf(this.operand);
                System.out.println("LEN: " + length);
                this.lenOfInst = this.length;
                incrementLocationCounter(this.length);
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
                    this.lenOfInst = this.operand.length() - 3;
                }
                break;

            case "word":
                if (!Character.isDigit(this.operand.charAt(0))) {
                    errorIndexList.add(8); // undefined symbol in operand
                    return;
                }
                this.length = 3;
                for (int i = 1; i < this.operand.length(); i++) {
                    if (i != operand.length()-1 && this.operand.charAt(i) == ',' && Character.isDigit(this.operand.charAt(i + 1))) {
                        this.length += 3;
                    } else if (i == operand.length()-1 && this.operand.charAt(i) == ',') {
                        errorIndexList.add(8);
                        break;
                    } else if (this.operand.charAt(i) == ',' && !Character.isDigit(this.operand.charAt(i+1))){
                        errorIndexList.add(8);
                        break;
                    }
                }
                this.lenOfInst = this.length;
                incrementLocationCounter(this.length);
                System.out.println("LEN: " + length);
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
                this.lenOfInst = 0;
                break;
            case "end":
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
                this.lenOfInst = 0;
                break;
            case "start":
                //TODO: check that start's operand is valid
                if (this.operand.trim().length() == 0){
                    errorIndexList.add(21);
                    return;
                }
                for (int i =0; i < this.operand.length(); i++){
                    if (!Character.isDigit(this.operand.charAt(i)) && this.operand.charAt(i) != 'a' && this.operand.charAt(i) != 'b'
                            && this.operand.charAt(i) != 'c' && this.operand.charAt(i) != 'd' && this.operand.charAt(i) != 'e'
                            && this.operand.charAt(i) != 'f'){
                        errorIndexList.add(8);
                        return;
                    }
                }
                this.lenOfInst = 0;
                LocationCounter.setLC(Integer.valueOf(operand));
                break;

        }
    }

    private boolean isHexadecimal(char c) {
        return Character.isDigit(c) || c=='a' || c=='b' || c=='c' || c=='d' || c=='e' || c=='f';
    }

    private void incrementLocationCounter(int numberOfBytes) {
        LocationCounter.setLC(LocationCounter.LC + numberOfBytes);
    }

    private void validateLabel(String labelField) {
        //ALLOW EMPTY LABELS (IN SOME CASES)

        System.out.println("VALIDATING LABEL...");

        this.label = labelField;

        if (this.label.trim().length() == 0){
            this.label = this.label.trim();
            return;
        }

        //if label starts with whitespace, misplaced label
        if (Character.isWhitespace(this.label.charAt(0)))
        {
            errorIndexList.add(0);
            System.out.println("label starts with whitespace");
        }

        //if label doesnt start with letter, error
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



        for (int i = 1; i < this.label.length(); i++){
            if (!Character.isLetterOrDigit(labelField.charAt(i)))
            {
                errorIndexList.add(15);
                System.out.println("label contains undefined symbol");
            }
        }

    }


    private void validateOperationField(String operationField) {

        System.out.println("VALIDATING OPERATION...");

        this.operation = operationField;

        if(Character.isWhitespace(operationField.charAt(0)))
        {
            errorIndexList.add(1);
            System.out.println("operation starts with whitespace");
        }

        this.operation = this.operation.trim();

        for (int i = 0; i < this.operation.length(); i++){
            if (this.operation.charAt(i) == ' ')
            {
                errorIndexList.add(16);
                System.out.println("operation contains space in the middle");
            }
        }

        Operation operation = OperationTable.getOptable().get(this.operation);
//        System.out.println(operation.getOperationMnemonic());

        if(operation == null)
        {
            errorIndexList.add(7);
            System.out.println("operation doesn't exist in optable");
        }

    }

    private void validateOperandField(String operandField) {

        this.operand = operandField;

        System.out.println("VALIDATING OPERAND FIELD...");

        if(Character.isWhitespace(this.operand.charAt(0)))
        {
            errorIndexList.add(2);
            System.out.println("space before operand");
        }

        this.operand = this.operand.trim();


        for (int i = 0; i < this.operand.length(); i++){
            if (this.operand.charAt(i) == ' ')
            {
                errorIndexList.add(8);
                System.out.println("operand field contains spaces in between");
            }
        }

        Operation operation = OperationTable.getOptable().get(this.operation);

        if (operation == null)
            return;

        int operationFormat = operation.getFormat();

        //if operation is a directive
        if (operationFormat == -1)
            return;

        incrementLocationCounter(operation.getFormat());
        this.lenOfInst = operation.getLengthOfInstruction();

        switch(operationFormat)
        {
            case 2:
                System.out.println("operation is format 2");
                //CLEAR and TIXR instructions can have 1 register operand only
                //SHIFTL and SHIFTR r1,n

                //TODO: LENGTH CAN BE 1 IF CLEAR OR TIXR INSTRUCTION

                if (this.operation.equals("tixr") || this.operation.equals("clear")){
                    System.out.println("HERE");
                    if (this.operand.length() > 1) {
                        errorIndexList.add(8);
                    }
                    if (RegisterTable.getInstance().getRegTable().get(String.valueOf(this.operand.charAt(0))) == null){
                        errorIndexList.add(11);
                        System.out.println("invalid register");
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
                System.out.println("format 3");
                if(Character.isDigit(this.operand.charAt(0)))
                {
                    System.out.println("operand cant start with digit, undefined symbol");
                    errorIndexList.add(8);
                }
                else if(this.operand.charAt(0) != '#' && this.operand.charAt(0) != '@' &&
                        !Character.isLetter(this.operand.charAt(0)))
                {
                    System.out.println("undefined symbol in operand at index 0");
                    errorIndexList.add(8);
                }


                for (int i = 1; i < this.operand.length(); i++){
                    if (!Character.isLetterOrDigit(this.operand.charAt(i)))
                    {
                        if(this.operand.charAt(i) == ',' && i == this.operand.length()-2 && this.operand.charAt(i+1) == 'x')
                        {
                            //ok, indexed addressing
                        }
                        else
                        {
                            errorIndexList.add(8);
                            System.out.println("undefined symbol in operand");
                        }
                    }
                }

                break;
                //first character must be alphabetic/#/@
                //loop: if any characters after character 0 is NOT alphanumeric, error (except ,X)
                ///////if there's a comma, it has to be followed by an x AND x has to be last element
                //string,x
                //string,xxxx

        }

    }
}
