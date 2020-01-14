package me.damiankaras.ev3cubesolver.brick;

import me.damiankaras.ev3cubesolver.brick.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.brick.Motors.SensorMotor;
import java.util.ArrayList;

public class Cube {

    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int CW = 0;
    public static final int CCW = 1;

    MotorManager motorManager;
    BasketMotor basketMotor;
    ArmMotor armMotor;
    SensorMotor sensorMotor;
    private ColorScanner colorScanner;

    private float[][] raw;
    private String display;
    private String faces = "ULFRBD";

    private long startTime;
    private long scanTime;
    private long solutionTime;

    private boolean isSolving = false;
    private boolean isScanning = true;

    Cube() {
        raw = new float[54][3];

        StringBuilder s = new StringBuilder(54);
        for (int i=0; i<54; i++)
            s.append('-');
        display = s.toString();

        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

        colorScanner = new ColorScanner(this);
    }

    void close() {
        colorScanner.close();
        System.out.println("Closing sensor port");
    }

    void solve() {
//        Logger.logAndSend("Solving");


//        isScanning = false;
//        display = "WWWWWWOOGOOYBBOBBOBROBRBWWBBYYRYYRYYRGRRGRRGWYYGGOGGOG";
//        sendCube();
//
//        faces = "FRULDB";
//
//        isScanning = false;
//        String solution = "D  R  U' (3f)";
//        solution = solution.substring(0, solution.indexOf('(')-1);
//        ArrayList<String> moves = new ArrayList<>(Arrays.asList(solution.split("  ")));
//        System.out.println(moves);
//        while (moves.size() > 0) {
//            move(moves.get(0));
//            moves.remove(0);
//        }
//        sendCube();

//        scan();

        startTime = System.currentTimeMillis();

        isSolving = true;

        final Solver solver = new Solver();

        new Thread(new Runnable() {
            @Override
            public void run() {

                scan();

                String solution = solver.solve(raw);

//                String solution = solver.solve(TestScans.INORRECT_SCAN);

                if(solution != null) {

                    solution = solution.substring(0, solution.indexOf('('));
                    int length = solution.length() / 3 + 1;
                    ArrayList<String> moves = new ArrayList<>();

                    for (int i = 0; i < length - 1; i++)
                        moves.add(solution.substring(i * 3, (i + 1) * 3).trim());

                    solutionTime = System.currentTimeMillis();
                    Logger.logAndSend(String.format("Solution found in %.2fs (%d moves). Total time: %.2fs",
                            (float) (solutionTime - scanTime) / 1000,
                            moves.size(),
                            (float) (solutionTime - startTime) / 1000));


                    executeMoves(moves);

                    Logger.logAndSend(String.format("Solve completed in %.2fs. Total time: %.2fs",
                            (float) (System.currentTimeMillis() - solutionTime) / 1000,
                            (float) (System.currentTimeMillis() - startTime) / 1000));
                } else {
                    Logger.logAndSend("All methods failed");
                }
            }
        }).start();

        isSolving = false;
    }

    boolean isSolving() {
        return isSolving;
    }

    void scan() {
        isScanning = false;
        Logger.logAndSend("\n\nScanning started");
        colorScanner.scan();
        scanTime = System.currentTimeMillis();
        Logger.logAndSend(String.format("Scanning completed in %.2fs", (float)(scanTime - startTime)/1000));
        isScanning = false;
    }

    void setRaw(int i, float[] raw) {
            this.raw[i] = raw;
//            updateHue(i);
//            updateDisplay(i);
    }

//    public float[] getHue() {
//        return hue;
//    }

