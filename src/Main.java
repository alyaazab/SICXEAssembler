public class Main {

    public static void main(String[] args) {
        OperationTable.fillOpTable();
        Utils utils = new Utils();
        RegisterTable.getInstance().fillRegisterTable();
        utils.extractFields("");

    }

}
