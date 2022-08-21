package me.damiankaras.ev3cubesolver.brick;

import me.damiankaras.ev3cubesolver.brick.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.brick.Motors.SensorMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class ColorScanner {

    private static final float MODIFIER_R = 0.95f;
    private static final float MODIFIER_G = 1f;
    private static final float MODIFIER_B = 1f;

    private static final int POSITION_OFFSET = 0;

    Cube cube;

    MotorManager motorManager;
    BasketMotor basketMotor;
    ArmMotor armMotor;
    SensorMotor sensorMotor;

    EV3ColorSensor ev3ColorSensor;
    SensorMode sensorMode;
    float[][] samples;
    int samplesTaken = 0;

    StringBuffer s;

    ColorScanner(Cube cube) {
        this.cube = cube;
        
        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

    }

    void sensorStart() {
        ev3ColorSensor = new EV3ColorSensor(SensorPort.S2);
        sensorMode = ev3ColorSensor.getRGBMode();
    }

    void sensorStop() {
        ev3ColorSensor.close();
        ev3ColorSensor = null;
        sensorMode = null;
    }

    float[][] scan() {

        s = new StringBuffer(54);
        for (int i = 0; i<54; i++)
            s.insert(i, '-');// default initialization


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        sensorStart();

        samples = new float[54][sensorMode.sampleSize()];
        samplesTaken = 0;

        basketMotor.resetTacho();
        basketMotor.setSpeed(350);



        scanFace();
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        scanFace();
        cube.rotateY(Cube.CW, 1, false);
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        cube.rotateZ();
        scanFace();

        basketMotor.floatDelayed();
        basketMotor.setSpeed(BasketMotor.DEFAULT_SPEED);



        sensorMotor.setPosition(SensorMotor.IDLE, true);
        sensorMotor.floatDelayed();

        sensorStop();
        return samples;
    }

    void scanFace() {
        basketMotor.resetTacho();
        sensorMotor.setPosition(SensorMotor.CENTER, false);
        takeSample();
        sensorMotor.setPosition(SensorMotor.EDGE, false);
        takeSample();
        sensorMotor.setPosition(SensorMotor.CORNER, true);

        basketMotor.rotate(BasketMotor.CW, 360, true);

        for (int i=0; i<7; i++) {
//            System.out.println("Tacho: " + basketMotor.getPos());
            while (basketMotor.getPos() < (i+1) * 45 + POSITION_OFFSET) {
                Delay.msDelay(1);
//                System.out.println("Tacho: " + basketMotor.getPos());
            }

            takeSample();

            if (i != 6)
                sensorMotor.setPosition(i % 2 == 1 ? SensorMotor.CORNER : SensorMotor.EDGE, true);

        }

        sensorMotor.setPosition(SensorMotor.IDLE, false);
    }

    void takeSample() {
        sensorMode.fetchSample(samples[samplesTaken], 0);

        System.out.println("Sample " + samplesTaken/9 + "," + (samplesTaken%9) + " at " + basketMotor.getPos() + "deg and " + sensorMotor.getTachoCount() /*+ String.format("#%02X%02X%02X", (int)(samples[samplesTaken][0]*256), (int)(samples[samplesTaken][1]*256), (int)(samples[samplesTaken][2]*256))*/);

        cube.setRaw(samplesTaken%9, samples[samplesTaken]);

        samplesTaken++;
    }
}
