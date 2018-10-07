package com.example.android.quicksquiz;

/**
 * Created by Akanksha_Rajwar on 25-09-2018.
 */

public class Post {

    public final String title;
    public final String date;
    public final String time;
    public final String author;
    public final String weblink;
    public final String category;
    public final String thumbnail;


    public Post(String titlePassed, String datePassed, String timePassed, String authorPassed, String weblinkPassed, String categoryPassed,String thumbnailPassed) {
        author = authorPassed;
        title = titlePassed;
        date = datePassed;
        time = timePassed;
        weblink = weblinkPassed;
        category = categoryPassed;
        thumbnail=thumbnailPassed;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getPostTime() {
        return time;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail(){return thumbnail;}

}
