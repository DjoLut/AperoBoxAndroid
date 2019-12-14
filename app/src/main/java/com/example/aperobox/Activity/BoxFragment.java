package com.example.aperobox.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;

import java.util.ArrayList;
import java.util.List;

public class BoxFragment extends Fragment {

    private int boxId;
    private List<Box> boxs;
    private BoxDAO boxDAO;
    public BoxFragment(int boxId){
        this.boxDAO = new BoxDAO();
        this.boxs = new ArrayList<>();
        this.boxId = boxId;
    }

    public BoxFragment(){
        this.boxDAO = new BoxDAO();
        this.boxs = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try{
            this.boxs.add(boxDAO.getBox(boxId));
            //this.boxs = boxDAO.getListBox(this.boxId);
        } catch (Exception e){
            Toast.makeText(getContext(),R.string.error_login_api,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.boxs_grid_fragment, container, false);

        // Set up the tool bar
        setUpToolbar(view);

        if(this.boxs!=null){

        }


        return view;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_2:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_4:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_6:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_8:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_10:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }


    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }

}
