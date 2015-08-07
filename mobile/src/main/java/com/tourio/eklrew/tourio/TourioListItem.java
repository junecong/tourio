package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Prud on 8/7/2015.
 */
public abstract class TourioListItem {
    private String picUrl;
    private Bitmap image;

    public TourioListItem(String picUrl) {
        this.picUrl = picUrl;
    }

    public TourioListItem() {

    }

    public Bitmap getImage() {
        return image;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void loadImage(ImageView bmImage) {
        new ImageLoadTask(bmImage).execute(picUrl);
    }

    public void loadImage(Context context,ImageView bmImage) {
        new ImageLoadTask(context,bmImage).execute(picUrl);
    }

    public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        boolean isForTourList;
        Context context;

        public ImageLoadTask(Context context,ImageView bmImage) {
            this.context=context;
            this.isForTourList=true;
            this.bmImage = bmImage;
        }

        public ImageLoadTask(ImageView bmImage) {
            this.isForTourList=false;
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                Log.e("pic url",urls[0]);
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            image = result;
            if (isForTourList) {
                BitmapDrawable resultDrawable = new BitmapDrawable(context.getResources(), result);
                bmImage.setBackgroundDrawable(resultDrawable);
            }
            else {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
