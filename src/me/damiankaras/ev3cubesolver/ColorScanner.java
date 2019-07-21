package me.damiankaras.ev3cubesolver;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
import me.damiankaras.ev3cubesolver.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.Motors.SensorMotor;

public class ColorScanner {

    private static final int POSITION_OFFSET = 0;

    MotorManager motorManager;
    BasketMotor basketMotor;
    ArmMotor armMotor;
    SensorMotor sensorMotor;

    EV3ColorSensor ev3ColorSensor;
    SensorMode sensorMode;
    float[][] samples;
    int samplesTaken = 0;

    ColorScanner() {
        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

        ev3ColorSensor = new EV3ColorSensor(SensorPort.S2);
        sensorMode = ev3ColorSensor.getRGBMode();
    }

    void scan() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        samples = new float[54][sensorMode.sampleSize()];
        samplesTaken = 0;

        basketMotor.resetTacho();
        basketMotor.setSpeed(200);

        scanFace();
        armMotor.turnCube();
        scanFace();
        armMotor.turnCube();
        scanFace();
        armMotor.turnCube();
        scanFace();
        basketMotor.rotate(BasketMotor.CW, 90, false);
        armMotor.turnCube();
        scanFace();
        armMotor.turnCube();
        armMotor.turnCube();
        scanFace();





//        correctSamples();


        for(int i=0; i<samplesTaken; i++) {
            System.out.println(samples[i][0] + "," + samples[i][1] + "," + samples[i][2]);
        }



    }

    void scanFace() {
        basketMotor.resetTacho();
        sensorMotor.setPosition(SensorMotor.CENTER, false);
        takeSample();
        sensorMotor.setPosition(SensorMotor.EDGE, false);
        takeSample();

        basketMotor.rotate(BasketMotor.CW, 360, true);

        for (int i=0; i<8; i++) {
            final int finalI = i;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(finalI != 0) takeSample();
                    if (finalI != 7)
                        sensorMotor.setPosition(finalI % 2 == 0 ? SensorMotor.CORNER : SensorMotor.EDGE, true);
                }
            }).start();

            if (finalI != 7)
                while (basketMotor.getPos() < (i+1) * 45 + POSITION_OFFSET) {
                    Delay.msDelay(1);
//                System.out.println("Tacho: " + basketMotor.getPos());
                }

        }

        sensorMotor.setPosition(SensorMotor.IDLE, false);


        System.out.println("End");
    }

    void takeSample() {
        sensorMode.fetchSample(samples[samplesTaken], 0);
//        System.out.println("Sample " + samplesTaken + " at " + BasketMotor.getTacho()/BasketMotor.GEAR_RATIO + "deg - R: " + samples[samplesTaken][0] + " G: " + samples[samplesTaken][1] + " B: " + samples[samplesTaken][2]);
        System.out.println("Sample " + (samplesTaken + 1) + " at " + basketMotor.getTacho() + "deg - " /*+ String.format("#%02X%02X%02X", (int)(samples[samplesTaken][0]*256), (int)(samples[samplesTaken][1]*256), (int)(samples[samplesTaken][2]*256))*/);
        samplesTaken++;
    }

    void correctSamples() {

        for(int i=0; i<53; i+=9) {
            System.out.println("Correcting sample " + i);
            samples[i][0] *= 0.93;
        }

    }
}
