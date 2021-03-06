package com.seweryn.schess.SearchAlgoritm;

import com.seweryn.schess.Enums.PuzzleType;
import com.seweryn.schess.IPuzzleTypeCalsificator;

/**
 * Created by sew on 2016-01-24.
 */
public class PuzzleTypeCalsificator implements IPuzzleTypeCalsificator {
    /**
     * method that estimates puzzzle hardness
     * @param  int ... params puzzle hardness factors
     * @return  PuzzleType type of puzzle
     */
    public PuzzleType clasify(int ...params){

        double wage = params[0] * 0.3 + params[1] * 0.4 + params[2] * 0.3;
        PuzzleType type = PuzzleType.EASY;
        if (wage <= 10) {
            type = PuzzleType.EASY;
        } else if (wage > 10 && wage <= 14) {
            type = PuzzleType.MEDIUM;
        } else if (wage < 16 && wage > 14) {
            type = PuzzleType.HARD;
        } else {
            type = PuzzleType.VERYHARD;
        }
        return  type;
    }
}
