import java.util.ArrayList;


public class Utils {

    private ArrayList<Integer> errorIndexList = new ArrayList<>();
    private String commentField;

    private String label, operation, operand;
    private int length;


    public Line extractFields(String line) {

        line = "bgn      lldx    #25                                                                                 ";

        String labelField = line.substring(0, 8);
        String operationField = line.substring(9, 15);
        String operandField = line.substring(17, 35);
//        this.commentField = line.substring(35, 66);

        System.out.println("label: " + labelField);
        System.out.println("operation: " + operationField);
        System.out.println("operand: " + operandField);
//        System.out.println(commentField);

        Line lineObj;
        if(isComment(line)) {
            System.out.println("this line is a comment");
            lineObj = new Line(0, null, null, null, line, null);
            return lineObj;
        }

        lineObj = new Line(0, labelField, operationField, operandField, null, errorIndexList);


        validateFixedFormat(labelField, operationField, operandField);
        System.out.println("errors: " + errorIndexList.size());
//        for(int i=0; i<errorIndexList.size(); i++)
//            System.out.println(errorIndexList.get(i));

//        Error.printErrors(errorIndexList);
        return lineObj;


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
        Operation operation = OperationTable.getOperation(this.operation);
        if (operation == null)
            return;

        switch (this.operation){
            case "equ":
                if (this.label.length() == 0)
                    errorIndexList.add(0); // missing or misplaced label

                if (SymbolTable.getInstance().getSymbol(this.operand) == null)
                    errorIndexList.add(20); // equ should have previously defined operands

                break;
            case "base":
                if (this.label.length() != 0)
                    errorIndexList.add(4); // this statement can't have a label
                break;
            case "resb":
                if (!Character.isLetter(this.operand.charAt(0)))
                    errorIndexList.add(8); // undefined symbol in operand
                break;
            case "word":
                if (!Character.isLetter(this.operand.charAt(0))) {
                    errorIndexList.add(8); // undefined symbol in operand
                    return;
                }
                this.length = 0;
                for (int i = 1; i < this.operand.length(); i++) {
                    if (this.operand.charAt(i) == ',' && Character.isDigit(this.operand.charAt(i + 1))) {
                        this.length += 3;
                    } else if (this.operand.charAt(i) == ',' && !Character.isDigit(this.operand.charAt(i + 1))) {
                        errorIndexList.add(8);
                        break;
                    }
                }
                break;
        }
    }


    private void validateLabel(String labelField) {
        //ALLOW EMPTY LABELS (IN SOME CASES)

        System.out.println("VALIDATING LABEL...");

        //if label starts with whitespace, misplaced label
        if (Character.isWhitespace(labelField.charAt(0)))
        {
            errorIndexList.add(0);
            System.out.println("label starts with whitespace");
        }

        //if label starts with letter, error
        if (!Character.isLetter(labelField.charAt(0)))
        {
            errorIndexList.add(13);
            System.out.println("label starts with undefined symbol");
        }

        //if operation mnemonic is used as label, error
        if(OperationTable.getOptable().get(labelField.trim()) != null)
        {
            errorIndexList.add(19);
            System.out.println("mnemonic used as label");
        }

        labelField = labelField.trim();
        this.label = labelField;


        for (int i = 1; i < labelField.length(); i++){
            if (!Character.isLetterOrDigit(labelField.charAt(i)))
            {
                errorIndexList.add(15);
                System.out.println("label contains undefined symbol");
            }
        }

    }


    private void validateOperationField(String operationField) {

        System.out.println("VALIDATING OPERATION...");

        if(Character.isWhitespace(operationField.charAt(0)))
        {
            errorIndexList.add(1);
            System.out.println("operation starts with whitespace");
        }

        operationField = operationField.trim();

        operation = operationField;

        for (int i = 0; i < operationField.length(); i++){
            if (operationField.charAt(i) == ' ')
            {
                errorIndexList.add(16);
                System.out.println("operation contains space in the middle");
            }
        }

        Operation operation = OperationTable.getOptable().get(operationField);
//        System.out.println(operation.getOperationMnemonic());

        if(operation==null)
        {
            errorIndexList.add(7);
            System.out.println("operation doesn't exist in optable");
        }

    }

    private void validateOperandField(String operandField) {

        System.out.println("VALIDATING OPERAND FIELD...");

        if(Character.isWhitespace(operandField.charAt(0)))
        {
            errorIndexList.add(2);
            System.out.println("space before operand");
        }

        operandField = operandField.trim();

        operand = operandField;

        for (int i = 0; i < operandField.length(); i++){
            if (operandField.charAt(i) == ' ')
            {
                errorIndexList.add(8);
                System.out.println("operand field contains spaces in between");
            }
        }
        this.operation = operandField.trim();

        Operation operation = OperationTable.getOptable().get(this.operation);
        if (operation == null)
            return;
        int operationFormat = operation.getFormat();

        switch(operationFormat)
        {
            case 2:
                System.out.println("operation is format 2");
                //CLEAR and TIXR instructions can have 1 register operand only
                //SHIFTL and SHIFTR r1,n

                //TODO: LENGTH CAN BE 1 IF CLEAR OR TIXR INSTRUCTION
                if(operandField.length() != 3)
                    errorIndexList.add(18);
                if(operandField.charAt(1) != ',')
                    errorIndexList.add(17);

                if(RegisterTable.getInstance().getRegTable().get(operandField.charAt(0)) == null ||
                        RegisterTable.getInstance().getRegTable().get(operandField.charAt(2)) == null )
                {
                    errorIndexList.add(11);
                    System.out.println("invalid register");
                }
                break;

            case 3:
                System.out.println("format 3");
                if(Character.isDigit(operandField.charAt(0)))
                {
                    System.out.println("operand cant start with digit, undefined symbol");
                    errorIndexList.add(8);
                }
                else if(operandField.charAt(0) != '#' && operandField.charAt(0) != '@' &&
                        !Character.isLetter(operandField.charAt(0)))
                {
                    System.out.println("undefined symbol in operand at index 0");
                    errorIndexList.add(8);
                }


                for (int i = 1; i < operandField.length(); i++){
                    if (!Character.isLetterOrDigit(operandField.charAt(i)))
                    {
                        if(operandField.charAt(i) == ',' && i == operandField.length()-2 && operandField.charAt(i+1) == 'x')
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


                //first character must be alphabetic/#/@
                //loop: if any characters after character 0 is NOT alphanumeric, error (except ,X)
                ///////if there's a comma, it has to be followed by an x AND x has to be last element
                //string,x
                //string,xxxx

        }

    }
}
