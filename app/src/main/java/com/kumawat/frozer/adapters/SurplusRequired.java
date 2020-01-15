package com.kumawat.frozer.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumawat.frozer.Data;
import com.kumawat.frozer.R;
import com.kumawat.frozer.SurplusPOJO;
import com.kumawat.frozer.activities.EditEntry;

import java.util.ArrayList;

public class SurplusRequired extends RecyclerView.Adapter<SurplusRequired.ViewHolder> {
    private Context context;
    private ArrayList<SurplusPOJO> arrayList;
    public SurplusRequired(Context context, ArrayList<SurplusPOJO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public SurplusRequired.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.surplus_inform_layout, viewGroup, false);
        return new SurplusRequired.ViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SurplusPOJO pojo = arrayList.get(position);
        holder.orderNo.setText(position+1+"");
        holder.amount.setText(pojo.getAmount());
        holder.place.setText(pojo.getPlace());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditEntry.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, place, orderNo;
        ImageView editBtn;
        public ViewHolder(View itemView){
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            place = itemView.findViewById(R.id.place);
            editBtn = itemView.findViewById(R.id.editButton);
            orderNo = itemView.findViewById(R.id.orderNo);
        }
    }
}
