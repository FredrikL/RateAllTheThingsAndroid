package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IPostCommentResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;

public class PostCommentTask extends AsyncTask<Comment, Void, Boolean> {

    private IService service;
    private IPostCommentResult postCommentResult;

    public PostCommentTask(IService service, IPostCommentResult postCommentResult) {
        this.postCommentResult = postCommentResult;
        this.service = service;
    }

    @Override
    protected Boolean doInBackground(Comment... comment) {
        return this.service.addComment(comment[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.postCommentResult.postCommentResult(result);
    }
}
