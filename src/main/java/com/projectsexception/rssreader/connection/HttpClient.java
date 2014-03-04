package com.projectsexception.rssreader.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by @FedeProEx on 4/03/14.
 */
public abstract class HttpClient {

    public abstract InputStream loadInputStream(URL url) throws IOException;

    public static HttpClient createClient() {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            return new OkHttpClient();
        } catch (ClassNotFoundException e) {
            return new JavaNetClient();
        }
    }

    static class JavaNetClient extends HttpClient {

        public InputStream loadInputStream(URL url) throws IOException {
            return url.openStream();
        }
    }

    static class OkHttpClient extends HttpClient {

        private com.squareup.okhttp.OkHttpClient client;

        public OkHttpClient(){
            client = new com.squareup.okhttp.OkHttpClient();
        }

        public InputStream loadInputStream(URL url) throws IOException {
            HttpURLConnection connection = client.open(url);
            return connection.getInputStream();
        }
    }

}
