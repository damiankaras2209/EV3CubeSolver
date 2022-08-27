package me.damiankaras.ev3cubesolver.brick;

import me.damiankaras.ev3cubesolver.brick.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.brick.Motors.SensorMotor;

import java.util.ArrayList;

public class Controller {

    private static final Controller instance = new Controller();
    public static Controller getInstance() {
        return instance;
    }

    private MotorManager motorManager;
    private BasketMotor basketMotor;
    private ArmMotor armMotor;
    private SensorMotor sensorMotor;

    private Cube cube;
    private Network net;

    private Controller() {
        net = Network.getInstance();

        motorManager = MotorManager.getInstance();
        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

        sensorMotor.resetPosition();
        armMotor.resetPosition();
//        basketMotor.resetPosition();
        cube = new Cube();

        net.setOnConnect(new Runnable() {
            @Override
            public void run() {
                cube.sendCube();
            }
        });
    }

    public void executeCommand(String cmd) {
        switch (cmd) {
            case "scan":
                if(!cube.isBusy()) cube.scan();
                break;
            case "solve":
                if(!cube.isBusy()) cube.solve();
                break;
            case "reset":
                reset();
                break;
            case "basketCW":
                basketMotor.forward();
                break;
            case "basketCCW":
                basketMotor.backward();
                break;
            case "basketSTOP":
                basketMotor.stop();
                break;
            case "lock":
                armMotor.lock();
                break;
            case "release":
                armMotor.release();
                armMotor.floatDelayed();
                break;
            case "rotatebottomCW":
                cube.rotateBottomWall(Cube.CW, 1);
                basketMotor.floatDelayed();
                armMotor.floatDelayed();
                break;
            case "rotatebottomCCW":
                cube.rotateBottomWall(Cube.CCW, 1);
                basketMotor.floatDelayed();
                armMotor.floatDelayed();
                break;
            case "rotateYCW":
                cube.rotateY(Cube.CW, 1, false);
                basketMotor.floatDelayed();
                break;
            case "rotateYCCW":
                cube.rotateY(Cube.CCW, 1, false);
                basketMotor.floatDelayed();
                break;
            case "rotateZ":
                cube.rotateZ();
                armMotor.floatDelayed();
                break;
            case "testMove":
                ArrayList<String> moves = new ArrayList<>();
                moves.add("B");
                moves.add("R'");
                moves.add("U'");
                moves.add("L");
                moves.add("F2");
                cube.executeMoves(moves);
                break;
            case "fillSolved":
                cube.fillSolved();
        }
    }

    public void stop() {

    }

    public void reset() {
        sensorMotor.resetPosition();
        armMotor.resetPosition();
        basketMotor.resetPosition();
        cube = new Cube();
    }
}
