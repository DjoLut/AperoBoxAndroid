package com.example.aperobox.Adapter.CommentaireLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Activity.CommentaireBoxFragment;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.R;
import java.util.ArrayList;
import java.util.List;

public class CommentaireViewAdapter extends RecyclerView.Adapter<CommentaireViewHolder> {
    private List<Commentaire> listeCommentaire;
    private CommentaireBoxFragment fragment;

    public CommentaireViewAdapter(ArrayList<Commentaire> listeCommentaire, CommentaireBoxFragment fragment){
        this.listeCommentaire = listeCommentaire;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public CommentaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentaire, parent, false);
        return new CommentaireViewHolder(layoutView, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentaireViewHolder holder, final int position) {
        if(listeCommentaire!=null && position < listeCommentaire.size()){
            CommentaireViewHolder commentaireViewHolder = (CommentaireViewHolder) holder;
            commentaireViewHolder.bind(listeCommentaire.get(position));
        }
    }

    @Override
    public int getItemCount() {

        return listeCommentaire.size();
    }
}
