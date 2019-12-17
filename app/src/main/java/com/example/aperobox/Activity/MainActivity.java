package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.R;

public class MainActivity extends AppCompatActivity implements NavigationHost{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new BoxsGridFragment())
                    .commit();
        }
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() { super.onStop(); }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
