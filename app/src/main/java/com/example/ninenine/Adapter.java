package com.example.ninenine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author devac
 * @date 09-06-2020
 */
public class Adapter extends FirestoreRecyclerAdapter<FoodItems,Adapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnItemClickListener mListener;
    public Adapter(@NonNull FirestoreRecyclerOptions<FoodItems> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FoodItems model) {
        holder.foodName.setText(model.getFood_Name());
        holder.calAmount.setText("Calories: "+model.getCalories());
        if(model.getQuantity() == null){
            holder.foodQty.setVisibility(View.INVISIBLE);
        }
        holder.foodQty.setText("QTY: "+model.getQuantity());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.historyitem_view,parent,false);
        return new ViewHolder(view,mListener);
    }
    public interface OnItemClickListener{
        //void OnItemClick(int position);
        void OnDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public void deleteItem(int position) {
        Home.dayTotal -= Double.parseDouble("" + getSnapshots().getSnapshot(position).get("calories")) * Double.parseDouble("" + getSnapshots().getSnapshot(position).get("quantity"));
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference= fStore.collection("users").document(Login.userID).collection("daily").document(Home.Date);
        documentReference.update("Day_Total", Home.dayTotal);
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView foodName,calAmount,foodQty;
        Button delete;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            foodName=itemView.findViewById(R.id.foodname);
            calAmount=itemView.findViewById(R.id.calamount);
            foodQty=itemView.findViewById(R.id.foodqty);
            delete=itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.OnDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

}
