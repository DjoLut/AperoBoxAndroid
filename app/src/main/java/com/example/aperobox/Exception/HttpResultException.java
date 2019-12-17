package com.example.aperobox.Exception;

import android.content.res.Resources;
import com.example.aperobox.R;
import java.net.HttpURLConnection;

public class HttpResultException extends Exception {

    private int statusCode;

    public HttpResultException(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        if(statusCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
            //return Resources.getSystem().getString(R.string.HttpUrlConnection_InternalError);
            return "erreur";
        }
        if(statusCode >= HttpURLConnection.HTTP_BAD_REQUEST){
            if(statusCode == HttpURLConnection.HTTP_BAD_REQUEST || statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                return "Mauvais nom d'utilisateur et/ou mauvais mot de passe";//Resources.getSystem().getString(R.string.HttpConnection_Unauthorized);
            }
            //return Resources.getSystem().getString(R.string.HttpUrlConnection_ClientError);
            return "erreur";
        }
        return super.getMessage();
    }
}