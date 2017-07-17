package simonnight.wifiscannerdata;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ViewDatabase extends AppCompatActivity {

    Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        ArrayList<String> Data = new ArrayList<String>();

        ListView listview = (ListView) findViewById(R.id.DataListView);

        DatabaseOp DOP = new DatabaseOp(ctx);
        String Record;
        Cursor CR = DOP.getInfo(DOP);
        CR.moveToFirst();

        do {

            Record = "Location: " + CR.getInt(0) + " Mac: " + CR.getString(1) + " RSSI: " +  CR.getInt(2);
            Data.add(Record);

           } while (CR.moveToNext());

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Data);

        listview.setAdapter(adapter);


    }
}