package com.tourio.eklrew.tourio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 8/9/2015.
 */
public class ProfileTourListAdapter extends BaseAdapter {
    private Context context;
    private List<TourListItem> tours;
    private static LayoutInflater inflater = null;

    public ProfileTourListAdapter(Context context,List<TourListItem> tours) {
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
        final ViewHolder holder;

        final TourListItem tour = tours.get(position);
        LatLng[] stops = tour.getStops();

        if (tourView == null){
            tourView = inflater.inflate(R.layout.profile_tour_item, null);
            holder = new ViewHolder();
            holder.mapView = (ImageView) tourView.findViewById(R.id.profile_tour_img);
            holder.nameView = (TextView) tourView.findViewById(R.id.tour_name);
            holder.durationView = (TextView) tourView.findViewById(R.id.tour_duration);
            holder.ratingFrame = (FrameLayout) tourView.findViewById(R.id.rating_frame);
            holder.titleLayout= (LinearLayout) tourView.findViewById(R.id.tour_title);

            tourView.setTag(holder);
        }
        else {
            holder = (ViewHolder) tourView.getTag();
        }

        if (tour.getImage() != null) {
            holder.mapView.setImageBitmap(tour.getImage());
        }
        else {
            tour.loadImage(holder.mapView);
        }

        holder.nameView.setText(tour.getName());
        holder.durationView.setText((int) (Math.round(tour.getDuration())) + " hours");

        TourioHelper.LayoutHelper.setRatingImage(inflater, holder.ratingFrame, (int) (Math.round(tour.getRating())));

        /*
        holder.mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, DetailTourActivity.class)
                        .putExtra("tour_id", tour.getTourId());
                context.startActivity(detailIntent);
            }
        });

        holder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, DetailTourActivity.class)
                        .putExtra("tour_id", tour.getTourId());
                context.startActivity(detailIntent);
            }
        });
        */

        return tourView;
    }

    private static class ViewHolder {
        public ImageView mapView;
        public TextView nameView;
        public TextView durationView;
        public FrameLayout ratingFrame;
        public LinearLayout titleLayout;
    }

    public void addAll(List<TourListItem> newTours) {
        tours = new ArrayList<TourListItem>();
        tours.addAll(newTours);
        notifyDataSetChanged();
    }
}