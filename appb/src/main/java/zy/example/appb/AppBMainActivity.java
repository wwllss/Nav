package zy.example.appb;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import zy.example.app_service.AppAModuleService;
import zy.nav.Nav;
import zy.nav.annotation.Route;

@Route("/app/b/main")
public class AppBMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_b_main);
    }

    public void getAppAFragment(View view) {

        List<String> msgList = new ArrayList<>();
        msgList.add("AppBMainActivity set 1");
        msgList.add("AppBMainActivity set 2");
        msgList.add("AppBMainActivity set 3");
        msgList.add("AppBMainActivity set 4");
        msgList.add("AppBMainActivity set 5");

        Fragment fragment = Nav.from(this)
                .withObject("msgList", msgList)
                .getFragment("/app/a/fragment/main");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, "AppAFragment")
                    .commit();
        }

    }

    public void reset(View view) {
        AppAModuleService service = Nav.getService(AppAModuleService.class);
        if (service != null) {
            service.reset();
        }
    }
}
