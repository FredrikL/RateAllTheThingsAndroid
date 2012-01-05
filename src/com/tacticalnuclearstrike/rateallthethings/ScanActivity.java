package com.tacticalnuclearstrike.rateallthethings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * User: Fredrik / 2011-12-19
 */
public class ScanActivity extends RoboActivity {

    final String TAG = "RateAllTheThings";
    final String URL = "http://rateallthethings.com";

    @InjectView(R.id.btnScan)
    Button btnScan;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.scan_activity);
        this.SetupButtons();
    }

    private void SetupButtons() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartScan();
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
            // handle scan result
            Log.d(TAG, scanResult.toString());
            BarCode code = this.lookUpBarCode(scanResult.getFormatName(), scanResult.getContents());
            // launch details activity
            if(code != null){
                // activity
                Intent i = new Intent(this, DetailsActivity.class);
                i.putExtra("barcode",code);
                this.startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Server problems", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No barcode found", Toast.LENGTH_LONG).show();
        }
    }

    private BarCode lookUpBarCode(String format, String code) {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(new HttpGet(URL + "/Details?format=" + format + "&code=" + code));
            InputStream content = response.getEntity().getContent();
            Reader reader = new InputStreamReader(content);

            return new Gson().fromJson(reader, BarCode.class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void SendJson(JSONObject json) {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        try {
            HttpPost post = new HttpPost(URL);
            StringEntity se = new StringEntity("JSON: " + json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);
            /*Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}