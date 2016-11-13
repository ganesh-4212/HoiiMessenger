package com.kempasolutions.app.hoiimessanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.XMPPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class RegisterActivity extends AppCompatActivity {
    int vcode = 1234;
    EditText inputPhone, inputOTP;
    Button btnSendOTP, btnRegister;
    private PropertyReader propertyReader;
    private Context context;
    private Properties properties;
    String phoneNumber;
    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputPhone = (EditText) findViewById(R.id.txt_phone);
        inputOTP = (EditText) findViewById(R.id.txt_otp);
        btnSendOTP = (Button) findViewById(R.id.btn_send_otp);
        btnRegister = (Button) findViewById(R.id.btn_register);
        if (!isNetworkAvailable()) {
            Snackbar.make(btnRegister, "No internet connection.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastActions.REGISTRATION_ACTION);
        registerReceiver(myReceiver, intentFilter);

        //Start our own service
        Intent intent = new Intent(getApplicationContext(),
                ChatService.class);
        startService(intent);

        super.onStart();
    }

    public void sendOTP(View view) {
        phoneNumber = inputPhone.getText().toString().trim();
        if (checkNumber(phoneNumber, 10)) {
            if (!isNetworkAvailable()) {
                Snackbar.make(btnSendOTP, "No internet connection.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // new SendOTPMessage().execute(phoneNumber);
            inputPhone.setEnabled(false);
            inputOTP.setText("");
            inputOTP.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            btnSendOTP.setEnabled(false);

        } else {
            Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            inputPhone.requestFocus();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    boolean checkNumber(String str, int len) {
        String numeric = "0123456789";
        if (str.length() != len) {
            return false;
        }
        for (char ch : str.toCharArray()) {
            if (!numeric.contains(String.valueOf(ch))) {
                return false;
            }
        }
        return true;
    }

    public void checkOtpAndRegister(View view) {
        String vcode = inputOTP.getText().toString().trim();
        if (checkNumber(vcode, 4)) {
            if (this.vcode == Integer.parseInt(vcode)) {
                propertyReader = new PropertyReader(getApplicationContext());
                properties = propertyReader.getMyProperties();
                properties.setProperty("phone", phoneNumber);
                PropertyWriter propertyWriter = new PropertyWriter(getApplicationContext());
                propertyWriter.saveProperties(properties);
                Toast.makeText(getApplicationContext(), "Phone number confirmed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(BroadcastActions.INVOKE_CHAT_SERVICE);
                intent.putExtra(BroadcastActions.INVOKE_CHAT_SERVICE, BroadcastActions.REGISTER_LOGIN);
                intent.putExtra(BroadcastActions.REGISTER_LOGIN_PHONE, phoneNumber);
                sendBroadcast(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
                inputPhone.setEnabled(true);
                inputOTP.setVisibility(View.INVISIBLE);
                btnRegister.setVisibility(View.INVISIBLE);
                btnSendOTP.setEnabled(true);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Invalid verification code", Toast.LENGTH_SHORT).show();
            inputOTP.requestFocus();
        }
    }

    public class SendOTPMessage extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String POST_PARAMS = "phone=" + params[0];
            String USER_AGENT = "Mozilla/5.0";

            System.out.println(POST_PARAMS);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://kempa:8080/sms/").openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();
                int responseCode = connection.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    int vcode = Integer.parseInt(response.toString().trim());
                    return vcode;

                } else {
                    System.out.println("POST request not worked");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer vcode) {
            super.onPostExecute(vcode);
            if (vcode >= 1000 && vcode <= 9999)
                RegisterActivity.this.vcode = vcode;
            else
                RegisterActivity.this.vcode = 1234;
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String result = arg1.getStringExtra(BroadcastActions.REGISTRATION_ACTION);
            Toast.makeText(RegisterActivity.this,
                    result,
                    Toast.LENGTH_LONG).show();
            if (result.compareTo(BroadcastActions.REGISTRATION_ACTION_RESULT) == 0) {
                unregisterReceiver(myReceiver);
                RegisterActivity.this.finish();
            }

        }

    }
}
