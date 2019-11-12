package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aperobox.Adapter.AllBoxAdapter;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView boxToDisplay;
    private LoadBox loadBoxTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        boxToDisplay = findViewById(R.id.recyclerViewBox);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        boxToDisplay.setLayoutManager(layoutManager);
        loadBoxTask = new LoadBox();
        loadBoxTask.execute();
    }


    private class LoadBox extends AsyncTask<String, Void, ArrayList<Box>>
    {
        @Override
        protected ArrayList<Box> doInBackground(String... params)
        {
            BoxDAO boxDAO = new BoxDAO();
            ArrayList<Box> persons = new ArrayList<>();
            try {
                persons = boxDAO.getAllBox();
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
            return persons;
        }

        @Override
        protected void onPostExecute(ArrayList<Box> boxes)
        {
            ArrayList<String> allBoxes = new ArrayList<>();
            for(Box b : boxes) {
                allBoxes.add(b.toString());
            }
            RecyclerView.Adapter adapter = new AllBoxAdapter(allBoxes);
            boxToDisplay.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadBoxTask != null)
            loadBoxTask.cancel(true);
    }

}
