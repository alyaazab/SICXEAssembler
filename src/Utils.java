public class Utils {


    public static boolean validateFixedFormat(String line) {

        line = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        String label = line.substring(0, 8);

        String operation = line.substring(9, 15);
        String operand = line.substring(17, 35);
        String comment = line.substring(35, 66);

        System.out.println(label);
        System.out.println(operation);
        System.out.println(operand);
        System.out.println(comment);
        return true;


    }
}
