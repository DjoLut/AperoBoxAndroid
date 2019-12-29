package com.example.aperobox.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aperobox.Adapter.CommentaireLayout.CommentaireViewAdapter;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Dao.CommentaireDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Date;

public class CommentaireBoxFragment extends Fragment {

    private Integer boxId;
    private SharedPreferences preferences;

    private RecyclerView recycler_view;
    private TextInputLayout text_input;
    private TextInputEditText edit_text;
    private MaterialButton envoyer;
    private LoadCommentaire loadCommentaire;
    private AjouterCommentaire ajouterCommentaire;
    private TextView aucun_commentaire;
    private View view;
    private Boolean internetAvaillable;

    private ArrayList<Commentaire> listeCommentaire;

    public CommentaireBoxFragment(Integer boxId){
        this.boxId = boxId;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        internetAvaillable = UtilDAO.isInternetAvailable(getContext());
        if(internetAvaillable) {
            view = inflater.inflate(R.layout.commentaire_box_fragment, container,false);

            this.recycler_view = view.findViewById(R.id.recycler_view);
            this.text_input = view.findViewById(R.id.commentaire_text_input);
            this.edit_text = view.findViewById(R.id.commentaire_edit_text);
            this.envoyer = view.findViewById(R.id.commentaire_envoyer);
            this.aucun_commentaire = ((TextView) view.findViewById(R.id.testAucunCommentaire));

            // Set cut corner background for API 23+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.findViewById(R.id.commentaire_grid)
                        .setBackgroundResource(R.drawable.product_grid_background_shape);
            }

            if (UtilDAO.isInternetAvailable(getContext())) {
                listeCommentaire = new ArrayList<>();
                loadCommentaire = new LoadCommentaire();
                loadCommentaire.execute();
            }
        } else {
            view = inflater.inflate(R.layout.joke, container, false);
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            setJoke(view);
        }


        return view;
    }

    private void setJoke(View view){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        TextView textView = view.findViewById(R.id.boxs_joke);
        textView.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadCommentaire!=null)
            loadCommentaire.cancel(true);
        if(ajouterCommentaire!=null){
            ajouterCommentaire.cancel(true);
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
        protected ArrayList<Commentaire> doInBackground(Integer... integers) {
            try {
                CommentaireDAO commentaireDAO = new CommentaireDAO();
                listeCommentaire = commentaireDAO.getAllCommentaireFromBox(boxId);
            } catch (final HttpResultException h){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommentaireBoxFragment.this.getContext(), getString(R.string.commentaire_fragment_error_load_commentaire) + "\n" + getString(R.string.retry), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return listeCommentaire;
        }

        @Override
        protected void onPostExecute(ArrayList<Commentaire> commentaires) {
            super.onPostExecute(commentaires);

            ProgressBar progressBar = view.findViewById(R.id.commentaire_fragment_progress_bar);
            progressBar.setElevation(0);

            setViewCommentaire();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            listeCommentaire.clear();
            listeCommentaire = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(internetAvaillable != UtilDAO.isInternetAvailable(getContext()))
            ((NavigationHost)getActivity()).navigateTo(new CommentaireBoxFragment(boxId), false);
    }

    public void ajouterCommentaire(Commentaire commentaire){
        if(UtilDAO.isInternetAvailable(getContext())) {
            if (listeCommentaire.isEmpty() || !listeCommentaire.contains(commentaire)) {
                ajouterCommentaire = new AjouterCommentaire();
                ajouterCommentaire.execute(commentaire);
            } else {
                Toast.makeText(getContext(), getString(R.string.commentaire_fragment_error_duplication), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
    }

    private void setViewCommentaire(){
        if(listeCommentaire.isEmpty()) {
            aucun_commentaire.setText(R.string.commentaire_fragment_aucun_commentaire);
        }

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
        recycler_view.setAdapter(new CommentaireViewAdapter(listeCommentaire, this));
    }
}
