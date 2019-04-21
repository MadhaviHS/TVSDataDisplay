package com.example.datadisplay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticatorActivity extends AppCompatActivity {

    TextInputEditText etUserName;
    TextInputLayout layoutUserName;
    TextInputEditText etPassword;
    TextInputLayout layoutPassword;
    Button submit;
    RetrofitService retrofitService;
    List<EmployeeDTO> employeeDTOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etUserName = findViewById(R.id.etUserName);
        layoutUserName = findViewById(R.id.layoutUserName);
        etPassword = findViewById(R.id.etPassword);
        layoutPassword = findViewById(R.id.layoutPassword);
        submit = findViewById(R.id.submit);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutUserName.setErrorEnabled(false);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutPassword.setErrorEnabled(false);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInClick();
            }
        });
    }

    private void onSignInClick() {

        if (TextUtils.isEmpty(etUserName.getEditableText().toString())) {
            layoutUserName.setError("must not empty");
//            layoutUserName.requestFocus();
//            return;
        }
        if (TextUtils.isEmpty(etPassword.getEditableText().toString())) {
            layoutPassword.setError("must not empty");
//            layoutPassword.requestFocus();
            return;
        }
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(etUserName.getEditableText().toString());
        loginDTO.setPassword(etPassword.getEditableText().toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://tvsfit.mytvs.in/reporting/vrm/api/test_new/int/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        retrofitService = retrofit.create(RetrofitService.class);
        retrofitService.signIn(loginDTO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = String.valueOf(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(res)) {

                        try {
                            JSONObject jsonObj = new JSONObject(res);
                            Object value = jsonObj.get("TABLE_DATA");
                            if (value != null)

//                            JSONArray array=jsonObi2.getJSONArray("data");
//                            for(int i=0;i<array.length();i++)
                            {
                                String str = value.toString();
                                JSONObject jsonObj2 = new JSONObject(str);
                                if (jsonObj2 != null) {
                                    JSONArray array = jsonObj2.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        String strArr = array.getString(i);
                                        if (!TextUtils.isEmpty(strArr)) {
                                            createDataModel(strArr);
                                            if (employeeDTOS != null && employeeDTOS.size() > 0)
                                                goToNextActivity();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else
                        Toast.makeText(AuthenticatorActivity.this, "Something went wrong.Please try again", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(AuthenticatorActivity.this, "Something went wrong.Please try again", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(AuthenticatorActivity.this).setMessage("Unable to connect to server.Please check network connection")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alerDialog.show();
            }
        });

    }

    private void goToNextActivity() {
        Intent intent = new Intent(AuthenticatorActivity.this, UserListActivity.class);
        intent.putParcelableArrayListExtra("employeeList", (ArrayList<? extends Parcelable>) employeeDTOS);
        startActivity(intent);
    }

    private void createDataModel(String strArr) {

        String str = strArr.replaceAll("[\\[\\]\"]", "");
        if (!TextUtils.isEmpty(str)) {
            String[] arr = str.split(",");
            EmployeeDTO employeeDTO = new EmployeeDTO();

            employeeDTO.setName(arr[0]);
            employeeDTO.setDesignation(arr[1]);
            employeeDTO.setCity(arr[2]);
            employeeDTO.setEid(arr[3]);
            employeeDTO.setDateOfJoining(arr[4]);
            employeeDTO.setSalary(arr[5]);
            employeeDTOS.add(employeeDTO);
        }
    }

}
