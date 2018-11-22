package com.sym.labo02.Services;

import org.json.JSONException;
import org.json.JSONObject;

public class GraphQLService extends JSONService {
    private String query = null;

    public GraphQLService(){
        super("http://sym.iict.ch/api/graphql");
    }


    public void sendData(String query){
        this.query = query;
        sendJSON();
    }

    @Override
    protected JSONObject formatToJSON() {
        // create your json here
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
