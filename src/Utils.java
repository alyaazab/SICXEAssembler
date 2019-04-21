import java.util.ArrayList;


public class Utils {

    private ArrayList<Integer> errorIndexList = new ArrayList<>();
    private Line lineObj;
    private String operationField;
    private String operandField;
    private String labelField;
    private String commentField;


    public Line extractFields(String line) {

        line = ".0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        this.labelField = line.substring(0, 8);
        this.operationField = line.substring(9, 15);
        this.operandField = line.substring(17, 35);
        this.commentField = line.substring(35, 66);

        System.out.println(labelField);
        System.out.println(operationField);
        System.out.println(operandField);
        System.out.println(commentField);

        if(isComment(line)) {
            lineObj = new Line(0, null, null, null, line, null);
            return lineObj;
        }

        lineObj = new Line(0, labelField, operationField, operandField, null, errorIndexList);


        validateFixedFormat(labelField, operationField, operandField);

        return lineObj;


    }


    private boolean isComment(String line) {
        return line.charAt(0) == '.';
    }


    private void validateFixedFormat(String labelField, String operationField, String operandField) {
        validateLabel(labelField);
        validateOperationField(operationField);
    }


    private void validateLabel(String label) {
        if (Character.isWhitespace(label.charAt(0)))
            errorIndexList.add(0); // misplaced label

        if (!Character.isLetter(label.charAt(0)))
            errorIndexList.add(13);

        label = label.trim();
        for (int i = 0; i < label.length(); i++){
            if (label.charAt(i) == ' ')
                errorIndexList.add(15);
        }

    }


    private void validateOperationField(String operationField) {

        if(Character.isWhitespace(operationField.charAt(0)))
            errorIndexList.add(1);

        operationField = operationField.trim();

        for (int i = 0; i < operationField.length(); i++){
            if (operationField.charAt(i) == ' ')
                errorIndexList.add(16);
        }

        Operation operation = OperationTable.getOptable().get(operationField);
        System.out.println(operation.getOperationMnemonic());

        if(operation==null)
            errorIndexList.add(7);

    }

    private void validateOperandField(String operandField) {
        if(Character.isWhitespace(operandField.charAt(0)))
            errorIndexList.add(2);

        operandField = operandField.trim();

        if(operandField.length() != 3)
            errorIndexList.add(18);


        for (int i = 0; i < operandField.length(); i++){
            if (operandField.charAt(i) == ' ')
                errorIndexList.add(8);
        }

        Operation operation = OperationTable.getOptable().get(operationField.trim());
        int operationFormat = operation.getFormat();

        switch(operationFormat)
        {
            case 2:
                if(operandField.charAt(1) != ',')
                    errorIndexList.add(17);

                if(RegisterTable.getInstance().getRegTable().get(operandField.charAt(0)) == null ||
                        RegisterTable.getInstance().getRegTable().get(operandField.charAt(0)) == null )
                    errorIndexList.add(11);
                break;

            case 3:
                //first character must be alphabetic/#/@
                //loop: if any characters after character 0 is NOT alphanumeric, error (except ,X)
                ///////if there's a comma, it has to be followed by an x


        }

    }
}
