package zy.nav.example;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import zy.nav.Nav;

public class RouteProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_proxy);
        TextView textView = findViewById(R.id.text);
        textView.setText(getIntent().getStringExtra(Nav.RAW_URI));
    }
}
