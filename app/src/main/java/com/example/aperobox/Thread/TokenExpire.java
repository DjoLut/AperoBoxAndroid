package com.example.aperobox.Thread;

public class TokenExpire extends Thread {
    long time;

    public TokenExpire(long time)
    {
        this.time = time;
    }

    public void run()
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
