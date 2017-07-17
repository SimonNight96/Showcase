package mobile.labs.acw;

import android.provider.BaseColumns;

/**
 * Created by 483124 on 21/03/2017.
 */
public class TableInfo {
    public  TableInfo(){

    }

    public static abstract class TableData implements BaseColumns{

        public static final String Table_Name = "ACW_Table";
        public static final String Layout_Column = "Layout";
        public static final String Num_Column = "NumOfColumns";
        public static final String Pic_Name_Column = "Pic_Names";
        public static final String Database_name = "ACW_Database";
        public static final String HighScore = "HighScore";
        public static final String PuzzleID = "PuzzleID";
        //stores database information to be used in other classes

    }
}
