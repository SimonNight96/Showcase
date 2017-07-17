package mobile.labs.acw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 483124 on 21/03/2017.
 */
public class DatabaseOp extends SQLiteOpenHelper {
    public  static final int database_version = 1;
    public String Create_qeury = "CREATE TABLE " + TableInfo.TableData.Table_Name + "("+ TableInfo.TableData.PuzzleID + " VARCHAR(30)," + TableInfo.TableData.Num_Column + " INT," + TableInfo.TableData.HighScore + " INT," + TableInfo.TableData.Layout_Column + " VARCHAR(50)," + TableInfo.TableData.Pic_Name_Column + " VARCHAR(200) );";

    public DatabaseOp(Context context){
        super(context, TableInfo.TableData.Database_name, null, database_version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Create_qeury);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInfo(DatabaseOp dop, String PuzzleID, String Layout, String pictureNames, int NumOfColumns, int highscore){

        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.TableData.PuzzleID, PuzzleID);
        CV.put(TableInfo.TableData.Layout_Column, Layout);
        CV.put(TableInfo.TableData.Pic_Name_Column, pictureNames);
        CV.put(TableInfo.TableData.Num_Column, NumOfColumns);
        CV.put(TableInfo.TableData.HighScore, highscore);
        SQ.insert(TableInfo.TableData.Table_Name, null,CV);

    }

    public Cursor getAllRecords(DatabaseOp dop){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloums = {TableInfo.TableData.PuzzleID, TableInfo.TableData.Num_Column, TableInfo.TableData.HighScore, TableInfo.TableData.Layout_Column, TableInfo.TableData.Pic_Name_Column };
        Cursor CR = SQ.query(TableInfo.TableData.Table_Name, coloums,null,null,null,null,null);
        return CR;

    }

    public Cursor getNumOfColumnsRecords(DatabaseOp dop, int NumOFColumns){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloums = {TableInfo.TableData.PuzzleID};
        String WhereClause = TableInfo.TableData.Num_Column + " = ? ";
        String[] variables = {String.valueOf(NumOFColumns)};
        Cursor CR = SQ.query(TableInfo.TableData.Table_Name, coloums,WhereClause,variables,null,null,null);
        return CR;

    }

    public Cursor ReturnDistinctColumn (DatabaseOp dop){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor CR = SQ.query(true,TableInfo.TableData.Table_Name, new String [] {TableInfo.TableData.Num_Column}, null,null,null,null,null, null);
        return CR;

    }

    public void updateHighScore(DatabaseOp dop, int NewHighscore, String PuzzleID ){

        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues CV = new ContentValues();

        CV.put(TableInfo.TableData.HighScore, NewHighscore);
        String[] values = {PuzzleID};

        SQ.update(TableInfo.TableData.Table_Name, CV, TableInfo.TableData.PuzzleID+"=?", values);

    }

}
