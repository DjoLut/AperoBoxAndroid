package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class BoxsGridFragment extends Fragment {


    private SharedPreferences preferences;

    private ArrayList<Box> boxes;
    private Utilisateur utilisateur;
    private RecyclerView boxToDisplay;
    private LoadBox loadBoxTask;
    private View view;

    private LayoutInflater inflater;
    private ViewGroup container;
    private Boolean internetAvaillable;
    private Bundle savedInstanceState;

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
        this.savedInstanceState = savedInstanceState;
        internetAvaillable = UtilDAO.isInternetAvailable(getContext());
        super.onCreateView(inflater,container,savedInstanceState);
        return setView();
    }

    private View setView(){
        //container.removeView(view);
        // Inflate the layout for this fragment with the ProductGrid theme
        if(UtilDAO.isInternetAvailable(getContext())) {
            view = this.inflater.inflate(R.layout.boxs_grid_fragment, this.container, false);

            // Set up the RecyclerView
            boxToDisplay = view.findViewById(R.id.recycler_view);
            boxToDisplay.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position % 3 == 2 ? 2 : 1;
                }
            });
            boxToDisplay.setLayoutManager(gridLayoutManager);
            loadBoxTask = new LoadBox();
            loadBoxTask.execute();
        }
        else {
            view = this.inflater.inflate(R.layout.joke, this.container, false);
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            setJoke(view);
        }

        // Set up the tool bar
        //setUpToolbar(view);

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.product_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(internetAvaillable != UtilDAO.isInternetAvailable(getContext())) {
            internetAvaillable = UtilDAO.isInternetAvailable(getContext());
            setView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if(menu.size()==0) {
            menuInflater.inflate(R.menu.toolbar_menu, menu);
            MenuItem icon = menu.getItem(0);
            icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Boolean isNightMode = !AperoBoxApplication.getInstance().isNightModeEnabled();
                    AperoBoxApplication.getInstance().setIsNightModeEnabled(isNightMode);
                    AppCompatDelegate.setDefaultNightMode(isNightMode?AppCompatDelegate.MODE_NIGHT_NO:AppCompatDelegate.MODE_NIGHT_YES);
                    //BoxsGridFragment.this.onDestroyView();
                    //BoxsGridFragment.this.onCreateView(inflater,container,savedInstanceState);
                    BoxsGridFragment.this.onDestroy();
                    BoxsGridFragment.this.onCreate(savedInstanceState);
                    BoxsGridFragment.this.getFragmentManager().beginTransaction().detach(BoxsGridFragment.this).attach(BoxsGridFragment.this).commit();
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

     */



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
                        Toast.makeText(getContext(), getString(R.string.box_grid_fragment_erreur_load_boxs) + "\n" + getString(R.string.retry), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return boxes;
        }

        @Override
        protected void onPostExecute(ArrayList<Box> boxes)
        {
            ArrayList<Box> allBoxes = new ArrayList<>();
            for(Box b : boxes) {
                if(b.getAffichable()==1) {
                    allBoxes.add(b);
                }
            }

            StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(allBoxes, BoxsGridFragment.this);
            boxToDisplay.setAdapter(adapter);
            int largePadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_large);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_small);
            boxToDisplay.addItemDecoration(new BoxsGridItemDecoration(largePadding, smallPadding));

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
