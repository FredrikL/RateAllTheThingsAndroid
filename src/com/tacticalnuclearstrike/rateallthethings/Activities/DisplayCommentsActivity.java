package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetCommentsForBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GetCommentsForBarCodeTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;
import roboguice.activity.RoboListActivity;

import java.util.List;

/**
 * User: Fredrik / 2012-01-22
 */
public class DisplayCommentsActivity extends RoboListActivity implements IGetCommentsForBarCodeResult {
    
    @Inject
    IService service;

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

            textView.setText(this.objects.get(position).Text);

            return rowView;
        }
    }
}

