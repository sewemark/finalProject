package com.seweryn.schess.Adapters;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.seweryn.schess.Activities.ChooseMapActivity;
import com.seweryn.schess.Adapters.BoardAdapter;
import com.seweryn.schess.Controllers.BoardLogicController;
import com.seweryn.schess.Enums.PieceType;
import com.seweryn.schess.Enums.PuzzleType;
import com.seweryn.schess.Logic.MoveLogic;
import com.seweryn.schess.Models.DatabaseObject;
import com.seweryn.schess.Models.Move;
import com.seweryn.schess.Models.Solution;
import com.seweryn.schess.Models.Vector;
import com.seweryn.schess.R;
import com.seweryn.schess.Static.Lodash;

import java.lang.*;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;


public final class GameBoardAdapter extends BoardAdapter {


     MoveLogic logic = new MoveLogic();
     BoardLogicController boargLogic;
     List<Solution> solutions;
     Deque<Move> moves = new ArrayDeque<Move>();
     public GameBoardAdapter(Context context,DatabaseObject databaseObject, String boardName,   PuzzleType puzleType) {
         super(new BoardLogicController(databaseObject.getBoard()),context,databaseObject.getBoard()[0].length, databaseObject.getBoard().length);
         this.board = Lodash.deepCopyIntMatrix(databaseObject.getBoard());
         this.solutions = databaseObject.getSolutions();
         Collections.reverse(this.solutions.get(0).boards);
         this.boargLogic = new BoardLogicController(board);

     }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;
        int resource=0;
        if (v == null) {
            v = boardLayoutInflater.inflate(R.layout.grid_item_blue, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
        }
        v.setOnDragListener(new GameBoardDragListener());
        Vector position = Vector.convertToVector(width, height, i);
        int tabValue = board[position.getX()][position.getY()];
        //int tempValue = board01[1][2];
        if(tabValue>0){
            resource = Lodash.getResource(tabValue);
            v.findViewById(R.id.grid_item_piece).setBackgroundResource(resource);
            v.findViewById(R.id.grid_item_piece).setTag(tabValue);
            v.findViewById(R.id.grid_item_piece).setOnTouchListener(new MyTouchListener());
        }
        picture = (ImageView) v.getTag(R.id.picture);
        Item item = getItem(i);
        if (item.name.equals("WhiteField")) {
            picture.setImageResource(R.drawable.shape_white);
        } else {
            picture.setImageResource(R.drawable.shape);
        }
        //name.setText(item.name);
        return v;
    }

     public void setHintsBackground(GridView gridView,int position, boolean onOrOff, PieceType pieceType){

         Integer[] possiblePositions = logic.PossibleMoves(this.width,this.height,Vector.convertToVector(this.width, this.height, position),pieceType);
         for(int i =0;i<possiblePositions.length;i++){
             FrameLayout item2 = (FrameLayout)gridView.getChildAt(possiblePositions[i]);
             if(onOrOff ==true ) {
                        View view = item2.findViewById(R.id.hint_image);
                        if(view!=null){
                            view.setBackgroundResource(Lodash.getResource(pieceType));
                        }
             }
             else{
                 View view = item2.findViewById(R.id.hint_image);
                 if(view != null){
                     view.setBackgroundResource(0);
                 }
             }
         }
     }
    public void undoMove(GridView gridView){
        if(this.moves.size()>0){
            Move lastMove = moves.pop();
            FrameLayout itemSource = (FrameLayout)gridView.getChildAt(lastMove.sourPositon);
            lastMove.view.setBackgroundResource(Lodash.getResource(lastMove.beatedPieceType));
            lastMove.view.setTag(lastMove.beatedPieceType);
            itemSource.addView(lastMove.view);

            FrameLayout itemdesination = (FrameLayout)gridView.getChildAt(lastMove.destinatioPosition);
            View view2 = itemdesination.findViewById(R.id.grid_item_piece);
            if(view2!=null){
                int  resource = Lodash.getResource(lastMove.pieceType);
                view2.setBackgroundResource(resource);
                view2.setTag(lastMove.pieceType);
                //view2.findViewById(R.id.grid_item_piece).setOnTouchListener(new MyTouchListener());
            }
            boargLogic.setPieceAtPosition(lastMove.sourPositon, lastMove.beatedPieceType);
            boargLogic.setPieceAtPosition(lastMove.destinatioPosition, lastMove.pieceType);


        }
    }
    public void showNextMove(GridView gridView){
        for(int i=0;i<this.solutions.size();i++){

            for(int j=0;j<this.solutions.get(i).boards.size();j++){
                if(Lodash.areBoardsEqual(this.solutions.get(i).boards.get(j),boargLogic.getBoard())) {
                    if (j+ 1 < this.solutions.get(i).boards.size()) {
                        Move m = Move.extractMove(boargLogic.getBoard(), this.solutions.get(i).boards.get(j + 1));
                        FrameLayout itemSource = (FrameLayout) gridView.getChildAt(m.sourPositon);
                        View viewToDelete = itemSource.getChildAt(2);
                        itemSource.removeViewAt(2);

                        if(m.destinatioPosition>=0) {
                            FrameLayout itemdesination = (FrameLayout) gridView.getChildAt(m.destinatioPosition);
                            View view2 = itemdesination.findViewById(R.id.grid_item_piece);
                            if (view2 != null) {
                                int resource = Lodash.getResource(m.pieceType);
                                view2.setBackgroundResource(resource);
                                view2.setTag(m.pieceType);
                                //view2.findViewById(R.id.grid_item_piece).setOnTouchListener(new MyTouchListener());
                            }
                            boargLogic.setPieceAtPosition(m.destinatioPosition, m.pieceType);
                        }
                        boargLogic.setPieceAtPosition(m.sourPositon, 0);
                        moves.push(new Move(viewToDelete,m.beatedPieceType, m.pieceType,m.sourPositon, m.destinatioPosition));

                        System.out.println(2);
                        return;
                     //   if(boargLogic.checkIfWin()){
                       //     showWinDialog(gridView);
                       // }
                    }
                }
            }
        }
        this.undoMove(gridView);
    }
    public void showWinDialog(View v){
        View layout = boardLayoutInflater.inflate(R.layout.win_popup,(ViewGroup)v.findViewById(R.id.popup));
        PopupWindow pwindo = new PopupWindow(layout, 300, 370, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        Intent mainMenu = new Intent(context, ChooseMapActivity.class);
        context.startActivity(mainMenu);
    }
     private final class MyTouchListener implements View.OnTouchListener {

         public boolean onTouch(View view, MotionEvent motionEvent) {
             if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                 ClipData data = ClipData.newPlainText("", "");
                 View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                 view.startDrag(data, shadowBuilder, view, 0);
                 view.setVisibility(View.INVISIBLE);
                 ViewGroup owner = (ViewGroup) view.getParent();
                 GridView owner2 = (GridView)owner.getParent();
                 setHintsBackground(owner2, owner2.getPositionForView(view), true, Lodash.getPiecType((int) view.getTag()));
                 return true;
             } else {
                 return false;
             }
         }
     }

