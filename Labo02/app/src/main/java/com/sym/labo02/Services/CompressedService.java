/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: This class offer methods to send a compressed request to the server using an AsyncTask
 * it also provide methods to compress and decompress a String
 *
 * Comments: -
 *
 * Source: https://www.programcreek.com/java-api-examples/java.util.zip.DeflaterOutputStream
 * https://www.programcreek.com/java-api-examples/index.php?api=java.util.zip.Inflater
 */

package com.sym.labo02.Services;

import android.os.AsyncTask;
import com.squareup.okhttp.*;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.SymComManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

public class CompressedService {

    private SymComManager scm;
    private String postRequestURL;
    private String request;

    /**
     * Empty Constructor
     */
    public CompressedService() {
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/txt";
    }


    /**
     * Send a request to the server
     * @param req, String text of the request
     * @throws Exception
     */
    public void sendRequest(String req) throws Exception {

        request = req;

        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                byte[] compressedData = compressData(request);
                //System.out.println("--------------------------------------------------------Compressed data length= " + compressedData.length);
                RequestBody rb = RequestBody.create(MediaType.parse("text/plain; charset=uft-8"), compressedData);

                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "plain/text")
                        .add("accept", "text/plain")
                        .add("X-Network", "CSD")
                        .add("X-Content-Encoding", "deflate");
                Headers h = header.build();
                Request req = scm.createRequest(
                        postRequestURL,
                        h,
                        rb);
                try {
                    //long beforeRequest = System.currentTimeMillis();
                    Response resp = scm.sendRequest(req);
                    //long afterRequest = System.currentTimeMillis();
                    //System.out.println("-------------------------------------------------------Request time = " + (afterRequest - beforeRequest) + " ms");
                    return decompressData(resp.body().bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String resp) {
                super.onPostExecute(resp);

                scm.getCommunicationEventListener().handleServerResponse(resp);
                //scm.getCommunicationEventListener().handleServerResponse(resp.toString());
            }
        };
        asyncTask.execute();
    }

    /**
     * Set the communication event listener
     * @param l, communication Event Listener
     */
    public void setCommunicationEventListener(CommunicationEventListener l) {
        scm.setCommunicationEventListener(l);
    }


    /**
     * Compress the datas
     * @param toCompress, String containing the datas to compress
     * @return a array of bytes containing the compressed data
     */
    public byte[] compressData(String toCompress) {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(byteArrayOutputStream, deflater);
        try {
            dos.write(toCompress.getBytes());
            dos.finish();
            dos.close();
            deflater.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Decompress a compressed data
     * @param bytes, data compressed
     * @return a String with the decompressed data
     */
    public static String decompressData(final byte[] bytes) {
        //We tried to use a InflaterInputStream, but the response couldn't be decompressed..
        byte[] body = bytes;
        Inflater i = new Inflater(true);
        i.setInput(body);
        ByteArrayOutputStream stream = new ByteArrayOutputStream(body.length);

        byte[] buffer = new byte[body.length];
        while (!i.finished()) {
            int count = 0;
            try {
                count = i.inflate(buffer);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
            stream.write(buffer, 0, count);
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }


}
