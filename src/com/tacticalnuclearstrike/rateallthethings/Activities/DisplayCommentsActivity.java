package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.actionbarcompat.ActionBarListActivity;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetCommentsForBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GetCommentsForBarCodeTask;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GravatarDownloaderTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;

import java.text.DateFormat;
import java.util.List;

/**
 * User: Fredrik / 2012-01-22
 */
public class DisplayCommentsActivity extends ActionBarListActivity implements IGetCommentsForBarCodeResult {

    @Inject
    IService service;
    @Inject
    ISettings settings;

    ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long barCodeId = getIntent().getLongExtra("BarCodeId", -1);
        if (barCodeId > 0) {
            this.pd = ProgressDialog.show(this, "", "Downloading comments...");
            new GetCommentsForBarCodeTask(this.service, this).execute(barCodeId);
        }
    }

    public void ReturnedComments(List<Comment> comments) {
        ArrayAdapter<Comment> adapter = new CommentAdapter(this, comments);
        setListAdapter(adapter);
        this.pd.dismiss();
    }

    class CommentAdapter extends ArrayAdapter<Comment> {

        private List<Comment> objects;
        private Context context;

        public CommentAdapter(Context context, List<Comment> objects) {
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
            ImageView avatar = (ImageView) rowView.findViewById(R.id.comment_avatar);
            TextView nameAndDateView = (TextView) rowView.findViewById(R.id.coment_author_date);

            Comment comment = this.objects.get(position);

            textView.setText(comment.Text);
            if (comment.AuthorName == null)
                nameAndDateView.setText(DateFormat.getDateInstance().format(comment.CreatedDate));
            else
                nameAndDateView.setText(String.format("%s, %s", comment.AuthorName, DateFormat.getDateInstance().format(comment.CreatedDate)));

            new GravatarDownloaderTask(avatar, settings).execute(comment.Avatar);
            return rowView;
        }


    }
}


