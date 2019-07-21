package me.damiankaras.ev3cubesolver.Motors;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;

public class SensorMotor extends EV3LargeRegulatedMotor {

    public static final int IDLE = -400;
    public static final int CENTER = -785;
    public static final int EDGE = -860 - 10;
    public static final int CORNER = -935 - 10;
    public static final int BACKED_OFF = -1050;

    static int previous;
    
    SensorMotor() {
        super(MotorPort.C);
    }

    public void resetPosition() {

        setStallThreshold(10, 50);
        forward();
        while (!isStalled());
        stop();
        resetTachoCount();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setPosition(-400, false);

        setSpeed(720);
        setStallThreshold(20, 500);

    }

    public void setPosition(int position, boolean immediatePosiotion) {

        long t = System.currentTimeMillis();

        if(previous > position && position == EDGE) rotateTo(position - 40, immediatePosiotion);
        else if(previous < position && position == CORNER) rotateTo(position + 40, immediatePosiotion);
        else rotateTo(position);

        previous = position;
//        System.out.println("Move time: " + (System.currentTimeMillis() - t));
//        System.out.println(getTachoCount());
    }

}
