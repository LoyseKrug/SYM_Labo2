package com.sym.labo02;

public class AsyncSendRequest {

    SymComManager scm;

    AsyncSendRequest(){
        scm = SymComManager.getInstance();
    }


    //TODO: lier les champs du layout
    public String sendRequest(String request, String url) throws Exception{
        return null; // TODO
    }

    public void setCommunicationEventListener (CommunicationEventListener l){

    }
}
