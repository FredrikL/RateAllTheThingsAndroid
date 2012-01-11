package com.tacticalnuclearstrike.rateallthethings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * User: Fredrik / 2011-12-19
 */
public class StartActivity extends RoboActivity {

    final String TAG = "RateAllTheThings";

    @InjectView(R.id.btnScan)
    Button btnScan;
    
    @InjectView(R.id.btnCreateUsers)
    Button btnCreateUsers;

    @Inject
    IService service;

    private ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.start_activity);
        this.SetupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void SetupButtons() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartScan();
            }
        });
        
        btnCreateUsers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
               new CreateUserTask().execute("lol@lol.se");
            }
        });
    }

    private void StartScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.d(TAG, scanResult.toString());
            new GetBarCodeDetailsTask().execute(scanResult.getFormatName(), scanResult.getContents());
        } else {
            Toast.makeText(this, "No barcode found", Toast.LENGTH_LONG).show();
        }
    }
    
    private void getBarCodeDetails(BarCode barcode) {
        if(barcode != null){
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra("BARCODE",barcode);
            this.startActivity(i);
        }
        else
        {
            Toast.makeText(this, "Server problems", Toast.LENGTH_LONG).show();
        }
    }
    
    private class GetBarCodeDetailsTask extends AsyncTask<String, Void, BarCode>{
        @Override
        protected BarCode doInBackground(String... params) {
            return service.lookUpBarCode(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(BarCode result) {
            getBarCodeDetails(result);
        }
    }

    private class CreateUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return service.createUser(strings[0]);
        }
        
        @Override
        protected void onPostExecute(String password){

        }
    }
}