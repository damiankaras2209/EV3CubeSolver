package me.damiankaras.ev3cubesolver.brick;

public class Logger {

    static void logAndSend(String log) {
        logAndSend(true, log);
    }

    static void logAndSend(boolean breakLine, String log) {
        System.out.println(log);
        Network.getInstance().send(NetworkData.DATATYPE_LOG, log + "/" + (breakLine ? 1 : 0));
    }

}
