package com.example.admin.projectcapstonemobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Credential;
import com.example.admin.projectcapstonemobile.model.ResObject;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.UserService;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private UserService userService;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.editText_login_username);
        edtPassword = (EditText) findViewById(R.id.editText_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login_login);
        userService = ApiUtils.getUserService();
        setTitle("Login");

        setTitle("Login");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                validationLogin(username, password);
                Credential credential = new Credential(username, password);
                               Call<ResObject> resObjectCall = userService.getUser(credential);
                resObjectCall.enqueue(new Callback<ResObject>() {
                    @Override
                    public void onResponse(Call<ResObject> call, Response<ResObject> response) {
                        if (response.isSuccessful()) {
                            userToken = response.body().getToken();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            //decode token
                            //get username
                            JWT parsedJWT = new JWT(userToken);
                            Claim subscriptionMetadata = parsedJWT.getClaim("sub");
                            String username = subscriptionMetadata.asString();
                            //get authorities
                            Claim subscriptionMetaData = parsedJWT.getClaim("authorities");
                            List<String> role = subscriptionMetaData.asList(String.class);
                            //save in shared preferences
                            SharedPreferences sharedPreferences = getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userToken", userToken);
                            editor.putString("userName", username);
                            editor.putString("userRole", role.get(0));
                            editor.commit();

                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResObject> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private boolean validationLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
