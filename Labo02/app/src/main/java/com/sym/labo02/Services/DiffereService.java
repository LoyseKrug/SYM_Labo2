/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class offer methods to send a request to the server and keep a list of the
 * request waiting for a connexion to the network. The list of waiting requests is stocked
 * in an ArrayList and shown to the user through a TextView
 *
 * Comments: -
 *
 * Sources: -
 *
 */

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

    /**
     * Empty Constructor
     */
    public DiffereService(){
        scm = SymComManager.getInstance();
        logList = new ArrayList<String>();
        postRequestURL = "http://sym.iict.ch/rest/txt";
        requestSent = false;
    }

    /**
     * Method trying to send a request to the server
     * @param e, entry containting the text of the request to send
     * @param c, context of the activité
     * @param r, textView that will contain the server's answer
     * @param l, textView that will contain all the entered logs from the last connexion to the server
     */
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

    /**
     * add a log to the list of logs waiting for a connexion to the server
     * @param entry, text of the request added to the log list
     * @param l, TextView where the logs are displayed
     */
    private void addLog(String entry, TextView l){
        String text = entry;
        if(!text.isEmpty() && !text.equals("")){
            requestSent = false;
            logList.add(text);
            logs.setText(logs.getText().toString() + "\n" + text);

        }
    }

    /**
     * Empty the list of logs
     * @param l, update the textView displaying the list of logs
     */
    private void emptyLogList(TextView l){
        logs.setText("");
        logList.clear();
    }


    /**
     * Test the connexion to the server
     * @param context Context of the current activity
     * @return wether the connexion was successful or not
     */
    private static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
