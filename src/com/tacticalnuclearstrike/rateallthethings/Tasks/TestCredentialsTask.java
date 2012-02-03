package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.ITestCredentialsTaskCallBack;
import com.tacticalnuclearstrike.rateallthethings.api.IService;

public class TestCredentialsTask extends AsyncTask<String, Void, Boolean> {

    private IService service;
    private ITestCredentialsTaskCallBack testCredentialsTaskCallBack;

    public TestCredentialsTask(IService service, ITestCredentialsTaskCallBack testCredentialsTaskCallBack){
        this.service = service;
        this.testCredentialsTaskCallBack = testCredentialsTaskCallBack;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if(strings[0].isEmpty() || strings[1].isEmpty())
            return false;
        return this.service.testCredentials(strings[0], strings[1]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.testCredentialsTaskCallBack.testCredentialsResult(result);
    }
}
