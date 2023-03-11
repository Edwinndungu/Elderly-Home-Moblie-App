package com.example.nyumbayawazeehomecare.Staff.CareGiver.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nyumbayawazeehomecare.R;
import com.example.nyumbayawazeehomecare.Staff.CareGiver.Models.FeedModel;
import com.example.nyumbayawazeehomecare.Staff.CareGiver.ReplyFeedback;

import java.util.ArrayList;
import java.util.List;

public class AdaptorFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<FeedModel> list,searchItems;
     private Context cnxt;
    private  Intent in;


    public AdaptorFeed(Context context, List<FeedModel> list) {
        this.list = list;
        this.searchItems=list;
        cnxt=context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        private TextView txv_name;
//
        public OriginalViewHolder(@NonNull View vw) {
            super(vw);

            txv_name=vw.findViewById(R.id.txv_name);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        RecyclerView.ViewHolder viewHolder;
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed,parent,false);
        viewHolder=new OriginalViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  OriginalViewHolder){
            final OriginalViewHolder view=(OriginalViewHolder) holder;
            final FeedModel data=list.get(position);


            view.txv_name.setText(data.getName());

            view.itemView.setOnClickListener(v->{
                Intent in=new Intent(cnxt, ReplyFeedback.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("memberID",data.getMemberID());
                in.putExtra("name",data.getName());
                cnxt.startActivity(in);
            });

        }
    }


    @Override
    public int getItemCount() {
        return list.size();

    }



    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    list = searchItems;
                } else {
                    ArrayList<FeedModel> filteredList = new ArrayList<>();

                    for (FeedModel androidVersion : list) {

                        if (androidVersion.getName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<FeedModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
