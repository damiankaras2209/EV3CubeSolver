package me.damiankaras.ev3cubesolver.brick.Motors;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class SensorMotor extends EV3LargeRegulatedMotor implements MotorInterface {

    public static final int IDLE = -400;
    public static final int CENTER = -785;
    public static final int EDGE = -860 - 10;
    public static final int CORNER = -935 - 10;
    public static final int BACKED_OFF = -1050;

    static int previous;
    
    SensorMotor() {
        super(MotorPort.C);
    }

    public void floatDelayed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isMoving())
                        flt();
                }
            }
        }).start();
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

        setPosition(IDLE, false);

//        System.out.println("Max speed" + getMaxSpeed());
        setSpeed(getMaxSpeed());
        setStallThreshold(20, 500);
        floatDelayed();
    }

    public void setPosition(int position, boolean immediateReturn) {

//        long t = System.currentTimeMillis();

        if(previous > position && position == EDGE) rotateTo(position - 40, immediateReturn);
        else if(previous < position && position == CORNER) rotateTo(position + 40, immediateReturn);
        else rotateTo(position, immediateReturn);

        previous = position;
//        System.out.println("Move time: " + (System.currentTimeMillis() - t));
//        System.out.println(getTachoCount());
    }

}
