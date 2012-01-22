package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IRateBarCodeResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;

public class RateBarCodeTask extends AsyncTask<Integer, Void, BarCode> {

    private IService service;
    private IRateBarCodeResult rateBarCodeResult;
    private long barCodeId;

    public RateBarCodeTask(IService service, IRateBarCodeResult rateBarCodeResult, long barCodeId) {

        this.service = service;
        this.rateBarCodeResult = rateBarCodeResult;
        this.barCodeId = barCodeId;
    }
    
    @Override
    protected BarCode doInBackground(Integer... rating) {
        return this.service.rateBarCode(barCodeId, rating[0]);
    }

    @Override
    protected void onPostExecute(BarCode result) {
        this.rateBarCodeResult.ratingSuccess(result);
    } 
}
