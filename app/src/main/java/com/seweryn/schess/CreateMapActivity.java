package com.seweryn.schess;

/**
 * Created by sew on 2015-11-20.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateMapActivity extends Activity {
    private  LayoutInflater boardLayoutInflater;
    private PopupWindow pwindo;
    Map<Integer,PieceType> map =  new HashMap<Integer,PieceType>();
    public  CreateMapActivity(){
        map.put(1,PieceType.KING);
        map.put(2,PieceType.TOWER);
        map.put(3,PieceType.PAWN);
        map.put(4,PieceType.BISHOP);
        map.put(5,PieceType.HORSE);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_map_main);

        final GridView gridView = (GridView) findViewById(R.id.createMapGridView);

        gridView.setNumColumns(4);
        final CreateMapAdapter gridViewAdapter = new CreateMapAdapter(this,4);
        gridView.setAdapter(gridViewAdapter);
        try {

            String FILENAME = "board04.data";
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream iis = new ObjectInputStream(fis);
            int[][] board = (int[][])iis.readObject();
            gridViewAdapter.setBoardToCreate(board);
            gridViewAdapter.notifyDataSetChanged();
            iis.close();
            fis.close();
        }
        catch (Exception e){

        }
        boardLayoutInflater = LayoutInflater.from(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                    try {
                        View layout = boardLayoutInflater.inflate(R.layout.add_piece_popup,(ViewGroup)v.findViewById(R.id.popup));
                        pwindo = new PopupWindow(layout, 300, 370, true);
                        ListView popupListView = (ListView)layout.findViewById(R.id.addPieceListView);
                        CreateMapPopupListViewAdapter listViewAdapter = new CreateMapPopupListViewAdapter(CreateMapActivity.this,PieceType.values());
                        popupListView.setAdapter(listViewAdapter);
                        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                                gridViewAdapter.setPieceOnPosition(position, i);
                                gridViewAdapter.notifyDataSetChanged();
                                //  Toast.makeText(CreateMapActivity.this, "myPos " + i, Toast.LENGTH_LONG).show();
                            }
                        });
                         Button btnClosePopup = (Button) layout.findViewById(R.id.popupCloseButton);
                        // btnClosePopup.setOnClickListener(cancel_button_click_listener);
                        btnClosePopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pwindo.dismiss();

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        Button saveButton = (Button) findViewById(R.id.saveButton);
        // btnClosePopup.setOnClickListener(cancel_button_click_listener);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILENAME = "board04.data";
                String string = "hello world!";
                try {

                    //File mydir = getDir("mydir", Context.MODE_PRIVATE); //Creating an internal dir;
                    //File fileWithinMyDir = new File(mydir, "myfile");+ //Getting a file within the dir.
                    //FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
                   FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    int[][] board = gridViewAdapter.getCreateBoard();
                    oos.writeObject(gridViewAdapter.getCreateBoard());
                    oos.close();
                    fos.close();
                     for(int i =0;i < fileList().length;i++) {
                       System.out.println(fileList()[i]);
                      System.out.println(getFilesDir());
                    }
                }
                catch(Exception e){
                }
            }
        });

    }
}
