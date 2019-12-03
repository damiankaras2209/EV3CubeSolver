package me.damiankaras.ev3cubesolver;

public class Logger {

    static void logAndSend(String log) {
        System.out.println(log);
        Network.getInstance().send(NetworkData.DATATYPE_LOG, log);
    }

}
