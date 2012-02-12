package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.Tasks.CreateUserTask;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import roboguice.inject.InjectView;

/**
 * User: Fredrik / 2012-01-15
 */
public class CreateAccountActivity extends ActionBarActivity {
    @InjectView(R.id.btnCreateAccount)
    Button btnCreateUsers;

    @InjectView(R.id.email)
    EditText email;

    @Inject
    IService service;

    @Inject
    ISettings settings;

    ProgressDialog pd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.create_account_activity);
        this.SetupButtons();
    }

    private void SetupButtons() {
        btnCreateUsers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startCreateUser();
            }
        });
    }

    private void startCreateUser() {
        String email = this.email.getText().toString();
        if (email.length() > 3) {
            this.pd = ProgressDialog.show(this, "", "Creating Account...");
            new CreateUserTask(this, service).execute(email);
        } else {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
        }
    }

    public void savePassword(String password) {
        this.pd.dismiss();
        if (password != null) {
            settings.setEmail(this.email.getText().toString());
            settings.setPassword(password);
        } else {
            // error management
        }
        this.finish();
    }
}
