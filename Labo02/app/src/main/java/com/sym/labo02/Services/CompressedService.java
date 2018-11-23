/**
 * Source: https://www.programcreek.com/java-api-examples/?class=java.util.zip.DeflaterOutputStream&method=finish
 * Exemple 18
 */

package com.sym.labo02.Services;

import android.os.AsyncTask;
import com.squareup.okhttp.*;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class CompressedService{

    private SymComManager scm;
    private String postRequestURL;
    private String request;


    public CompressedService(){
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/txt";
    }


    public void sendRequest(String req) throws Exception{

        request = req;

        AsyncTask<String, Void, byte[]> asyncTask = new AsyncTask<String, Void, byte[]>(){

            @Override
            protected byte[] doInBackground(String... strings) {

                RequestBody rb = RequestBody.create(MediaType.parse("text/plain"), compressData(request));

                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "application/json")
                        .add("accept", "application/json")
                        .add("XNetwork" ,"CSD")
                        .add("X-Content-Encoding", "defalte");
                Request req = scm.createRequest(
                        postRequestURL,
                        scm.createHeader("text/plain", "text/plain"),
                        rb);
                try {
                    Response resp = scm.sendRequest(req);
                    return resp.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(byte[] resp) {
                super.onPostExecute(resp);

                scm.getCommunicationEventListener().handleServerResponse(decompressData(resp));
                //scm.getCommunicationEventListener().handleServerResponse(resp.toString());
            }
        };
        asyncTask.execute();
    }

    public void setCommunicationEventListener (CommunicationEventListener l){
        scm.setCommunicationEventListener(l);
    }


    public byte[] compressData(String toCompress){
        ByteArrayOutputStream ba = new ByteArrayOutputStream(toCompress.length()); // * 2 + 20
        DeflaterOutputStream out = new DeflaterOutputStream(ba);
        byte[] bytes = toCompress.getBytes(Charset.forName("UTF-8"));
        try {
            out.write(bytes);
            out.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] compressed = ba.toByteArray();
        return compressed;
    }

    public static String decompressData(final byte[] bytes) {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buf = new byte[bytes.length];
        try (InflaterInputStream iis = new InflaterInputStream(bais)) {
            int count = iis.read(buf);
            while (count != -1) {
                baos.write(buf, 0, count);
                count = iis.read(buf);
            }
            return new String(baos.toByteArray(), Charset.forName("UTF-8"));
        } catch (final Exception e) {
            return null;
        }
    }






}
