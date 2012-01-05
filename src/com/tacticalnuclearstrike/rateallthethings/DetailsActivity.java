package com.tacticalnuclearstrike.rateallthethings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;

/**
 * User: Fredrik / 2012-01-05
 */
public class DetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarCode barcode = (BarCode)getIntent().getSerializableExtra("BARCODE");
        if(barcode == null)
        {
            Toast.makeText(this, "No barcode passed", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }    
}