/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date:
 *
 * Objective: This class is a singleton used to make asynchrone requests to the server
 *
 * Comments: The class must be a singleton to avoid creating many instances of okHttpClient
 *
 * Sources: http://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
 */

package com.sym.labo02;

import com.squareup.okhttp.*;

import java.io.IOException;

public class SymComManager {

    public static SymComManager instance = null;
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

    void setCommunicationEventListener(CommunicationEventListener cel){
        this.cel = cel;
    }

    /**
     * Create and send a post an asynchronous request to the server
     * @param url, server address
     * @param req, string, content of the post request
     */
    void sendRequest(String url, final String req){
        //creating the body of the request
        RequestBody postBody = new FormEncodingBuilder()
                .add("text", req)
                .build();

        //creating the request
        Request request = new Request.Builder()
                .url(url)
                .post(postBody)
                .addHeader("Content-Type", "text/plain")
                .build();

        //sending the request. We enqueue a callback in order to treat separatly the server answer
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response res) throws IOException {
                //if the request leads to a success, the Commnunication Event Listener handle the answer
                cel.handleServerResponse(res.body().string());
            }
        });
    }
}
