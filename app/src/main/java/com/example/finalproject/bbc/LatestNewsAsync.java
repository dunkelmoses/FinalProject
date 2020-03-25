package com.example.finalproject.bbc;

import android.os.AsyncTask;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.finalproject.bbc.LatestNewsFragment.BNadapter;
import static com.example.finalproject.bbc.LatestNewsFragment.newsItems;
import static com.example.finalproject.bbc.LatestNewsFragment.progressBar;

class LatestNewsAsync extends AsyncTask<Void, Void, InputStream> {
    String newsAPI = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

    @Override
    protected InputStream doInBackground(Void... voids) {
        InputStream inputStream = null;
        try {
            URL mUrl = new URL(newsAPI);
            HttpURLConnection connection = (HttpURLConnection)
                    mUrl.openConnection();

            inputStream = connection.getInputStream();
            try {
                XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser pullParser = pullParserFactory.newPullParser();
                pullParser.setInput(inputStream, null);
                int currentElement = pullParser.getEventType();
                boolean insideItem = false;
                newsItems.clear();
                BBCNewsItem bbcNewsItem = null;
                while (currentElement != XmlPullParser.END_DOCUMENT){
                    String tag = pullParser.getName();
                    switch (currentElement){
                        case XmlPullParser.START_TAG:
                            if(tag.equals("item")){
                                insideItem = true;
                                bbcNewsItem= new BBCNewsItem();
                            }else if(tag.equals("title")){
                                if(insideItem){
                                    String title = pullParser.nextText();
                                    bbcNewsItem.setTitle(title);
                                }
                            }else if(tag.equals("description")){
                                if(insideItem){
                                    String description = pullParser.nextText();
                                    bbcNewsItem.setDescription(description);
                                }
                            }else if(tag.equals("link")){
                                if(insideItem){
                                    String link = pullParser.nextText();
                                    bbcNewsItem.setLink(link);
                                }
                            }else if(tag.equals("pubDate")){
                                if(insideItem){
                                    String pubDate = pullParser.nextText();
                                    bbcNewsItem.setPubDate(pubDate);
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if(tag.equals("item")){
                                newsItems.add(bbcNewsItem);
                                insideItem = false;
                            }
                            break;
                    }
                    currentElement = pullParser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    @Override
    protected void onPostExecute(InputStream s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.GONE);
        BNadapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }
}