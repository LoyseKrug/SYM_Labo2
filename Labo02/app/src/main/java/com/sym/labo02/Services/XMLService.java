/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class is used to format and send a request containing a xml body
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02.Services;

import android.os.AsyncTask;
import android.util.Log;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.IOException;

public class XMLService{

    public static final MediaType XML
            = MediaType.parse("application/json; charset=utf-8");

    private SymComManager scm;
    private String postRequestURL;

    private String firstName = null;
    private String lastName = null;
    private String middleName = null;
    private String gender = null;
    private String phoneNumber = null;


    /**
     * Constructor
     */
    public XMLService(){
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/xml";
    }

    /**
     * Method used to send an XML contining informations about a person to the server
     * @param fn firstName String
     * @param ln lastName String
     * @param mn middleName String
     * @param g gender String
     * @param p phone number String
     */
    public void sendXML(String fn, String ln, String mn, String g, String p){
        firstName = fn;
        lastName = ln;
        middleName = mn;
        gender = g;
        phoneNumber = p;

        //An AsyncTask is created to send the request to the server
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... strings) {
                Document toSend = formatToXML();
                XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
                String xmlString = outputter.outputString(toSend);
                Log.i("XML" , xmlString);
                RequestBody body = RequestBody.create(MediaType.parse("application/xml"), xmlString);
                Request request = scm.createRequest(
                        postRequestURL,
                        scm.createHeader("application/xml", "application/xml") ,
                        body);
                try {
                    Response resp = scm.sendRequest(request);
                    return resp.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Error in request";
            }

            @Override
            protected void onPostExecute(String resp) {
                super.onPostExecute(resp);
                scm.getCommunicationEventListener().handleServerResponse(resp);
            }
        };
        asyncTask.execute();
    }

    /**
     * Format the given datas to XML
     * @return an XML Document
     */
    private Document formatToXML() {
        Document xmlDoc = new Document();
        Element directory = new Element("directory");
        xmlDoc.setRootElement(directory);
        xmlDoc.setDocType(new DocType("directory","http://sym.iict.ch/directory.dtd"));

        Element person = new Element("person");

        Element name = new Element("name");
        name.addContent(lastName);

        Element fn = new Element("firstname");
        fn.addContent(firstName);

        Element mn = new Element("middlename");
        mn.addContent(middleName);

        Element g = new Element("gender");
        g.addContent(gender);

        Element p = new Element("phone");
        p.addContent(phoneNumber);
        p.setAttribute("type", "home");

        person.addContent(name);
        person.addContent(fn);
        person.addContent(mn);
        person.addContent(g);
        person.addContent(p);
        directory.setContent(person);

        return xmlDoc;
    }

    /**
     * Set the communication Event Listener
     * @param l Communication Event Listener
     */
    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }
}
