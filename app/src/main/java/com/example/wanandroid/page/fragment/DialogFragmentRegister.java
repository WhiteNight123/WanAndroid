package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.wanandroid.R;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/10
 */
public class DialogFragmentRegister extends DialogFragment {
    private static final String TAG = "DialogRegister";
    private Activity mActivity;
    private View mRootView;
    private Button mButtonRegister;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    String errorMsg;
    int errorCode;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 7:
                    String responseData = msg.obj.toString();
                    jsonDecodeTest(responseData);
                    if (errorCode == -1) {
                        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        mRootView = inflater.inflate(R.layout.fragment_register, null);
        dialog.setView(mRootView);
        mButtonRegister = (Button) mRootView.findViewById(R.id.fragment_btn_register);
        mEtAccount = (EditText) mRootView.findViewById(R.id.fragment_et_register_account);
        mEtPassword = (EditText) mRootView.findViewById(R.id.fragment_et_register_password);
        mEtRePassword = (EditText) mRootView.findViewById(R.id.fragment_et_register_repassword);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        return dialog.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    private void register() {
        String account = mEtAccount.getText().toString();
        String password = mEtPassword.getText().toString();
        String rePassword = mEtRePassword.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("username", account);
        map.put("password", password);
        map.put("repassword", rePassword);
        NetUtil.sendHttpRequest("https://www.wanandroid.com/user/register", "POST", map, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 7;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: " + e.toString());
            }
        });
    }

    private void jsonDecodeTest(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("errorCode");
            errorMsg = jsonObject.getString("errorMsg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
