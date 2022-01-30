package com.ksr.socialapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ksr.socialapp.R;

public class AddFragment extends Fragment {

    private Button postBT;
    private EditText postDescriptionET;
    private ImageView postImage, addImage;

    public AddFragment() {
        //Required Empty Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        postBT = view.findViewById(R.id.postBT);
        postDescriptionET = view.findViewById(R.id.postDescriptionET);
        postImage = view.findViewById(R.id.postImage);
        addImage = view.findViewById(R.id.addImage);


        postDescriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;


    }
}
