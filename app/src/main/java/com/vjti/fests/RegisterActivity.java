package com.vjti.fests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        findViewById(R.id.register_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ((EditText) findViewById(R.id.register_et_username)).getText().toString().equals("") ) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_username);
                } else if ( ((EditText) findViewById(R.id.register_et_email)).getText().toString().equals("")) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_email);
                } else if ( !((EditText) findViewById(R.id.register_et_email)).getText().toString().contains("@")) {
                    Utils.showAlert(RegisterActivity.this, R.string.alert_register_enter_valid_email);
                } else {
                    getSharedPreferences(Utils.SHARED_PREFS, MODE_PRIVATE).edit().putString(Utils.PREF_USER_NAME, ((EditText) findViewById(R.id.register_et_username)).getText().toString()).commit();
                    getSharedPreferences(Utils.SHARED_PREFS, MODE_PRIVATE).edit().putString(Utils.PREF_EMAIL, ((EditText) findViewById(R.id.register_et_email)).getText().toString()).commit();

                    new RegisterUser().execute(null,null,null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Utils.showAlert(this, R.string.alert_register_first);
    }

    private class RegisterUser extends AsyncTask<Void, Integer, Void> {

        private ProgressDialog progressDialog;
//        private final String REGISTER_URLS[] = {""
//                                               "",
//                                               ""};

        @Override
            protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Register for push notification
            for (int i=0; i < 3; i++) {
                publishProgress( (i+1) * 30);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            getSharedPreferences(Utils.SHARED_PREFS, MODE_PRIVATE).edit().putBoolean(Utils.PREF_REGISTRATION_DONE, true).commit();
            RegisterActivity.this.finish();
        }
    }
}
