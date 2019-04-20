public class Main {

    public static void main(String[] args) {
        OperationTable.fillOpTable();
        Line line = Utils.extractFields("");
        System.out.println("comment is " + line.getComment());
    }

}
