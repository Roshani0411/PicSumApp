package com.techflitter.myapplicationgit.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.techflitter.myapplicationgit.R;

public class MessageDialog extends DialogFragment {
    OnClick listener;
    OnClickLogout onClickLogout;

    public TextView tvMsg;
    public TextView tvMsgInfo;
    public ImageView imgClose;
    public Button btCancel;
    public Button btOk;
    public LinearLayout llMain;
    String dialogType = "";
    String msgTitle = "";


    String tvMsgText = "", tvMsgInfoText = "", cancelTxt = "", okTxt = "";

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(0, R.style.myDialogTheme);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ok, container, false);

        btOk = view.findViewById(R.id.btOk);
        btCancel = view.findViewById(R.id.btCancel);
        tvMsg = view.findViewById(R.id.tvMsg);
        tvMsgInfo = view.findViewById(R.id.tvMsgInfo);
        imgClose = view.findViewById(R.id.imgClose);
        llMain = view.findViewById(R.id.llMain);
        btOk.setText("Ok");

        btOk.setOnClickListener(v -> {
            if (listener != null) {
                listener.set(true);
            }

            if (onClickLogout != null) {
                onClickLogout.logout(true);
            }

        });

        imgClose.setOnClickListener(v -> btCancel.performClick());

        btCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.set(false);
            }
            if (onClickLogout != null) {
                onClickLogout.cancelBtn(false);
            }

        });
        return view;
    }

    public static MessageDialog getInstance() {
        MessageDialog msgDialog = new MessageDialog();
        return msgDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            tvMsgText = getArguments().getString("tvMsgText", null);
            tvMsgInfoText = getArguments().getString("tvMsgInfoText", null);

            cancelTxt = getArguments().getString("cancelTxt", "");
            okTxt = getArguments().getString("okTxt", "");
            dialogType = getArguments().getString("dialogType", "");

            Log.d("MessageDialog", "dialogType = " + dialogType);
            Log.d("MessageDialog", "tvMsgInfoText = " + tvMsgInfoText);
            Log.d("MessageDialog", "tvMsgText = " + tvMsgText);
            Log.d("MessageDialog", "okTxt = " + okTxt);
            Log.d("MessageDialog", "cancelTxt = " + cancelTxt);


            if (getArguments().containsKey("msgTitle")) {
                msgTitle = getArguments().getString("msgTitle", "");
                Log.d("MessageDialog", "msgTitle = " + msgTitle);
                if (!TextUtils.isEmpty(msgTitle)) {
                    tvMsg.setVisibility(View.VISIBLE);
                }
            } else {
                tvMsg.setVisibility(View.GONE);
            }

            if (dialogType.equals("SUCCESS")) {
                llMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_200));
            } else {
                llMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            }


            if (TextUtils.isEmpty(cancelTxt)) {
                btCancel.setVisibility(View.GONE);
            } else {
                btCancel.setVisibility(View.GONE);
            }


            btCancel.setText(cancelTxt);
            btOk.setText(okTxt);

            tvMsgInfo.setText(tvMsgText);

        }
    }



    public void setListener(OnClick listener) {
        this.listener = listener;
    }

    public interface OnClick {
        public void set(boolean ok);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    public void setListenerLogout(OnClickLogout onClickLogout) {
        this.onClickLogout = onClickLogout;
    }

    public interface OnClickLogout {
        public void logout(boolean yes);

        public void cancelBtn(boolean cancel);
    }


}