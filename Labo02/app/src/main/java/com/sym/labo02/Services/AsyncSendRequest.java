package com.sym.labo02.Services;

import android.os.AsyncTask;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;

import java.io.IOException;

public class AsyncSendRequest {

    private SymComManager scm;
    private String postRequestURL;
    private String request;

    public AsyncSendRequest(){
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/txt";
    }


    //TODO: lier les champs du layout
    public void sendRequest(String req) throws Exception{

        request = req;

        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                Request req = scm.createRequest(
                        postRequestURL,
                        scm.createHeader("text/plain", "text/plain"),
                        scm.createTextBody(request));
                try {
                    Response resp = scm.sendRequest(req);
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

    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }
}
