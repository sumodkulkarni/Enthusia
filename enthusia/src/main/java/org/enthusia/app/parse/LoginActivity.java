package org.enthusia.app.parse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpPost;
import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.parse.helper.ParseUtils;
import org.enthusia.app.parse.helper.PrefManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/a/enthusia.org/forms/d/1PCB970d_xK_HFsWUInST9uvNNw3AUAT-_6AHWjfb3ro/formResponse";
    //input element ids found from the live form page
    public static final String NAME_KEY = "entry.342053311";
    public static final String EMAIL_KEY="entry.1782001611";


    private EditText user_name;
    private EditText inputEmail;
    private PrefManager pref;
    private Context context;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
        getSupportActionBar().setTitle("User Registration");

        inputEmail = (EditText) findViewById(R.id.register_et_email);
        user_name = (EditText) findViewById(R.id.register_et_username);

        if ((Utils.getPrefs(this, Utils.PREF_USER_NAME, String.class)) != null && !(Utils.getPrefs(this, Utils.PREF_USER_NAME, String.class)).equals("")) {
            ((EditText) findViewById(R.id.register_et_username)).setText(Utils.getPrefs(this, Utils.PREF_USER_NAME, String.class).toString());
            ((EditText) findViewById(R.id.register_et_email)).setText(Utils.getPrefs(this, Utils.PREF_EMAIL, String.class).toString());

            findViewById(R.id.register_et_username).setEnabled(false);
            findViewById(R.id.register_et_email).setEnabled(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isNetworkConnected())
                        Utils.showAlert(LoginActivity.this, Html.fromHtml(getString(R.string.alert_register_no_network)).toString());
                        login();
                }
            }, 150);
        }

        // Verifying parse configuration. This is method is for developers only.
        ParseUtils.verifyParseConfiguration(this);

        pref = new PrefManager(getApplicationContext());
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, EnthusiaStartActivity.class);
            startActivity(intent);

            finish();
        }

        findViewById(R.id.register_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String EMAIL_PATTERN = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";

                if (!isNetworkConnected()) {
                    Utils.showAlert(LoginActivity.this, Html.fromHtml(getString(R.string.alert_register_no_network)).toString());
                } else if (((EditText) findViewById(R.id.register_et_username)).getText().toString().equals("")) {
                    Utils.showAlert(LoginActivity.this, R.string.alert_register_enter_username);
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.register_et_username));
                } else if (((EditText) findViewById(R.id.register_et_email)).getText().toString().equals("")) {
                    Utils.showAlert(LoginActivity.this, R.string.alert_register_enter_email);
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.register_et_email));
                } else if (!((EditText) findViewById(R.id.register_et_email)).getText().toString().matches(EMAIL_PATTERN)) {
                    Utils.showAlert(LoginActivity.this, R.string.alert_register_enter_valid_email);
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.register_et_email));
                } else {
                    Utils.putPrefs(LoginActivity.this, Utils.PREF_USER_NAME, ((EditText) findViewById(R.id.register_et_username)).getText().toString().trim());
                    Utils.putPrefs(LoginActivity.this, Utils.PREF_EMAIL, ((EditText) findViewById(R.id.register_et_email)).getText().toString().trim());

                    //Create an object for PostDataTask AsyncTask
                    PostDataTask postDataTask = new PostDataTask();

                    //execute asynctask
                    postDataTask.execute(URL,
                            user_name.getText().toString(),
                            inputEmail.getText().toString());

                    ParseUtils.subscribeWithEmail(inputEmail.getText().toString());
                    login();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Utils.showInfo(LoginActivity.this, R.string.alert_register_first);
    }


    private void login() {
        String email = inputEmail.getText().toString();

        if (isValidEmail(email)) {

            pref.createLoginSession(email);

            Intent intent = new Intent(LoginActivity.this, EnthusiaStartActivity.class);
            startActivity(intent);

            finish();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private boolean isNetworkConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info : networkInfo) {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }

        return false;

    }

    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String name = contactData[1];
            String email = contactData[2];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody =    NAME_KEY + "=" + URLEncoder.encode(name, "UTF-8") +
                        "&" + EMAIL_KEY + "=" + URLEncoder.encode(email,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }

            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            if(result){
                Utils.putPrefs(getApplicationContext(), Utils.PREF_REGISTRATION_DONE, true);
                findViewById(R.id.register_btn_register).setEnabled(false);
                findViewById(R.id.register_btn_register).setBackgroundColor(getResources().getColor(R.color.gray_background));
            }
            else
                Utils.showAlert(LoginActivity.this, Html.fromHtml(getString(R.string.alert_register_no_network)).toString());
        }

    }
}
