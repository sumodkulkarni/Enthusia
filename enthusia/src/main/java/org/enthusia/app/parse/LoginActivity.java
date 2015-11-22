package org.enthusia.app.parse;

import android.app.ProgressDialog;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.client.methods.HttpPost;
import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.parse.helper.ParseUtils;
import org.enthusia.app.parse.helper.PrefManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {

    private EditText inputEmail;
    private Button btnLogin;
    private PrefManager pref;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
        getSupportActionBar().setTitle("User Registration");

        inputEmail = (EditText) findViewById(R.id.register_et_email);

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

    private class RegisterUser extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog progressDialog;
        private final static String GCM_SENDER_ID = "623894493052";
        private final static String SERVER_URL = "http://enthusia.org/public_html/admin/register.php";


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LoginActivity.this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.showErrorDialogFragment(resultCode, LoginActivity.this, 9000);
                }
                this.cancel(true);
            }

            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String regId = getRegistrationId();

            if (regId == null) {

                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    regId = gcm.register(GCM_SENDER_ID);

                    // Register With Enthusia Server

                    HttpURLConnection register = null;

                    try {
                        register = (HttpURLConnection) new URL(SERVER_URL).openConnection();

                        HashMap<String, String> data = new HashMap<>();
                        data.put("regId", regId);
                        data.put("name", (String) Utils.getPrefs(LoginActivity.this, Utils.PREF_USER_NAME, String.class));
                        data.put("email", (String) Utils.getPrefs(LoginActivity.this, Utils.PREF_EMAIL, String.class));

                        StringBuilder body = new StringBuilder();
                        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> current = iterator.next();
                            body.append( current.getKey() )
                                    .append( '=' )
                                    .append( current.getValue() );

                            if (iterator.hasNext())
                                body.append('&');
                        }

                        register.setDoOutput(true);
                        register.setUseCaches(false);
                        register.setFixedLengthStreamingMode(body.toString().getBytes().length);
                        register.setRequestMethod(HttpPost.METHOD_NAME);
                        register.getOutputStream().write(body.toString().getBytes());
                        register.getOutputStream().close();
                        int status = register.getResponseCode();

                        if (status != 200) {
                            throw new IOException("Post Failed With Error Code: " + status);
                        }

                    } catch (IOException ex) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showAlert(LoginActivity.this, "Registration Failed");
                            }
                        });
                        return false;
                    } finally {
                        if (register != null) {
                            register.disconnect();
                        }
                    }


                    // Store Registration Values

                    Utils.putPrefs(LoginActivity.this, Utils.PREF_REGISTRATION_ID, regId);
                    Utils.putPrefs(LoginActivity.this, Utils.PREF_APP_VERSION, getAppVersion());

                } catch (IOException ex) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showAlert(LoginActivity.this, "Unable to Register");
                        }
                    });
                    return false;
                }


            }
            return true;
        }

        private String getRegistrationId() {
            String regId = (String)  Utils.getPrefs(getApplicationContext(), Utils.PREF_REGISTRATION_ID, String.class);

            if (regId == null)
                return null;

            if (getAppVersion() != (Integer) Utils.getPrefs(getApplicationContext(), Utils.PREF_APP_VERSION, Integer.class))
                return null;

            return regId;
        }

        private int getAppVersion() {
            try {
                return getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
            if (result) {
                Utils.putPrefs(LoginActivity.this, Utils.PREF_REGISTRATION_DONE, true);
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
