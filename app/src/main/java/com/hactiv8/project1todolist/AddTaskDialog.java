package com.hactiv8.project1todolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddTaskDialog extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.background_dialog_task);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private SaveTaskCallBack callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack=(SaveTaskCallBack) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.add_dialog, null , false);
        builder.setView(view);

        TextInputEditText etTitle=view.findViewById(R.id.et_addDialog_title);
        TextInputLayout inputLayout=view.findViewById(R.id.etLayout_addDialog_title);

        View btnSave=view.findViewById(R.id.btn_addDialog_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTitle.length()>0)
                {
                    callBack.onSaveCallBack(new Task(etTitle.getText().toString().trim() , false));
                    dismiss();
                }
                else {
                    inputLayout.setError("Catatan Kosong!");
                }
            }
        });

        return builder.create();
    }

    interface SaveTaskCallBack{
        void onSaveCallBack(Task task);
    }
}
