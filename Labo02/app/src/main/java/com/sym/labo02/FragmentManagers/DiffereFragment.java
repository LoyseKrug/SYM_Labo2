/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date:
 *
 * Objective: -
 *
 * Comments: -
 *
 * Sources: https://www.baeldung.com/guide-to-okhttp
 *
 */


package com.sym.labo02.FragmentManagers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sym.labo02.R;
import com.sym.labo02.SymComManager;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiffereFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiffereFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiffereFragment extends AsyncFragment {

    private EditText textToSend = null;
    private TextView logs = null;
    private Button sendButton = null;
    private TextView response = null;

    private boolean requestSent = false;
    private ArrayList<String> logList = new ArrayList<String>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DiffereFragment() {
        // Required empty public constructor
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
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
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
                response.setText("");
                addLog();
                AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>(){

                    @Override
                    protected Void doInBackground(String... strings) {
                        while(requestSent == false){
                            if(SymComManager.isNetworkAvailable(getActivity().getApplicationContext())){

                                for(String s : logList){
                                    Request req = scm.createRequest(
                                            postRequestURL,
                                            scm.createHeader("text/plain", "text/plain"),
                                            scm.createTextBody(s));
                                    Response resp = scm.sendRequest(req);
                                    try {
                                        response.setText(response.getText().toString() + resp.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                emptyLogList();
                                requestSent = true;
                            } else {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return null;
                    }

                };
                asyncTask.execute();

            }
        });

        return view;
    }

    private void addLog(){
        String text = textToSend.getText().toString();
        if(!text.isEmpty() && !text.equals("")){
            requestSent = false;
            logList.add(text);
            logs.setText(logs.getText().toString() + "\n" + text);

        }
    }

    private void emptyLogList(){
        logs.setText("");
        logList.clear();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
