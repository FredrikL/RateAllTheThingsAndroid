package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetUser;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.User;

public class GetUserTask extends AsyncTask<Void,Void, User> {

    private IService service;
    private IGetUser getUser;

    public GetUserTask(IService service, IGetUser getUser){
        this.service = service;
        this.getUser = getUser;
    }

    @Override
    protected User doInBackground(Void... v) {
        return this.service.getCurrentUser();
    }

    @Override
    protected void onPostExecute(User user){
        this.getUser.getUserResult(user);
    }
}
