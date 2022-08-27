package me.damiankaras.ev3cubesolver.brick;

import me.damiankaras.ev3cubesolver.brick.Motors.ArmMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.BasketMotor;
import me.damiankaras.ev3cubesolver.brick.Motors.MotorManager;
import me.damiankaras.ev3cubesolver.brick.Motors.SensorMotor;
import java.util.ArrayList;
import java.util.Arrays;

public class Cube {

    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int CW = 0;
    public static final int CCW = 1;

    public static final char[] colorChars = {'W', 'O', 'Y', 'R', 'G', 'B', '-'};
    public static final short[] colorToDisplay = {4, 7, 6, 3, 0, 1, 2, 5, 8, 13, 14, 17, 16, 15, 12, 9, 10, 11, 31, 28, 29, 32, 35, 34, 33, 30, 27, 40, 39, 36, 37, 38, 41, 44, 43, 42, 49, 46, 47, 50, 53, 52, 51, 48, 45, 22, 19, 20, 23, 26, 25, 24, 21, 18};
    public static final short[] displayToColor = {4, 5, 6, 3, 0, 7, 2, 1, 8, 15, 16, 17, 14, 9, 10, 13, 12, 11, 53, 46, 47, 52, 45, 48, 51, 50, 49, 26, 19, 20, 25, 18, 21, 24, 23, 22, 29, 30, 31, 28, 27, 32, 35, 34, 33, 44, 37, 38, 43, 36, 39, 42, 41, 40};
    public static final short[] rotateColorZ =        {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 1, 2, 3, 4, 5, 6, 7, 8, 36, 39, 40, 41, 42, 43, 44, 37, 38, 45, 52, 53, 46, 47, 48, 49, 50, 51};
    public static final short[] rotateColorY_CW =      {0, 3, 4, 5, 6, 7, 8, 1, 2, 36, 39, 40, 41, 42, 43, 44, 37, 38, 18, 25, 26, 19, 20, 21, 22, 23, 24, 45, 52, 53, 46, 47, 48, 49, 50, 51, 27, 30, 31, 32, 33, 34, 35, 28, 29, 9, 16, 17, 10, 11, 12, 13, 14, 15};
    public static final short[] rotateColorY_CCW =     {0, 7, 8, 1, 2, 3, 4, 5, 6, 45, 48, 49, 50, 51, 52, 53, 46, 47, 18, 21, 22, 23, 24, 25, 26, 19, 20, 36, 43, 44, 37, 38, 39, 40, 41, 42, 9, 16, 17, 10, 11, 12, 13, 14, 15, 27, 30, 31, 32, 33, 34, 35, 28, 29};
    public static final short[] rotateColorD_CW =       {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 39, 40, 12, 13, 14, 15, 16, 38, 18, 25, 26, 19, 20, 21, 22, 23, 24, 27, 28, 29, 30, 47, 48, 49, 34, 35, 36, 37, 31, 32, 33, 41, 42, 43, 44, 45, 46, 17, 10, 11, 50, 51, 52, 53};
    public static final short[] rotateColorD_CCW =      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 48, 49, 12, 13, 14, 15, 16, 47, 18, 21, 22, 23, 24, 25, 26, 19, 20, 27, 28, 29, 30, 38, 39, 40, 34, 35, 36, 37, 17, 10, 11, 41, 42, 43, 44, 45, 46, 31, 32, 33, 50, 51, 52, 53};



    MotorManager motorManager;
    BasketMotor basketMotor;
    ArmMotor armMotor;
    SensorMotor sensorMotor;
    private ColorScanner colorScanner;

    private float[][] raw;
    private int[] colors;
    private String faces = "ULFRBD";

    private long startTime;
    private long scanTime;
    private long solutionTime;

    private boolean isSolving = false;
    private boolean isScanning = false;
    private boolean isScanGood = false;

    Cube() {
        raw = new float[54][3];
        colors = new int[54];
        for (int i=0; i<54; i++)
            colors[i] = 6;

/*        for (int i=0; i<54; i++) {
            System.out.println(i + " = " + colorToDisplayIndex(i) + " = " + displayToColorIndex(colorToDisplayIndex(i)));
        }*/

        motorManager = MotorManager.getInstance();

        basketMotor = motorManager.getBasketMotor();
        armMotor = motorManager.getArmMotor();
        sensorMotor = motorManager.getSensorMotor();

        colorScanner = new ColorScanner(this);
    }

    void fillSolved() {
        StringBuilder s = new StringBuilder(54);
        String str = "052413";
        for (int i=0; i<6; i++)
            for(int j=0; j<9; j++)
                colors[9*i + j] = Integer.parseInt(str.substring(i, i+1));
        faces = "ULFRBD";
        sendCube();
    }

    void fillClean() {
        for (int i=0; i<54; i++)
            colors[i] = 6;
        faces = "ULFRBD";
        isScanGood = false;
        sendCube();
    }

    boolean isBusy() {
        return isSolving || isScanning;
    }

    void solve() {

        startTime = System.currentTimeMillis();

        isSolving = true;

        final Solver solver = new Solver(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                if(!isScanGood)
                    scan();


                String solution = null;
                if(isScanGood)
                    solution = solver.solve(Solver.toSolverString(colors));
                else
                    Logger.logAndSend("fail");


                if(solution != null) {

                    StringBuilder s = new StringBuilder(solution.length());
                    s.insert(0, solution);
                    for(int i=0; i<solution.length(); i++) {
                        switch(solution.charAt(i)) {
                            case 'U': s.setCharAt(i, faces.charAt(0)); break;
                            case 'L': s.setCharAt(i, faces.charAt(1)); break;
                            case 'F': s.setCharAt(i, faces.charAt(2)); break;
                            case 'R': s.setCharAt(i, faces.charAt(3)); break;
                            case 'B': s.setCharAt(i, faces.charAt(4)); break;
                            case 'D': s.setCharAt(i, faces.charAt(5)); break;
                        }
                    }
                    solution = s.toString();

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
                isScanGood = false;
            }
        });
        t.start();

