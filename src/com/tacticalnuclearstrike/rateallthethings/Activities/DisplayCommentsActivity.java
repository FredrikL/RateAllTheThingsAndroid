package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetCommentsForBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GetCommentsForBarCodeTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;
import roboguice.activity.RoboListActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * User: Fredrik / 2012-01-22
 */
public class DisplayCommentsActivity extends RoboListActivity implements IGetCommentsForBarCodeResult {
    
    @Inject
    IService service;
    @Inject
    ISettings settings;

    ProgressDialog pd;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        long barCodeId =getIntent().getLongExtra("BarCodeId", -1);
        if(barCodeId > 0) {
            this.pd = ProgressDialog.show(this, "", "Downloading comments...");
            new GetCommentsForBarCodeTask(this.service, this).execute(barCodeId);
        }
    }

    public void ReturnedComments(List<Comment> comments) {
        ArrayAdapter<Comment> adapter = new Adapter(this, comments);
        setListAdapter(adapter);
        this.pd.dismiss();
    }
    class Adapter extends ArrayAdapter<Comment>{

        private List<Comment> objects;
        private Context context;

        public Adapter(Context context, List<Comment> objects) {
            super(context, R.layout.comment_item, objects);
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.comment_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.comment_text);
            ImageView avatar = (ImageView)rowView.findViewById(R.id.comment_avatar);

            Comment comment =                          this.objects.get(position);
            textView.setText(comment.Text);
            //avatar.setImageBitmap(getImageBitmap(comment.Avatar));
            new GravatarDownloaderTask(avatar).execute(comment.Avatar);
            return rowView;
        }


    }
    
    class GravatarDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public GravatarDownloaderTask(ImageView imageView){

            this.imageView = imageView;
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
}

