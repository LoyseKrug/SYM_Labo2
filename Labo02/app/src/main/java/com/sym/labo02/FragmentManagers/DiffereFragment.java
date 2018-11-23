/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: In this fragment the user will be able to send requests, and if no connexion with the network is found
 * the request is saved in the memory. When the connexion if back, all the requests saved are sent one after the other
 *
 * Comments: -
 *
 * Sources: https://www.baeldung.com/guide-to-okhttp
 *
 */


package com.sym.labo02.FragmentManagers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sym.labo02.R;
import com.sym.labo02.Services.DiffereService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiffereFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiffereFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiffereFragment extends AsyncFragment {


    private DiffereService ds;

    private EditText textToSend = null;
    private TextView logs = null;
    private Button sendButton = null;
    private TextView response = null;

    private OnFragmentInteractionListener mListener;

    public DiffereFragment() {
        ds = new DiffereService();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DiffereFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiffereFragment newInstance() {
        DiffereFragment fragment = new DiffereFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_differe, container, false);

        textToSend = (EditText) view.findViewById(R.id.sendTextfield);
        logs  = (TextView) view.findViewById(R.id.logsDisplayer);
        response = (TextView) view.findViewById(R.id.responseDisplayer);
        sendButton = (Button) view.findViewById(R.id.sendBtn);

        logs.setMovementMethod(new ScrollingMovementMethod());
        response.setMovementMethod(new ScrollingMovementMethod());

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ds.sentRequest(textToSend.getText().toString(), getActivity().getBaseContext(), response, logs);
            }
        });

        return view;
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
