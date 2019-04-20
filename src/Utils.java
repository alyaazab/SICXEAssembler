import java.util.ArrayList;


public class Utils {

    private static ArrayList<Integer> errorIndexList = new ArrayList<>();


    public static Line extractFields(String line) {

        line = ".0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        String label = line.substring(0, 8);

        String operationField = line.substring(9, 15);
        String operand = line.substring(17, 35);
        String comment = line.substring(35, 66);

        System.out.println(label);
        System.out.println(operationField);
        System.out.println(operand);
        System.out.println(comment);

        if(isComment(line))
            return new Line(0, null, null, null, line, null);

        validateFixedFormat(label, operationField, operand);

        return new Line(0, label, operationField, operand, null, errorIndexList);


    }


    private static boolean isComment(String line) {
        return line.charAt(0) == '.';
    }


    private static void validateFixedFormat(String label, String operationField, String operandField) {
        validateLabel(label);
        validateOperationField(operationField);
    }


    private static void validateLabel(String label) {
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


    private static void validateOperationField(String operationField) {

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
}
