package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.aperobox.Dao.LigneProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.network.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;

import com.example.aperobox.staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        internetAvaillable = UtilDAO.isInternetAvailable(getContext());
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
        setUpToolbar(view);

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

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        View acceuil = view.findViewById(R.id.menu_acceuil);
        acceuil.setOnClickListener(null);
        acceuil.setElevation((float)1);


        View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new BoxFragment(),true);
            }
        });

        View panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new PanierFragment(),true);
            }
        });

        view.findViewById(R.id.menu_a_propos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new AProposFragment(utilisateur),true);
            }
        });

        view.findViewById(R.id.menu_nous_contactez).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.contact_mail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_mail_sujet);
                startActivity(Intent.createChooser(intent, getString(R.string.contact_mail_chooser)));
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = preferences.getString("access_token", null);
        if(access_token!=null)
            view.findViewById(R.id.menu_compte).setVisibility(View.INVISIBLE);
        else
        view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new LoginFragment(),true);
            }
        });

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(UtilDAO.isInternetAvailable(getContext())?R.id.product_grid:R.id.joke),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }


    private void setJoke(View view){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        TextView textView = view.findViewById(R.id.boxs_joke);
        textView.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if(menu.size()==0) {
            menuInflater.inflate(R.menu.toolbar_menu, menu);
            MenuItem icon = menu.getItem(0);
            icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        return true;
                    } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        return true;
                    }
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, menuInflater);
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
                Toast.makeText(getContext(), "Erreur de chargement de toute les boxs", Toast.LENGTH_SHORT).show();
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

            // Set cut corner background for API 23+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.findViewById(R.id.product_grid)
                        .setBackgroundResource(R.drawable.product_grid_background_shape);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
