package com.sym.labo02.FragmentManagers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SerializationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SerializationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SerializationFragment extends AsyncFragment {

    private EditText firstName = null;
    private EditText lastName = null;
    private EditText middleName = null;
    private Spinner gender = null;
    private EditText phoneNumber = null;
    private Button sendJSON = null;
    private Button sendXML = null;
    private TextView response = null;

    protected String jsonURL = "http://sym.iict.ch/rest/json";
    private String xmlURL = "http://sym.iict.ch/rest/xml";

    private static final String[] genders = {"Male", "Female"};

    private OnFragmentInteractionListener mListener;

    public SerializationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DiffereFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SerializationFragment newInstance() {
        SerializationFragment fragment = new SerializationFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serialization, container, false);

        firstName = (EditText) view.findViewById(R.id.editFirstName);
        lastName = (EditText) view.findViewById(R.id.editLastName);
        middleName = (EditText) view.findViewById(R.id.editMiddleName);
        gender = (Spinner) view.findViewById(R.id.spinnerGender);
        phoneNumber = (EditText) view.findViewById(R.id.editPhoneNumber);
        sendJSON = (Button) view.findViewById(R.id.jsonBtn);
        sendXML = (Button) view.findViewById(R.id.xmlBtn);
        response = (TextView) view.findViewById(R.id.responseDisplayer);

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genders);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        sendJSON.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(checkEntries()){
                    scm.setCommunicationEventListener(new CommunicationEventListener() {
                        @Override
                        public boolean handleServerResponse(String res) {
                            response.setText(res);
                            return true;
                        }
                    });

                    AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            JSONObject toSend = formatToJSON();
                            Headers.Builder header = new Headers.Builder();
                            header.add("content-type", "application/json")
                                    .add("accept","application/json");
                            Headers h = header.build();
                            Request request = scm.createRequest( jsonURL, h ,scm.createJsonBody(toSend));
                            Response resp = scm.sendRequest(request);
                            try {
                                return resp.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String resp) {
                            super.onPostExecute(resp);
                            scm.getCommunicationEventListener().handleServerResponse(resp);
                        }
                    };
                    asyncTask.execute();

                }
            }
        });



        sendXML.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(checkEntries()){
                    scm.setCommunicationEventListener(new CommunicationEventListener() {
                        @Override
                        public boolean handleServerResponse(String res) {
                            response.setText(res);
                            return true;
                        }
                    });

                    AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            String toSend = formatToXML();
                            Request request = scm.createRequest(
                                    xmlURL,
                                    scm.createHeader("application/xml", "application/xml") ,
                                    scm.createTextBody(toSend));
                            Response resp = scm.sendRequest(request);
                            try {
                                return resp.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String resp) {
                            super.onPostExecute(resp);
                            scm.getCommunicationEventListener().handleServerResponse(resp);
                        }
                    };
                    asyncTask.execute();

                }
            }
        });

        return view;
    }



    private boolean checkEntries() {
        boolean isValid = false;
        if (firstName.getText().toString().isEmpty() || firstName.getText().toString().equals("")
                || lastName.getText().toString().isEmpty() || lastName.getText().toString().equals("")
                || phoneNumber.getText().toString().isEmpty() || phoneNumber.getText().toString().equals("")
                || gender.getSelectedItem().toString().isEmpty() || gender.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fields with an * must be set", Toast.LENGTH_SHORT).show();
        } else {
            String phone = phoneNumber.getText().toString();
            try {
                //we check if the entered values are numeric
                Integer.parseInt(phone);
                isValid = true;
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "You must enter a number for the phone numbers", Toast.LENGTH_SHORT).show();
            }
        }
        return isValid;
    }

    protected JSONObject formatToJSON() {
        // create your json here
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", firstName.getText().toString());
            jsonObject.put("lastName", lastName.getText().toString());
            jsonObject.put("middleName", middleName.getText().toString());
            jsonObject.put("gender", gender.getSelectedItem().toString());
            jsonObject.put("phone", Integer.parseInt(phoneNumber.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String formatToXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE directory SYSTEM \"http://sym.iict.ch/directory.dtd\">\n" +
                "<directory>";
        xml += "\n<person>";
        xml += "\n<name>" + lastName.getText().toString() + "</name>" +
                "\n<firstname>" + firstName.getText().toString() + "</firstname>" +
                "\n<middlename>" + middleName.getText().toString() + "</middlename>" +
                "\n<gender>" + gender.getSelectedItem().toString() + "</gender>" +
                "\n<phone type=\"home\">" + phoneNumber.getText().toString() + "</phone>";
        xml += "\n</person>\n</directory>";
        return null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
