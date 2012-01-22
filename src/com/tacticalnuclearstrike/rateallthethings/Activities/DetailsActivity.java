package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IPostCommentResult;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IUpdateBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.PostCommentTask;
import com.tacticalnuclearstrike.rateallthethings.Tasks.UpdateBarCodeTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DetailsActivity extends RoboActivity implements IUpdateBarCodeResult, IPostCommentResult {
    @InjectView(R.id.tvFormat) TextView format;
    @InjectView(R.id.tvCode) TextView code;
    @InjectView(R.id.barcodeName) EditText barcodeName;
    @InjectView(R.id.commentText) EditText commentText;
    
    @InjectView(R.id.btnUpdateBarcode) Button btnUpdateBarcode;
    @InjectView(R.id.btnPostComment) Button btnPostComment;
    @InjectView(R.id.btnDisplayComments) Button btnDisplayComments;
    
    @Inject IService service;
    @Inject ISettings settings;

    private BarCode currentBarCode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.details_activity);

        setupButtons();

        loadBarCode();
    }

    private void setupButtons() {
        btnUpdateBarcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                callUpdateBarCode();
            }
        });
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                postComment();
            }
        });
        btnDisplayComments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayComments();
            }
        });
    }
    
    private void displayComments(){
        Intent i = new Intent(this, DisplayCommentsActivity.class);
        i.putExtra("BarCodeId", this.currentBarCode.Id);
        this.startActivity(i);
    }

    private void loadBarCode() {
        currentBarCode = (BarCode) getIntent().getSerializableExtra("BARCODE");
        if (currentBarCode == null) {
            Toast.makeText(this, "No barcode passed", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            Log.d("RateAllTheThings", "Not null");
            format.setText("Format: " + currentBarCode.Format);
            code.setText("Code: " + currentBarCode.Code);
            barcodeName.setText(currentBarCode.Name);
        }
    }

    private void callUpdateBarCode() {
        currentBarCode.Name = barcodeName.getText().toString();

        new UpdateBarCodeTask(this.service, this).execute(currentBarCode);
    }

    public void success(Boolean result) {
        if(result)
            this.finish();
        else
        {
            Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
        }
    }
    
    private void postComment(){
        String text = this.commentText.getText().toString();
        Comment comment = new Comment();
        comment.Text = text;
        comment.BarCodeId = this.currentBarCode.Id;
        
        new PostCommentTask(this.service, this).execute(comment);
    }

    public void postCommentResult(Boolean result) {
        Log.d(this.settings.getTag(), "postCommentResult: " + result.toString());
    }
}
