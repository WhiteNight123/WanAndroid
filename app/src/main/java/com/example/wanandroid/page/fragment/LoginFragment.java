package com.example.wanandroid.page.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wanandroid.page.fragment.MyFragment.LOGIN_STATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/9
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private Activity mActivity;
    private View mRootView;
    private TextView mTVRegister;
    private Button mButtonLogin;
    private TextInputLayout mTILAccount;
    private TextInputLayout mTILPassword;
    private CheckBox mCheckBox;
    private SharedPreferences mPref;
    String errorMsg = "-1";
    int errorCode;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String responseData = msg.obj.toString();
            jsonDecodeTest(responseData);
            Log.e(TAG, "handleMessage: " + errorCode);
            switch (msg.what) {
                case 5:
                    if (errorCode == -1) {
                        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
                        LOGIN_STATE = 0;
                        mPref.edit().remove("cookies").apply(); //登陆不成功,清除cookie
                    } else {
                        Toast.makeText(mActivity, "登陆成功", Toast.LENGTH_SHORT).show();
                        LOGIN_STATE = 1;
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
                        View v = mActivity.getWindow().peekDecorView();
                        if (null != v) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                        mActivity.onBackPressed();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mButtonLogin = (Button) mActivity.findViewById(R.id.fragment_button_login);
        mButtonLogin.setOnClickListener(this::onClick);
        mTVRegister = (TextView) mActivity.findViewById(R.id.fragment_tv_login_register);
        mTVRegister.setOnClickListener(this::onClick);
        mCheckBox = (CheckBox) mRootView.findViewById(R.id.fragment_cb_autologin);
        mCheckBox.setClickable(false);
        mTILAccount = (TextInputLayout) mActivity.findViewById(R.id.fragment_til_login_account);
        mTILPassword = (TextInputLayout) mActivity.findViewById(R.id.fragment_til_login_password);
    }

    private void initView() {
        mPref = mActivity.getSharedPreferences("login", Context.MODE_PRIVATE);
    }


    private void sendPostNetRequest(String mUrl, HashMap<String, String> params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection.setFollowRedirects(true);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.connect();
                    StringBuilder dataToWrite = new StringBuilder();
                    for (String key : params.keySet()) {
                        dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                    }
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(dataToWrite.substring(0, dataToWrite.length() - 1).getBytes());
                    InputStream in = connection.getInputStream();
                    String responseData = StreamToString(in);

                    Map<String, List<String>> cookies = connection.getHeaderFields();
                    List<String> setCookies = cookies.get("Set-Cookie");
                    String cookie = "";
                    for (String str : setCookies) {
                        cookie += str + ";";
                    }
                    Log.e(TAG, "run: final " + cookie);
                    mPref.edit().putString("cookie", cookie).apply();

                    Message message = Message.obtain();
                    message.what = 5;
                    message.obj = responseData;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "网络遇到错误了", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String oneLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while ((oneLine = reader.readLine()) != null) {
                sb.append(oneLine).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_button_login:
                String account = mTILAccount.getEditText().getText().toString();
                String password = mTILPassword.getEditText().getText().toString();
                HashMap<String, String> map = new HashMap<>();
                map.put("username", account);
                map.put("password", password);
                sendPostNetRequest("https://www.wanandroid.com/user/login", map);
                break;
            case R.id.fragment_tv_login_register:
                DialogFragmentRegister dialogFragmentRegister = new DialogFragmentRegister();
                dialogFragmentRegister.show(getParentFragmentManager(), "register");
                break;
        }

    }
}
