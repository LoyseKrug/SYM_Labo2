/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date:
 *
 * Objective: This class manage the Async fragment of the app. In this fragment, the user is supposed to send a
 * text/plain post request to the server. The answer is displayed in the fragments response field.
 *
 * Comments: The class must be a singleton to avoid creating many instances of okHttpClient.
 * (plus, in ordre to connect to the server, an Android.permission.Internet has been added to the manifest)
 *
 * Sources: -
 *
 */

package com.sym.labo02.FragmentManagers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.R;
import com.sym.labo02.Services.AsyncSendRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AsyncFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AsyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsyncFragment extends Fragment {

    private AsyncSendRequest asr;

    private EditText textToSend;
    private TextView response;
    private Button sendButton;

    private OnFragmentInteractionListener mListener;

    public AsyncFragment() {
        asr = new AsyncSendRequest();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AsyncFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AsyncFragment newInstance() {
        AsyncFragment fragment = new AsyncFragment();
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
        View view =  inflater.inflate(R.layout.fragment_sendandreceived, container, false);

        //The class variables are linked with the layout elements
        textToSend = (EditText) view.findViewById(R.id.sendTextfield);
        response = (TextView) view.findViewById(R.id.responseText);
        sendButton = (Button) view.findViewById(R.id.sendBtn);

        //As the answer can be longer than the text view, this field must be scrollable
        response.setMovementMethod(new ScrollingMovementMethod());

        asr.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String res) {
                response.setText(res);
                return true;
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    asr.sendRequest(textToSend.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
