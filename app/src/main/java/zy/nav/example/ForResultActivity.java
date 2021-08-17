package zy.nav.example;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import zy.nav.annotation.Route;

@Route("/app/forResult")
public class ForResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_result);
    }

    public void setResult(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
