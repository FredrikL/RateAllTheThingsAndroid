package com.tacticalnuclearstrike.rateallthethings.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.tacticalnuclearstrike.rateallthethings.R;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * User: Fredrik / 2012-01-05
 */
public class DetailsActivity extends RoboActivity {
    @InjectView(R.id.tvFormat)
    TextView format;
    @InjectView(R.id.tvCode)
    TextView code;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.details_activity);
        

        BarCode barcode = (BarCode)getIntent().getSerializableExtra("BARCODE");
        if(barcode == null)
        {
            Toast.makeText(this, "No barcode passed", Toast.LENGTH_LONG).show();
            this.finish();
        }  else {
            Log.d("RateAllTheThings", "Not null");
        format.setText("Format: " + barcode.Format);
        code.setText("Code: " + barcode.Code);
        }
    }    
}