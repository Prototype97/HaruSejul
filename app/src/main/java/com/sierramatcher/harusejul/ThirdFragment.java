package com.sierramatcher.harusejul;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class ThirdFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RelativeLayout space;
    InputMethodManager imm;
    EditText thirdLine;

    //세번째줄을 위한 프래그먼트, 처음 화면에서 키보드는 숨기게 했고(눌러야 뜨도록), 키보드가 아닌 영역 누르면 키보드 다시 숨기게
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_third, container, false);

        space = (RelativeLayout) rootView.findViewById(R.id.space);
        thirdLine = (EditText) rootView.findViewById(R.id.thirdLine);
        space.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(thirdLine.getWindowToken(), 0);
                return false;
            }
        });

        return rootView;
    }

}
