import static java.lang.Character.isWhitespace;

public class Utils {


    public static Line extractFields(String line) {

        line = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        String label = line.substring(0, 8);

        String operationField = line.substring(9, 15);
        String operand = line.substring(17, 35);
        String comment = line.substring(35, 66);

        System.out.println(label);
        System.out.println(operationField);
        System.out.println(operand);
        System.out.println(comment);

        return new Line(0, label, operationField, operand, null);


    }


    public static boolean validateFixedFormat(String line) {



        return true;
    }

    private static boolean isComment(String line) {
        return line.charAt(0) == '.';
    }

    private static boolean validateOperationField(String operationField) {

        operationField = "addr";

        if(isWhitespace(operationField.charAt(0)))
            return false;

        Operation operation = OperationTable.getOptable().get("lda");
        System.out.println(operation.getOperationMnemonic());

        if(operation==null)
            return false;

        return true;
    }
}