    private void updateDisplay(int k) {

        float hue;
        float maxSubMin;


        maxSubMin = Math.max(raw[k][0], Math.max(raw[k][1], raw[k][2])) - Math.min(raw[k][0], Math.min(raw[k][1], raw[k][2]));

        if (raw[k][0] > raw[k][1] && raw[k][0] > raw[k][2])
            hue = (raw[k][1] - raw[k][2]) / maxSubMin;
        else if (raw[k][1] > raw[k][0] && raw[k][1] > raw[k][2])
            hue = 2 + (raw[k][2] - raw[k][0]) / maxSubMin;
        else
            hue = 4 + (raw[k][0] - raw[k][1]) / maxSubMin;

        hue = hue * 60;


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


        int i = k/9;
        int j = k - i*9;

        int targetFacelet = 0;
        int targetFace = 0;

//        switch (i) {
//            case 0: targetFace = 0;break;
//            case 1: targetFace = 1;break;
//            case 2: targetFace = 3;break;
//            case 3: targetFace = 4;break;
//            case 4: targetFace = 5;break;
//            case 5: targetFace = 2;break;
//        }
//
//
//        if(i == 0) {
//            switch (j) {
//                case 0: targetFacelet = 4;break;
//                case 1: targetFacelet = 7;break;
//                case 2: targetFacelet = 6;break;
//                case 3: targetFacelet = 3;break;
//                case 4: targetFacelet = 0;break;
//                case 5: targetFacelet = 1;break;
//                case 6: targetFacelet = 2;break;
//                case 7: targetFacelet = 5;break;
//                case 8: targetFacelet = 8;break;
//            }
//        } else if(i == 1) {
//            switch (j) {
//                case 0: targetFacelet = 4;break;
//                case 1: targetFacelet = 5;break;
//                case 2: targetFacelet = 8;break;
//                case 3: targetFacelet = 7;break;
//                case 4: targetFacelet = 6;break;
//                case 5: targetFacelet = 3;break;
//                case 6: targetFacelet = 0;break;
//                case 7: targetFacelet = 1;break;
//                case 8: targetFacelet = 2;break;
//            }
//        } else if(i == 3) {
//            switch (j) {
//                case 0: targetFacelet = 4;break;
//                case 1: targetFacelet = 3;break;
//                case 2: targetFacelet = 0;break;
//                case 3: targetFacelet = 1;break;
//                case 4: targetFacelet = 2;break;
//                case 5: targetFacelet = 5;break;
//                case 6: targetFacelet = 8;break;
//                case 7: targetFacelet = 7;break;
//                case 8: targetFacelet = 6;break;
//            }
//        } else {
//            switch (j) {
//                case 0: targetFacelet = 4;break;
//                case 1: targetFacelet = 1;break;
//                case 2: targetFacelet = 2;break;
//                case 3: targetFacelet = 5;break;
//                case 4: targetFacelet = 8;break;
//                case 5: targetFacelet = 7;break;
//                case 6: targetFacelet = 6;break;
//                case 7: targetFacelet = 3;break;
//                case 8: targetFacelet = 0;break;
//            }
//        }

        System.out.println(" ");
//        System.out.println("i: " + i + ", j: " + j + " : target " + targetFace + "," + targetFacelet);

        StringBuffer s = new StringBuffer(display);

        switch (j) {
            case 0: targetFacelet = 4;break;
            case 1: targetFacelet = 7;break;
            case 2: targetFacelet = 6;break;
            case 3: targetFacelet = 3;break;
            case 4: targetFacelet = 0;break;
            case 5: targetFacelet = 1;break;
            case 6: targetFacelet = 2;break;
            case 7: targetFacelet = 5;break;
            case 8: targetFacelet = 8;break;
        }

        switch (color) {
            case 0: s.setCharAt(9 * targetFace + targetFacelet, 'W');break;
            case 1: s.setCharAt(9 * targetFace + targetFacelet, 'O');break;
            case 2: s.setCharAt(9 * targetFace + targetFacelet, 'Y');break;
            case 3: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
            case 4: s.setCharAt(9 * targetFace + targetFacelet, 'G');break;
            case 5: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
        }

        display = s.toString();
        sendCube();

    }

