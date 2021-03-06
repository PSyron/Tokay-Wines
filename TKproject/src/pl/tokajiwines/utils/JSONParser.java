
package pl.tokajiwines.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

public class JSONParser {

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, String username, String password) {

        InputStream sInput = null;
        JSONObject sJSONObj = null;
        String sJSONCont = "";

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            StringBuilder authentication = new StringBuilder().append(username).append(":")
                    .append(password);
            String result = Base64.encodeBytes(authentication.toString().getBytes());
            httpPost.setHeader("Authorization", "Basic " + result);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            sInput = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sInput, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            sInput.close();
            sJSONCont = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            sJSONObj = new JSONObject(sJSONCont);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String

        return sJSONObj;

    }

    public InputStream retrieveStream(String url, String username, String password,
            List<NameValuePair> params) {

        // Making HTTP request
        try {
            
            HttpParams my_httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(my_httpParams, 10000);
            HttpConnectionParams.setSoTimeout(my_httpParams, 30000);
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient(my_httpParams);
            HttpPost httpPost = new HttpPost(url);

            StringBuilder authentication = new StringBuilder().append(username).append(":")
                    .append(password);
            String result = Base64.encodeBytes(authentication.toString().getBytes());
            httpPost.setHeader("Authorization", "Basic " + result);

            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            
            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == 200) {

                HttpEntity httpEntity = httpResponse.getEntity();
    
                return httpEntity.getContent();
            }
            

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
        }
         catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
