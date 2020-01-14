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
            case "solve":
                if(!cube.isSolving()) cube.solve();
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
                break;
            case "rotatebottomCW":
                cube.rotateBottomWall(Cube.CW, 1);
                break;
            case "rotatebottomCCW":
                cube.rotateBottomWall(Cube.CCW, 1);
                break;
            case "rotateYCW":
                cube.rotateY(Cube.CW, 1, false);
                break;
            case "rotateYCCW":
                cube.rotateY(Cube.CCW, 1, false);
                break;
            case "rotateZ":
                cube.rotateZ();
                break;
            case "testMove":
                ArrayList<String> moves = new ArrayList<>();
                moves.add("R");
                moves.add("U");
                cube.executeMoves(moves);
                break;
        }
    }

    public void stop() {

    }

    public void reset() {
        sensorMotor.resetPosition();
        armMotor.resetPosition();
        basketMotor.resetPosition();
        cube.close();
        cube = new Cube();
    }
}
