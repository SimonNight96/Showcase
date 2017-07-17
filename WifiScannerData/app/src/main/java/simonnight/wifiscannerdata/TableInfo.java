package simonnight.wifiscannerdata;


import android.provider.BaseColumns;

public class TableInfo {

    public TableInfo(){


    }

    public static abstract class TableData implements BaseColumns{

        public static final String Location = "location";
        public static final String Mac_address = "mac_address";
        public static final String RSSI = "rssi";
        public static final String Database_name = "wifi";
        public static final String Table_Name = "wifi_info";

    }


}
