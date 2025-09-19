package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, City city);
        // or: void editCity(int position, String name, String province);
    }

    private AddCityDialogListener listener;

    private static final String ARG_POSITION = "position";
    private static final String ARG_NAME = "name";
    private static final String ARG_PROVINCE = "province";


    public static AddCityFragment newInstance(int position, String name, String province) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_NAME, name);
        b.putString(ARG_PROVINCE, province);
        f.setArguments(b);
        return f;
    }

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) listener = (AddCityDialogListener) context;
        else throw new RuntimeException(context + " must implement AddCityDialogListener");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        boolean isEdit = args != null && args.containsKey(ARG_POSITION);
        int position = isEdit ? args.getInt(ARG_POSITION, -1) : -1;
        if (isEdit) {
            editCityName.setText(args.getString(ARG_NAME, ""));
            editProvinceName.setText(args.getString(ARG_PROVINCE, ""));
        }

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(isEdit ? "Edit city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "OK" : "Add", (d, w) -> {
                    String name = editCityName.getText().toString().trim();
                    String prov = editProvinceName.getText().toString().trim();
                    if (isEdit) {
                        listener.editCity(position, new City(name, prov));
                        // or listener.editCity(position, name, prov);
                    } else {
                        listener.addCity(new City(name, prov));
                    }
                })
                .create();
    }
}