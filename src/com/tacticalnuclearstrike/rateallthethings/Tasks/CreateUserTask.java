package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.AccountActivity;
import com.tacticalnuclearstrike.rateallthethings.api.IService;

public class CreateUserTask extends AsyncTask<String, Void, String> {

    private AccountActivity accountActivity;
    private IService service;

    public CreateUserTask(AccountActivity accountActivity, IService service){
        this.accountActivity = accountActivity;
        this.service = service;
    }

    @Override
    protected String doInBackground(String... strings) {
        return this.service.createUser(strings[0]);
    }

    @Override
    protected void onPostExecute(String password){
        this.accountActivity.savePassword(password);
    }
}
