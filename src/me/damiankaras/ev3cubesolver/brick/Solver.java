package me.damiankaras.ev3cubesolver.brick;


import cs.min2phase.Search;

public class Solver {

    private final static short R = 0;
    private final static short G = 1;
    private final static short B = 2;

    private final static int METHOD_CLOSEST_STATIC = 10;
    private final static int METHOD_CLOSEST_CENTER = 11;

    enum Hue {
        white(85.677f), orange(-24.532f), yellow(67.052f), red(8.828f), green(150.763f), blue(196.964f);


        private float hue;

        Hue(float hue) {this.hue = hue;}
        public float getHue() {
            return hue;
        }
    }

    public Solver() {
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

    String solve(float[][] raw) {
        Logger.logAndSend("Solving");
        Search search = new Search();


        int mask = 0;
        mask |= false ? Search.USE_SEPARATOR : 0;
        mask |= false ? Search.INVERSE_SOLUTION : 0;
        mask |= true ? Search.APPEND_LENGTH : 0;

        float[] hueRaw = calculateHue(raw);


        String out;

        Logger.logAndSend(false, "   Raw hue, closest static...   ");
        out = search.solution(generateCube(hueRaw, METHOD_CLOSEST_STATIC), 21, 100, 0, mask);
        if(!out.contains("Error")) {
            Logger.logAndSend("success");
            return out;
        }
        Logger.logAndSend("fail");
        Logger.logAndSend(false, "   Raw hue, closest center...   ");
        out = search.solution(generateCube(hueRaw, METHOD_CLOSEST_CENTER), 21, 100, 0, mask);
        if(!out.contains("Error")) {
            Logger.logAndSend("success");
            return out;
        }
        Logger.logAndSend("fail");

        float[] hueCorrected = calculateHue(raw, 0.95f, 1, 1);

        Logger.logAndSend(false, "   Corrected hue, closest static...   ");
        out = search.solution(generateCube(hueCorrected, METHOD_CLOSEST_STATIC), 21, 100, 0, mask);
        if(!out.contains("Error")) {
            Logger.logAndSend("success");
            return out;
        }
        Logger.logAndSend("fail");
        Logger.logAndSend(false, "   Corrected hue, closest center...   ");
        out = search.solution(generateCube(hueCorrected, METHOD_CLOSEST_CENTER), 21, 100, 0, mask);
        if(!out.contains("Error")) {
            Logger.logAndSend("success");
            return out;
        }
        Logger.logAndSend("fail");
        return null;
    }

}
