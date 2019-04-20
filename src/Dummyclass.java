import java.util.ArrayList;

public class Dummyclass {


    private static ArrayList<Integer> errorIndexList = new ArrayList<>();


    public static Line extractFields(String lineString){


        lineString = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        String label = lineString.substring(0, 8);

        String operation = lineString.substring(9, 15);
        String operand = lineString.substring(17, 35);
        String comment = lineString.substring(35, 66);

        System.out.println(label);
        System.out.println(operation);
        System.out.println(operand);
        System.out.println(comment);

        validateFixedFormat(label, operation, operand, comment);

        Line line = new Line(label, operation, operand);

        //TODO: set errors in Line class

        return line;
    }


    public static boolean validateFixedFormat(String label, String operation, String operand, String comment) {
        return validateLabel(label) && validateOperation(operation) && validateOperand(operand) && validateComment(comment);
    }

    public static boolean validateComment(String comment) {

        int size = errorIndexList.size();
        String comm = comment.trim();

        if(comm.charAt(0) != '.')
            return false;

        if (comment.charAt(0) == ' ')
            errorIndexList.add(14);

        return errorIndexList.size() <= size;
    }


    public static boolean validateOperand(String operand) {
        return false;
    }

    public static boolean validateOperation(String operation) {
        return false;
    }

    public static boolean validateLabel(String label) {
        int size = errorIndexList.size();
        if (label.charAt(0) == ' ')
            errorIndexList.add(0); // miss-placed label

        if (!Character.isLetter(label.charAt(0)))
            errorIndexList.add(13);

        label = label.trim();
        for (int i = 0; i < label.length(); i++){
            if (label.charAt(i) == ' ')
                errorIndexList.add(15);
        }

        return errorIndexList.size() <= size;
    }
    
    
    
}
