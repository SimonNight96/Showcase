package mobile.labs.acw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static PuzzleItem m_CurrentPuzzle = new PuzzleItem();
    private String m_CurrentlySelectedPuzzle;
    private ArrayList<String> DownloaedPuzzleID = new ArrayList<String>();
    private ArrayList<String> PictureNamesArray = new ArrayList<String>();
    private ArrayList<String> AvalibePuzzleID = new ArrayList<>();
    private ArrayList<String> downloadedPuzzleSpinnerInfo = new ArrayList<>();
    private ArrayList<Integer> filterSpinnerColumns = new ArrayList<>();
    private ArrayList<String> filteredLIstViewArray = new ArrayList<>();
    private int REQUEST_CODE = 1;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button chooseButton = (Button) findViewById(R.id.ChoosePuzzleBtn);
        final Button downloadBtn = (Button) findViewById(R.id.DownloadBtn);
        final Button filterBtn = (Button) findViewById(R.id.FilterBtn);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner downloaedSpinner = (Spinner) findViewById(R.id.DownloadedSpinner);

        DatabaseOp DB = new DatabaseOp(context);

        Cursor CR = DB.getAllRecords(DB);
        CR.moveToFirst();
        if(CR.getCount() > 0) {
            do {

                String Record = "" + CR.getString(0) + ", " + getString(R.string.Highscore) + CR.getInt(2);;
                DownloaedPuzzleID.add(CR.getString(0));
                downloadedPuzzleSpinnerInfo.add(Record);

                //returns all records from database and populates list of downloaded puzzles and list of downloaded ID's

            } while (CR.moveToNext());
        }

        CR.close();
        DB.close();

        fillFilterSpinner();

        new downloadJSONSpinner().execute("http://www.hull.ac.uk/php/349628/08027/acw/index.json");
        //Gets puzzle indexs from the server

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, downloadedPuzzleSpinnerInfo);
        downloaedSpinner.setAdapter(adapter);
        //populates list with downloaded puzzle data

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                m_CurrentlySelectedPuzzle = spinner.getSelectedItem().toString();
                //sets memeber variable to store currently selected puzzle index

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) { // checks if phone is connected to the internet

                    String URL = "http://www.hull.ac.uk/php/349628/08027/acw/puzzles/" + m_CurrentlySelectedPuzzle + ".json";
                    downloadPuzzleItem puzzle = new downloadPuzzleItem();
                    //returns puzzle item containing the puzzle details (number of columns, layout and pictureset)

                    puzzle.execute(URL);

                    try {
                        m_CurrentPuzzle = puzzle.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    URL = "http://www.hull.ac.uk/php/349628/08027/acw/picturesets/" + m_CurrentPuzzle.getPictureSet();
                    downloadPictureSet pictures = new downloadPictureSet();
                    pictures.execute(URL);
                    //gets the picture sets and returns a string of the jpeg file names

                    try {
                        PictureNamesArray = pictures.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    downloadImages downloadPictures = new downloadImages();

                    downloadPictures.execute(PictureNamesArray);

                    //download the pictures to ensure they are in internal storage

                    DatabaseOp DB = new DatabaseOp(context);

                    String tempLayout = "";

                    for (int i = 0; i < m_CurrentPuzzle.getLayout().size(); i++) {

                        tempLayout += String.valueOf(m_CurrentPuzzle.getLayout().get(i)) + ",";
                        //converts layout in one string split by commas

                    }

                    String tempPicNames = "";

                    for (int i = 0; i < PictureNamesArray.size(); i++) {

                        tempPicNames += PictureNamesArray.get(i) + ",";
                        //converts jpeg files in one string split by commas


                    }

                    DB.putInfo(DB, m_CurrentlySelectedPuzzle, tempLayout, tempPicNames, m_CurrentPuzzle.getNumofColumns(), 0);
                    //inserts data into database

                    downloadedPuzzleSpinnerInfo.clear();
                    downloaedSpinner.setAdapter(null);

                    Cursor CR = DB.getAllRecords(DB);
                    CR.moveToFirst();
                    if(CR.getCount() > 0) {
                        do {

                            String Record = "" + CR.getString(0) + ", " + getString(R.string.Highscore) + CR.getInt(2);
                            downloadedPuzzleSpinnerInfo.add(Record);

                        } while (CR.moveToNext());
                    }

                    CR.close();
                    DB.close();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, downloadedPuzzleSpinnerInfo);
                    downloaedSpinner.setAdapter(adapter);
                    //refreshes the list of downloaded puzzles

                    DownloaedPuzzleID.add(m_CurrentlySelectedPuzzle);

                    ResetSpinner();
                    //resets the puzzles to download list
                    fillFilterSpinner();


                }
                else{
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }

        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String puzzle = "";

                 puzzle = downloaedSpinner.getSelectedItem().toString();

                    DatabaseOp DOP = new DatabaseOp(context);
                    Bundle Bundles = new Bundle();

                    Cursor CR = DOP.getAllRecords(DOP); //returns all records from database
                    CR.moveToFirst();
                    if (CR.getCount() > 0) {
                        do {

                            String[] Puzzles = puzzle.split(",");

                            if (CR.getString(0).equals(Puzzles[0])) {

                                String LayoutTemp = CR.getString(3);

                                ArrayList<Integer> Layout = new ArrayList<Integer>();

                                String[] spilt = LayoutTemp.split(",");

                                for (int i = 0; i < spilt.length; i++) {

                                    Layout.add(Integer.parseInt(spilt[i]));

                                }

                                //gets layout string, splits it by the comma and adds it to an arraylist

                                String PicTemp = CR.getString(4);

                                ArrayList<String> Pic = new ArrayList<String>();

                                String[] picSpilt = PicTemp.split(",");

                                for (int i = 0; i < picSpilt.length; i++) {

                                    Pic.add(picSpilt[i]);

                                }

                                //gets pictureset string, splits it by the comma and adds it to an arraylist

                                Bundles.putInt("NumOfColumns", CR.getInt(1));
                                Bundles.putIntegerArrayList("Layout", Layout);
                                Bundles.putStringArrayList("PicturesNames", Pic);
                                Bundles.putInt("HighScore", CR.getInt(2));
                                Bundles.putString("PuzzleID", Puzzles[0]);
                                //creates bundle to be passed with the intent
                            }

                        } while (CR.moveToNext());
                    }

                    CR.close();
                    DOP.close();

                    Intent intentdownload = new Intent(MainActivity.this, GridViewClass.class);
                    intentdownload.putExtras(Bundles);
                    startActivityForResult(intentdownload, REQUEST_CODE);
                //opens puzzle activity sending puzzle information

                }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Spinner FilterSpinner = (Spinner) findViewById(R.id.NumOfColumnsSpinner);

                final ListView filterPuzzleList = (ListView) findViewById(R.id.FilteredPuzzleListView);

                filteredLIstViewArray.clear();
                filterPuzzleList.setAdapter(null);

                DatabaseOp DB = new DatabaseOp(context);

                String currentlyselectedColumns = FilterSpinner.getSelectedItem().toString();
                int SelectedColumns = Integer.valueOf(currentlyselectedColumns);

                Cursor CR = DB.getNumOfColumnsRecords(DB, SelectedColumns);
                CR.moveToFirst();
                if(CR.getCount() > 0) {
                    do {

                        filteredLIstViewArray.add(CR.getString(0));
                        //retuns all the puzzle pid of puzzles with that amount of columns


                    } while (CR.moveToNext());
                }

                CR.close();
                DB.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, filteredLIstViewArray);
                filterPuzzleList.setAdapter(adapter);

            }
        });

    }

    @Override
    protected void onActivityResult(int pRequestCode,int pResultCode, Intent pData) {

        if(pRequestCode == REQUEST_CODE){

            SharedPreferences sharedPreferences = getSharedPreferences("GridViewClass" ,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            //http://stackoverflow.com/questions/15475377/sharedpreferences-clear-save
            //deletes saved state about puzzle if user has returned to the main menu

            final Spinner downloaedSpinner = (Spinner) findViewById(R.id.DownloadedSpinner);

            downloadedPuzzleSpinnerInfo.clear();
            downloaedSpinner.setAdapter(null);

            DatabaseOp DB = new DatabaseOp(context);

            Cursor CR = DB.getAllRecords(DB);
            CR.moveToFirst();
            if(CR.getCount() > 0) {
                do {

                    String Record = "" + CR.getString(0) + ", " + getString(R.string.Highscore) + CR.getInt(2);
                    DownloaedPuzzleID.add(CR.getString(0));
                    downloadedPuzzleSpinnerInfo.add(Record);

                } while (CR.moveToNext());
            }

            CR.close();
            DB.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, downloadedPuzzleSpinnerInfo);
            downloaedSpinner.setAdapter(adapter);

            ResetSpinner();

            //updates both the downloaded and downloadable lists

            fillFilterSpinner();

        }
    }

    private class downloadJSONSpinner extends AsyncTask<String, String, String>{

        protected String doInBackground(String...args){
            String result = "";
            String PuzzleName = "";
            try
            {
                InputStream stream = (InputStream)new URL(args[0]).getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while (line != null)
                {
                    result += line;
                    line = reader.readLine();
                }

                JSONObject json = new JSONObject(result);

                JSONArray PuzzleIndex = json.getJSONArray("PuzzleIndex");

                for(int i = 0; i < PuzzleIndex.length(); i++){
                    PuzzleName += PuzzleIndex.get(i) + " ";
                }

                //accesses the server and returns json array containing puzzle indexs

            }catch (Exception e){
                e.printStackTrace();
            }

            return PuzzleName;
        }

        protected void onPostExecute(String pResult){

            String[] spilt = pResult.split("\\s+");

            Spinner spinner = (Spinner) findViewById(R.id.spinner);

            for(int i = 0; i < spilt.length; i++){

                boolean puzzledownloaded = false;

                String temp = spilt[i].split("\\.", 2)[0];

                for(int j =0; j < DownloaedPuzzleID.size(); j++){

                    if(temp.equals(DownloaedPuzzleID.get(j))){
                        puzzledownloaded = true;
                    }
                }
                if(puzzledownloaded == false){
                    AvalibePuzzleID.add(temp);
                }

                //removes downloaded puzzle indexs from avalible puzzle list

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, AvalibePuzzleID);
            spinner.setAdapter(adapter);
        }

    }

    public void ResetSpinner(){


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setAdapter(null);

        for(int i = 0; i < AvalibePuzzleID.size(); i++){

            for(int j =0; j < DownloaedPuzzleID.size(); j++){

                if(AvalibePuzzleID.get(i).equals(DownloaedPuzzleID.get(j))){
                    AvalibePuzzleID.remove(i);
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, AvalibePuzzleID);
        spinner.setAdapter(adapter);

        //resets the list of puzzles to download
    }

    private class downloadPuzzleItem extends AsyncTask<String, String, PuzzleItem>{

        protected PuzzleItem doInBackground(String...args){

            ArrayList<Integer> Layout = new ArrayList<>();
            int NumofColumns = -1;
            String PictureSet = "";
            String result = "";

            try
            {
                InputStream stream = (InputStream)new URL(args[0]).getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while (line != null)
                {
                    result += line;
                    line = reader.readLine();
                }

                JSONObject json = new JSONObject(result);
                JSONObject JSON = json.getJSONObject("Puzzle");

                PictureSet = JSON.getString("PictureSet");

                JSONArray PuzzleLayout = JSON.getJSONArray("Layout");

                for(int i = 0; i < PuzzleLayout.length(); i++){
                    Layout.add(PuzzleLayout.getInt(i));
                }

                NumofColumns = PuzzleLayout.length()/JSON.getInt("Rows");


            }catch (Exception e){
                e.printStackTrace();
            }

            PuzzleItem puzzleItem = new PuzzleItem(PictureSet, NumofColumns, Layout);

            //gets puzzle data from json object then creates a puzzle item object to return

            return puzzleItem;
        }

        protected void onPostExecute(PuzzleItem pResult){

        }

    }

    private class downloadPictureSet extends AsyncTask<String, String, ArrayList<String>>{

        protected ArrayList<String> doInBackground(String...args){

            ArrayList<String> Pictures = new ArrayList<>();
            String result = "";

            try
            {
                InputStream stream = (InputStream)new URL(args[0]).getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while (line != null)
                {
                    result += line;
                    line = reader.readLine();
                }

                JSONObject json = new JSONObject(result);

                JSONArray pictureFiles = json.getJSONArray("PictureFiles");

                for(int i = 0; i < pictureFiles.length(); i++){
                    Pictures.add(pictureFiles.getString(i));
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return Pictures;
            //returns arraylist of jpeg file names
        }

        protected void onPostExecute(ArrayList<String> pPictures){

        }

    }
    private class downloadImages extends AsyncTask<ArrayList<String>, String , ArrayList<Bitmap>>{

        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... args){
            ArrayList<Bitmap> PictureSet = new ArrayList<Bitmap>();
            for(int i = 0; i < args[0].size(); i++) {
                Bitmap bitmap = null;
                try {
                    //try to read from internal storage
                    String fileName = args[0].get(i);
                    FileInputStream reader = getApplicationContext().openFileInput(fileName);
                    bitmap = BitmapFactory.decodeStream(reader);

                } catch (FileNotFoundException fileNotFound) {

                    try {
                        //if file was not found try to download it and store it for next time
                        String url = "http://www.hull.ac.uk/php/349628/08027/acw/images/" + args[0].get(i);
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                        FileOutputStream writer = null;
                        try {
                            writer = getApplicationContext().openFileOutput(args[0].get(i), Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, writer);

                        } catch (Exception e) {

                            Log.i("My error", e.getMessage());
                        } finally {
                            writer.close();
                        }

                    } catch (Exception e) {
                        Log.i("My error", e.getMessage());
                    }
                }
            }
            return PictureSet;
            //returns arraylist of bitmaps
        }

        protected void onPostExecute(ArrayList<Bitmap> picSet){


        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
        //http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        //checks if the internet is turned on for the phone or not
    }

    public void fillFilterSpinner(){

      final Spinner FilterSpinner = (Spinner) findViewById(R.id.NumOfColumnsSpinner);

        filterSpinnerColumns.clear();
        FilterSpinner.setAdapter(null);

        DatabaseOp DB = new DatabaseOp(context);

        Cursor CR = DB.ReturnDistinctColumn(DB);
        //returns all the distinct number of columns
        CR.moveToFirst();
        if(CR.getCount() > 0) {
            do {

                filterSpinnerColumns.add(Integer.valueOf(CR.getString(0)));


            } while (CR.moveToNext());
        }

        CR.close();
        DB.close();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, filterSpinnerColumns);
        FilterSpinner.setAdapter(adapter);



    }
}
