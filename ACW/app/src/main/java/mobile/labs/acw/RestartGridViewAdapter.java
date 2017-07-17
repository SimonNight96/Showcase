package mobile.labs.acw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 483124 on 16/03/2017.
 */
public class RestartGridViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bitmap> m_pictures;
    ArrayList<Integer> m_ClickedTiles = new ArrayList<>();
    ArrayList<Integer> Layout;

    public RestartGridViewAdapter(Context context, ArrayList<Bitmap> pictures, String ClickedTiles, ArrayList<Integer> Layout){

        //https://www.caveofprogramming.com/guest-posts/custom-gridview-with-imageview-and-textview-in-android.html

        this.context = context;
        this.m_pictures = pictures;
        this.Layout = Layout;
        String[] spilt = ClickedTiles.split("\\s+");

        //checks that positions have been clicked
        if(!ClickedTiles.equals( "DEFAULT")) {
            for (int i = 0; i < spilt.length; i++) {
                int temp = Integer.parseInt(spilt[i]);
                m_ClickedTiles.add(temp);
            }
        }

    }


    @Override
    public int getCount() {
        return Layout.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        //initialise holder
        boolean positionClicked = false;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater(); //declare inflater
            convertView = inflater.inflate(R.layout.grid_view_layout, parent, false); // inflate customer layout
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image); //set holder varaible to imageview in custom layout
            convertView.setTag(viewHolder); //store tag

        } else
        {
            viewHolder = (ViewHolder) convertView.getTag(); //returns tag if view is not null
        }

        if(m_ClickedTiles != null) { //checks tile have been clicked

            for (int i = 0; i < m_ClickedTiles.size(); i++) {

                if (position == m_ClickedTiles.get(i)) {

                    positionClicked = true;
                    //checks to see if current position has been clicked already

                }

            }
        }

        if(positionClicked){

            ImageView imageView = viewHolder.image;
            int temp = Layout.get(position);
            imageView.setImageBitmap(m_pictures.get(temp - 1));
            //if clicked already change image view from holder to picture from layout

        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frontpicture);
            ImageView imageView = viewHolder.image;
            imageView.setImageBitmap(bitmap);
            //if not already clicked set imageview to card back
        }

        return convertView;

    }

    static class ViewHolder {
        ImageView image;
    }
}
