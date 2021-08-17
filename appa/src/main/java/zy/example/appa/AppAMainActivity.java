package zy.example.appa;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import zy.nav.Nav;
import zy.nav.annotation.Arg;
import zy.nav.annotation.Route;

@Route({"/appa/module", "/app/a/main"})
public class AppAMainActivity extends AppCompatActivity {

    @Arg("str")
    String moduleAppName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_a_main);
    }

    public void goAppB(View view) {

        Nav.from(this).to("/app/b/main");

    }
}
