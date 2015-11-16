package org.enthusia.app.enthusia.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.balysv.materialripple.MaterialRippleLayout;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.okhttp.MediaType;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Sumod on 16-Nov-15.
 */
public class EnthusiaRegisterFragment extends Fragment {


    public static final String TAG = "EventRegAvty";

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1-n_LK0Au28NcwvN5n00XsLG-pcVI19d5phtjcsPhXik/formResponse";
    //input element ids found from the live form page
    public static final String NAME_KEY = "entry.797811249";
    public static final String EMAIL_KEY="entry_1665827040";
    public static final String PHONE_KEY = "entry_866064106";
    public static final String EVENT_KEY = "entry_1341551153";
    public static final String COLLEGE_NAME_KEY = "entry_2029202329";
    public static final String COLLEGE_ADDRESS_KEY = "entry_1944762147";

    private Context context;
    private int event;
    DynamicHeightImageView event_register_imageView;
    EditText participant_name, participant_email, participant_phone, participant_collegeName, participant_collegeAddress;
    MaterialSpinner eventSpinner;
    MaterialRippleLayout register_button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_event_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        event_register_imageView = (DynamicHeightImageView) getActivity().findViewById(R.id.event_register_imageView);
        participant_name = (EditText) getActivity().findViewById(R.id.participant_name);
        participant_email = (EditText) getActivity().findViewById(R.id.participant_email);
        participant_phone = (EditText) getActivity().findViewById(R.id.participant_phone);
        eventSpinner = (MaterialSpinner) getActivity().findViewById(R.id.register_event_spinner);
        participant_collegeName = (EditText) getActivity().findViewById(R.id.participant_college_name);
        participant_collegeAddress = (EditText) getActivity().findViewById(R.id.participant_college_address);
        register_button = (MaterialRippleLayout) getActivity().findViewById(R.id.register_button);

        populateSpinner();
        //event_register_imageView.setImageResource(EnthusiaEvents.drawables[event]);

        //save the activity in a context variable to be used afterwards
        context = getActivity();


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(participant_email.getText().toString()) ||
                        TextUtils.isEmpty(participant_name.getText().toString()) ||
                        TextUtils.isEmpty(participant_phone.getText().toString()) ||
                        TextUtils.isEmpty(participant_collegeName.getText().toString())) {
                    Toast.makeText(context, "All fields are mandatory.", Toast.LENGTH_LONG).show();
                    return;
                }
                //Check if a valid email is entered
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(participant_email.getText().toString()).matches()) {
                    participant_email.setError("");
                    Toast.makeText(context, "Please enter a valid email.", Toast.LENGTH_LONG).show();
                    return;
                }

                //Create an object for PostDataTask AsyncTask
                PostDataTask postDataTask = new PostDataTask();

                //execute asynctask
                postDataTask.execute(URL,
                        participant_name.getText().toString(),
                        participant_email.getText().toString(),
                        participant_phone.getText().toString(),
                        eventSpinner.getSelectedItem().toString(),
                        participant_collegeName.getText().toString(),
                        participant_collegeAddress.getText().toString());
            }
        });



    }

    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String name = contactData[1];
            String email = contactData[2];
            String phone = contactData[3];
            String event = contactData[4];
            String collegeName = contactData[5];
            String collegeAddress = contactData[6];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody =    NAME_KEY + "=" + URLEncoder.encode(name,"UTF-8") +
                        "&" + EMAIL_KEY + "=" + URLEncoder.encode(email,"UTF-8") +
                        "&" + PHONE_KEY + "=" + URLEncoder.encode(phone,"UTF-8") +
                        "&" + EVENT_KEY + "=" + URLEncoder.encode(event,"UTF-8") +
                        "&" + COLLEGE_NAME_KEY + "=" + URLEncoder.encode(collegeName,"UTF-8") +
                        "&" + COLLEGE_ADDRESS_KEY + "=" + URLEncoder.encode(collegeAddress,"UTF-8");
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
                Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_LONG).show();
                register_button.setEnabled(false);
                register_button.setBackgroundColor(getResources().getColor(R.color.gray_background));
            }
            else
                Toast.makeText(context, "There was some network error. Please check internet access and try again after some time.", Toast.LENGTH_LONG).show();
        }

    }



    private void populateSpinner(){
        List<String> events = new ArrayList<>();

        for (int i = 0; i<EnthusiaEvents.events.length; i++){
            events.add(EnthusiaEvents.events[i]);
        }



        ArrayAdapter<String> mySpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, events);
        eventSpinner.setAdapter(mySpinnerAdapter);
        eventSpinner.setSelection(event+1);
        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event_register_imageView.setImageResource(EnthusiaEvents.drawables[position]);
                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
