package com.desafiolatam.desafioface.views.main;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.desafiolatam.desafioface.R;
import com.desafiolatam.desafioface.networks.GetUsers;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.HashMap;
import java.util.Map;

public class FinderDialogFragment extends DialogFragment {

    private FinderCallback callback;

    private SpinKitView loading;
    private String name;


    public FinderDialogFragment() {
    }

    public static FinderDialogFragment newInstance() {
        return new FinderDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FinderCallback) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_finder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageButton imageButton = view.findViewById(R.id.searchBtn);
        final EditText editText = view.findViewById(R.id.searchEt);
        loading = view.findViewById(R.id.loadingSkv);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText.getText().toString();
                if (name.trim().length() > 0 ) {
                    setCancelable(false);
                    imageButton.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("page", "1");
                    map.put("name", name);
                    new QueryUsers(-1).execute(map);

                }


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
    }

    private class QueryUsers extends GetUsers {

        public QueryUsers(int additionalPages) {
            super(additionalPages);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dismiss();
            callback.search(name);
            loading.setVisibility(View.GONE);

        }
    }



}
