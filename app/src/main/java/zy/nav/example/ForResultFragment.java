package zy.nav.example;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import zy.nav.Nav;

public class ForResultFragment extends Fragment {

    public static ForResultFragment newInstance() {
        Bundle args = new Bundle();
        ForResultFragment fragment = new ForResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_for_result, container, false);
        inflate.findViewById(R.id.btn_for_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.from(ForResultFragment.this).to("/app/forResult", 2222);
            }
        });
        return inflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2222 && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "Fragment For Result Success", Toast.LENGTH_SHORT).show();
        }
    }
}
