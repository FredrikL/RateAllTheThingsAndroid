package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces.ITestCredentialsTaskCallBack;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.TestCredentialsTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import roboguice.inject.InjectView;

/**
 * Created by IntelliJ IDEA.
 * User: Fredrik
 * Date: 2012-01-30
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends ActionBarActivity implements ITestCredentialsTaskCallBack {
    @InjectView(R.id.login_email)
    EditText loginEmail;
    @InjectView(R.id.login_password)
    EditText loginPassword;

    @InjectView(R.id.btnSignIn)
    Button btnSignIn;
    
    @Inject
    IService service;
    @Inject
    ISettings settings;
    
    private ProgressDialog pd;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.login_activity);
        
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });        
    }

    private void signIn() {
        String username = this.loginEmail.getText().toString().trim();
        String password = this.loginPassword.getText().toString().trim();
        this.pd = ProgressDialog.show(this, "","Signing in...");
        new TestCredentialsTask(this.service, this).execute(username, password);
    }

    @Override
    public void testCredentialsResult(Boolean result) {
        this.pd.dismiss();
        this.settings.setEmail(this.loginEmail.getText().toString().trim());
        this.settings.setPassword(this.loginPassword.getText().toString().trim());
    }

}
