package simonnight.wifiscannerdata;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE = 1;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE = 2;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 3;

    int counter = 0;
    int counter02 = 0;

    int location;
    Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText locationTBX = (EditText) findViewById(R.id.LocationTxtB);
        Button ScanBtn = (Button) findViewById(R.id.ScanBtn);
        Button ReadBtn = (Button) findViewById(R.id.ReadBtn);
        Button DelBtn = (Button) findViewById(R.id.delBtn);
        Button ScanningBtn = (Button) findViewById(R.id.StartScanningBtn);


        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (locationTBX.getText().toString() == "") {

                    Toast.makeText(MainActivity.this, "No location entered", Toast.LENGTH_SHORT).show();
                } else {
                    location = Integer.parseInt(locationTBX.getText().toString());

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                                MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE);
                    }

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                                MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE);
                    }

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }


                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                WifiManager ScanWifiManager;

                                DatabaseOp DB = new DatabaseOp(ctx);

                                ScanWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                ScanWifiManager.startScan();
                                List<ScanResult> results = ScanWifiManager.getScanResults();


                                for (ScanResult result : results) {

                                    DB.putInfo(DB, result.BSSID, location, result.level);
                                    locationTBX.setText("");

                                }


                            }

                        }
                    }


                }
            }
        });


        ReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ViewDatabase.class);
                startActivity(intent);

            }
        });

        DelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseOp DB = new DatabaseOp(ctx);

                DB.deleteDB(ctx);


            }
        });

        ScanningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LocationScan.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Permission was granted",
                            Toast.LENGTH_LONG).show();
                    return;

                } else {
                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Permission was granted",
                            Toast.LENGTH_LONG).show();
                    return;

                } else {
                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;

            }

            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Permission was granted",
                            Toast.LENGTH_LONG).show();
                    return;

                } else {
                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;

            }
        }
    }

}
