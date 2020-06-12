package com.example.ninenine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class MyCustomAdapter2 extends ArrayAdapter<CalorieCount> {

    private Context context;
    private List<CalorieCount> FoodItem;
    private List<CalorieCount> FoodItemFiltered;

    public MyCustomAdapter2( Context context, List<CalorieCount> FoodItem) {
        super(context, R.layout.list_custom_items,FoodItem);

        this.context = context;
        this.FoodItem = FoodItem;
        this.FoodItemFiltered = FoodItem;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_items,null,true);
        TextView fooditem = view.findViewById(R.id.fooditem);

        fooditem.setText(FoodItemFiltered.get(position).getFooditem()+","+FoodItemFiltered.get(position).getBrand());
        return view;
    }

    @Override
    public int getCount() {
        try {
            return FoodItemFiltered.size();
        }
        catch(NullPointerException ignored) {

        }
        return 0;
    }

    @Nullable
    @Override
    public CalorieCount getItem(int position) {
        return FoodItemFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = FoodItem.size();
                    filterResults.values = FoodItem;

                }else{
                    List<CalorieCount> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(CalorieCount itemsModel:FoodItem){
                        if(itemsModel.getFooditem().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                FoodItemFiltered = (List<CalorieCount>) results.values;
                Suggestions.calorieCountList1 = (List<CalorieCount>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
