package org.enthusia.app.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import org.enthusia.app.R;
import org.enthusia.app.Utils;

public class RegisterDialog extends Dialog {

    public RegisterDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enthusia_dialog_user_registration);
        setTitle(R.string.register_user);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.register_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ((EditText) findViewById(R.id.register_et_username)).getText().toString().equals("") ) {
                    Utils.makeToast(getContext(), R.string.alert_register_enter_username);
                } else if ( ((EditText) findViewById(R.id.register_et_email)).getText().toString().equals("")) {
                    Utils.makeToast(getContext(), R.string.alert_register_enter_email);
                } else if ( !((EditText) findViewById(R.id.register_et_email)).getText().toString().contains("@")) {
                    Utils.makeToast(getContext(), R.string.alert_register_enter_valid_email);
                } else {
                    Utils.putPrefs(getContext(), Utils.PREF_USER_NAME, ((EditText) findViewById(R.id.register_et_username)).getText().toString());
                    Utils.putPrefs(getContext(), Utils.PREF_EMAIL, ((EditText) findViewById(R.id.register_et_email)).getText().toString());

                    new RegisterUser().execute(null,null,null);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Utils.showAlert(getOwnerActivity(), R.string.alert_register_first);
    }

    private class RegisterUser extends AsyncTask<Void, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Register for push notification
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
            Utils.putPrefs(getContext(), Utils.PREF_REGISTRATION_DONE, true);
            dismiss();
        }
    }
}
