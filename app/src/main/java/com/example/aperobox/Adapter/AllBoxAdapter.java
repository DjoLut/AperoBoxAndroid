package com.example.aperobox.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.R;
import java.util.ArrayList;

public class AllBoxAdapter extends RecyclerView.Adapter<AllBoxAdapter.ViewHolder>{

    private ArrayList<String> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ViewHolder(View v)
        {
            super(v);
            textView = v.findViewById(R.id.textViewBox);
        }
    }

    public AllBoxAdapter(ArrayList<String> dataSet)
    {
        this.dataSet = dataSet;
    }

    @Override
    public AllBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_box_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.textView.setText(dataSet.get(position));
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

}
