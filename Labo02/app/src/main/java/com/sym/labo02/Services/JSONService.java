package com.sym.labo02.Services;


import android.os.AsyncTask;
import com.squareup.okhttp.*;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private SymComManager scm;
    private String postRequestURL;

    private String firstName = null;
    private String lastName = null;
    private String middleName = null;
    private String gender = null;
    private int phoneNumber;



    public JSONService(){
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/json";
    }

    public void sendJSON(String fn, String ln, String mn, String g, int p){
        firstName = fn;
        lastName = ln;
        middleName = mn;
        gender = g;
        phoneNumber = p;

        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... strings) {
                JSONObject toSend = formatToJSON();
                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "application/json")
                        .add("accept","application/json");
                Headers h = header.build();
                Request request = scm.createRequest( postRequestURL, h ,createJsonBody(toSend));
                Response resp = scm.sendRequest(request);
                try {
                    return resp.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String resp) {
                super.onPostExecute(resp);
                scm.getCommunicationEventListener().handleServerResponse(resp);
            }
        };
        asyncTask.execute();
    }

    protected RequestBody createJsonBody(JSONObject json){
        RequestBody reqBody = RequestBody.create(JSON,json.toString());
        return reqBody;
    }

    protected JSONObject formatToJSON() {

        // create your json here
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("middleName", middleName);
            jsonObject.put("gender", gender);
            jsonObject.put("phone", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }

}
