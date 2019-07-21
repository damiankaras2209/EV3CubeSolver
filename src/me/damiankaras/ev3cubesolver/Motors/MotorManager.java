package me.damiankaras.ev3cubesolver.Motors;


import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.RegulatedMotor;

public class MotorManager {

    private static final MotorManager instance = new MotorManager();
    public static MotorManager getInstance() {
        return instance;
    }

//    public static final int MOTOR_BASKET = 0;
//    public static final int MOTOR_SENSOR = 1;
//    public static final int MOTOR_ARM = 2;

    private ArmMotor armMotor;
    private BasketMotor basketMotor;
    private SensorMotor sensorMotor;

    private MotorManager() {
        armMotor = new ArmMotor();
        basketMotor = new BasketMotor();
        sensorMotor = new SensorMotor();
    }

//    public RegulatedMotor getMotor(int motor) {
//        switch (motor) {
//            case MOTOR_BASKET: return basketMotor;
//            case MOTOR_SENSOR: return sensorMotor;
//            case MOTOR_ARM: return armMotor;
//            default: return null;
//        }
//    }

    public ArmMotor getArmMotor() {
        return armMotor;
    }

    public BasketMotor getBasketMotor() {
        return basketMotor;
    }

    public SensorMotor getSensorMotor() {
        return sensorMotor;
    }
}