     class GameBoardDragListener implements View.OnDragListener {
         Drawable enterShape = context.getResources().getDrawable(R.drawable.shape_droptarget);
         Drawable normalShape = context.getResources().getDrawable(R.drawable.shape);

         @Override
         public boolean onDrag(View v, DragEvent event) {
             int action = event.getAction();
             switch (event.getAction()) {
                 case DragEvent.ACTION_DRAG_STARTED:
                     // do nothing
                     break;
                 case DragEvent.ACTION_DRAG_ENTERED:
                     v.setBackgroundDrawable(enterShape);
                     break;
                 case DragEvent.ACTION_DRAG_EXITED:
                     v.setBackgroundDrawable(normalShape);
                     break;
                 case DragEvent.ACTION_DROP:
                     // Dropped, reassign View to ViewGroup
                     View view = (View) event.getLocalState();
                     ViewGroup owner = (ViewGroup) view.getParent();
                     GridView owner2 = (GridView)owner.getParent();
                     int position = owner2.getPositionForView(view);
                     owner.removeView(view);
                     PieceType pieceType =  Lodash.getPiecType((int) view.getTag());
                     Integer[] possiblePositions = logic.PossibleMoves(width, height, Vector.convertToVector(width, height, position),pieceType );
                     setHintsBackground((GridView) owner.getParent(), position, false, Lodash.getPiecType((int) view.getTag()));
                     FrameLayout container = (FrameLayout) v;
                     int destinationPosition =  owner2.getPositionForView(container);
                     if(!Lodash.HasElement(possiblePositions,destinationPosition) ){
                         owner.addView(view);
                     }
                     else {
                         for(int i = 0; i < possiblePositions.length; i++) {
                             if (possiblePositions[i] == destinationPosition) {
                                 int tempValue = boargLogic.getPieceAtPosition(destinationPosition);
                                 if (boargLogic.removePiece(position, destinationPosition, (int) view.getTag())) {
                                     if (container.getChildCount() > 2) {
                                         View viewToDelete = container.getChildAt(2);
                                         container.removeViewAt(2);
                                         moves.push(new Move(viewToDelete, tempValue, boargLogic.getPieceAtPosition(destinationPosition), position, destinationPosition));

                                         //  container.findViewById(R.id.grid_item_piece).setBackgroundResource(0);
                                       //  container.findViewById(R.id.grid_item_piece).setTag(-1);
                                     }
                                     container.addView(view);
                                 } else {
                                     owner.addView(view);
                                 }
                             }
                         }
                     }
                     view.setVisibility(View.VISIBLE);
                     if(controller.checkIfWinPosition()){
                         showWinDialog(v);
                     }
                     break;
                 case DragEvent.ACTION_DRAG_ENDED:
                     v.setBackgroundDrawable(normalShape);
                 default:
                     break;
             }
             return true;
         }


     }
 }