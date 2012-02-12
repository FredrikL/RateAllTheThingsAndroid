package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IGetUser;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.IUpdateAliasResult;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.GetUserTask;
import com.tacticalnuclearstrike.rateallthethings.Tasks.UpdateAliasTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import com.tacticalnuclearstrike.rateallthethings.model.User;
import roboguice.inject.InjectView;

/**
 * Created by IntelliJ IDEA.
 * User: Fredrik
 * Date: 2012-02-12
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
public class AccountActivity extends ActionBarActivity implements IGetUser, IUpdateAliasResult {

    @Inject
    IService service;
    @Inject
    ISettings settings;

    @InjectView(R.id.alias)
    EditText aliasEditText;

    @InjectView(R.id.btnUpdateAccount)
    Button btnUpdateAccount;

    ProgressDialog pd;
    
    User user;

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
        if (user != null)                  {
            this.aliasEditText.setText(user.Alias);
            this.user = user;
        }
        else
            Log.e(this.settings.getTag(), "user is null");
    }

    private void updateAccount() {
        String alias = this.aliasEditText.getText().toString().trim();
        this.user.Alias = alias;
        this.pd = ProgressDialog.show(this, "", getString(R.string.updating_account));
        new UpdateAliasTask(this.service, this).execute(this.user);
    }

    @Override
    public void updateAliasResult(Boolean result) {
        this.pd.dismiss();
        if (result)
            this.finish();
        else {
            Toast.makeText(this, getString(R.string.update_account_error), Toast.LENGTH_LONG).show();
        }
    }
}

