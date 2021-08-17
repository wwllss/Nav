package zy.nav.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import zy.example.app_service.AppMainModuleService;
import zy.example.app_service.TestClassService;
import zy.example.app_service.TestService;
import zy.nav.Nav;
import zy.nav.annotation.Arg;
import zy.nav.annotation.Route;

@Route("/app/main")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        textView.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ForResultFragment.newInstance(), "ForResultFragment")
                .commit();
    }

    public void goKotlinModule(View view) {
        Nav.from(this)
                .withString("kotlin_msg", "from app_module")
                .to("/app/k/main");
    }

    @Override
    public void onClick(View v) {
        /*Nav.from(MainActivity.this)
                .withString("kotlin_msg", "from interceptor")
                .withInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) {
                        Request request = chain.request();
                        String url = request.url();
                        if (!url.contains("app/k")) {
                            request.redirect("/app/k/main");
                        }
                        return chain.process(request);
                    }
                })
                .to("/app/a/main");*/

        TestClassService service1 = Nav.getService(TestClassService.class, "1");
        TestClassService service2 = Nav.getService(TestClassService.class, "2", null, null);


        TestService testService = Nav.getService(TestService.class, "", null, "int");
        TestService testService2 = Nav.getService(TestService.class, "2", null, "Integer");
        AppMainModuleService appMainModuleService = Nav.getService(AppMainModuleService.class);
    }

    public void forResult(View view) {
        Nav.from(this).to("/app/forResult", 1111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == RESULT_OK) {
            Toast.makeText(this, "forResult Success", Toast.LENGTH_SHORT).show();
        }
    }

    public void goProxy(View view) {
        Nav.from(this).to("http://www.jd.com/alkdf");
    }
}
