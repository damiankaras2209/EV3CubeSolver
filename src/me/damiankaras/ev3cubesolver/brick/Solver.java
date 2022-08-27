package me.damiankaras.ev3cubesolver.brick;


import cs.min2phase.Search;

import java.util.Arrays;

public class Solver {

    private final static short R = 0;
    private final static short G = 1;
    private final static short B = 2;

    private final static int METHOD_CLOSEST_STATIC = 10;
    private final static int METHOD_CLOSEST_CENTER = 11;

    Cube cube;

    enum Hue {
        white(85.677f), orange(-24.532f), yellow(67.052f), red(8.828f), green(150.763f), blue(196.964f);


        private float hue;

        Hue(float hue) {this.hue = hue;}
        public float getHue() {
            return hue;
        }
    }

    public Solver(Cube cube) {
        this.cube = cube;
    }

    int[] verify(float[][] raw) {
        Logger.logAndSend("Verifying");
        int[] cube = generateCube(raw);
        return cube;
    }

    String solve(String solverString) {

        if(!Network.getInstance().isClientConnected()) {
//            Network.getInstance().send(NetworkData.DATATYPE_COMMAND, );

        } else {

            Search search = new Search();

            int mask = 0;
            mask |= false ? Search.USE_SEPARATOR : 0;
            mask |= false ? Search.INVERSE_SOLUTION : 0;
            mask |= true ? Search.APPEND_LENGTH : 0;

            String out;

            Logger.logAndSend(false, "Solving... ");
            out = search.solution(solverString, 21, 100, 0, mask);
            if (!out.contains("Error")) {
                //Logger.logAndSend("success");
                return out;
            }
        }
        return null;
    }

    private int[] generateCube(float[][] raw) {

        float[][] modifiers = {{0.95f,1f,1f}, {1f,1f,1f}};
        int[] colors = new int[54];

        float closestValue;
        int closestInd;
        float diff;
        int[] occurrences;
        float[] hue;

        int[] correctedColors;




        for(int k=0; k<modifiers.length; k++) {

            hue = calculateHue(raw, modifiers[k][0], modifiers[k][1], modifiers[k][2]);
//
//            occurrences = new int[6];
//
//            Logger.logAndSend(false, String.format("   (%.2f, %.2f, %.2f) closest center...   ",
//                    modifiers[k][0],
//                    modifiers[k][1],
//                    modifiers[k][2]));
//
//            for (int i = 0; i < 54; i++) {
//
//                closestValue = 360;
//                closestInd = 0;
//
//                for (int j = 0; j < 6; j++) {
//                    diff = Math.abs(hue[i] - hue[j * 9]);
//
//                    if (diff < closestValue) {
//                        closestValue = diff;
//                        closestInd = j;
//                    }
//                }
//
//                colors[i] = closestInd;
//
//                occurrences[closestInd]++;
//            }
//
//
//            if ((correctedColors = verifyAndFix(colors, occurrences)) != null) {
//                cube.setColors(correctedColors);
//                return toSolverString(correctedColors);
//            }
//
//
//
            occurrences = new int[6];
////
//            Logger.logAndSend("fail");
            Logger.logAndSend(false, String.format("   (%.2f, %.2f, %.2f) closest static...    ",
                    modifiers[k][0],
                    modifiers[k][1],
                    modifiers[k][2]));

            for (int i = 0; i < 54; i++) {

                closestValue = 360;
                closestInd = 0;

                for (int j = 0; j < 6; j++) {
                    diff = Math.abs(hue[i] - Hue.values()[j].getHue());

                    if (diff < closestValue) {
                        closestValue = diff;
                        closestInd = j;
                    }
                }

                colors[i] = closestInd;

                occurrences[closestInd]++;

            }

            if ((correctedColors = verifyAndFix(colors, occurrences)) != null) {
                return correctedColors;
            }
            Logger.logAndSend("fail");
        }

        return null;

    }

