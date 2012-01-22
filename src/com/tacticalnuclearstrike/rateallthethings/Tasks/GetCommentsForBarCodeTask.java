package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetCommentsForBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;

import java.util.List;

public class GetCommentsForBarCodeTask extends AsyncTask<Long, Void, List<Comment>> {

    private IService service;
    private IGetCommentsForBarCodeResult getCommentsForBarCodeResult;

    public GetCommentsForBarCodeTask(IService service, IGetCommentsForBarCodeResult getCommentsForBarCodeResult) {
        this.service = service;
        this.getCommentsForBarCodeResult = getCommentsForBarCodeResult;
    }

    @Override
    protected List<Comment> doInBackground(Long... barCodeId) {
        return this.service.getCommentsForBarCode(barCodeId[0]);
    }

    @Override
    protected void onPostExecute(List<Comment> comments) {
        this.getCommentsForBarCodeResult.ReturnedComments(comments);
    }
}
