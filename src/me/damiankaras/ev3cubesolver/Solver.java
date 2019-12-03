package me.damiankaras.ev3cubesolver;

import cs.min2phase.Search;

public class Solver {

    private final static short R = 0;
    private final static short G = 1;
    private final static short B = 2;

    private final static int METHOD_CLOSEST_STATIC = 10;
    private final static int METHOD_CLOSEST_CENTER = 11;

    enum Hue {
        white(85.548f), orange(-15.0f), yellow(67.052f), red(8.802f), green(149.734f), blue(195.928f);


        private float hue;

        Hue(float hue) {this.hue = hue;}
        public float getHue() {
            return hue;
        }
    }

    Solver() {

    }

    String generateCube(float[] hue, int method) {


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

//        System.out.print("\n");



        //solving order


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

    String solve(float[] hue) {
        Search search = new Search();

        int mask = 0;
        mask |= false ? Search.USE_SEPARATOR : 0;
        mask |= false ? Search.INVERSE_SOLUTION : 0;
        mask |= true ? Search.APPEND_LENGTH : 0;


//        String cube = generateCube(raw, METHOD_CLOSEST_CENTER);

        Logger.logAndSend("Trying closest static hue method");

        String out = search.solution(generateCube(hue, METHOD_CLOSEST_STATIC), 21, 100, 0, mask);

        if(out.contains("Error")) {
            Logger.logAndSend("Closest static failed. Trying closest center hue method");
            out = search.solution(generateCube(hue, METHOD_CLOSEST_CENTER), 21, 100, 0, mask);
        }

        Logger.logAndSend(out);

        return out;
    }

}
