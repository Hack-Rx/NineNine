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

public class Suggestions extends AppCompatActivity {

    EditText edtSearch;private  int positionFood;
    ListView listView=null;
    SimpleArcLoader simpleArcLoader=null;private int calss;


   // public List<CalorieCount> calorieCountList = new ArrayList<>();
    public static List<CalorieCount> calorieCountList1;
    CalorieCount caloriecount;
    MyCustomAdapter myCustomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        Intent intent = getIntent();
        positionFood = intent.getIntExtra("position", 0);
        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView1);
        simpleArcLoader = findViewById(R.id.loader1);
        calorieCountList1 = new ArrayList<>();

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
                    startActivity(new Intent(Suggestions.this,DetailActivity.class).putExtra("position",position));
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
        final String word;
        if(Float.parseFloat(Home.bmi)>=25.00){
            word="green";
        }
        else if(Float.parseFloat(Home.bmi)<=19.00){
           word="eggs";
        }
        else {
           word="veg";
        }
        System.out.println(Home.bmi+word);
        String APPID="5d3c0207";
        String APPKEY="3dd65e4c6d2c3102894e7745d758a34d";
        String url  = "https://api.nutritionix.com/v1_1/search/"+word+"?results=0:20&fields=item_name,brand_name,item_id,nf_calories,nf_calories_from_fat,nf_protein&appId="+APPID+"&appKey="+APPKEY;
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
                        float m,n,o;
                        if(Float.parseFloat(Home.bmi)>=25){
                            m=0;n=200;o=0;
                        }
                        else if(Float.parseFloat(Home.bmi)<=19){
                            m=300;n=600;o=2;
                        }
                        else {
                            m=100;n=500;o=1;
                        }
                        try {

                            JSONArray jsonArray = response.getJSONArray("hits");

                            float a,b,p;
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
                                a=Float.parseFloat(calories);
                                p=Float.parseFloat(protein);
                                if(a>=m&&a<=n&&p>=o){
                                    b=1;
                                }else{
                                    b=0;
                                }
                                caloriecount= new CalorieCount(foodid,fooditem,calories,caloriesfromfat,protein,serve,brand);
                                try {
                                    if(b==1){
                                        calorieCountList1.add(caloriecount);
                                        b=0;
                                    }
                                }
                                catch (NullPointerException ignored){

                                }

                            }

                            myCustomAdapter = new MyCustomAdapter(Suggestions.this,calorieCountList1);
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
                Toast.makeText(Suggestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
