/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class is used to format and send a request containing a json body
 *
 * Comments: -
 *
 * Sources: -
 *
 */

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


    /**
     * Empty Constructor
     */
    public JSONService(){
        this("http://sym.iict.ch/rest/json");
    }

    /**
     * JSONService constructor
     * @param url, url to where the request is sent
     */
    protected JSONService(String url){
        scm = SymComManager.getInstance();
        postRequestURL = url;
    }

    /**
     * Method used to send data about a person to the server
     * @param fn, firstName String
     * @param ln lastName String
     * @param mn middleName String
     * @param g gender String
     * @param p phone Number integer
     */
    public void sendData(String fn, String ln, String mn, String g, int p){
        firstName = fn;
        lastName = ln;
        middleName = mn;
        gender = g;
        phoneNumber = p;

        sendJSON();
    }

    /**
     * Method used to send datas to the server
     */
    protected void sendJSON(){
        //An AsyncTask is created to send the request to the server in background
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... strings) {
                JSONObject toSend = formatToJSON();
                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "application/json")
                        .add("accept","application/json");
                Headers h = header.build();
                Request request = scm.createRequest( postRequestURL, h ,createJsonBody(toSend));
                try {
                    Response resp = scm.sendRequest(request);
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

    /**
     * Create a JSON body from a JSONObject
     * @param json, JSONObject
     * @return the requestBody to provide to the request
     */
    protected RequestBody createJsonBody(JSONObject json){
        RequestBody reqBody = RequestBody.create(JSON,json.toString());
        return reqBody;
    }

    /**
     * Create a JSONObject from the class attributes
     * @return JSONObject
     */
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

    /**
     * Set a CommunicationEventListener
     * @param l communication Event Listener
     */
    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }

}
