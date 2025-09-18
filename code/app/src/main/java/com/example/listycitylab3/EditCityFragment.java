package com.example.listycitylab3;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditCityFragment extends DialogFragment{
    public static final String ARG_CITY = "arg_city";
    public static final String ARG_POS  = "arg_pos";
    public static final String REQ_KEY  = "req_edit_city";

    public static EditCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);   // City implements Serializable
        args.putInt(ARG_POS, position);

        EditCityFragment f = new EditCityFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        City city   = (City) (args != null ? args.getSerializable(ARG_CITY) : null);
        int pos     = (args != null) ? args.getInt(ARG_POS, -1) : -1;

        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_edit_city, null);

        TextInputLayout tilName = view.findViewById(R.id.til_city_name);
        TextInputLayout tilProv = view.findViewById(R.id.til_province);
        TextInputEditText etName = view.findViewById(R.id.et_city_name);
        TextInputEditText etProv = view.findViewById(R.id.et_province);

        if (city != null) {
            etName.setText(city.getName());
            etProv.setText(city.getProvince());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit City")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Edit", (d, which) -> {
                    String newName = String.valueOf(etName.getText()).trim();
                    String newProv = String.valueOf(etProv.getText()).trim();

                    if (newName.isEmpty()) {
                        // basic validation: keep dialog open by re-opening self (simple for lab)
                        tilName.setError("Enter a city");
                        return;
                    } else tilName.setError(null);

                    if (newProv.isEmpty()) {
                        tilProv.setError("Enter a province/state");
                        return;
                    } else tilProv.setError(null);

                    City updated = new City(newName, newProv);
                    Bundle result = new Bundle();
                    result.putSerializable(ARG_CITY, updated);
                    result.putInt(ARG_POS, pos);
                    getParentFragmentManager().setFragmentResult(REQ_KEY, result);
                })
                .create();

        return dialog;
    }
}