    public void executeMoves(ArrayList<String> moves) {
        while (moves.size() > 0) {
            move(moves.get(0));
            moves.remove(0);
        }
    }

    private void move(String move) {

        System.out.println("Move: " + move);
        System.out.println("Faces: " + faces);

        int multiplier = move.contains("2") ? 2 : 1;
        int dir = move.contains("'") ? CW : CCW;
        char ch = move.charAt(0);

        int x = faces.indexOf(ch);
        switch (x) {
            case 0:
                rotateZ();
                rotateZ(true);
                break;
            case 1:
                rotateZ(true);
                break;
            case 2:
                rotateY(CW, 1, false);
                rotateZ(true);
                break;
            case 3:
                rotateY(CW, 2, false);
                rotateZ(true);
                break;
            case 4:
                rotateY(CCW, 1, false);
                rotateZ(true);
                break;
        }

        rotateBottomWall(dir, multiplier);

    }

    void sendCube() {
        Network.getInstance().send(NetworkData.DATATYPE_CUBE, display);
    }

    public void rotateY(int dir, int iterations, boolean immediateReturn) {

        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, iterations*90, immediateReturn);


        StringBuilder stringBuilder = new StringBuilder(display);

        for(int j=0; j<iterations; j++) {
            StringBuilder facesBuilder = new StringBuilder(faces);
            if (dir == 0) {
                facesBuilder.setCharAt(1, faces.charAt(2));
                facesBuilder.setCharAt(2, faces.charAt(3));
                facesBuilder.setCharAt(3, faces.charAt(4));
                facesBuilder.setCharAt(4, faces.charAt(1));
            } else {
                facesBuilder.setCharAt(2, faces.charAt(1));
                facesBuilder.setCharAt(3, faces.charAt(2));
                facesBuilder.setCharAt(4, faces.charAt(3));
                facesBuilder.setCharAt(1, faces.charAt(4));
            }
            faces = facesBuilder.toString();

            System.out.println(faces);

            if(!isScanning) {

                if (dir == 0) {
                    stringBuilder.setCharAt(0, display.charAt(2));
                    stringBuilder.setCharAt(1, display.charAt(5));
                    stringBuilder.setCharAt(2, display.charAt(8));
                    stringBuilder.setCharAt(3, display.charAt(1));
                    stringBuilder.setCharAt(5, display.charAt(7));
                    stringBuilder.setCharAt(6, display.charAt(0));
                    stringBuilder.setCharAt(7, display.charAt(3));
                    stringBuilder.setCharAt(8, display.charAt(6));

                    stringBuilder.setCharAt(3 * 9 + 2, display.charAt(3 * 9 + 0));
                    stringBuilder.setCharAt(3 * 9 + 5, display.charAt(3 * 9 + 1));
                    stringBuilder.setCharAt(3 * 9 + 8, display.charAt(3 * 9 + 2));
                    stringBuilder.setCharAt(3 * 9 + 1, display.charAt(3 * 9 + 3));
                    stringBuilder.setCharAt(3 * 9 + 7, display.charAt(3 * 9 + 5));
                    stringBuilder.setCharAt(3 * 9 + 0, display.charAt(3 * 9 + 6));
                    stringBuilder.setCharAt(3 * 9 + 3, display.charAt(3 * 9 + 7));
                    stringBuilder.setCharAt(3 * 9 + 6, display.charAt(3 * 9 + 8));

                    for (int i = 0; i < 9; i++) {
                        stringBuilder.setCharAt(2 * 9 + i, display.charAt(1 * 9 + i));
                        stringBuilder.setCharAt(1 * 9 + i, display.charAt(5 * 9 + i));
                        stringBuilder.setCharAt(5 * 9 + i, display.charAt(4 * 9 + i));
                        stringBuilder.setCharAt(4 * 9 + i, display.charAt(2 * 9 + i));
                    }

                } else {
                    stringBuilder.setCharAt(2, display.charAt(0));
                    stringBuilder.setCharAt(5, display.charAt(1));
                    stringBuilder.setCharAt(8, display.charAt(2));
                    stringBuilder.setCharAt(1, display.charAt(3));
                    stringBuilder.setCharAt(7, display.charAt(5));
                    stringBuilder.setCharAt(0, display.charAt(6));
                    stringBuilder.setCharAt(3, display.charAt(7));
                    stringBuilder.setCharAt(6, display.charAt(8));

                    stringBuilder.setCharAt(3 * 9 + 0, display.charAt(3 * 9 + 2));
                    stringBuilder.setCharAt(3 * 9 + 1, display.charAt(3 * 9 + 5));
                    stringBuilder.setCharAt(3 * 9 + 2, display.charAt(3 * 9 + 8));
                    stringBuilder.setCharAt(3 * 9 + 3, display.charAt(3 * 9 + 1));
                    stringBuilder.setCharAt(3 * 9 + 5, display.charAt(3 * 9 + 7));
                    stringBuilder.setCharAt(3 * 9 + 6, display.charAt(3 * 9 + 0));
                    stringBuilder.setCharAt(3 * 9 + 7, display.charAt(3 * 9 + 3));
                    stringBuilder.setCharAt(3 * 9 + 8, display.charAt(3 * 9 + 6));

                    for (int i = 0; i < 9; i++) {
                        stringBuilder.setCharAt(1 * 9 + i, display.charAt(2 * 9 + i));
                        stringBuilder.setCharAt(5 * 9 + i, display.charAt(1 * 9 + i));
                        stringBuilder.setCharAt(4 * 9 + i, display.charAt(5 * 9 + i));
                        stringBuilder.setCharAt(2 * 9 + i, display.charAt(4 * 9 + i));
                    }
                }
            }
        }

