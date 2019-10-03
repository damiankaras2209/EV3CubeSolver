package me.damiankaras.ev3cubesolver;

import cs.min2phase.Search;

public class Solver {

    private final static short R = 0;
    private final static short G = 1;
    private final static short B = 2;

    private final static int METHOD_CLOSEST_STATIC = 10;
    private final static int METHOD_CLOSEST_CENTER = 11;

//    private final static float HUE_WHITE = 68.646f;
//    private final static float HUE_RED = 8.802f;
//    private final static float HUE_YELLOW = 85.548f;
//    private final static float HUE_ORANGE = -17.379f;
//    private final static float HUE_GREEN = 149.734f;
//    private final static float HUE_BLUE = 195.928f;

    enum Hue {
        white(85.548f), orange(-17.79f), yellow(67.052f), red(8.802f), green(149.734f), blue(195.928f);


        private float hue;

        Hue(float hue) {this.hue = hue;}
        public float getHue() {
            return hue;
        }
    }

    Solver() {

    }

    String generateCube(float[][] raw, int method) {

        float[] hue = new float[54];

        float maxSubMin;

//        System.out.println("Hues:");

        for(int i=0; i<54; i++) {

            maxSubMin = Math.max(raw[i][R], Math.max(raw[i][G], raw[i][B])) - Math.min(raw[i][R], Math.min(raw[i][G], raw[i][B]));

            if(raw[i][R] > raw[i][G] && raw[i][R] > raw[i][B])
                hue[i] = (raw[i][G] - raw[i][B]) / maxSubMin;
            else if(raw[i][G] > raw[i][R] && raw[i][G] > raw[i][B])
                hue[i] = 2 + (raw[i][B] - raw[i][R]) / maxSubMin;
            else
                hue[i] = 4 + (raw[i][R] - raw[i][G]) / maxSubMin;

            hue[i] *= 60;

//            System.out.println(hue[i]);
        }

//        System.out.println("closest Centers: ");

        int[] color = new int[54];

        float closestValue;
        int closestInd;

        float diff;

        switch(method) {
            case METHOD_CLOSEST_CENTER:

                for(int i=0; i<54; i++) {

                    closestValue = 360;
                    closestInd = 0;

                    for(int j=0; j<6; j++) {
                        diff = Math.abs(hue[i] - hue[j * 9]);

                        if(diff < closestValue) {
                            closestValue = diff;
                            closestInd = j;
                        }
                    }

                    color[i] = closestInd;

//            System.out.print(color[i]);

                }

                break;
            case METHOD_CLOSEST_STATIC:

                for(int i=0; i<54; i++) {

                    closestValue = 360;
                    closestInd = 0;

                    for(int j=0; j<6; j++) {
                        diff = Math.abs(hue[i] - Hue.values()[j].getHue());

                        if(diff < closestValue) {
                            closestValue = diff;
                            closestInd = j;
                        }
                    }

                    color[i] = closestInd;

//            System.out.print(color[i]);

                }

                break;
        }

        for(int i=0; i<54; i++) {

            closestValue = 360;
            closestInd = 0;

            for(int j=0; j<6; j++) {
                diff = Math.abs(hue[i] - hue[j * 9]);

                if(diff < closestValue) {
                    closestValue = diff;
                    closestInd = j;
                }
            }

            color[i] = closestInd;

//            System.out.print(color[i]);

        }

//        System.out.print("\n");


        StringBuffer s = new StringBuffer(54);

        for (int i = 0; i<54; i++)
            s.insert(i, '-');// default initialization

        int targetFacelet = 0;
        int targetFace = 0;

        for(int i=0; i<6; i++) {
            for(int j=0; j<9; j++) {

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
                switch (color[9 * i + j]) {
                    case 0: s.setCharAt(9 * targetFace + targetFacelet, 'U');break;
                    case 1: s.setCharAt(9 * targetFace + targetFacelet, 'R');break;
                    case 2: s.setCharAt(9 * targetFace + targetFacelet, 'D');break;
                    case 3: s.setCharAt(9 * targetFace + targetFacelet, 'L');break;
                    case 4: s.setCharAt(9 * targetFace + targetFacelet, 'B');break;
                    case 5: s.setCharAt(9 * targetFace + targetFacelet, 'F');break;
                }


            }
        }

        return s.toString();
    }

    void solve(float[][] raw) {
        Search search = new Search();

        int mask = 0;
        mask |= false ? Search.USE_SEPARATOR : 0;
        mask |= false ? Search.INVERSE_SOLUTION : 0;
        mask |= true ? Search.APPEND_LENGTH : 0;


//        String cube = generateCube(raw, METHOD_CLOSEST_CENTER);

        String out = search.solution(generateCube(raw, METHOD_CLOSEST_STATIC), 21, 100, 0, mask);

        if(out.contains("Error")) {
            System.out.println("Closest static failed. Trying closest center");
            out = search.solution(generateCube(raw, METHOD_CLOSEST_CENTER), 21, 100, 0, mask);
        }

        System.out.println(out);


    }

}
