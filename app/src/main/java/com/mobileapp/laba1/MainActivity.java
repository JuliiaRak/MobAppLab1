package com.mobileapp.laba1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_oneMatrix, btn_twoMatrix, btn_about, btn_graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_oneMatrix = findViewById(R.id.buttonOneMatrix);
        btn_twoMatrix = findViewById(R.id.buttonTwoMatrix);
        btn_about = findViewById(R.id.buttonAbout);
        btn_graph= findViewById(R.id.buttonGraph);


        btn_oneMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneMatrix oneMatrix = new OneMatrix();
                setNewFragment(oneMatrix);
            }
        });

        btn_twoMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoMatrix twoMatrix = new TwoMatrix();
                setNewFragment(twoMatrix);
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutFragment aboutFragment = new AboutFragment();
                setNewFragment(aboutFragment);
            }
        });

        btn_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Graph graph = new Graph();
                setNewFragment(graph);
            }
        });
    }
    private  void setNewFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}