package com.tourio.eklrew.tourio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 7/31/2015.
 */
public class StopListAdapter extends BaseAdapter {
    private Context context;
    private List<Stop> stops;
    private static LayoutInflater inflater = null;

    public StopListAdapter(Context context,List<Stop> stops) {
        this.context = context;
        this.stops = stops;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stops.size();
    }

    @Override
    public Object getItem(int position) {
        return stops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder holder;
        Stop stop = stops.get(position);

        View stopView = convertView;

        if (stopView == null){
            stopView = inflater.inflate(R.layout.stop_list_item, null);
            holder = new ViewHolder();
            holder.stopNumberView = (TextView)stopView.findViewById(R.id.stop_number);
            holder.stopNameView = (TextView)stopView.findViewById(R.id.stop_name);
            holder.stopDescriptionView = (TextView)stopView.findViewById(R.id.stop_description);
            holder.stopImageView = (ImageView) stopView.findViewById(R.id.stop_image);

            stopView.setTag(holder);
        }
        else {
            holder = (ViewHolder) stopView.getTag();
        }

        if (stopView == null)
            stopView = inflater.inflate(R.layout.stop_list_item, null);

        if (stop.getImage() != null) {
            holder.stopImageView.setImageBitmap(stop.getImage());
        } else {
            // MY DEFAULT IMAGE
            //holder.stopImageView.setImageResource(R.drawable.ic_launcher);
            stop.loadImage(holder.stopImageView);
        }
        holder.stopNumberView.setText(""+(position+1));
        holder.stopNameView.setText(stop.getName());
        holder.stopDescriptionView.setText(stop.getDescription());

        return stopView;
    }

    private static class ViewHolder {
        TextView stopNumberView;
        TextView stopNameView;
        TextView stopDescriptionView;
        ImageView stopImageView;
    }
}
