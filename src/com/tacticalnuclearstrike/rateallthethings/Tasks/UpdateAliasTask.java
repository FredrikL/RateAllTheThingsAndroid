package com.tacticalnuclearstrike.rateallthethings.Tasks;

import android.os.AsyncTask;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IUpdateAliasResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.User;

public class UpdateAliasTask extends AsyncTask<User, Void, Boolean> {

    private IService serivce;
    private IUpdateAliasResult updateAliasResult;

    public UpdateAliasTask(IService serivce, IUpdateAliasResult updateAliasResult){

        this.serivce = serivce;
        this.updateAliasResult = updateAliasResult;
    }
    
    @Override
    protected Boolean doInBackground(User... user){
        return this.serivce.updateUser(user[0]);
    }

    @Override
    protected void onPostExecute(Boolean result){
       this.updateAliasResult.updateAliasResult(result);
    }
}