//        try {
//            t.join();
//            for (int i=0; i<54; i++){
//                colors[i] = predictColor(i);
//            }
//            sendCube();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        isSolving = false;
    }

    boolean isSolving() {
        return isSolving;
    }

    void scan() {
        fillClean();
        isScanning = true;
        Logger.logAndSend(true, "Scanning started");
        colorScanner.scan();
        scanTime = System.currentTimeMillis();
        Logger.logAndSend(String.format("Scanning completed in %.2fs", (float)(scanTime - startTime)/1000));
        for(int i=0; i<raw.length; i++) {
            System.out.println(String.format("%.8f,%.8f,%.8f", raw[i][0], raw[i][1], raw[i][2]));
        }
        colors = (new Solver(this)).verify(raw);
        if(colors != null) {
            isScanGood = true;
            sendCube();
        } else {
            isScanGood = false;
            colors = new int[54];
            fillClean();
        }
        isScanning = false;
    }

    void setRaw(int i, float[] raw) {
        this.raw[i] = raw;
//          updateHue(i);
        this.colors[i] = predictColor(i);
        sendCube();
    }

//    public float[] getHue() {
//        return hue;
//    }

    public String getDisplayString() {
        StringBuilder s = new StringBuilder(54);
        for(int i=0; i<54; i++)
            s.insert(i, '-');
        for(int i=0; i<54; i++)
            s.setCharAt(colorToDisplayIndex(i), colorChars[colors[i]]);
        return s.toString();
    }


    public void setColors(int[] colors) {
        this.colors = colors;
        sendCube();
    }

    private int predictColor(int k) {

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

        return color;
    }

    int colorToDisplayIndex(int x) {
        return colorToDisplay[x];
    }

    int displayToColorIndex(int x) {
        return displayToColor[x];
    }

    public void executeMoves(ArrayList<String> moves) {
        while (moves.size() > 0) {
            move(moves.get(0));
            moves.remove(0);
        }
        basketMotor.floatDelayed();
        armMotor.floatDelayed();
    }

    private void move(String move) {

        System.out.println("Move: " + move);
        System.out.println("Faces: " + faces);

        int iterations = move.contains("2") ? 2 : 1;
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

        rotateBottomWall(dir, iterations);

    }

    void sendCube() {
        Network.getInstance().send(NetworkData.DATATYPE_CUBE, getDisplayString() + "," + faces);
    }

    public void rotateY(int dir, int iterations, boolean immediateReturn) {

        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, iterations*90, immediateReturn);

        for(int k=0; k<iterations; k++) {
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


            int[] colorsCopy = Arrays.copyOf(colors, colors.length);
            float[][] rawCopy = new float[raw.length][raw[0].length];
            for(int i=0; i<54; i++)
                for(int j=0; j<3; j++)
                    rawCopy[i][j] = raw[i][j];

            if (dir == CW)
                for (int i = 0; i < 54; i++) {
                    colors[i] = colorsCopy[rotateColorY_CW[i]];
                    for (int j = 0; j < 3; j++)
                        raw[i][j] = rawCopy[rotateColorY_CW[i]][j];
                }
            else
                for (int i = 0; i < 54; i++) {
                    colors[i] = colorsCopy[rotateColorY_CCW[i]];
                    for (int j = 0; j < 3; j++)
                        raw[i][j] = rawCopy[rotateColorY_CCW[i]][j];
                }


            sendCube();
        }
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


        int[] colorsCopy = Arrays.copyOf(colors, colors.length);
        float[][] rawCopy = new float[raw.length][raw[0].length];
        for(int i=0; i<54; i++)
            for(int j=0; j<3; j++)
                rawCopy[i][j] = raw[i][j];

        for(int i=0; i<54; i++) {
            colors[i] = colorsCopy[rotateColorZ[i]];
            for(int j=0; j<3; j++)
                raw[i][j] = rawCopy[rotateColorZ[i]][j];
    }

        sendCube();

    }

    public void rotateBottomWall(int dir, int iterations) {

        armMotor.lock();
        basketMotor.rotate(dir == CW ? BasketMotor.CW : BasketMotor.CCW, iterations*90 + 15, false);
        basketMotor.rotate(dir == CW ? BasketMotor.CCW : BasketMotor.CW, 15, false);
        armMotor.release();

        for(int k=0; k<iterations; k++) {

            int[] colorsCopy = Arrays.copyOf(colors, colors.length);
            float[][] rawCopy = new float[raw.length][raw[0].length];
            for(int i=0; i<54; i++)
                for(int j=0; j<3; j++)
                    rawCopy[i][j] = raw[i][j];

            if (dir == CW)
                for (int i = 0; i < 54; i++) {
                    colors[i] = colorsCopy[rotateColorD_CW[i]];
                    for (int j = 0; j < 3; j++)
                        raw[i][j] = rawCopy[rotateColorD_CW[i]][j];
                }
            else
                for (int i = 0; i < 54; i++) {
                    colors[i] = colorsCopy[rotateColorD_CCW[i]];
                    for (int j = 0; j < 3; j++)
                        raw[i][j] = rawCopy[rotateColorD_CCW[i]][j];
                }

        }
        sendCube();
    }
}
