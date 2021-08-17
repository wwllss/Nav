package zy.nav.example;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Map;

import zy.nav.Nav;
import zy.nav.annotation.Arg;
import zy.nav.annotation.Route;

@Route("/firstFragment")
public class FirstFragment extends Fragment {

    @Arg
    String firstFragName;

    @Arg("obj")
    Object firstFragObj;

    @Arg
    List<String> firstFragList;

    @Arg
    Map<String, Object> firstFragMap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Nav.inject(this);
    }
}
