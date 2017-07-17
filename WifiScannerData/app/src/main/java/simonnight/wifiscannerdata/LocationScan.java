package simonnight.wifiscannerdata;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.LogRecord;

public class LocationScan extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE = 1;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE = 2;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 3;

    Context ctx = this;
    int CurrentMatchCounter = 0;
    int CurrentHighestMacthCount = 0;
    int EstimateLocation = 0;
    private Handler Handler = new Handler();
    private Runnable runnable;
    private int mCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_scan);

        Button Startbtn = (Button) findViewById(R.id.StartBtn);
        final TextView Test = (TextView) findViewById(R.id.SizeTbx);
        final TextView Test02 = (TextView) findViewById(R.id.CurrentMatch);
        final TextView Send = (TextView) findViewById(R.id.SendDatatbx);
        final TextView Rec = (TextView) findViewById(R.id.RecievedDatatbx);


        Startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LocationScan.this,
                            new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                            MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE);
                }

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LocationScan.this,
                            new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                            MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE);
                }

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LocationScan.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            runnable = new Runnable() {
                                @Override
                                public void run() {

                            WifiManager ScanWifiManager;

                            DatabaseOp DB = new DatabaseOp(ctx);

                            Cursor CR = DB.getLocationSizeInfo(DB);
                            CR.moveToFirst();

                            DatabaseOp DBN = new DatabaseOp(ctx);


                            int DBSize = CR.getInt(0);

                            ScanWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            ScanWifiManager.startScan();
                            List<ScanResult> results = ScanWifiManager.getScanResults();

                            for (int i = 1; i <= DBSize; i++) {

                                for (ScanResult result : results) {

                                    String CurrentMac = result.BSSID;
                                    int rssi = result.level;

                                    CR = DBN.getScanInfo(DBN, CurrentMac, Integer.toString(i), (rssi + 3), (rssi - 3));
                                    CR.moveToFirst();


                                    if (CR.getCount() != 0) {

                                        Rec.setText("Location: " + CR.getInt(0) + " Mac: " + CR.getString(1) + " RSSI: " + CR.getInt(2));
                                        CurrentMatchCounter++;

                                    }

                                }

                                if (CurrentMatchCounter > CurrentHighestMacthCount) {

                                    CurrentHighestMacthCount = CurrentMatchCounter;
                                    EstimateLocation = i;

                                }

                                Test02.setText("Current Highest match " + CurrentHighestMacthCount);

                                CurrentMatchCounter = 0;

                            }

                            CurrentHighestMacthCount = 0;

                            Test.setText("Location: " + EstimateLocation);

                                    mCounter++;

                                    Send.setText(""+mCounter+"");

                                    Handler.postDelayed(runnable,2000);

                                }
                            };

                            Handler.post(runnable);

                            }
                        }

                    }


                }
        });



    }

}
