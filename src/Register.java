import java.util.HashMap;

public class Register {

    private static Register ourInstance = new Register();

    public static Register getInstance() {
        return ourInstance;
    }

    private Register() {
    }


    private HashMap<String, Integer> registerMap = new HashMap<>();

    public Integer getRegisterNumber(String reg){
        return registerMap.get(reg);
    }

    public void initializeRegisterMap(){
        // TODO: add registers to map manually
    }

}