        display = stringBuilder.toString();
        sendCube();
    }

    public void rotateZ() {
        rotateZ(false);
    }

    public void rotateZ(boolean lockAtEnd) {

        armMotor.turnCube(lockAtEnd);


        StringBuilder  facesBuilder = new StringBuilder(faces);
            facesBuilder.setCharAt(0, faces.charAt(3));
            facesBuilder.setCharAt(1, faces.charAt(0));
            facesBuilder.setCharAt(3, faces.charAt(5));
            facesBuilder.setCharAt(5, faces.charAt(1));

        faces = facesBuilder.toString();
        System.out.println(faces);

        if(!isScanning) {
            StringBuilder stringBuilder = new StringBuilder(display);

            stringBuilder.setCharAt(2*9 + 2, display.charAt(2*9 + 0));
            stringBuilder.setCharAt(2*9 + 5, display.charAt(2*9 + 1));
            stringBuilder.setCharAt(2*9 + 8, display.charAt(2*9 + 2));
            stringBuilder.setCharAt(2*9 + 1, display.charAt(2*9 + 3));
            stringBuilder.setCharAt(2*9 + 7, display.charAt(2*9 + 5));
            stringBuilder.setCharAt(2*9 + 0, display.charAt(2*9 + 6));
            stringBuilder.setCharAt(2*9 + 3, display.charAt(2*9 + 7));
            stringBuilder.setCharAt(2*9 + 6, display.charAt(2*9 + 8));

            stringBuilder.setCharAt(5*9 + 0, display.charAt(5*9 + 2));
            stringBuilder.setCharAt(5*9 + 1, display.charAt(5*9 + 5));
            stringBuilder.setCharAt(5*9 + 2, display.charAt(5*9 + 8));
            stringBuilder.setCharAt(5*9 + 3, display.charAt(5*9 + 1));
            stringBuilder.setCharAt(5*9 + 5, display.charAt(5*9 + 7));
            stringBuilder.setCharAt(5*9 + 6, display.charAt(5*9 + 0));
            stringBuilder.setCharAt(5*9 + 7, display.charAt(5*9 + 3));
            stringBuilder.setCharAt(5*9 + 8, display.charAt(5*9 + 6));

            stringBuilder.setCharAt( 0, display.charAt(9 + 6));
            stringBuilder.setCharAt( 1, display.charAt(9 + 3));
            stringBuilder.setCharAt( 2, display.charAt(9 + 0));
            stringBuilder.setCharAt( 3, display.charAt(9 + 7));
            stringBuilder.setCharAt( 4, display.charAt(9 + 4));
            stringBuilder.setCharAt( 5, display.charAt(9 + 1));
            stringBuilder.setCharAt( 6, display.charAt(9 + 8));
            stringBuilder.setCharAt( 7, display.charAt(9 + 5));
            stringBuilder.setCharAt( 8, display.charAt(9 + 2));

            stringBuilder.setCharAt( 9 + 0, display.charAt(3*9 + 6));
            stringBuilder.setCharAt( 9 + 1, display.charAt(3*9 + 3));
            stringBuilder.setCharAt( 9 + 2, display.charAt(3*9 + 0));
            stringBuilder.setCharAt( 9 + 3, display.charAt(3*9 + 7));
            stringBuilder.setCharAt( 9 + 4, display.charAt(3*9 + 4));
            stringBuilder.setCharAt( 9 + 5, display.charAt(3*9 + 1));
            stringBuilder.setCharAt( 9 + 6, display.charAt(3*9 + 8));
            stringBuilder.setCharAt( 9 + 7, display.charAt(3*9 + 5));
            stringBuilder.setCharAt( 9 + 8, display.charAt(3*9 + 2));

            stringBuilder.setCharAt( 3*9 + 0, display.charAt(4*9 + 6));
            stringBuilder.setCharAt( 3*9 + 1, display.charAt(4*9 + 3));
            stringBuilder.setCharAt( 3*9 + 2, display.charAt(4*9 + 0));
            stringBuilder.setCharAt( 3*9 + 3, display.charAt(4*9 + 7));
            stringBuilder.setCharAt( 3*9 + 4, display.charAt(4*9 + 4));
            stringBuilder.setCharAt( 3*9 + 5, display.charAt(4*9 + 1));
            stringBuilder.setCharAt( 3*9 + 6, display.charAt(4*9 + 8));
            stringBuilder.setCharAt( 3*9 + 7, display.charAt(4*9 + 5));
            stringBuilder.setCharAt( 3*9 + 8, display.charAt(4*9 + 2));

            stringBuilder.setCharAt( 4*9 + 0, display.charAt(6));
            stringBuilder.setCharAt( 4*9 + 1, display.charAt(3));
            stringBuilder.setCharAt( 4*9 + 2, display.charAt(0));
            stringBuilder.setCharAt( 4*9 + 3, display.charAt(7));
            stringBuilder.setCharAt( 4*9 + 4, display.charAt(4));
            stringBuilder.setCharAt( 4*9 + 5, display.charAt(1));
            stringBuilder.setCharAt( 4*9 + 6, display.charAt(8));
            stringBuilder.setCharAt( 4*9 + 7, display.charAt(5));
            stringBuilder.setCharAt( 4*9 + 8, display.charAt(2));

//            for (int i = 0; i < 9; i++) {
//                stringBuilder.setCharAt(0 * 9 + i, display.charAt(1 * 9 + i));
//                stringBuilder.setCharAt(1 * 9 + i, display.charAt(3 * 9 + i));
//                stringBuilder.setCharAt(3 * 9 + i, display.charAt(4 * 9 + i));
//                stringBuilder.setCharAt(4 * 9 + i, display.charAt(0 * 9 + i));
//            }

            display = stringBuilder.toString();
            sendCube();
        }
    }

    public void rotateBottomWall(int dir, int multiplier) {

        armMotor.lock();
        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, multiplier*90 + 15, false);
        basketMotor.rotate(dir == CW ? BasketMotor.CCW : BasketMotor.CW, 15, false);
        armMotor.release();



        StringBuilder stringBuilder = new StringBuilder(display);


        if(dir == 0) {
            stringBuilder.setCharAt(3*9 + 2, display.charAt(3*9 + 0));
            stringBuilder.setCharAt(3*9 + 5, display.charAt(3*9 + 1));
            stringBuilder.setCharAt(3*9 + 8, display.charAt(3*9 + 2));
            stringBuilder.setCharAt(3*9 + 1, display.charAt(3*9 + 3));
            stringBuilder.setCharAt(3*9 + 7, display.charAt(3*9 + 5));
            stringBuilder.setCharAt(3*9 + 0, display.charAt(3*9 + 6));
            stringBuilder.setCharAt(3*9 + 3, display.charAt(3*9 + 7));
            stringBuilder.setCharAt(3*9 + 6, display.charAt(3*9 + 8));

            stringBuilder.setCharAt(2*9 + 2, display.charAt(1*9 + 2));
            stringBuilder.setCharAt(2*9 + 5, display.charAt(1*9 + 5));
            stringBuilder.setCharAt(2*9 + 8, display.charAt(1*9 + 8));

            stringBuilder.setCharAt(1*9 + 2, display.charAt(5*9 + 2));
            stringBuilder.setCharAt(1*9 + 5, display.charAt(5*9 + 5));
            stringBuilder.setCharAt(1*9 + 8, display.charAt(5*9 + 8));

            stringBuilder.setCharAt(5*9 + 2, display.charAt(4*9 + 2));
            stringBuilder.setCharAt(5*9 + 5, display.charAt(4*9 + 5));
            stringBuilder.setCharAt(5*9 + 8, display.charAt(4*9 + 8));

            stringBuilder.setCharAt(4*9 + 2, display.charAt(2*9 + 2));
            stringBuilder.setCharAt(4*9 + 5, display.charAt(2*9 + 5));
            stringBuilder.setCharAt(4*9 + 8, display.charAt(2*9 + 8));
        } else {
            stringBuilder.setCharAt(3*9 + 0, display.charAt(3*9 + 2));
            stringBuilder.setCharAt(3*9 + 1, display.charAt(3*9 + 5));
            stringBuilder.setCharAt(3*9 + 2, display.charAt(3*9 + 8));
            stringBuilder.setCharAt(3*9 + 3, display.charAt(3*9 + 1));
            stringBuilder.setCharAt(3*9 + 5, display.charAt(3*9 + 7));
            stringBuilder.setCharAt(3*9 + 6, display.charAt(3*9 + 0));
            stringBuilder.setCharAt(3*9 + 7, display.charAt(3*9 + 3));
            stringBuilder.setCharAt(3*9 + 8, display.charAt(3*9 + 6));

            stringBuilder.setCharAt(2*9 + 2, display.charAt(4*9 + 2));
            stringBuilder.setCharAt(2*9 + 5, display.charAt(4*9 + 5));
            stringBuilder.setCharAt(2*9 + 8, display.charAt(4*9 + 8));

            stringBuilder.setCharAt(4*9 + 2, display.charAt(5*9 + 2));
            stringBuilder.setCharAt(4*9 + 5, display.charAt(5*9 + 5));
            stringBuilder.setCharAt(4*9 + 8, display.charAt(5*9 + 8));

            stringBuilder.setCharAt(5*9 + 2, display.charAt(1*9 + 2));
            stringBuilder.setCharAt(5*9 + 5, display.charAt(1*9 + 5));
            stringBuilder.setCharAt(5*9 + 8, display.charAt(1*9 + 8));

            stringBuilder.setCharAt(1*9 + 2, display.charAt(2*9 + 2));
            stringBuilder.setCharAt(1*9 + 5, display.charAt(2*9 + 5));
            stringBuilder.setCharAt(1*9 + 8, display.charAt(2*9 + 8));
        }

        display = stringBuilder.toString();
        sendCube();

    }



}
