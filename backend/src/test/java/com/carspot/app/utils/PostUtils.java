package com.carspot.app.utils;

import com.carspot.app.entity.Post;
import lombok.Data;


public class PostUtils {


    public static final Post postOne = new Post(1L,
                               "This is my first post on the website",
                               "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                               "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                               2019, "Province 1", "city 1");

    private static final Post postTwo = new Post(2L,
                               "This is my second post on the website",
                               "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                               "BMW", "i35", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                               2019, "Province 2", "city 2");

    private static final Post postThree = new Post(3L,
                                 "This is my third post on the website",
                                 "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                                 "Audi", "A3", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                                 2019, "Province 3", "city 3");

    private static final Post duplicatePost = new Post(4L,
            "This is my first post on the website",
            "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
            "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
            2019, "Province 1", "city 1");


    public static Post getPostOne() {
        return postOne;
    }

    public static Post getPostTwo() {
        return postTwo;
    }

    public static Post getPostThree() {
        return postThree;
    }

    public static Post getDuplicatePost() {
        return duplicatePost;
    }



}
