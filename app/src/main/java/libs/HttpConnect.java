package libs;

/**
 * Created by ariflaksito on 10/19/17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpConnect {

    private HttpClient httpclient = new DefaultHttpClient();
    private HttpPost httppost;
    private HttpGet httpget;
    private HttpPut httpput;
    private HttpDelete httpdel;
    private String json;
    private String out;

    public HttpConnect(String url, String json) {
        httppost = new HttpPost(url);
        httpput = new HttpPut(url);
        this.json = json;
    }

    public HttpConnect(String url) {
        httpget = new HttpGet(url);
        httpdel = new HttpDelete(url);
    }


    // digunakan untuk method POST
    public boolean postData() {
        InputStream inputStream = null;
        try{
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httppost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httppost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                out = convertInputStreamToString(inputStream);
            else
                out = null;

            return true;

        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    // digunakan untuk method PUT
    public boolean putData() {
        InputStream inputStream = null;
        try{
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpput.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpput.setHeader("Accept", "application/json");
            httpput.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpput);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                out = convertInputStreamToString(inputStream);
            else
                out = null;

            return true;

        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    // digunakan untuk method GET
    public boolean getData() {
        HttpClient Client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            out = Client.execute(httpget, responseHandler);
            return true;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delData(){
        HttpClient Client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            out = Client.execute(httpdel, responseHandler);
            return true;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getOutput() {
        return out;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}