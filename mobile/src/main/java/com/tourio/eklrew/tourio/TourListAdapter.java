package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    public View getView(int position,View tourView,ViewGroup parent) {
        ViewHolder holder;

        TourListItem tour = tours.get(position);
        LatLng[] stops = tour.getStops();

        if (tourView == null){
            tourView = inflater.inflate(R.layout.tour_list_item, null);
            holder = new ViewHolder();
            holder.mapView = (ImageView) tourView.findViewById(R.id.map);
            holder.nameView = (TextView) tourView.findViewById(R.id.tour_name);
            holder.durationView = (TextView) tourView.findViewById(R.id.tour_duration);
            holder.ratingFrame = (FrameLayout) tourView.findViewById(R.id.rating_frame);

            tourView.setTag(holder);
        }
        else {
            holder = (ViewHolder) tourView.getTag();
        }


        if (tour.getImage() != null) {
            BitmapDrawable resultDrawable = new BitmapDrawable(context.getResources(), tour.getImage());
            holder.mapView.setBackgroundDrawable(resultDrawable);
        }
        else {
            String mapUrl = TourioHelper.GoogleMapsStaticApiHelper.BASE_STATIC_MAPS_API_URL;
            Log.v("new map url",mapUrl);
            for (int i=0;i<stops.length;i++) {
                LatLng stop = stops[i];
                mapUrl += "&markers=size:mid%7Ccolor:red%7Clabel:"+(i+1)+"%7C"+stop.latitude+","+stop.longitude;
            }
            tour.setPicUrl(mapUrl);
            tour.loadImage(context,holder.mapView);
        }


//        String mapUrl = TourioHelper.GoogleMapsStaticApiHelper.BASE_STATIC_MAPS_API_URL;
//        Log.v("new map url",mapUrl);
//        for (int i=0;i<stops.length;i++) {
//            LatLng stop = stops[i];
//            mapUrl += "&markers=size:mid%7Ccolor:red%7Clabel:"+(i+1)+"%7C"+stop.latitude+","+stop.longitude;
//        }
//        (new DownloadImageTask(context,holder.mapView)).execute(mapUrl);

        holder.nameView.setText(tour.getName());
        holder.durationView.setText((int) (Math.round(tour.getDuration()))+" hours");

        TourioHelper.LayoutHelper.setRatingImage(inflater,holder.ratingFrame,(int) (Math.round(tour.getRating())));

        return tourView;
    }

    private static class ViewHolder {
        public ImageView mapView;
        public TextView nameView;
        public TextView durationView;
        public FrameLayout ratingFrame;
    }

    public void addAll(List<TourListItem> newTours) {
        tours = new ArrayList<TourListItem>();
        tours.addAll(newTours);
        notifyDataSetChanged();
    }
}