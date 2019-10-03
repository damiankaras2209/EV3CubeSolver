package me.damiankaras.ev3cubesolver.Motors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class BasketMotor extends EV3MediumRegulatedMotor {

    BasketMotor() {
        super(MotorPort.B);
    }

    public static final int CW = 0;
    public static final int CCW = 1;
    public static final int GEAR_RATIO = 3;

    public void resetPosition() {
        rotate(90);
        rotate(-90);
        resetTachoCount();
    }

    public void rotate(int dir) {
        rotate(dir, 90, false);
    }

    public void rotate(int dir, int deg, boolean immediateReturn) {
        switch (dir) {
            case CW:
                rotate(deg*GEAR_RATIO, immediateReturn);
                break;
            case CCW:
                rotate(-deg*GEAR_RATIO, immediateReturn);
                break;
        }
    }

    public void resetTacho() {
        resetTachoCount();
    }

    public int getTacho() {
        return getTachoCount()/GEAR_RATIO;
    }

    public int getPos() {
        return (int)(getPosition()/GEAR_RATIO + 0.5f);
    }

    public void rotateAndExecute(int degree, int executeEvery, int positionOffset , Runnable runnable) {
        resetTachoCount();
        int nextExecution = 0;

        rotate(CW, 360, true);

        while(getPos() < degree) {
            new Thread(runnable).start();
            while(getPos() + positionOffset < nextExecution) {

                Delay.msDelay(1);
                System.out.println("Tacho: " + getPos());
            }
            nextExecution += executeEvery;
        }
        System.out.println("End");
//        stop();
    }

}
