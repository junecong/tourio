package com.tourio.eklrew.tourio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanumal on 7/24/2015.
 */
public class TourListAdapter extends BaseAdapter {
    private Context context;
    private List<TourListItem> tours;
    private static LayoutInflater inflater = null;

    final String BASE_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?size=400x200";

    public TourListAdapter(Context context,List<TourListItem> tours) {
        this.context = context;
        this.tours = tours;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tours.size();
    }

    @Override
    public Object getItem(int position) {
        return tours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        TourListItem tour = tours.get(position);
        ArrayList<Stop> stops = tour.getStops();

        View tourView = convertView;
        if (tourView == null)
            tourView = inflater.inflate(R.layout.tour_list_item, null);

        ImageView mapView = (ImageView) tourView.findViewById(R.id.map);
        TextView nameView = (TextView) tourView.findViewById(R.id.tour_name);
        TextView ratingView = (TextView) tourView.findViewById(R.id.tour_rating);
        TextView durationView = (TextView) tourView.findViewById(R.id.tour_duration);
        TextView cityView = (TextView) tourView.findViewById(R.id.tour_city);

        String mapUrl = BASE_STATIC_MAPS_API_URL + "&markers=";
        for (int i=0;i<stops.size();i++) {
            Stop stop = stops.get(i);
            mapUrl += stop.getLatitude()+","+stop.getLongitude()+"|";
        }

        Log.v("map url: ",mapUrl);
        new DownloadImageTask(mapView).execute(mapUrl);

        nameView.setText(tour.getName());
        ratingView.setText(Double.toString(tour.getRating())+" stars");
        durationView.setText(Double.toString(tour.getDuration())+" hours");
        cityView.setText(tour.getCity());

        return tourView;
    }
}