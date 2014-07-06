package org.enthusia.app.gcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.client.methods.HttpPost;
import org.enthusia.app.R;
import org.enthusia.app.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        findViewById(R.id.register_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkConnected()) {
                    Utils.showAlert(RegisterActivity.this, Html.fromHtml(getString(R.string.alert_register_no_network)).toString());
                } else if ( ((EditText) findViewById(R.id.register_et_username)).getText().toString().equals("") ) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_username);
                } else if ( ((EditText) findViewById(R.id.register_et_email)).getText().toString().equals("")) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_email);
                } else if ( !((EditText) findViewById(R.id.register_et_email)).getText().toString().contains("@")) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_valid_email);
                } else {
                    Utils.putPrefs(RegisterActivity.this, Utils.PREF_USER_NAME, ((EditText) findViewById(R.id.register_et_username)).getText().toString().trim());
                    Utils.putPrefs(RegisterActivity.this, Utils.PREF_EMAIL, ((EditText) findViewById(R.id.register_et_email)).getText().toString().trim());

                    new RegisterUser().execute(null,null,null);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Utils.showInfo(RegisterActivity.this, R.string.alert_register_first);
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
        private final static String SERVER_URL = "http://enthusia.zapto.org:8080/register.php";


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(RegisterActivity.this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.showErrorDialogFragment(resultCode, RegisterActivity.this, 9000);
                }
                this.cancel(true);
            }

            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Register for push notification

            String regId = getRegistrationId();

            if (regId == null) {

                try {
                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(RegisterActivity.this);
                        regId = gcm.register(GCM_SENDER_ID);

                        // Register With Enthusia Server

                        HttpURLConnection register = null;

                        try {
                            register = (HttpURLConnection) new URL(SERVER_URL).openConnection();

                            HashMap<String, String> data = new HashMap<String, String>();
                            data.put("regId", regId);
                            data.put("name", (String) Utils.getPrefs(RegisterActivity.this, Utils.PREF_USER_NAME, String.class));
                            data.put("email", (String) Utils.getPrefs(RegisterActivity.this, Utils.PREF_EMAIL, String.class));

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
                            Utils.showAlert(RegisterActivity.this, "Registration Failed");
                            return false;
                        } finally {
                            if (register != null) {
                                register.disconnect();
                            }
                        }


                        // Store Registration Values

                        Utils.putPrefs(RegisterActivity.this, Utils.PREF_REGISTRATION_ID, regId);
                        Utils.putPrefs(RegisterActivity.this, Utils.PREF_APP_VERSION, getAppVersion());

                } catch (IOException ex) {
                    Utils.showAlert(RegisterActivity.this, "Unable to Register");
                    return false;
                }


            }
            return true;
        }

        private String getRegistrationId() {
            String regId = (String)  Utils.getPrefs(getApplicationContext(), Utils.PREF_REGISTRATION_ID, String.class);

            if (regId == null)
                return null;

            if (getAppVersion() != ((Integer) Utils.getPrefs(getApplicationContext(), Utils.PREF_APP_VERSION, Integer.class)).intValue())
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
                Utils.putPrefs(RegisterActivity.this, Utils.PREF_REGISTRATION_DONE, true);
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}