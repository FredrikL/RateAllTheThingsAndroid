package com.tacticalnuclearstrike.rateallthethings.api;

import android.util.Base64;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;
import com.tacticalnuclearstrike.rateallthethings.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Service implements IService{
    final String URL = "http://rateallthethings.apphb.com"; // fallback url
    private ISettings settings;

    @Inject
    public Service(ISettings settings){
        this.settings = settings;
    }

    private void addAuthHeader(HttpRequestBase request) {
        String s = this.settings.getEmail() + ":" + this.settings.getPassword();
        request.addHeader("Authorization", "Basic " + Base64.encodeToString(s.getBytes(), Base64.NO_WRAP));
    }

    private HttpGet getHttpGetWithBasicAuth(String url){
        HttpGet httpGet = new HttpGet(url);
        addAuthHeader(httpGet);
        return httpGet;
    }
    
    private HttpPost getHttpPostWithBasicAuth(String url) {
        HttpPost httpPost = new HttpPost(url);
        addAuthHeader(httpPost);
        return httpPost;
    }
    
    public BarCode lookUpBarCode(String format, String code) {
        try {
            String url = URL + "/BarCode/" + URLEncoder.encode(format, "utf-8") + "/" + URLEncoder.encode(code,"utf-8");
            Log.d(this.settings.getTag(), url);
            HttpGet httpGet = this.getHttpGetWithBasicAuth(url);

            Reader reader = executeRequestAndReturnAsReader(httpGet);

            Type barCodeArrayType = new TypeToken<ArrayList<BarCode>>(){}.getType();
            List<BarCode> items = new Gson().fromJson(reader, barCodeArrayType);
            return items.get(0);
        } catch (Exception ioe) {
            Log.e(this.settings.getTag(), ioe.getMessage(), ioe);
        }
        return null;
    }

    private Reader executeRequestAndReturnAsReader(HttpRequestBase httpGet) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpGet);
        Log.d(this.settings.getTag(), "Response is " + response.getStatusLine().getStatusCode());
        InputStream content = response.getEntity().getContent();
        return new InputStreamReader(content);
    }

    public String createUser(String email) {
        String password = null;
        try{
            String url = URL + "/User/?email=" + URLEncoder.encode(email, "utf-8");
            Log.d(this.settings.getTag(), "Url is: " + url);
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

    public BarCode updateBarCode(BarCode barCode) {
        try{
            String url = URL + "/BarCode/" ;
            HttpPost post = this.getHttpPostWithBasicAuth(url);

            String content = new Gson().toJson(barCode);

            post.setEntity(new StringEntity(content, "UTF8"));
            post.setHeader("Content-type", "application/json");
            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);

            Log.d(this.settings.getTag(), "Response is " + resp.getStatusLine().getStatusCode());
            Type barCodeArrayType = new TypeToken<ArrayList<BarCode>>(){}.getType();
            InputStream responseContent = resp.getEntity().getContent();
            InputStreamReader reader= new InputStreamReader(responseContent);
            List<BarCode> items = new Gson().fromJson(reader, barCodeArrayType);
            return items.get(0);

        } catch (Exception ioe) {
            Log.e(this.settings.getTag(), ioe.getMessage(), ioe);
            return null;
        }
    }

    public Boolean addComment(Comment comment) {
        try{
            String url = URL + "/Comment/" + comment.BarCodeId;
            HttpPost post = this.getHttpPostWithBasicAuth(url);

            String content = new Gson().toJson(comment);

            post.setEntity(new StringEntity(content, "UTF8"));
            post.setHeader("Content-type", "application/json");
            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);

            Log.d(this.settings.getTag(), "Response is " + resp.getStatusLine().getStatusCode());
        } catch (Exception e) {
            Log.e(this.settings.getTag(), e.getMessage(), e);
            return false;
        }
        return true;
    }

    public List<Comment> getCommentsForBarCode(long barCodeId) {
        List<Comment> retVal;
        try{
            String url = URL + "/Comment/" + barCodeId;
            HttpGet httpGet = this.getHttpGetWithBasicAuth(url);

            Reader reader = executeRequestAndReturnAsReader(httpGet);

            Type type = new TypeToken<ArrayList<Comment>>(){}.getType();
            retVal = new Gson().fromJson(reader, type);
        } catch(Exception e) {
            Log.e(this.settings.getTag(), e.getMessage(), e);
            return new ArrayList<Comment>();
        }
        return retVal;
    }

    public BarCode rateBarCode(long barCodeId, int rating) {
        try{
            String url = URL + "/BarCode/Rate/" + barCodeId + "/" + rating;
            Log.d(this.settings.getTag(), url);
            HttpPost post = this.getHttpPostWithBasicAuth(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);

            Log.d(this.settings.getTag(), "Response is " + resp.getStatusLine().getStatusCode());

            Type barCodeArrayType = new TypeToken<ArrayList<BarCode>>(){}.getType();
            InputStream content = resp.getEntity().getContent();
            InputStreamReader reader= new InputStreamReader(content);
            List<BarCode> items = new Gson().fromJson(reader, barCodeArrayType);
            return items.get(0);
        } catch (Exception e) {
            Log.e(this.settings.getTag(), e.getMessage(), e);
            return null;
        }
    }

    public Boolean testCredentials(String username, String password) {
        try {
            String url = URL + "/Auth/";

            HttpGet httpGet = new HttpGet(url);
            String s = username + ":" + password;
            httpGet.addHeader("Authorization", "Basic " + Base64.encodeToString(s.getBytes(), Base64.NO_WRAP));

            Reader reader = executeRequestAndReturnAsReader(httpGet);

            Type barCodeArrayType = new TypeToken<ArrayList<Boolean>>(){}.getType();
            List<Boolean> items = new Gson().fromJson(reader, barCodeArrayType);
            return items.get(0);
        } catch (Exception ioe) {
            Log.e(this.settings.getTag(), ioe.getMessage(), ioe);
        }
        return false;
    }

    @Override
    public User getCurrentUser() {
        try{
            String url = URL + "/Account/";
            Log.d(this.settings.getTag(), url);
            HttpGet post = this.getHttpGetWithBasicAuth(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);

            Log.d(this.settings.getTag(), "Response is " + resp.getStatusLine().getStatusCode());

            Type type = new TypeToken<User>(){}.getType();
            InputStream content = resp.getEntity().getContent();
            InputStreamReader reader= new InputStreamReader(content);
            return new Gson().fromJson(reader, type);
        } catch (Exception e) {
            Log.e(this.settings.getTag(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean updateUser(User user) {
        try{
            String url = URL + "/Account";
            HttpPost post = this.getHttpPostWithBasicAuth(url);

            String content = new Gson().toJson(user);

            post.setEntity(new StringEntity(content, "UTF8"));
            post.setHeader("Content-type", "application/json");
            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);

            Log.d(this.settings.getTag(), "Response is " + resp.getStatusLine().getStatusCode());
        } catch (Exception e) {
            Log.e(this.settings.getTag(), e.getMessage(), e);
            return false;
        }
        return true;
    }

    private class CreateUserResponse {
        public String password;
    }
}