    private int[] verifyAndFix(int[] color, int[] occurrences) {

        Search search = new Search();

        if(search.verify(toSolverString(color)) == 0) {
            Logger.logAndSend(true, "correct");
            return color;
        }


        int completeColors = 0;
        int over = -1;
        int overCount = 0;
        int under = -1;
        int underCount = 0;
        for(int i=0; i<6; i++) {
            if(occurrences[i] == 9) completeColors++;
            else if(occurrences[i] > 9) {
                over = i;
                overCount = occurrences[i];
            }
            else {
                under = i;
                underCount = occurrences[i];
            }
        }

        System.out.println("Complete colors: " + completeColors + ", over: " + overCount + ", under: " + underCount);
        System.out.println(Arrays.toString(color));

        if(completeColors == 4) {



            for(int j=0; j<54; j++) {
                if(color[j] == over) {
                    int[] colorCopy = Arrays.copyOf(color, color.length);
                    colorCopy[j] = under;
                    int result = search.verify(toSolverString(colorCopy));
                    System.out.println(j + " = " + result);
                    if(result == 0) {
                        Logger.logAndSend(true, "altered scan...   ");
                        System.out.println(Arrays.toString(colorCopy));
                        return colorCopy;
                    }
                }
            }
        }
        return null;
    }

    public static String toSolverString(int[] color) {
        StringBuffer s = new StringBuffer(54);

        for (int i = 0; i<54; i++)
            s.insert(i, '-');// default initialization

        int targetFacelet = 0;
        int targetFace = 0;

        for(int i=0; i<6; i++) {
            for(int j=0; j<9; j++) {


                switch (i) {
                    case 0: targetFace = 0;break;
                    case 1: targetFace = 1;break;
                    case 2: targetFace = 3;break;
                    case 3: targetFace = 4;break;
                    case 4: targetFace = 5;break;
                    case 5: targetFace = 2;break;
                }

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

//                switch (color[9 * i + j]) {
//                    case 0: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
//                    case 1: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
//                    case 2: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
//                    case 3: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
//                    case 4: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
//                    case 5: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
//                }

                for(int k=0; k<6; k++) {
                    if (color[9 * i + j] == color[9*k])
                        switch (9*k) {
                            case 0: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
                            case 9: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
                            case 9*2: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
                            case 9*3: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
                            case 9*4: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
                            case 9*5: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
                        }
                }

//                switch (color[9 * i + j]) {
//                    case 0: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
//                    case 1: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
//                    case 2: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
//                    case 3: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
//                    case 4: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
//                    case 5: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
//                }


            }
        }
        System.out.println(s.toString());
        return s.toString();
    }

    private float[] calculateHue(float[][] raw, float centerR, float centerG,  float centerB) {
        float hue[] = new float[raw.length];
        float maxSubMin;


        for(int i=0; i<raw.length; i++) {

            if(i % 9 == 0) {
                raw[i][0] *= centerR;
                raw[i][1] *= centerG;
                raw[i][2] *= centerB;
            }

            maxSubMin = Math.max(raw[i][0], Math.max(raw[i][1], raw[i][2])) - Math.min(raw[i][0], Math.min(raw[i][1], raw[i][2]));

            if (raw[i][0] > raw[i][1] && raw[i][0] > raw[i][2])
                hue[i] = (raw[i][1] - raw[i][2]) / maxSubMin;
            else if (raw[i][1] > raw[i][0] && raw[i][1] > raw[i][2])
                hue[i] = 2 + (raw[i][2] - raw[i][0]) / maxSubMin;
            else
                hue[i] = 4 + (raw[i][0] - raw[i][1]) / maxSubMin;

            hue[i] = hue[i] * 60;
        }
        return hue;
    }

    private float[] calculateHue(float[][] raw) {
        return calculateHue(raw, 1f, 1f, 1f);
    }

}
