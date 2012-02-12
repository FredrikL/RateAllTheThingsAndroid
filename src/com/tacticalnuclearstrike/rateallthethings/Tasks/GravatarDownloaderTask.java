package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GravatarDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private ISettings settings;

    public GravatarDownloaderTask(ImageView imageView, ISettings settings){

        this.imageView = imageView;
        this.settings = settings;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return getImageBitmap(strings[0]);
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            Log.d(settings.getTag(), url);
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(settings.getTag(), "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        this.imageView.setImageBitmap(result);
    }
}
