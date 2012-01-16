package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.IUpdateBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;

public class UpdateBarCodeTask extends AsyncTask<BarCode, Void, Boolean> {

    private IService service;
    private IUpdateBarCodeResult updateBarCodeResult;

    public UpdateBarCodeTask(IService service, IUpdateBarCodeResult updateBarCodeResult){

        this.service = service;
        this.updateBarCodeResult = updateBarCodeResult;
    }

    @Override
    protected Boolean doInBackground(BarCode... barCodes) {
        return this.service.updateBarCode(barCodes[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.updateBarCodeResult.success(result);
    }
}
