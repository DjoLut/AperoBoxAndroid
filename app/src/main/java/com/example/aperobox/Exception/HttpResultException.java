package com.example.aperobox.Exception;

import com.example.aperobox.Utility.Constantes;
import java.net.HttpURLConnection;

public class HttpResultException extends Exception {

    private int statusCode;

    public HttpResultException(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {

        switch (statusCode){
            case HttpURLConnection.HTTP_BAD_REQUEST:    return Constantes.HTTP_STATUS_400_BAD_REQUEST;
            case HttpURLConnection.HTTP_UNAUTHORIZED:   return Constantes.HTTP_STATUS_401_UNAUTHORIZED;
            case HttpURLConnection.HTTP_FORBIDDEN:      return Constantes.HTTP_STATUS_403_FORBIDDEN;
            case HttpURLConnection.HTTP_NOT_FOUND:      return Constantes.HTTP_STATUS_404_NOT_FOUND;
            case HttpURLConnection.HTTP_BAD_METHOD:     return Constantes.HTTP_STATUS_405_METHOD_NOT_ALLOWED;
            case HttpURLConnection.HTTP_CLIENT_TIMEOUT: return Constantes.HTTP_STATUS_408_REQUEST_TIME_OUT;
            default:                                    return Constantes.HTTP_STATUS_500_INTERNAL_SERVER_ERROR;
        }

    }
}