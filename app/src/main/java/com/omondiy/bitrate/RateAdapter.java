package com.omondiy.bitrate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RateAdapter extends ArrayAdapter<Cards> implements Filterable {
    ArrayList<Cards> data=new ArrayList<>(); //data = countryList
    private ArrayList<Cards> originalList;

    private List<Cards> cardlist = null;

    private Filter filter;
    Context context;
    SharedPreferences set;
    SharedPreferences.Editor ed;
    SharedPreferences userSettings;

    public RateAdapter(Context a, int textViewResourceId, ArrayList<Cards> items) {
        super(a, textViewResourceId, items);
        this.data = items;
        //this.data.addAll(data);
        this.originalList = new ArrayList<>();
        this.originalList.addAll(data);
        this.context = a;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Cards getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new NameFilter();
        }
        return filter;
    }
    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView currrency;
        public TextView coin;
        public TextView text3;
        public ImageView img1;



    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub

        return false;
    }

    @SuppressWarnings("null")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/

            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.list_item_card, null);
            holder = new ViewHolder();
            userSettings = PreferenceManager.getDefaultSharedPreferences(context);
            set = getContext().getSharedPreferences("infomoby", Context.MODE_PRIVATE);
            ed = set.edit();

            holder.currrency = (TextView) v.findViewById(R.id.currrency);
            holder.coin = (TextView) v.findViewById(R.id.coin);
            holder.text3 = (TextView) v.findViewById(R.id.text3);
            holder.img1 = (ImageView) v.findViewById(R.id.img1);


            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();
        Cards app = data.get(position);
        try {
            if (set.getString("fbid", "").compareTo("") != 0) {
                Picasso.with(context)
                        .load("https://graph.facebook.com/" + set.getString("fbid", "") + "/picture?type=large").transform(new CircleTransform())
                        .into(holder.img1);
            } else {
                Picasso.with(context).load(R.drawable.ic_menu_camera).transform(new CircleTransform()).into(holder.img1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.currrency.setText(app.getCurrency());
        holder.coin.setText(app.getCoin());
        holder.text3.setText(app.getText3());

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(getContext(), ViewCard.class);
                // Pass all data flag
                intent.putExtra("currencyval",(cardlist.get(position).getCurrency()));
                intent.putExtra("coinval",(cardlist.get(position).getCoin()));
                // Start SingleItemView Class
                getContext().startActivity(intent);
            }
        });
        return v;

    }

    private String getDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT-04:00"));
            Date value = formatter.parse(OurDate);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("E, dd-MM-yyyy"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

        } catch (Exception e) {
            OurDate = "";
        }
        return OurDate;
    }

    private class NameFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Cards> filteredItems = new ArrayList<>();

                for(int i = 0, l = originalList.size(); i < l; i++)
                {
                    Cards nameList = originalList.get(i);
                    if(nameList.getCurrency().toString().toLowerCase().contains(constraint))
                        filteredItems.add(nameList);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            data = (ArrayList<Cards>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = data .size(); i < l; i++)
                add(data .get(i));
            notifyDataSetInvalidated();
        }
    }
    final long totalScrollTime = Long.MAX_VALUE; //total scroll time. I think that 300 000 000 years is close enouth to infinity. if not enought you can restart timer in onFinish()

    final int scrollPeriod = 20; // every 20 ms scoll will happened. smaller values for smoother

    final int heightToScroll = 20; // will be scrolled to 20 px every time. smaller values for smoother scrolling

    class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap;
            if ((source != null) && (source.getConfig() != null)) {
                bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            }else{
                bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}











