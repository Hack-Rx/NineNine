package com.example.ninenine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class Preferred extends AppCompatActivity {

    EditText edtSearch;private  int positionFood;
    ListView listView=null;
    SimpleArcLoader simpleArcLoader=null;

    public static List<CalorieCount>  calorieCountList2;
    CalorieCount caloriecount;
    MyCustomAdapter2 myCustomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred);
        Intent intent = getIntent();
        positionFood = intent.getIntExtra("position", 0);
        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView2);
        simpleArcLoader = findViewById(R.id.loader2);
        calorieCountList2 = new ArrayList<>();

        getSupportActionBar().setTitle("Food Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fetchData();


        try {
            myCustomAdapter.notifyDataSetChanged();
        }
        catch(NullPointerException ignored){

        }
        try{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Preferred.this,DetailActivity2.class).putExtra("position",position));
                }
            });

        }catch (NullPointerException ignored){

        }

        try{
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
                        try {
                            myCustomAdapter.getFilter().filter(s);
                            myCustomAdapter.notifyDataSetChanged();
                        }
                        catch(NullPointerException ignored){

                        }
                    }
                }

            });
        }catch (NullPointerException ignored){

        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        //String search = edtSearch.getText().toString();
        //System.out.println(Home.bmi+words);
        String words = Personalization.pfi;
        float calVal = Float.parseFloat(Personalization.pc);
        String APPID="5d3c0207";
        String APPKEY="3dd65e4c6d2c3102894e7745d758a34d";
        String url  = "https://api.nutritionix.com/v1_1/search/"+words+"?results=0:20&fields=item_name,brand_name,item_id,nf_calories,nf_calories_from_fat,nf_protein&appId="+APPID+"&appKey="+APPKEY;
        System.out.println(url);
        try{
            simpleArcLoader.start();
        }catch (NullPointerException ignored){
        }

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
                                System.out.println(fooditem);
                                String calories = jsonObject.getString("nf_calories");
                                String caloriesfromfat = jsonObject.getString("nf_calories_from_fat");

                                String protein = jsonObject.getString("nf_protein");

                                String serve = jsonObject.getString("nf_serving_size_qty");
                                String brand = jsonObject.getString("brand_name");
                                float x= Float.parseFloat(calories);

                                caloriecount= new CalorieCount(foodid,fooditem,calories,caloriesfromfat,protein,serve,brand);

                                        if(Float.parseFloat(Personalization.pc)>=x){
                                            System.out.println("here: "+Float.parseFloat(Personalization.pc));
                                            calorieCountList2.add(caloriecount);

                                        }
                            }

                            myCustomAdapter = new MyCustomAdapter2(Preferred.this,calorieCountList2);
                            listView.setAdapter(myCustomAdapter);


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
                Toast.makeText(Preferred.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
