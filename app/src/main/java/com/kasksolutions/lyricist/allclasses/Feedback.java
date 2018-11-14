package com.kasksolutions.lyricist.allclasses;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.kasksolutions.lyricist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity
{
    private static final int REQUEST_CODE_EMAIL = 1;
    Button submitButton;
    EditText feddbackEditText;
    TextView emailTextView;
    String feedback,email;

    String item;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submitButton=findViewById(R.id.submitFeedback);
        feddbackEditText=findViewById(R.id.edt);
        emailTextView=findViewById(R.id.mail);

        spinner = findViewById(R.id.spinner2);
        String[] years = {"Select Category","Problem","Suggestions","Add Lyrics","Others"};
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years );
        langAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(langAdapter);

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
       {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
           {
               item=adapterView.getItemAtPosition(i).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView)
           {
               Toast.makeText(Feedback.this, "Please choose category", Toast.LENGTH_SHORT).show();
           }
       });

     //  feeback();

      //  Log.d("mailllllllll",""+email+"\t"+feedback+"\t"+item);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            emailTextView.setText(accountName);
            // email.setText(accountName);
        }
    }
    public void mail(View v)
    {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }
    }
    public void submitFeedback(View view)
    {
        feedback=feddbackEditText.getText().toString().trim();
        email=emailTextView.getText().toString().trim();

     //   String rep=feedback.replaceAll("\n","##");

        if (email.matches(""))
        // if (feedback.isEmpty())
        {
            Toast.makeText(this, "Give your mail", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if (item.contains("Select Category"))
            {
                Toast.makeText(this, "Please choose Category", Toast.LENGTH_SHORT).show();
            }

            else
            {
                if (feedback.matches(""))
                {
                    Toast.makeText(this, "Write feedback", Toast.LENGTH_SHORT).show();
                }

                else
                    {

                  //  Toast.makeText(this, "" + rep, Toast.LENGTH_SHORT).show();
                    //feeback();
                    Toast.makeText(this, "Thanks for your valuable feedback" +
                            "Your feedback is submitted sucessfully", Toast.LENGTH_SHORT).show();
                    feddbackEditText.setText("");
                    emailTextView.setText("");
                    spinner.setSelection(0);
                    feeback();
                }
            }

        }

    }
    public void feeback()
    {
        Map<String,String> postData=new HashMap<>();
        postData.put("mailId",""+email);
        postData.put("mailDescription","FeedBack:"+feedback+"Category:"+item);
        Log.d("mailllllllll",""+email+"\t"+feedback+"\t"+item);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, "http://kasksolutions.com:90/LiriceApp/mail"
                , new JSONObject(postData), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Toast.makeText(Feedback.this, "Success", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("tag123", "Error: " + error.getMessage());
                    }
                })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed()
    {

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Do you want to go back");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //When Ipress OK it will close the appliction

                Feedback.super.onBackPressed();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //When Ipress Cancel  it will close the Alert Dilog

                dialogInterface.dismiss();
            }
        });

        ad.show();
    }
}
