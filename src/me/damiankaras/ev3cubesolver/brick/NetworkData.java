package me.damiankaras.ev3cubesolver.brick;

public class NetworkData {

    public static final String DATATYPE_CUBE = "CB";
    public static final String DATATYPE_COMMAND = "CMD";
    public static final String DATATYPE_LOG = "L";

    private static NetworkData instance = new NetworkData();

    NetworkData() {

    }

    static NetworkData getInstance() {
        return instance;
    }

    void interpret(String input) {

        int prefixEnd = input.indexOf("/");
        String prefix = input.substring(0, prefixEnd);
        String data = input.substring(prefixEnd + 1);

        System.out.println("Prefix: " + prefix + ", Data: " + data);


//        System.out.println("Interpreter: " + Thread.currentThread());

        switch(prefix) {
            case DATATYPE_COMMAND:
                Controller.getInstance().executeCommand(data);
                break;
        }

    }
}
