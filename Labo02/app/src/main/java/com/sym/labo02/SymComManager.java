/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date:
 *
 * Objective: This class is a singleton used to make asynchrone requests to the server
 *
 * Comments: The class must be a singleton to avoid creating many instances of okHttpClient
 *
 * Sources:
 * http://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
 * https://stackoverflow.com/questions/40389163/check-internet-connection-okhttp
 */

package com.sym.labo02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.squareup.okhttp.*;
import org.json.JSONObject;

import java.io.IOException;

public class SymComManager {

    public static SymComManager instance = null;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private CommunicationEventListener cel = null;
    private OkHttpClient client;


    private SymComManager(){
        client = new OkHttpClient();
    }

    public static SymComManager getInstance(){
        if(instance == null){
            instance = new SymComManager();
        }
        return instance;
    }

    public void setCommunicationEventListener(CommunicationEventListener cel){
        this.cel = cel;
    }

    public Headers createHeader(String textContent, String accept){
        Headers.Builder header = new Headers.Builder();
        header.add("content-type", textContent)
                .add("accept", accept);
        return header.build();
    }

    public Request createRequest(String url, Headers headers, RequestBody rb){
        //creating the request
        Request request = new Request.Builder()
                .url(url)
                .post(rb)
                .headers(headers)
                .build();

        return request;
    }

    public RequestBody createTextBody(String req){
        RequestBody reqBody = new FormEncodingBuilder()
                .add("text", req)
                .build();
        return  reqBody;
    }

    public RequestBody createJsonBody(JSONObject json){
        RequestBody reqBody = RequestBody.create(JSON,json.toString());
        return reqBody;
    }

    /**
     */
    public Response sendRequest(Request request){

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CommunicationEventListener getCommunicationEventListener(){
        return cel;
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return (networkInfo != null && networkInfo.isConnected());
    }
}
