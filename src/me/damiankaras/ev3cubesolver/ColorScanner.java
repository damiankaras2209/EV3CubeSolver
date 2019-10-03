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

import java.util.Arrays;

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

    boolean[] colorsSent = new boolean[6];

    StringBuffer s;

    ColorScanner(Cube cube) {


        this.cube = cube;
        
        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

        ev3ColorSensor = new EV3ColorSensor(SensorPort.S2);
        sensorMode = ev3ColorSensor.getRGBMode();
    }

    float[][] scan() {

        s = new StringBuffer(54);
        for (int i = 0; i<54; i++)
            s.insert(i, '-');// default initialization

        Arrays.fill(colorsSent, false);

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
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        scanFace();
        cube.rotateY(Cube.CW, 90, false);
        cube.rotateZ();
        scanFace();
        cube.rotateZ();
        cube.rotateZ();
        scanFace();



//        correctSamples();



        for(int i=0; i<samplesTaken; i++) {
            System.out.println(String.format("%.8f,%.8f,%.8f", samples[i][0], samples[i][1], samples[i][2]));
        }


        return samples;
    }

    void scanFace() {
        basketMotor.resetTacho();
        sensorMotor.setPosition(SensorMotor.CENTER, false);
        takeSample();
        sensorMotor.setPosition(SensorMotor.EDGE, false);
        takeSample();

        cube.rotateY(Cube.CW, 360, true);
//        basketMotor.rotate(BasketMotor.CW, 360, true);

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


//        System.out.println("End");
    }

    void takeSample() {
        sensorMode.fetchSample(samples[samplesTaken], 0);

        if(samplesTaken % 9 == 0) {
            samples[samplesTaken][0] *= MODIFIER_R;
            samples[samplesTaken][1] *= MODIFIER_G;
            samples[samplesTaken][2] *= MODIFIER_B;
        }


        float hue;

        float maxSubMin;

//        System.out.println("Hues:");


            maxSubMin = Math.max(samples[samplesTaken][0], Math.max(samples[samplesTaken][1], samples[samplesTaken][2])) - Math.min(samples[samplesTaken][0], Math.min(samples[samplesTaken][1], samples[samplesTaken][2]));

            if(samples[samplesTaken][0] > samples[samplesTaken][1] && samples[samplesTaken][0] > samples[samplesTaken][2])
                hue = (samples[samplesTaken][1] - samples[samplesTaken][2]) / maxSubMin;
            else if(samples[samplesTaken][1] > samples[samplesTaken][0] && samples[samplesTaken][1] > samples[samplesTaken][2])
                hue = 2 + (samples[samplesTaken][2] - samples[samplesTaken][0]) / maxSubMin;
            else
                hue = 4 + (samples[samplesTaken][0] - samples[samplesTaken][1]) / maxSubMin;

            hue *= 60;


        float closestValue = 360;
        int closestInd = 0;
        float diff;
        
        for(int j=0; j<6; j++) {
            diff = Math.abs(hue - Solver.Hue.values()[j].getHue());

            if(diff < closestValue) {
                closestValue = diff;
                closestInd = j;
            }
        }

        int color = closestInd;



        int i = samplesTaken/9;
        int j = samplesTaken - i*9;

        System.out.println("i: " + i + ", j: " + j);



        int targetFacelet = 0;
        int targetFace = 0;


        if(i == 0) {
            switch (j) {
                case 0: targetFacelet = 4;break;
                case 1: targetFacelet = 5;break;
                case 2: targetFacelet = 2;break;
                case 3: targetFacelet = 1;break;
                case 4: targetFacelet = 0;break;
                case 5: targetFacelet = 3;break;
                case 6: targetFacelet = 6;break;
                case 7: targetFacelet = 7;break;
                case 8: targetFacelet = 8;break;
            }
        } else if(i == 1) {
            switch (j) {
                case 0: targetFacelet = 4;break;
                case 1: targetFacelet = 7;break;
                case 2: targetFacelet = 8;break;
                case 3: targetFacelet = 5;break;
                case 4: targetFacelet = 2;break;
                case 5: targetFacelet = 1;break;
                case 6: targetFacelet = 0;break;
                case 7: targetFacelet = 3;break;
                case 8: targetFacelet = 6;break;
            }
        } else if(i == 2) {
            switch (j) {
                case 0: targetFacelet = 4;break;
                case 1: targetFacelet = 3;break;
                case 2: targetFacelet = 6;break;
                case 3: targetFacelet = 7;break;
                case 4: targetFacelet = 8;break;
                case 5: targetFacelet = 5;break;
                case 6: targetFacelet = 2;break;
                case 7: targetFacelet = 1;break;
                case 8: targetFacelet = 0;break;
            }
        } else if(i == 3) {
            switch (j) {
                case 0: targetFacelet = 4;break;
                case 1: targetFacelet = 1;break;
                case 2: targetFacelet = 0;break;
                case 3: targetFacelet = 3;break;
                case 4: targetFacelet = 6;break;
                case 5: targetFacelet = 7;break;
                case 6: targetFacelet = 8;break;
                case 7: targetFacelet = 5;break;
                case 8: targetFacelet = 2;break;
            }
        } else {
            switch (j) {
                case 0: targetFacelet = 4;break;
                case 1: targetFacelet = 3;break;
                case 2: targetFacelet = 6;break;
                case 3: targetFacelet = 7;break;
                case 4: targetFacelet = 8;break;
                case 5: targetFacelet = 5;break;
                case 6: targetFacelet = 2;break;
                case 7: targetFacelet = 1;break;
                case 8: targetFacelet = 0;break;
            }
        }

        //scan:     U R D L B F
        //solver:   U R F D L B

        switch (i) {
            case 0: targetFace = 0;break;
            case 1: targetFace = 1;break;
            case 2: targetFace = 3;break;
            case 3: targetFace = 4;break;
            case 4: targetFace = 5;break;
            case 5: targetFace = 2;break;
        }

//                switch (color[9 * i + j]) {
//                    case 0: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
//                    case 1: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
//                    case 2: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
//                    case 3: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
//                    case 4: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
//                    case 5: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
//                }
//
        switch (color) {
            case 0: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
            case 1: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
            case 2: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
            case 3: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
            case 4: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
            case 5: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
        }

        if(!colorsSent[color]) {
            Network.getInstance().send(Network.TYPE_COLOR, (Character.toString(s.charAt(9 * targetFace + targetFacelet)) + color));
            colorsSent[color] = true;
        }



        Network.getInstance().send(Network.TYPE_CUBE, s.toString());

//            System.out.print(color[i]);


        
//        System.out.println("Sample " + samplesTaken + " at " + BasketMotor.getTacho()/BasketMotor.GEAR_RATIO + "deg - R: " + samples[samplesTaken][0] + " G: " + samples[samplesTaken][1] + " B: " + samples[samplesTaken][2]);
        System.out.println("Sample " + (samplesTaken + 1) + " at " + basketMotor.getTacho() + "deg - " /*+ String.format("#%02X%02X%02X", (int)(samples[samplesTaken][0]*256), (int)(samples[samplesTaken][1]*256), (int)(samples[samplesTaken][2]*256))*/);
        samplesTaken++;
    }

    void correctSamples() {

        for(int i=0; i<53; i+=9) {
            System.out.println("Correcting sample " + i);
            samples[i][0] *= MODIFIER_R;
            samples[i][1] *= MODIFIER_G;
            samples[i][2] *= MODIFIER_B;
        }

    }
}
