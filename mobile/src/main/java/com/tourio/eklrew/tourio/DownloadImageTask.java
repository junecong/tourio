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
 * Created by Prud on 7/24/2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    boolean isForList;
    Context context;

    public DownloadImageTask(Context context,ImageView bmImage) {
        this.context=context;
        this.isForList=true;
        this.bmImage = bmImage;
    }

    public DownloadImageTask(ImageView bmImage) {
        this.isForList=false;
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
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (isForList) {
            BitmapDrawable resultDrawable = new BitmapDrawable(context.getResources(), result);
            bmImage.setBackgroundDrawable(resultDrawable);
        }
        else {
            bmImage.setImageBitmap(result);
        }
    }
}