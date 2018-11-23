package com.sym.labo02.RecyclerViewComponent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sym.labo02.Object.Post;
import com.sym.labo02.R;
import org.w3c.dom.Text;

import java.util.LinkedList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LinkedList<Post> posts = new LinkedList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView contentText;
        private final TextView dateText;

        public MyViewHolder(View v) {
            super(v);
            titleText = v.findViewById(R.id.titleText);
            contentText = v.findViewById(R.id.contentText);
            dateText = v.findViewById(R.id.dateText);
        }

        void bind(Post post){
            titleText.setText(post.getTitle());
            contentText.setText(post.getContent());
            dateText.setText(post.getDate());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(LinkedList<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(LinkedList<Post> posts){
        this.posts = posts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Post post = posts.get(position);
        holder.bind(post);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}