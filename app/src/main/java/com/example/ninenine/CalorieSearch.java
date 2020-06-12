package com.example.ninenine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;


public class CalorieSearch extends AppCompatActivity {
    EditText editFood;
    Button go;
    TextView dispFood;
    TextView dispCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_search);
        editFood=findViewById(R.id.editFood);
        go=findViewById(R.id.Search);
        dispFood=findViewById(R.id.dispFood);
        dispCal=findViewById(R.id.dispCal);

        String APPID="8818f970";
        String APPKEY="9cc66effc4e7750e170ff9358930011e";
        String search=editFood.getText().toString();
        String URL="https://api.nutritionix.com/v1_1/search/"+search+"?fields=item_name,nf_calories&appId="+APPID+"&appKey="+APPKEY;

        final RequestQueue requestQueue=Volley.newRequestQueue(this);
        final JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        JSONArray a=new JSONArray();
                        JSONArray a= null;
                        try {
                            a = response.getJSONArray("hits");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        assert a != null;
                            try {
                                Log.e("Rest Response",a.getJSONObject(0).getJSONObject("fields").getString("item_name").toString());
                                dispFood.setText(a.getJSONObject(0).getJSONObject("fields").getString("item_name").toString());
                                dispCal.setText(a.getJSONObject(0).getJSONObject("fields").getString("nf_calories").toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                },
                new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error){
                        Log.e("Rest Response",error.toString());
                    }
                }

        );
        go.setOnClickListener(new View.OnClickListener() {
                                  @Override
                    public void onClick(View v) {
                                      requestQueue.add(objectRequest);
                  }
            }

        );

    }


}
