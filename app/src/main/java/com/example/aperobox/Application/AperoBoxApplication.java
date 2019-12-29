package com.example.aperobox.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.aperobox.Activity.BoxsGridFragment;
import com.example.aperobox.Activity.NavigationHost;
import com.example.aperobox.Dao.UtilisateurDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.JwtToken;
import com.example.aperobox.Model.LoginModel;
import com.example.aperobox.R;
import com.example.aperobox.Thread.TokenExpire;

public class AperoBoxApplication extends Application {
    public static final String NIGHT_MODE = "NIGHT_MODE";
    private boolean isNightModeEnabled;
    public static String token;
    private static LoginModel loginModel;
    private Connection connection;
    private JwtToken jwtToken;

    private static AperoBoxApplication instance;
    private static Context appContext;

    public static AperoBoxApplication getInstance() {
        if(instance==null)
            instance= new AperoBoxApplication();
        return instance;
    }

    public static Context getAppContext() { return appContext; }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getInstance();
        token = null;
        this.setAppContext(getApplicationContext());
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        this.isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (connection!=null)
            connection.cancel(true);
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public void setLoginModel(LoginModel loginModel){
        this.loginModel = loginModel;
    }

    public void startExpiration(JwtToken jwtToken){
        long time = jwtToken.getExpires_in()*1000;

        token = jwtToken.getAccess_token();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connection = new Connection();
                connection.execute(loginModel);
            }
        }, jwtToken.getExpires_in()*1000);
    }

    public void deconnexion(){
        token = null;
    }

    /*
    private class Expiration extends AsyncTask<JwtToken, Void, Void>
    {
        @Override
        protected Void doInBackground(JwtToken...token)
        {
            long time = token[0].getExpires_in()*1000;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Connection connection = new Connection();
                    connection.execute(loginModel);
                }
            }, long - 5000);


            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(connection!=null)
                connection.cancel(true);
        }
    }

     */

    private class Connection extends AsyncTask<LoginModel, Void, Void> {

        private HttpResultException exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(LoginModel... loginModels) {
            if(token!=null) {
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                try {
                    jwtToken = utilisateurDAO.connection(loginModels[0]);
                    token = jwtToken.getAccess_token();
                } catch (Exception e) {
                    cancel(true);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void toke)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("access_token", token);
            editor.commit();
            startExpiration(jwtToken);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}