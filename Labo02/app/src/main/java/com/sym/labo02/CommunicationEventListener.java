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

import android.widget.TextView;

public interface CommunicationEventListener {

    public boolean handleServerResponse(String response);

}
