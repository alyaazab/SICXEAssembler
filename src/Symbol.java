public class Symbol {

    private int address;
    private int length;
    private boolean relocatable;
    private String label;


    public Symbol(String label, int address, int length, boolean relocatable) {
        this.address = address;
        this.length = length;
        this.relocatable = relocatable;
        this.label = label;
    }
}
