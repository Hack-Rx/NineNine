package com.example.ninenine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Calories extends AppCompatActivity {

    EditText edtSearch;
    ListView listView;
    SimpleArcLoader simpleArcLoader;private int calss;
    ImageButton camButton;
    static String camSearch="";


    public static List<CalorieCount> calorieCountList;
    //public  List<CalorieCount> calorieCountList1 = new ArrayList<>();
    CalorieCount caloriecount;
    MyCustomAdapter1 myCustomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView);
        simpleArcLoader = findViewById(R.id.loader);
        calorieCountList= new ArrayList<>();
        camButton=findViewById(R.id.camButton);

        edtSearch.setText(""+camSearch);

        getSupportActionBar().setTitle("Food Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calories.this,Camitem.class));
            }
        });

        fetchData();
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Calories.this,details1.class).putExtra("position",position));
                }
            });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()!=0){
                    fetchData();

                        myCustomAdapter.getFilter().filter(s);
                        myCustomAdapter.notifyDataSetChanged();
                    }
                }


        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        String search = edtSearch.getText().toString();
        String APPID="5d3c0207";
        String APPKEY="3dd65e4c6d2c3102894e7745d758a34d";
        String url  = "https://api.nutritionix.com/v1_1/search/"+search+"?results=0:20&fields=item_name,brand_name,item_id,nf_calories,nf_calories_from_fat,nf_protein&appId="+APPID+"&appKey="+APPKEY;
        System.out.println(url);
        simpleArcLoader.start();


        final JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("hits");

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("fields");

                                String foodid = jsonObject.getString("item_id");
                                String fooditem = jsonObject.getString("item_name");
                                //System.out.println(fooditem);
                                String calories = jsonObject.getString("nf_calories");
                                String caloriesfromfat = jsonObject.getString("nf_calories_from_fat");

                                String protein = jsonObject.getString("nf_protein");

                                String serve = jsonObject.getString("nf_serving_size_qty");
                                String brand = jsonObject.getString("brand_name");
                                /*a=Float.parseFloat(calories);
                                if(a<=100){
                                    b=1;
                                }else{
                                    b=0;
                                }*/
                                caloriecount= new CalorieCount(foodid,fooditem,calories,caloriesfromfat,protein,serve,brand);
                                        calorieCountList.add(caloriecount);


                            }

                                myCustomAdapter = new MyCustomAdapter1(Calories.this,calorieCountList);

                                listView.setAdapter(myCustomAdapter);
                                //System.out.println(calorieCountList.get(2));
                                simpleArcLoader.stop();
                                simpleArcLoader.setVisibility(View.GONE);





                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(Calories.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
