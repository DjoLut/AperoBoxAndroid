package com.example.aperobox.Activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.google.android.material.button.MaterialButton;

public class OptionFragment extends Fragment {

    private Boolean isNightMode;
    private SharedPreferences preferences;
    private static final String SAVED_BUNDLE_TAG = "optionFragment";
    private Bundle bundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isNightMode = AperoBoxApplication.getInstance().isNightModeEnabled();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_BUNDLE_TAG, null);
        editor.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.option_fragment, container, false);

        MaterialButton menu = ((MainActivity)getActivity()).option;
        menu.setOnClickListener(null);
        menu.setElevation(1);

        Switch darkmode = view.findViewById(R.id.dark_mode);
        darkmode.setChecked(isNightMode);
        darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isNightMode = !AperoBoxApplication.getInstance().isNightModeEnabled();
                AperoBoxApplication.getInstance().setIsNightModeEnabled(isNightMode);
                AppCompatDelegate.setDefaultNightMode(isNightMode?AppCompatDelegate.MODE_NIGHT_NO:AppCompatDelegate.MODE_NIGHT_YES);
                preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                AperoBoxApplication.token = preferences.getString("access_token", null);
                //((NavigationHost)getActivity()).navigateTo(OptionFragment.this, false);
                OptionFragment.this.getActivity().recreate();
            }
        });

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.option_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MaterialButton accueil = ((MainActivity)getActivity()).acceuil;
        accueil.setElevation(0);
        MaterialButton apropos = ((MainActivity)getActivity()).apropos;
        apropos.setElevation(0);
        MaterialButton compte = ((MainActivity)getActivity()).compte;
        compte.setElevation(0);
        MaterialButton option = ((MainActivity)getActivity()).option;
        option.setOnClickListener(null);
        option.setElevation(1);
        MaterialButton panierM = ((MainActivity)getActivity()).panier;
        panierM.setElevation(0);
        MaterialButton boxPerso = ((MainActivity)getActivity()).boxPersonnalise;
        boxPerso.setElevation(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bundle==null) {
            bundle = new Bundle();
        }
        outState.putBundle(SAVED_BUNDLE_TAG, bundle);
    }
}