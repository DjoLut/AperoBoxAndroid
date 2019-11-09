package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import com.example.aperobox.R;

public class Options extends AppCompatActivity {

    private Switch switchModeSombre;
    private Button boutonDeconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        switchModeSombre = (Switch) findViewById(R.id.switchModeSombre);
        boutonDeconnection = (Button) findViewById(R.id.boutonDeconnection);

    }
}
