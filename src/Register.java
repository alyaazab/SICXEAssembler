public class Register {

    private static Register ourInstance = new Register();

    public static Register getInstance() {
        return ourInstance;
    }

    private Register() {
    }


    private int A, X, L, B, S, T, PC, SW;


}
