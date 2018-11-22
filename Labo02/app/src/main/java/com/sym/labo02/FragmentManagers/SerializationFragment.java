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
import com.sym.labo02.Services.JSONService;
import com.sym.labo02.Services.XMLService;
import com.sym.labo02.SymComManager;
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

    private JSONService jsonService = null;
    private XMLService xmlService = null;

    private EditText firstName = null;
    private EditText lastName = null;
    private EditText middleName = null;
    private Spinner gender = null;
    private EditText phoneNumber = null;
    private Button sendJSON = null;
    private Button sendXML = null;
    private TextView response = null;

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
                    if(jsonService == null){
                        jsonService = new JSONService();
                        jsonService.setCommunicationEventListener(new CommunicationEventListener() {
                            @Override
                            public boolean handleServerResponse(String res) {
                                response.setText(res);
                                return true;
                            }
                        });
                    }
                    jsonService.sendJSON( firstName.getText().toString(),
                            lastName.getText().toString(),
                            middleName.getText().toString(),
                            gender.getSelectedItem().toString(),
                            Integer.parseInt(phoneNumber.getText().toString()));
                }
            }
        });



        sendXML.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(checkEntries()){
                    xmlService = new XMLService();
                    xmlService.setCommunicationEventListener(new CommunicationEventListener() {
                        @Override
                        public boolean handleServerResponse(String res) {
                            response.setText(res);
                            return true;
                        }
                    });

                    xmlService.sendXML( firstName.getText().toString(),
                            lastName.getText().toString(),
                            middleName.getText().toString(),
                            gender.getSelectedItem().toString(),
                            phoneNumber.getText().toString());


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
