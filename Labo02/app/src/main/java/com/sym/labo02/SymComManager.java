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

import com.squareup.okhttp.*;
import java.io.IOException;

public class SymComManager {

    public static SymComManager instance = null;
    private CommunicationEventListener cel = null;
    private OkHttpClient client;

    /**
     * Constructor of the SymComManager, creating the okHttp client
     */
    private SymComManager(){
        client = new OkHttpClient();
    }

    /**
     * Method returning the unique instance of SymComManager, creating it if it doesn't exist yet
     * @return the instance of SymComManager
     */
    public static SymComManager getInstance(){
        if(instance == null){
            instance = new SymComManager();
        }
        return instance;
    }

    /**
     * Set the class communication event listener
     * @param cel, communication Event Listener
     */
    public void setCommunicationEventListener(CommunicationEventListener cel){
        this.cel = cel;
    }

    /**
     * Method used to create basic headers for the request to send
     * @param textContent, content of the text-content header
     * @param accept, content of the accept header
     * @return an entity containing all the headers
     */
    public Headers createHeader(String textContent, String accept){
        Headers.Builder header = new Headers.Builder();
        header.add("content-type", textContent)
                .add("accept", accept);
        return header.build();
    }

    /**
     * Method constructing the request with all the elements it contains
     * @param url
     * @param headers
     * @param rb
     * @return the request constructed
     */
    public Request createRequest(String url, Headers headers, RequestBody rb){
        //creating the request
        Request request = new Request.Builder()
                .url(url)
                .post(rb)
                .headers(headers)
                .build();

        return request;
    }

    /**
     * Method used to create a RequestBody
     * @param req
     * @return
     */
    public RequestBody createTextBody(String req){
        RequestBody reqBody = new FormEncodingBuilder()
                .add("text", req)
                .build();
        return  reqBody;
    }

    /**
     * Send a formated request to the server
     * @param request, the request to send
     * @return the response comming from the server
     * @throws IOException
     */
    public Response sendRequest(Request request) throws IOException{
        return client.newCall(request).execute();
    }

    /**
     * communicationEventListenerGetter
     * @return the communication Event Listener
     */
    public CommunicationEventListener getCommunicationEventListener(){
        return cel;
    }

}
