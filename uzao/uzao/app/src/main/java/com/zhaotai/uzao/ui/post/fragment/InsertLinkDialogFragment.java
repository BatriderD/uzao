package com.zhaotai.uzao.ui.post.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zhaotai.uzao.R;

/**
 * Time: 2018/7/28 0028
 * Created by LiYou
 * Description : 输入超链接dialog
 */
public class InsertLinkDialogFragment extends DialogFragment {

    public static InsertLinkDialogFragment newInstance() {
        return new InsertLinkDialogFragment();
    }

    public interface Callback {
        void onClick(String linkAddress, String linkName);
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "InsertLinkDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_insert_link, null);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            EditText et_linkAddress = (EditText) view.findViewById(R.id.et_link_address);
                            EditText et_linkName = (EditText) view.findViewById(R.id.et_link_name);
                            callback.onClick(et_linkAddress.getText().toString(), et_linkName.getText().toString());
                        }
                    }
                })
        ;
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
