import java.util.ArrayList;

public class ObjectCodeGenerator {

    ArrayList<Line> lineArrayList;
    private int n=0, i=0, x=0, b=0, p=0, e=0;
    private String opcode = "";
    private int operationFormat=0;
    private String instructionCode = "", r1="", r2="", flags="",  disp="", address="";

    public ObjectCodeGenerator(ArrayList<Line> lineArrayList) {
        this.lineArrayList = lineArrayList;
    }

    public void generateObjectCode() {
        for(Line line : lineArrayList)
        {
            instructionCode = "";
            //if line is a comment, skip
            if(!line.getComment().equals(""))
                continue;

            Operation operation = line.getOperation();
            String operandField = line.getOperandField();


            //print operation
            if(operation!=null)
            {
                System.out.println(operation.toString());
                operationFormat = operation.getFormat();
            }
            else
            {
                System.out.println("operation is null");
                continue;
            }




            switch(operationFormat)
            {
                case -1:
                    //directive, no opcode


                    break;
                case 2:
                    opcode = convertHexToBin(operation.getBinaryCode());
                    //get r1 and r2
                    r1 = Integer.toString(RegisterTable.getInstance().getRegTable()
                            .get(operandField.substring(0, 1)).getAddress());
                    if(operandField.trim().length() > 1)
                        r2 = Integer.toString(RegisterTable.getInstance().getRegTable()
                                .get(operandField.substring(2, 3)).getAddress());

                    break;


                case 3:
                    opcode = convertHexToBin(operation.getBinaryCode()).substring(0,6);
                    System.out.println("opcode = " + opcode);
                    e = 0;

                    break;


                case 4:
                    opcode = convertHexToBin(operation.getBinaryCode()).substring(0,6);
                    System.out.println("opcode = " + opcode);
                    e = 1;



                    break;
            }

            instructionCode = instructionCode + opcode + r1 + r2;
            System.out.println("INSTRUCTION CODE: " + instructionCode);

        }
    }

    private String convertHexToBin(String hexadecimal) {

        int decimal = Integer.parseInt(hexadecimal, 16);
        String binary = Integer.toBinaryString(decimal);


        return leftPad(binary);
    }

    private static String leftPad(String str){

        String padString = "00000000";

        if(str.length() < 8 )
            return padString.substring(str.length()) + str;
        else
            return str;
    }

}
