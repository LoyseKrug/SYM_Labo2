/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class offer methods to send a request to the server using an AsyncTask
 * it also provides a method used to set a CommunicationEventListener which will be called
 * at the end of the communication with the server
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02.Services;

import android.os.AsyncTask;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;

import java.io.IOException;

public class AsyncSendRequest {

    private SymComManager scm;
    private String postRequestURL;
    private String request;

    /**
     * Empty Contructor
     */
    public AsyncSendRequest(){
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/txt";
    }

    /**
     * Send request to the server
     * @param req, String text to send
     * @throws Exception
     */
    public void sendRequest(String req) throws Exception{

        request = req;

        //We create an AsyncTask to send the request to the server in Background
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "plain/text")
                        .add("accept", "text/plain");
                        //.add("X-Network", "CSD");
                Headers h = header.build();
                Request req = scm.createRequest(
                        postRequestURL,
                        h,
                        scm.createTextBody(request));
                try {
                    //long beforeRequest = System.currentTimeMillis();
                    Response resp = scm.sendRequest(req);
                    //long afterRequest = System.currentTimeMillis();
                    //System.out.println("---------------------------------------------------Request time = " + (afterRequest - beforeRequest) + " ms");
                    return resp.body().string();
                } catch (IOException | RuntimeException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
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
     * Set the Communication Event Listener
     * @param l
     */
    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }
}
