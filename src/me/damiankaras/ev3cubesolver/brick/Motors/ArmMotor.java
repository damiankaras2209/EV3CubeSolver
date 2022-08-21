package me.damiankaras.ev3cubesolver.brick.Motors;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class ArmMotor extends EV3LargeRegulatedMotor implements MotorInterface {

    ArmMotor() {
        super(MotorPort.A);
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

        setSpeed(180);
        setStallThreshold(10, 50);

        backward();
        while (!isStalled());
        stop();
        resetTachoCount();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setStallThreshold(20, 500);
        setSpeed(500);
        floatDelayed();
    }

    public void turnCube(boolean lockAtEnd) {
        rotateTo(180);
        rotateTo(lockAtEnd ? 115 : 10);
    }

    public void turnCube() {
        turnCube(false);
    }

    public void lock() {
        rotateTo(115);
    }

    public void release() {
        rotateTo(10);
    }

}
