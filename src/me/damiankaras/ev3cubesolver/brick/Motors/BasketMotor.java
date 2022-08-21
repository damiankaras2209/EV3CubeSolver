package me.damiankaras.ev3cubesolver.brick.Motors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class BasketMotor extends EV3MediumRegulatedMotor implements MotorInterface {

    public static final int DEFAULT_SPEED = 700;
    public static final int FLOAT_DELAY = 1000;

    public static final int CW = 0;
    public static final int CCW = 1;
    private static final int GEAR_RATIO = 3;

    BasketMotor() {
        super(MotorPort.B);
        setSpeed(DEFAULT_SPEED);
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

        int x = getPos()%90;
        int dir, deg;

        if(x >= 0) {
            x = Math.abs(x);
            dir = x > 45 ? CW : CCW;
            deg = x > 45 ? 90-x : x;
        } else {
            x = Math.abs(x);
            dir = x > 45 ? CCW : CW;
            deg = x > 45 ? 90-x : x;
        }

        setSpeed(200);
        rotate(dir, deg, false);
        setSpeed(DEFAULT_SPEED);
        resetTacho();
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
        int pos = (int)(getPosition()/GEAR_RATIO + 0.5f);
        return pos >= 360 ? pos - 360 : pos;
    }
}
