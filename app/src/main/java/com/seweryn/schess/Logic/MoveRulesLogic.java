package com.seweryn.schess.Logic;

import com.seweryn.schess.Enums.PieceType;
import com.seweryn.schess.Logic.MovesStrategy.IMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.BishopMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.KingMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.KnightMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.PawnMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.QueenMoveStrategy;
import com.seweryn.schess.Logic.MovesStrategy.RockMoveStrategy;

/**
 * Created by sew on 2015-11-08.
 */

public class MoveRulesLogic implements IMoveRulesLogic {
    public MoveRulesLogic(){

    }
    /**
          * method that returns proper move strategy for a piece
          * @param  int width of the board
          * @param  int height of the board
          * @param  PieceType piece type
          * @return  returns the move strategy class for a particular piece type
     */
    public IMoveStrategy getMoveStrategy(int width, int height, PieceType pieceType){
       switch(pieceType){
           case KING:
               return new KingMoveStrategy(width,height);
           case ROOK:
               return  new RockMoveStrategy(width,height);
           case PAWN:
               return  new PawnMoveStrategy(width,height);
           case BISHOP:
               return new BishopMoveStrategy(width,height);
           case QUEEN:
               return new QueenMoveStrategy(width,height);
           case KNIGHT:
               return new KnightMoveStrategy(width,height);
           default:
               throw new AssertionError();
       }
    }

}
