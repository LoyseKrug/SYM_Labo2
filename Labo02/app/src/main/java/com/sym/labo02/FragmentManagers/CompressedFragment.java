package com.sym.labo02.FragmentManagers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.R;
import com.sym.labo02.Services.CompressedService;

/**
 * A simple {@link AsyncFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompressedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompressedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompressedFragment extends AsyncFragment {

    private CompressedService cs;
    private EditText textToSend;
    private TextView response;
    private Button sendButton;

    private OnFragmentInteractionListener mListener;

    public CompressedFragment() {
        cs = new CompressedService();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompressedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompressedFragment newInstance() {
        CompressedFragment fragment = new CompressedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sendandreceived, container, false);

        textToSend = (EditText) view.findViewById(R.id.sendTextfield);
        response = (TextView) view.findViewById(R.id.responseText);
        sendButton = (Button) view.findViewById(R.id.sendBtn);

        //As the answer can be longer than the text view, this field must be scrollable
        response.setMovementMethod(new ScrollingMovementMethod());

        cs.setCommunicationEventListener(new CommunicationEventListener() {
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
                    if(!textToSend.getText().toString().isEmpty()){
                        cs.sendRequest(textToSend.getText().toString());
                    }
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
}
