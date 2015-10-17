package org.enthusia.app.enthusia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.etsy.android.grid.util.DynamicHeightImageView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

import java.util.ArrayList;
import java.util.List;

public class EventRegistrationActivity extends ActionBarActivity {

    private int event;
    DynamicHeightImageView event_register_imageView;
    EditText register_event_et_username, register_event_et_email, register_event_et_phone;
    Spinner eventSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);

        Intent intent = getIntent();
        event = intent.getIntExtra("Event", 0);

        event_register_imageView = (DynamicHeightImageView) findViewById(R.id.event_register_imageView);
        register_event_et_username = (EditText) findViewById(R.id.register_event_et_username);
        register_event_et_email = (EditText) findViewById(R.id.register_event_et_email);
        register_event_et_phone = (EditText) findViewById(R.id.register_event_et_phone);
        eventSpinner = (Spinner) findViewById(R.id.register_event_spinner_select_event);

        event_register_imageView.setImageResource(EnthusiaEvents.drawables[event]);

        populateSpinner();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateSpinner(){
        List<String> events = new ArrayList<>();

        for (int i = 0; i<EnthusiaEvents.events.length; i++){
            events.add(EnthusiaEvents.events[i]);
        }

        ArrayAdapter<String> mySpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, events);
        eventSpinner.setAdapter(mySpinnerAdapter);
        eventSpinner.setSelection(event);
        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event_register_imageView.setImageResource(EnthusiaEvents.drawables[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
