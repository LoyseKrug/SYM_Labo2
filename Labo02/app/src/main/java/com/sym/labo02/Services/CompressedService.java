/**
 * Source: https://www.programcreek.com/java-api-examples/java.util.zip.DeflaterOutputStream
 * Exemple 12
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


    public CompressedService() {
        scm = SymComManager.getInstance();
        postRequestURL = "http://sym.iict.ch/rest/txt";
    }


    public void sendRequest(String req) throws Exception {

        request = req;

        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {

                RequestBody rb = RequestBody.create(MediaType.parse("text/plain; charset=uft-8"), compressData(request));

                Headers.Builder header = new Headers.Builder();
                header.add("content-type", "plain/text")
                       // .add("accept", "text/plain")
                        .add("X-Network", "CSD")
                        .add("X-Content-Encoding", "deflate");
                Headers h = header.build();
                Request req = scm.createRequest(
                        postRequestURL,
                        h,
                        rb);
                Response resp = scm.sendRequest(req);
                if (resp.isSuccessful() && resp.body() != null) {

                } else { // la réponse n'est pas valide
                    System.out.println(resp);
                }
                try {
                    return decompressData(resp.body().bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

    public void setCommunicationEventListener(CommunicationEventListener l) {
        scm.setCommunicationEventListener(l);
    }


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

    public static String decompressData(final byte[] bytes) {
        /*
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
        */

        byte[] body = bytes;
        Inflater inflater = new Inflater(true);
        inflater.setInput(body);
        ByteArrayOutputStream stream = new ByteArrayOutputStream(body.length);

        byte[] buffer = new byte[body.length];
        while (!inflater.finished()) {
            int count = 0;
            try {
                count = inflater.inflate(buffer);
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
