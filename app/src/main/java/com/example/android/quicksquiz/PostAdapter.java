package com.example.android.quicksquiz;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Akanksha_Rajwar on 26-09-2018.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(Activity context, ArrayList<Post>posts){
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_posts, parent, false);
        }
        Post currentPost=getItem(position);

        TextView sectionText= listItemView.findViewById(R.id.sectionTV);
        TextView timeText=listItemView.findViewById(R.id.timeTV);
        TextView dateText=listItemView.findViewById(R.id.dateTV);
        TextView titleText=listItemView.findViewById(R.id.titleTV);
        TextView authorText=listItemView.findViewById(R.id.authorTV);
        TextView urlText=listItemView.findViewById(R.id.urlTV);
        ImageView putImage=listItemView.findViewById(R.id.putThumbnail);
        //"<a href='http://www.google.com'> Google </a>";
        String webURL=currentPost.getWeblink();
        String URLtext="<a href="+webURL+">See full Story>></a>";
        sectionText.setText("#"+currentPost.getCategory());
        timeText.setText(currentPost.getPostTime());
        dateText.setText(currentPost.getDate());
        titleText.setText(currentPost.getTitle());
        authorText.setText(currentPost.getAuthor());
        String thumbString=currentPost.getThumbnail();
        /*URI uri= null;

        try {
            uri = new URI(thumbString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

       Glide.with(this.getContext()).load(thumbString).into(putImage);




        urlText.setText(Html.fromHtml(URLtext));
        urlText.setMovementMethod(LinkMovementMethod.getInstance());
        return listItemView;

    }


}
