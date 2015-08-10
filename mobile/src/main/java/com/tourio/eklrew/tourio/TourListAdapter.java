package com.tourio.eklrew.tourio;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        final ViewHolder holder;

        final TourListItem tour = tours.get(position);
        LatLng[] stops = tour.getStops();

        if (tourView == null){
            tourView = inflater.inflate(R.layout.tour_list_item, null);
            holder = new ViewHolder();
            holder.mapView = (ImageView) tourView.findViewById(R.id.map);
            holder.nameView = (TextView) tourView.findViewById(R.id.tour_name);
            holder.durationView = (TextView) tourView.findViewById(R.id.tour_duration);
            holder.ratingFrame = (FrameLayout) tourView.findViewById(R.id.rating_frame);
            holder.leftButton = (Button) tourView.findViewById(R.id.left_button);
            holder.rightButton = (Button) tourView.findViewById(R.id.right_button);
            holder.titleLayout = (LinearLayout) tourView.findViewById(R.id.tour_title);

            tourView.setTag(holder);
        }
        else {
            holder = (ViewHolder) tourView.getTag();
        }

        //tour.setImageView(holder.mapView);

        /*
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
        */

        if (tour.getImage() != null) {
            //BitmapDrawable resultDrawable = new BitmapDrawable(context.getResources(), tour.getImage());
            //holder.mapView.setBackgroundDrawable(resultDrawable);
            holder.mapView.setImageBitmap(tour.getImage());
        }
        else {
            tour.loadImage(holder.mapView);
        }



        holder.nameView.setText(tour.getName());
        holder.durationView.setText((int) (Math.round(tour.getDuration())) + " hours");

        TourioHelper.LayoutHelper.setRatingImage(inflater, holder.ratingFrame, (int) (Math.round(tour.getRating())));

        /*
        final GestureDetector gestureDetector = new GestureDetector(context, new GestureListener() {
            public void onSingleTapConfirmed() {
                Intent detailIntent = new Intent(context, DetailTourActivity.class)
                        .putExtra("tour_id", tour.getTourId());
                context.startActivity(detailIntent);
            }
            public void onLeftSwipe() {
                tour.changeImage(context, holder.mapView, false);
            }
            public void onRightSwipe() {
                tour.changeImage(context,holder.mapView,true);
            }
        });

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        holder.mapView.setOnTouchListener(gestureListener);
        */

        holder.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tour.changeImage(context, holder.mapView, false);
            }
        });

        holder.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tour.changeImage(context, holder.mapView, true);
            }
        });

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

        return tourView;
    }

    private static class ViewHolder {
        public ImageView mapView;
        public TextView nameView;
        public TextView durationView;
        public FrameLayout ratingFrame;
        public Button leftButton;
        public Button rightButton;
        public LinearLayout titleLayout;
    }

    public void addAll(List<TourListItem> newTours) {
        tours = new ArrayList<TourListItem>();
        tours.addAll(newTours);
        notifyDataSetChanged();
    }


    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onLeftSwipe();
                    return true;
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onRightSwipe();
                    return true;
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTapConfirmed();
            return true;
        }

        public void onLeftSwipe() {

        }
        public void onRightSwipe() {

        }

        public void onSingleTapConfirmed() {

        }
    }
}