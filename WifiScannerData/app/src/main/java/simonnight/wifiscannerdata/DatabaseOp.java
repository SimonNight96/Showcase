package simonnight.wifiscannerdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DatabaseOp extends SQLiteOpenHelper{
    public static final int database_version = 1;
    public String Create_query = "CREATE TABLE " + TableInfo.TableData.Table_Name + "("+ TableInfo.TableData.Location + " INT," + TableInfo.TableData.Mac_address + " VARCHAR(20)," + TableInfo.TableData.RSSI + " INT );";

    public DatabaseOp(Context context){
        super(context, TableInfo.TableData.Database_name, null, database_version);

    }

    @Override
    public void onCreate(SQLiteDatabase sdb){

        sdb.execSQL(Create_query);

    }

    public void onUpgrade(SQLiteDatabase argo, int arg1, int arg2){

    }

    public void putInfo(DatabaseOp dop, String mac_address, int location, int rssi ){

        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.TableData.Location, location);
        CV.put(TableInfo.TableData.Mac_address, mac_address);
        CV.put(TableInfo.TableData.RSSI, rssi);
        SQ.insert(TableInfo.TableData.Table_Name,null,CV);

    }

    public Cursor getInfo(DatabaseOp dop){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloums = {TableInfo.TableData.Location, TableInfo.TableData.Mac_address, TableInfo.TableData.RSSI};
        Cursor CR = SQ.query(TableInfo.TableData.Table_Name, coloums,null,null,null,null,null);
        return CR;
    }

    public void deleteDB(Context context){

        context.deleteDatabase(TableInfo.TableData.Database_name);

    }

    public Cursor getScanInfo(DatabaseOp dop, String Mac, String location, int high_rssi, int low_rssi){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloums = {TableInfo.TableData.Location, TableInfo.TableData.Mac_address, TableInfo.TableData.RSSI};
        String WhereClause = TableInfo.TableData.Location + " = ? AND " + TableInfo.TableData.Mac_address + " = ? AND " + TableInfo.TableData.RSSI + " < ? AND " +
        TableInfo.TableData.RSSI + " > ?";
        String[] Variables = {location,Mac,String.valueOf(high_rssi), String.valueOf(low_rssi)};
        Cursor CR = SQ.query(TableInfo.TableData.Table_Name, coloums,WhereClause,Variables,null,null,null);
        return CR;
    }

    public Cursor getLocationSizeInfo(DatabaseOp dop){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor CR = SQ.query(TableInfo.TableData.Table_Name, new String [] {"MAX("+ TableInfo.TableData.Location+")"}, null, null, null, null, null);
        return CR;
    }










}
