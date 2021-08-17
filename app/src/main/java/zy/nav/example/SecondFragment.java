package zy.nav.example;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import zy.nav.Nav;
import zy.nav.annotation.Arg;

public class SecondFragment extends FirstFragment {

    @Arg
    boolean aBoolean;

    @Arg
    byte aByte;

    @Arg
    short aShort;

    @Arg
    int anInt;

    @Arg
    long aLong;

    @Arg
    float aFloat;

    @Arg
    double aDouble;

    @Arg
    char aChar;

    @Arg
    Boolean aBoxBoolean;

    @Arg
    Byte aBoxByte;

    @Arg
    Short aBoxShort;

    @Arg
    Integer anBoxInt;

    @Arg
    Long aBoxLong;

    @Arg
    Float aBoxFloat;

    @Arg
    Double aBoxDouble;

    @Arg
    Character aBoxChar;

    @Arg
    String secondFragName;

    @Arg("obj")
    Object secondFragObj;

    @Arg
    List<String> secondFragList;

    @Arg
    Map<String, Object> secondFragMap;

    @Arg("gaoShiQing")
    Map<String, List<String>> listMap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Nav.inject(this);
    }
}
