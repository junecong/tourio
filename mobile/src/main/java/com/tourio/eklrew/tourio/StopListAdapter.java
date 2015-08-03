package com.tourio.eklrew.tourio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        Stop stop = stops.get(position);

        View stopView = convertView;
        if (stopView == null)
            stopView = inflater.inflate(R.layout.stop_list_item, null);

        TextView stopNumberView = (TextView)stopView.findViewById(R.id.stop_number);
        TextView stopNameView = (TextView)stopView.findViewById(R.id.stop_name);
        TextView stopDescriptionView = (TextView)stopView.findViewById(R.id.stop_description);
        ImageView stopImageView = (ImageView) stopView.findViewById(R.id.stop_image);

        (new DownloadImageTask(stopImageView)).execute(stop.getPicUrl());
        stopNumberView.setText(""+(position+1));
        stopNameView.setText(stop.getName());
        stopDescriptionView.setText(stop.getDescription());

        return stopView;
    }
}
