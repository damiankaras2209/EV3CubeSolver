package me.damiankaras.ev3cubesolver;

import me.damiankaras.ev3cubesolver.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.Motors.SensorMotor;

public class Cube {

    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int CW = 0;
    public static final int CCW = 1;

    private String cube;
    MotorManager motorManager;
    BasketMotor basketMotor;
    ArmMotor armMotor;
    SensorMotor sensorMotor;

    Cube() {
        cube = "FFFFFFFFFBBBBBBBBBUUUUUUUUURRRRRRRRRDDDDDDDDDLLLLLLLLL";
        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();
    }

    public void rotateY(int dir, int deg, boolean immediateReturn) {

        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, deg, immediateReturn);

        StringBuilder stringBuilder = new StringBuilder();

                if (dir == 0) {
                    stringBuilder.append(cube.charAt(3));
                    stringBuilder.append(cube.charAt(5));
                    stringBuilder.append(cube.charAt(2));
                    stringBuilder.append(cube.charAt(1));
                    stringBuilder.append(cube.charAt(4));
                    stringBuilder.append(cube.charAt(0));
                } else {
                    stringBuilder.append(cube.charAt(5));
                    stringBuilder.append(cube.charAt(3));
                    stringBuilder.append(cube.charAt(2));
                    stringBuilder.append(cube.charAt(0));
                    stringBuilder.append(cube.charAt(4));
                    stringBuilder.append(cube.charAt(1));
                }

        cube = stringBuilder.toString();

        sendCube();
    }

    public void rotateZ() {

        armMotor.turnCube();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cube.charAt(0));
        stringBuilder.append(cube.charAt(1));
        stringBuilder.append(cube.charAt(3));
        stringBuilder.append(cube.charAt(4));
        stringBuilder.append(cube.charAt(5));
        stringBuilder.append(cube.charAt(2));
        cube = stringBuilder.toString();

        sendCube();
    }

    public void rotateBottomWall(int dir) {

        armMotor.lock();
        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, 90, false);
        armMotor.release();


    }

    void sendCube() {
//        System.out.println(cube);

//        Network net = Network.getInstance();

//        net.send(Network.TYPE_CUBE, cube);

    }

}
