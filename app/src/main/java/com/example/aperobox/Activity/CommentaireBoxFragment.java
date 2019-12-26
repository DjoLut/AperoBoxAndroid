package com.example.aperobox.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.CommentaireLayout.CommentaireViewAdapter;
import com.example.aperobox.Dao.CommentaireDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentaireBoxFragment extends Fragment {

    private Integer boxId;
    private SharedPreferences preferences;

    private RecyclerView recycler_view;
    private TextInputLayout text_input;
    private TextInputEditText edit_text;
    private MaterialButton envoyer;
    private LoadCommentaire loadCommentaire;
    private AjouterCommentaire ajouterCommentaire;

    private ArrayList<Commentaire> listeCommentaire;

    public CommentaireBoxFragment(){}

    public CommentaireBoxFragment(Integer boxId){
        this.boxId = boxId;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commentaire_box_fragment, container,false);

        this.recycler_view = view.findViewById(R.id.recycler_view);
        this.text_input = view.findViewById(R.id.commentaire_text_input);
        this.edit_text = view.findViewById(R.id.commentaire_edit_text);
        this.envoyer = view.findViewById(R.id.commentaire_envoyer);

        setUpToolbar(view);

        if(listeCommentaire==null) {
            loadCommentaire = new LoadCommentaire();
            loadCommentaire.execute();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadCommentaire!=null)
            loadCommentaire.cancel(true);
        if(ajouterCommentaire!=null){
            loadCommentaire.cancel(false);
        }
    }


    private class AjouterCommentaire extends AsyncTask<Commentaire, Void, Commentaire>{
        @Override
        protected Commentaire doInBackground(Commentaire... commentaire) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String access_token = preferences.getString("access_token", null);
            if(access_token != null && !access_token.isEmpty()) {
                try {
                    CommentaireDAO commentaireDAO = new CommentaireDAO();
                    commentaireDAO.ajoutCommentaire(access_token, commentaire[0]);
                } catch (final HttpResultException h){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    cancel(true);
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getString(R.string.commentaire_fragment_error_ajout_commentaire) + "\n" + getString(R.string.retry), Toast.LENGTH_SHORT).show();
                        }
                    });
                    cancel(true);
                }
            }
            return commentaire[0];
        }

        @Override
        protected void onPostExecute(Commentaire commentaire) {
            super.onPostExecute(commentaire);
            listeCommentaire.add(commentaire);
            edit_text.setText("");
            ((NavigationHost)getActivity()).navigateTo(CommentaireBoxFragment.this, false);
            //CommentaireBoxFragment.this.getActivity().recreate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class LoadCommentaire extends AsyncTask<Integer, Void, ArrayList<Commentaire>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listeCommentaire = new ArrayList<>();
        }

        @Override
        protected ArrayList<Commentaire> doInBackground(Integer... integers) {
            try {
                CommentaireDAO commentaireDAO = new CommentaireDAO();
                listeCommentaire = commentaireDAO.getAllCommentaireFromBox(boxId);
            } catch (final HttpResultException h){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommentaireBoxFragment.this.getActivity().onBackPressed();
                        Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommentaireBoxFragment.this.getActivity().onBackPressed();
                        Toast.makeText(getContext(), getString(R.string.commentaire_fragment_error_load_commentaire) + "\n" + getString(R.string.retry), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return listeCommentaire;
        }

        @Override
        protected void onPostExecute(ArrayList<Commentaire> commentaires) {
            super.onPostExecute(commentaires);
            setViewCommentaire();
        }
    }

    public void ajouterCommentaire(Commentaire commentaire){
        if((listeCommentaire == null || listeCommentaire.isEmpty()) || !listeCommentaire.contains(commentaire)){
            ajouterCommentaire = new AjouterCommentaire();
            ajouterCommentaire.execute(commentaire);
        } else {
            Toast.makeText(getContext(),getString(R.string.commentaire_fragment_error_duplication), Toast.LENGTH_SHORT).show();
        }
    }

    private void setViewCommentaire(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = preferences.getString("access_token", null);
        if(access_token!=null) {
            this.edit_text.setVisibility(View.VISIBLE);
            this.text_input.setVisibility(View.VISIBLE);
            this.envoyer.setVisibility(View.VISIBLE);
            this.envoyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edit_text.getText().toString().isEmpty()) {
                        String texte = edit_text.getText().toString();
                        Commentaire commentaire = new Commentaire();
                        commentaire.setBox(boxId);
                        commentaire.setTexte(texte);
                        commentaire.setDateCreation(new Date());

                        ajouterCommentaire(commentaire);
                    }
                }
            });
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(new CommentaireViewAdapter(listeCommentaire, CommentaireBoxFragment.this));
    }


    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        View acceuil = view.findViewById(R.id.menu_acceuil);
        acceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), true);
            }
        });

        View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new BoxPersonnaliseFragment(), true);
            }
        });

        View option = view.findViewById(R.id.menu_option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new OptionFragment(),true);
            }
        });

        View panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new PanierFragment(), true);
            }
        });

        view.findViewById(R.id.menu_a_propos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new AProposFragment(), true);
            }
        });

        view.findViewById(R.id.menu_nous_contactez).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_mail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_mail_sujet);
                startActivity(Intent.createChooser(intent, getString(R.string.contact_mail_chooser)));
            }
        });

        // Compte onclick listener
        MaterialButton compte = view.findViewById(R.id.menu_compte);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = preferences.getString("access_token", null);
        if(access_token!=null) {
            compte.setVisibility(View.VISIBLE);
            compte.setText(R.string.deconnection_title);
            compte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(getContext(), "Déconnecté", Toast.LENGTH_LONG).show();
                    ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), true);
                }
            });
            panier.setVisibility(View.VISIBLE);
            panier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost)getActivity()).navigateTo(new PanierFragment(), true);
                }
            });
        }
        else {
            compte.setText(R.string.connexion_title);
            panier.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true);
                }
            });
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.commentaire_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }
}
