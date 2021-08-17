package zy.nav.example;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import zy.nav.annotation.Arg;
import zy.nav.annotation.Route;

@Route("/secondAct")
public class SecondActivity extends AppCompatActivity {

    @Arg
    String firstArg;

    @Arg("testInjectObj")
    Object object;

    @Arg
    List<String> nameList;

    @Arg
    Map<String, Object> cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.text);
        textView.setText("second");
    }
}
