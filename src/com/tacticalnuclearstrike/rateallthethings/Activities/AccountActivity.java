package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.CreateUserTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * User: Fredrik / 2012-01-15
 */
public class AccountActivity extends RoboActivity {
    @InjectView(R.id.btnCreateAccount)
    Button btnCreateUsers;
    
    @InjectView(R.id.email)
    EditText email;

    @Inject
    IService service;

    @Inject
    ISettings settings;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.account_activity);
        this.SetupButtons();
    }

    private void SetupButtons() {
        btnCreateUsers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                  startCreateUser();
            }
        });
    }

    private void startCreateUser(){
        String email = this.email.getText().toString();
        new CreateUserTask(this, service).execute(email);
    }

    public void savePassword(String password) {
        if(password != null)
            settings.setPassword(password);
        else {
            // error management
        }
        this.finish();
    }
}
