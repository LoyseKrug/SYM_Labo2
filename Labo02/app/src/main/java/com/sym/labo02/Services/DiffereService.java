package com.sym.labo02.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sym.labo02.SymComManager;

import java.io.IOException;
import java.util.ArrayList;

public class DiffereService {

    private SymComManager scm;
    private String postRequestURL;
    private boolean requestSent;
    private ArrayList<String> logList;
    private Context context;
    private TextView response = null;
    private TextView logs = null;
    private String entry;




    public DiffereService(){
        scm = SymComManager.getInstance();
        logList = new ArrayList<String>();
        postRequestURL = "http://sym.iict.ch/rest/txt";
        requestSent = false;
    }

    public void sentRequest(String e, Context c, TextView r, TextView l){
        logs = l;
        context = c;
        response = r;
        entry = e;

        response.setText("");
        addLog(entry, logs);

        AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... strings) {
                while(requestSent == false){
                    if(isNetworkAvailable(context)){

                        for(String s : logList){
                            Request req = scm.createRequest(
                                    postRequestURL,
                                    scm.createHeader("text/plain", "text/plain"),
                                    scm.createTextBody(s));
                            try {
                                Response resp = scm.sendRequest(req);
                                response.setText(response.getText().toString() + resp.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        emptyLogList(logs);
                        requestSent = true;
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

        };
        asyncTask.execute();
    }

    private void addLog(String entry, TextView l){
        String text = entry;
        if(!text.isEmpty() && !text.equals("")){
            requestSent = false;
            logList.add(text);
            logs.setText(logs.getText().toString() + "\n" + text);

        }
    }

    private void emptyLogList(TextView l){
        logs.setText("");
        logList.clear();
    }

    private static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return (networkInfo != null && networkInfo.isConnected());
    }

}
