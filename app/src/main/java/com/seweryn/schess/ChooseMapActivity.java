package com.seweryn.schess;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ChooseMapActivity extends Activity {
    DatabaseHandler dbContext;
    public ChooseMapActivity(){

    }
    private void helperFunction(String[] tab,String  temp){
        for(int i =0; i<tab.length;i++){
            tab[i] +=temp;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceeState) {
        super.onCreate(savedInstanceeState);
        setContentView(R.layout.choose_map);
        dbContext =new DatabaseHandler(ChooseMapActivity.this);
        GridView easyGridView = (GridView)findViewById(R.id.chooseMapGridView);
        String[] easyPuzzles = dbContext.getPuzzleListByType(PuzzleType.EASY);
        //this.helperFunction(easyPuzzles,"E");
        String[] mediumPuzzles = dbContext.getPuzzleListByType(PuzzleType.MEDIUM);
        //this.helperFunction(mediumPuzzles, "M");
        ArrayList<String> lista = new ArrayList<String>();
        lista.addAll(0,Arrays.asList(easyPuzzles));
        lista.addAll(Arrays.asList(mediumPuzzles));
       // String[] easyPuzzles = new String[]{"bla","bla","bla"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,lista );
        easyGridView.setAdapter(adapter);
        easyGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent chooseBoard = new Intent(ChooseMapActivity.this, GameActivity.class);

                chooseBoard.putExtra("boardName", ((TextView) v).getText());
                chooseBoard.putExtra("boardType", PuzzleType.EASY);
                ChooseMapActivity.this.startActivity(chooseBoard);
                startActivity(chooseBoard);
                Toast.makeText(getApplicationContext(),
                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}