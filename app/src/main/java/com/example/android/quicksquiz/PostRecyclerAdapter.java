package com.example.android.quicksquiz;

/**
 * Created by Akanksha_Rajwar on 07-10-2018.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PostRecyclerAdapter extends  RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {

    private Context context;
    List<Post> postCardList;
    public PostRecyclerAdapter(Context context, List<Post> postCardList) {
         this.context=context;
         this.postCardList=postCardList;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionText;
        private TextView timeText;
        private TextView dateText;
        private TextView titleText;
        private TextView authorText;
        private TextView urlText;



        public PostViewHolder(View view)
        {
            super(view);

            sectionText= view.findViewById(R.id.sectionTV);
            timeText=view.findViewById(R.id.timeTV);
            dateText=view.findViewById(R.id.dateTV);
            titleText=view.findViewById(R.id.titleTV);
            authorText=view.findViewById(R.id.authorTV);
            urlText=view.findViewById(R.id.urlTV);
        }
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_posts, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentPost=postCardList.get(position);
        String webURL=currentPost.getWeblink();
        String URLtext="<a href="+webURL+">See full Story>></a>";
        holder.sectionText.setText("#"+currentPost.getCategory());
        holder.timeText.setText(currentPost.getPostTime());
        holder.dateText.setText(currentPost.getDate());
        holder.titleText.setText(currentPost.getTitle());
        holder.authorText.setText(currentPost.getAuthor());
        holder.urlText.setText(Html.fromHtml(URLtext));
        holder.urlText.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @Override
    public int getItemCount() {
        return postCardList.size();
    }


}
