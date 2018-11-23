/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date:
 *
 * Objective: Interface to handle communication Event listener
 *
 * Comments: -
 *
 * Sources: -
 */

package com.sym.labo02;

public interface CommunicationEventListener {

    /**
     * Method implementing the action taken when the server answer is recieved
     * @param response, response recieved from the server
     * @return whether the server has answered or not
     */
    boolean handleServerResponse(String response);

}
