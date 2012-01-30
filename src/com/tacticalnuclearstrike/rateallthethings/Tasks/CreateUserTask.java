package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.CreateAccountActivity;
import com.tacticalnuclearstrike.rateallthethings.api.IService;

public class CreateUserTask extends AsyncTask<String, Void, String> {

    private CreateAccountActivity createAccountActivity;
    private IService service;

    public CreateUserTask(CreateAccountActivity createAccountActivity, IService service){
        this.createAccountActivity = createAccountActivity;
        this.service = service;
    }

    @Override
    protected String doInBackground(String... strings) {
        return this.service.createUser(strings[0]);
    }

    @Override
    protected void onPostExecute(String password){
        this.createAccountActivity.savePassword(password);
    }
}
