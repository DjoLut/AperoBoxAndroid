package com.example.aperobox.Activity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Adapter.BoxGridLayout.BoxGridViewAdapter;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class BoxsGridFragment extends Fragment {

    private ArrayList<Box> boxes;
    private RecyclerView boxToDisplay;
    private LoadBox loadBoxTask;
    private View view;

    private LayoutInflater inflater;
    private ViewGroup container;
    private Boolean internetAvaillable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        internetAvaillable = UtilDAO.isInternetAvailable(getContext());
        super.onCreateView(inflater,container,savedInstanceState);
        return setView();
    }

    private View setView(){

        MaterialButton menu = ((MainActivity)getActivity()).acceuil;
        menu.setElevation(1);

        // Inflate the layout for this fragment with the ProductGrid theme
        if(UtilDAO.isInternetAvailable(getContext())) {
            view = this.inflater.inflate(R.layout.boxs_grid_fragment, this.container, false);

            // Set up the RecyclerView
            boxToDisplay = view.findViewById(R.id.box_grid_fragment_recycler_view);
            boxToDisplay.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE?3:1, GridLayoutManager.VERTICAL, false);
            boxToDisplay.setLayoutManager(gridLayoutManager);
            loadBoxTask = new LoadBox();
            loadBoxTask.execute();
        }
        else {
            view = this.inflater.inflate(R.layout.joke, this.container, false);
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            setJoke(view);
        }

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view.findViewById(R.id.product_grid)!=null)
                view.findViewById(R.id.product_grid)
                        .setBackgroundResource(R.drawable.product_grid_background_shape);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(internetAvaillable != UtilDAO.isInternetAvailable(getContext()))
            ((NavigationHost)getActivity()).navigateTo(new BoxsGridFragment(), false);

        MaterialButton accueil = ((MainActivity)getActivity()).acceuil;
        accueil.setElevation(1);
        MaterialButton apropos = ((MainActivity)getActivity()).apropos;
        apropos.setElevation(0);
        MaterialButton compte = ((MainActivity)getActivity()).compte;
        compte.setElevation(0);
        MaterialButton option = ((MainActivity)getActivity()).option;
        option.setElevation(0);
        MaterialButton panierM = ((MainActivity)getActivity()).panier;
        panierM.setElevation(0);
        MaterialButton boxPerso = ((MainActivity)getActivity()).boxPersonnalise;
        boxPerso.setElevation(0);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadBoxTask != null)
            loadBoxTask.cancel(true);
    }

    private void setJoke(View view){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        TextView textView = view.findViewById(R.id.boxs_joke);
        textView.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
    }

    private class LoadBox extends AsyncTask<String, Void, ArrayList<Box>>
    {
        @Override
        protected ArrayList<Box> doInBackground(String... params)
        {
            BoxDAO boxDAO = new BoxDAO();
            boxes = new ArrayList<>();
            try {
                boxes = boxDAO.getAllBox();
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.box_grid_fragment_erreur_load_boxs) + "\n" + getString(R.string.retry), Toast.LENGTH_LONG).show();
                    }
                });
            }

            return boxes;
        }

        @Override
        protected void onPostExecute(ArrayList<Box> boxes)
        {
            ArrayList<Box> allBoxes = new ArrayList<>();
            for(Box b : boxes)
                if(b.getAffichable()==1)
                    allBoxes.add(b);

            ProgressBar progressBar = view.findViewById(R.id.box_grid_fragment_progress_bar);
            progressBar.setElevation(0);

            BoxGridViewAdapter adapter = new BoxGridViewAdapter(allBoxes, BoxsGridFragment.this);
            boxToDisplay.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}