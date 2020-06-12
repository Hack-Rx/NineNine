package com.example.ninenine.Helper;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author devac
 * @date 12-06-2020
 */
public class InternetCheck extends AsyncTask<Void,Void,Boolean> {

    public interface Consumer{
        void accept (boolean internet);
    }
    Consumer consumer;
    public InternetCheck(Consumer comsumer){
        this.consumer=comsumer;
        execute();
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            Socket socket =new Socket();
            socket.connect(new InetSocketAddress("google.com",80),1500);
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        consumer.accept(aBoolean);
    }
}
