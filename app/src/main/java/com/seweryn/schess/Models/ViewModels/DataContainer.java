package com.seweryn.schess.Models.ViewModels;

/**
 * Created by sew on 2016-01-19.
 */
public class DataContainer{
    public DataContainer(String _boardName, boolean _wasSolved, boolean _wasHintsUsed){
        this.boardname = _boardName;
        this.wasSolved =_wasSolved;
        this.hintsUsed = _wasHintsUsed;
    }
    public String boardname;
    public boolean wasSolved;
    public boolean hintsUsed;
}
