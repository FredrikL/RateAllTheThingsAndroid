package com.tacticalnuclearstrike.rateallthethings.api;

import android.util.Base64;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Service implements IService{
    final String URL = "http://rateallthethings.com";
    private ISettings settings;

    @Inject
    public Service(ISettings settings){
        this.settings = settings;
    }

    private HttpGet getHttpGetWithBasicAuth(String url){
        HttpGet httpGet = new HttpGet(url);

        String s = this.settings.getEmail() + ":" + this.settings.getPassword();
        httpGet.addHeader("Authorization", "Basic " + Base64.encodeToString(s.getBytes(), Base64.NO_WRAP));
        return httpGet;
    }
    
    public BarCode lookUpBarCode(String format, String code) {
        try {
            String url = URL + "/BarCode/" + format + "/" + code;
            HttpGet httpGet = this.getHttpGetWithBasicAuth(url);

            Reader reader = executeRequestAndReturnAsReader(httpGet);

            Type barCodeArrayType = new TypeToken<ArrayList<BarCode>>(){}.getType();
            List<BarCode> items = new Gson().fromJson(reader, barCodeArrayType);
            return items.get(0);
        } catch (IOException ioe) {
            Log.e(this.settings.getTag(), ioe.getMessage(), ioe);
        }
        return null;
    }

    private Reader executeRequestAndReturnAsReader(HttpRequestBase httpGet) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpGet);
        InputStream content = response.getEntity().getContent();
        return new InputStreamReader(content);
    }

    public String createUser(String email) {
        String password = null;
        try{
            String url = URL + "/User/?email=" + email;
            HttpPost post = new HttpPost(url);

            Reader reader = executeRequestAndReturnAsReader(post);

            Type type = new TypeToken<ArrayList<CreateUserResponse>>(){}.getType();
            List<CreateUserResponse> items = new Gson().fromJson(reader, type);
            if(items.size() == 1)
            {
                password=  items.get(0).password;
            }

        } catch (Exception ioe) {
            Log.e(this.settings.getTag(), ioe.getMessage(), ioe);
        }
        return password;
    }
    
    private class CreateUserResponse {
        public String password;
    }
}
