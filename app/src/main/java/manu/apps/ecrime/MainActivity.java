package manu.apps.ecrime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import manu.apps.ecrime.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
}