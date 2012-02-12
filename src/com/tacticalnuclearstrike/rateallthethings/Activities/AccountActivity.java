package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetUser;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GetUserTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.User;
import org.w3c.dom.UserDataHandler;
import roboguice.inject.InjectView;

/**
 * Created by IntelliJ IDEA.
 * User: Fredrik
 * Date: 2012-02-12
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
public class AccountActivity extends ActionBarActivity implements IGetUser {
    
    @Inject
    IService service;
    
    @InjectView(R.id.alias)
    EditText alias;
    
    @InjectView(R.id.btnUpdateAccount)
    Button btnUpdateAccount;

    ProgressDialog pd;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.account_activity);
        
        this.btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount();
            }
        });
        
        this.pd = ProgressDialog.show(this, "", getString(R.string.account_details));
        new GetUserTask(this.service, this).execute();
    }

    @Override
    public void getUserResult(User user) {
        this.pd.dismiss();
        this.alias.setText(user.Alias);
    }

    private void updateAccount() {

    }
}

