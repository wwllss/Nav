package zy.example.appa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import zy.nav.Nav;
import zy.nav.annotation.Arg;
import zy.nav.annotation.Route;


@Route("/app/a/fragment/main")
public class AppAFragment extends Fragment implements Observer {

    @Arg
    List<String> msgList;

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Nav.inject(this);
        DataManager.getInstance().addObserver(this);
        View inflate = inflater.inflate(R.layout.fragment_app_a_main, container, false);
        textView = inflate.findViewById(R.id.text);
        textView.setText("");
        if (msgList != null && !msgList.isEmpty()) {
            for (String msg : msgList) {
                textView.append("\n" + msg);
            }
        }
        return inflate;
    }

    @Override
    public void onDestroyView() {
        DataManager.getInstance().deleteObserver(this);
        super.onDestroyView();
    }

    @Override
    public void update(Observable o, Object arg) {
        textView.setText(DataManager.getInstance().getData());
    }
}
