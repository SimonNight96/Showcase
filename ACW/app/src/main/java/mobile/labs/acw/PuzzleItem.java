package mobile.labs.acw;

import java.util.ArrayList;

/**
 * Created by 483124 on 09/03/2017.
 */
public class PuzzleItem {
    private String PictureSet;
    private int NumofColumns;
    private ArrayList<Integer> Layout;

    public PuzzleItem(){

    }

    public PuzzleItem(String PictureSet, int NumofColumns, ArrayList<Integer> Layout ) {
        super();
        this.PictureSet = PictureSet;
        this.NumofColumns = NumofColumns;
        this.Layout = Layout;

    }

    public String getPictureSet() {
        return PictureSet;
    }

    public int getNumofColumns() {
        return NumofColumns;
    }

    public ArrayList<Integer> getLayout() {
        return Layout;
    }
}
