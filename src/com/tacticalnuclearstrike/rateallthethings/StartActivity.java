package com.tacticalnuclearstrike.rateallthethings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tacticalnuclearstrike.rateallthethings.Activities.AccountActivity;
import com.tacticalnuclearstrike.rateallthethings.Activities.DetailsActivity;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.ISettings;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import roboguice.inject.InjectView;

/**
 * User: Fredrik / 2011-12-19
 */
public class StartActivity extends ActionBarActivity {
    @InjectView(R.id.btnScan)
    Button btnScan;

    @Inject
    IService service;

    @Inject
    ISettings settings;

    ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.start_activity);
        this.SetupButtons();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.startAccountActivityIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account:
                startAccountActivity();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAccountActivityIfNeeded() {
        if (!this.settings.hasEmailAndPassword()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.start_account_activity)).setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startAccountActivity();
                }
            });
            builder.show();
        }
    }

    private void startAccountActivity() {
        Intent i = new Intent(this, AccountActivity.class);
        this.startActivity(i);
    }

    private void SetupButtons() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartScan();
            }
        });
    }

    private void StartScan() {
        if(!this.settings.hasEmailAndPassword())
            return;
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.d(this.settings.getTag(), scanResult.toString());
            this.pd = ProgressDialog.show(this, "", "Downloading details...");
            new GetBarCodeDetailsTask().execute(scanResult.getFormatName(), scanResult.getContents());
        } else {
            Toast.makeText(this, "No barcode found", Toast.LENGTH_LONG).show();
        }
    }

    private void getBarCodeDetails(BarCode barcode) {
        this.pd.dismiss();
        if (barcode != null) {
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra("BARCODE", barcode);
            this.startActivity(i);
        } else {
            Toast.makeText(this, "Server problems", Toast.LENGTH_LONG).show();
        }
    }

    private class GetBarCodeDetailsTask extends AsyncTask<String, Void, BarCode> {
        @Override
        protected BarCode doInBackground(String... params) {
            return service.lookUpBarCode(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(BarCode result) {
            getBarCodeDetails(result);
        }
    }
}