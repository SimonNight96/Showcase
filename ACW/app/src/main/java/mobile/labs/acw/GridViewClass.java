package mobile.labs.acw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class GridViewClass extends AppCompatActivity {

    private int m_ClickedtilePos = -1;
    private Context context = this;
    private ArrayList<Bitmap> m_PictureArray = new ArrayList<Bitmap>();
    private int m_NumOfTilesSelected = 0;
    private int m_FirstTileLocation = -1;
    private int m_SecondTileLocation = -1;
    private Bitmap m_FrontPic;
    private ImageView PreviousIMGV;
    private ImageView CurrentIMGV;
    private ArrayList<Integer> m_LockedPositions = new ArrayList<Integer>();
    private String m_StoredPositions = "";
    private int m_Highscore;
    GridView gridView;
    ArrayList<Integer> Layout = new ArrayList<>();
    private boolean FirstTimeRun = true;

    private int m_totalMatchedTiles = 0;
    TextView timerTextView;
    TextView currentHSScore;
    String m_puzzleID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        m_FrontPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.frontpicture);
        gridView = (GridView) findViewById(R.id.gridView);
        startTime = System.currentTimeMillis();

        timerTextView = (TextView)findViewById(R.id.timertxt);
        currentHSScore = (TextView)findViewById(R.id.HighScoreTxt);

        timerHandler.postDelayed(timerRunnable, 0);
        //starts timer going

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                boolean lockedPositionClicked = true;

                for (int i = 0; i < m_LockedPositions.size(); i++) { //checks all positions that are already matched and locked to see if current click is in one of those positions

                    if (position == m_LockedPositions.get(i)) {
                        lockedPositionClicked = false; // if so change boolean value
                    }

                }

                if (lockedPositionClicked) {
                    if (m_ClickedtilePos != position) { //checks if tile has been clicked again in a row

                        CurrentIMGV = ((RestartGridViewAdapter.ViewHolder) v.getTag()).image;
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedtile);
                        CurrentIMGV.setImageBitmap(bitmap);
                        //gets imageview from viewholder and sets current bitmap to selected card bitmap

                        m_ClickedtilePos = position; //stores position to check for reset clickk
                        m_NumOfTilesSelected++; //increments number of tiles clicked

                        if (m_NumOfTilesSelected == 1) { //check if first tile clicked

                            m_FirstTileLocation = position; //if first tile clicked stores position and image view
                            PreviousIMGV = CurrentIMGV;

                        } else {

                            m_SecondTileLocation = position; //if second stores position

                        }


                        if (m_NumOfTilesSelected == 2) { //if second tile clicked

                            if (Layout.get(m_FirstTileLocation) == Layout.get(m_SecondTileLocation)) { //checks if tiles clicked are a match

                                m_LockedPositions.add(m_FirstTileLocation); //store position in list of positions that are now locked
                                m_LockedPositions.add(m_SecondTileLocation);
                                m_StoredPositions += m_FirstTileLocation + " " + m_SecondTileLocation + " "; //store positions to be used in adapter to ensure pictures are displayed when rotated
                                m_totalMatchedTiles = m_totalMatchedTiles + 2; //increases count of matched cards

                                flipCorrect(m_FirstTileLocation, m_SecondTileLocation, PreviousIMGV, CurrentIMGV); //calls to flip cards

                                if(m_totalMatchedTiles == Layout.size()){ //checks if all cards matched

                                    timerHandler.removeCallbacks(timerRunnable); //stops timer
                                    long millis = System.currentTimeMillis() - startTime; //find difference between current time and time puzzle started
                                    int Score = (int) (millis / 1000); //converts it to seconds
                                    Score = 240 - Score; //minues time from 4 minutes

                                    TextView currentScoretxt = (TextView) findViewById(R.id.currentScoreTxt);

                                    currentScoretxt.setText(getString(R.string.currentScore) + " " + Score); //displays current score

                                    if(Score > m_Highscore){ //checks if higher than stored highscore

                                        DatabaseOp DB = new DatabaseOp(context);

                                        DB.updateHighScore(DB, Score, m_puzzleID); //edits highscore in database

                                        DB.close();

                                        currentHSScore.setText(getString(R.string.Highscore) + " " + Score);

                                    }

                                }

                                Reset(); //resets all counters

                            } else { //if cards dont matchg


                                int temp = Layout.get(m_FirstTileLocation);
                                PreviousIMGV.setImageBitmap(m_PictureArray.get(temp - 1));

                                temp = Layout.get(position);
                                CurrentIMGV.setImageBitmap(m_PictureArray.get(temp - 1)); //sets image views to display clicked pictures


                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        PreviousIMGV.setImageBitmap(m_FrontPic); //then wait half a second before changing image views back to back of card
                                        CurrentIMGV.setImageBitmap(m_FrontPic);

                                        Reset();

                                    }
                                }, 500);


                            }

                        }

                    } else { //if resetting clicked tile
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frontpicture);
                        CurrentIMGV.setImageBitmap(bitmap); //change card image back to back of card
                        m_ClickedtilePos = -1; //resets stored last tile clicked position
                        m_NumOfTilesSelected--; //minus from number of tile clicked
                    }
                }
            }

        });

    }

    Handler timerHandler = new Handler();
    long startTime = 0;
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime; //takes base time and figures out how far away it is from current time in miliseconds
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            //http://stackoverflow.com/questions/4597690/android-timer-how

            timerHandler.postDelayed(this, 1000); //post to the main thred every second to repeat every second
        }
    };

    public void flipCorrect(final int firstPos, final int secondPos, final ImageView PrevIMGV, final ImageView CurIMGV){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int temp = Layout.get(firstPos);
                PrevIMGV.setImageBitmap(m_PictureArray.get(temp - 1));

                temp = Layout.get(secondPos);
                CurIMGV.setImageBitmap(m_PictureArray.get(temp - 1));
                //flips imageviews to correct pictures

            }
        }, 500);

    }

    @Override
     public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String temp = sharedPreferences.getString("ClickedTiles", "DEFAULT"); //gets stored clicked tiles

        if(temp != "DEFAULT") {

            m_StoredPositions = temp + m_StoredPositions; // adds new clicked tiles to stored ones

        }

        String LockedPosString = "DEFAULT";

        if(!m_LockedPositions.isEmpty()) {

            if(LockedPosString == "DEFAULT") {
                LockedPosString = "";
            }

            for (int i = 0; i < m_LockedPositions.size(); i++) {

                LockedPosString += String.valueOf(m_LockedPositions.get(i)) + " "; //stores all locked positions in one string

            }
        }

        editor.putString("ClickedTiles", m_StoredPositions);
        editor.putString("LockedPositions", LockedPosString);

        editor.putInt("NumOfSelectedTiles", m_totalMatchedTiles);
        editor.putLong("StartTime", startTime);
        editor.putBoolean("FirstRun", FirstTimeRun);
        editor.commit();

        //stores all realvent data in shared preferences
    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int NumOfColoums = bundle.getInt("NumOfColumns");
        Layout = bundle.getIntegerArrayList("Layout");
        final ArrayList<String> pictureset = bundle.getStringArrayList("PicturesNames");
        currentHSScore.setText(getString(R.string.Highscore) + " "+ String.valueOf(bundle.getInt("HighScore")));
        m_Highscore = bundle.getInt("HighScore");
        m_puzzleID = bundle.getString("PuzzleID", "ERROR");

        //gets bunlde extras to use with customer adapter

        downloadImages pictures = new downloadImages();
        pictures.execute(pictureset);

        try {
            m_PictureArray = pictures.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //get bitmaps and store in arraylist

        String temp = sharedPreferences.getString("ClickedTiles", "DEFAULT");

        if(temp == "")
        {
            temp = "DEFAULT";
        }

        m_totalMatchedTiles = sharedPreferences.getInt("NumOfSelectedTiles", 0);
        FirstTimeRun = sharedPreferences.getBoolean("FirstRun", true);

        String LockedPosTemp;

        LockedPosTemp = sharedPreferences.getString("LockedPositions", "DEFAULT");

        if(!LockedPosTemp.equals("DEFAULT")){

            String[] spilt = LockedPosTemp.split("\\s+");

            for(int i =0; i < spilt.length; i++){

                m_LockedPositions.add(Integer.parseInt(spilt[i]));

            }
        }


    if(!FirstTimeRun) {

         startTime = sharedPreferences.getLong("StartTime", 0);
         timerHandler.postDelayed(timerRunnable, 0);
        //checks if running for the second time (rotating) in which case it needs to get store's start time or it would use 0 which produces huge time

    }else{

         FirstTimeRun = false;

        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(NumOfColoums);
        RestartGridViewAdapter gridViewAdapter = new RestartGridViewAdapter(context, m_PictureArray,temp, Layout);
        gridView.setAdapter(gridViewAdapter);
        //sets grid view with a custom adapter

    }


    private void Reset(){

        PreviousIMGV = null;
        CurrentIMGV = null;

        m_ClickedtilePos = -1;
        m_FirstTileLocation = -1;
        m_SecondTileLocation = -1;
        m_NumOfTilesSelected = 0;
        //resets all counters


    }

    private class downloadImages extends AsyncTask<ArrayList<String>, String , ArrayList<Bitmap>> {

        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... args){
            ArrayList<Bitmap> PictureSet = new ArrayList<Bitmap>();
            for(int i = 0; i < args[0].size(); i++) {
                Bitmap bitmap = null;
                try {
                    //try to read from internal storage
                    FileInputStream reader = getApplicationContext().openFileInput(args[0].get(i));
                    bitmap = BitmapFactory.decodeStream(reader);
                    PictureSet.add(bitmap);

                } catch (FileNotFoundException fileNotFound) {

                  Log.i("GridViewClass", "Files not found!");

                }
            }
            return PictureSet;
        }

        protected void onPostExecute(ArrayList<Bitmap> picSet){


        }
    }

}
