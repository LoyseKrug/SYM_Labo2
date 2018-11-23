package com.sym.labo02.FragmentManagers;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sym.labo02.CommunicationEventListener;
import com.sym.labo02.Object.Author;
import com.sym.labo02.Object.Post;
import com.sym.labo02.R;
import com.sym.labo02.RecyclerViewComponent.MyAdapter;
import com.sym.labo02.Services.GraphQLService;
import com.sym.labo02.Services.JSONService;
import com.sym.labo02.Services.XMLService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class GraphQLFragment extends AsyncFragment {

    private OnFragmentInteractionListener mListener;
    private GraphQLService graphQLService;

    private Spinner authorList;
    private RecyclerView postList;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinkedList<Author> authors = new LinkedList<>();
    private LinkedList<Post> posts = new LinkedList<>();

    public GraphQLFragment(){
        // Required empty public constructor
    }


    public static GraphQLFragment newInstance() {
        GraphQLFragment fragment = new GraphQLFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graphQLService = new GraphQLService();
        graphQLService.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                setAuthorFromJson(response);
                ArrayAdapter<Author> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, authors);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                authorList.setAdapter(adapter);
                return true;
            }
        });

        // Get Authors
       String queryAuthors = "{allAuthors{id first_name last_name}}";
       graphQLService.sendData(queryAuthors);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graphql, container, false);

        authorList = (Spinner) view.findViewById(R.id.authorList);
        postList = (RecyclerView) view.findViewById(R.id.postList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        postList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        postList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(posts);
        postList.setAdapter(mAdapter);




        authorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                String query = "{allPostByAuthor(authorId:" + i + "){title content date}}";
                graphQLService.setCommunicationEventListener(new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(String response) {
                        setPostsFromJson(response);
                        // specify an adapter (see also next example)
                        mAdapter.setPosts(posts);
                        postList.setAdapter(mAdapter);
                        return true;
                    }
                });
                graphQLService.sendData(query);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }

    private void setPostsFromJson(String json){
        System.out.println(json);
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonArray = jsonObj.getJSONObject("data").getJSONArray("allPostByAuthor");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                posts.add(new Post(explrObject.getString("title"), explrObject.getString("content"), explrObject.getString("date")));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

    }

    private void setAuthorFromJson(String json){
        System.out.println(json);
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonArray = jsonObj.getJSONObject("data").getJSONArray("allAuthors");
            System.out.println(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                authors.add(new Author(explrObject.getInt("id"), explrObject.getString("first_name"), explrObject.getString("last_name")));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
