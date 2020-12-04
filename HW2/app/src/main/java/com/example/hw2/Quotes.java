package com.example.hw2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Quotes{

    private static ArrayList<String> quotes = new ArrayList<>();
    static {
        quotes.add(". My wife told me to stop impersonating a flamingo. I had to put my foot down.");
        quotes.add("2. I went to buy some camo pants but couldn’t find any.");
        quotes.add("I failed math so many times at school, I can’t even count.");
        quotes.add("I used to have a handle on life, but then it broke.");
        quotes.add("I used to have a handle on life, but then it broke");
        quotes.add("I used to have a handle on life, but then it broke.");
        quotes.add("I was wondering why the frisbee kept getting bigger and bigger, but then it hit me.");
        quotes.add("I heard there were a bunch of break-ins over at the car park. That is wrong on so many levels.");
        quotes.add("I want to die peacefully in my sleep, like my grandfather… Not screaming and yelling like the passengers in his car.");
        quotes.add("When life gives you melons, you might be dyslexic");
        quotes.add("Don’t you hate it when someone answers their own questions? I do");
        quotes.add("It takes a lot of balls to golf the way I do");
    }


    public static String[] getQuote(){
        int index = (int) (Math.random() * quotes.size());
        return new String[]{"quote of the moment:", quotes.get(index)};
    }
}
