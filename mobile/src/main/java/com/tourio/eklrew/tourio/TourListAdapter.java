package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
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
        LatLng[] stops = tour.getStops();

        View tourView = convertView;
        if (tourView == null)
            tourView = inflater.inflate(R.layout.tour_list_item, null);

        ImageView mapView = (ImageView) tourView.findViewById(R.id.map);
        TextView nameView = (TextView) tourView.findViewById(R.id.tour_name);
        TextView durationView = (TextView) tourView.findViewById(R.id.tour_duration);

        //String mapUrl = BASE_STATIC_MAPS_API_URL + "size="+ width+ "x"+ height +"&markers=";
        String mapUrl = BASE_STATIC_MAPS_API_URL;
        for (int i=0;i<stops.length;i++) {
            LatLng stop = stops[i];
            mapUrl += "&markers=size:mid%7Ccolor:blue%7Clabel:"+(i+1)+"%7C"+stop.latitude+","+stop.longitude;
        }

        Log.v("map url: ", mapUrl);
        new DownloadImageTask(mapView).execute(mapUrl);

        nameView.setText(tour.getName());
        durationView.setText((int) (Math.round(tour.getDuration()))+" hours");

        FrameLayout ratingFrame = (FrameLayout) tourView.findViewById(R.id.rating_frame);
        TourHelper.setRatingImage(context,ratingFrame,(int) (Math.round(tour.getRating())));

        return tourView;
    }

    public void addAll(List<TourListItem> newTours) {
        tours = new ArrayList<TourListItem>();
        tours.addAll(newTours);
        notifyDataSetChanged();
    }
}