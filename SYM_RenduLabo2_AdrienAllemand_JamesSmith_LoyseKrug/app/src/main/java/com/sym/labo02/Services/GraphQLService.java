/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class inherits from JSONService and provides methods to generate a
 * JSONObject used for the GraphQL request.
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02.Services;

import org.json.JSONException;
import org.json.JSONObject;

public class GraphQLService extends JSONService {
    private String query = null;

    /**
     * Empty constructor
     */
    public GraphQLService(){
        super("http://sym.iict.ch/api/graphql");
    }

    /**
     * Method to send the query to the server
     * @param query
     */
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
