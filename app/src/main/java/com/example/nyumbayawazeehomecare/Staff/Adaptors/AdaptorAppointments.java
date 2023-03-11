package com.example.nyumbayawazeehomecare.Staff.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nyumbayawazeehomecare.R;
import com.example.nyumbayawazeehomecare.Staff.CareGiver.AppointmentDetails;
import com.example.nyumbayawazeehomecare.Staff.Model.AppointmentsModel;

import java.util.ArrayList;
import java.util.List;

public class AdaptorAppointments extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AppointmentsModel> list,searchItems;
     private Context cnxt;
    private  Intent in;

    public AdaptorAppointments(Context context, List<AppointmentsModel> list) {
        this.list = list;
        this.searchItems=list;
        cnxt=context;
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        private TextView txv_name,txv_gender,txv_dob,txv_appointmentID,
                txv_date,txv_status,txv_familyMember;
        private ImageView img_view;
        public OriginalViewHolder(@NonNull View vw) {
            super(vw);
            txv_appointmentID=vw.findViewById(R.id.txv_appointmentID);
            txv_name=vw.findViewById(R.id.txv_name);
            txv_familyMember=vw.findViewById(R.id.txv_familyMember);
            txv_gender=vw.findViewById(R.id.txv_gender);
            txv_dob=vw.findViewById(R.id.txv_dob);
            txv_date=vw.findViewById(R.id.txv_date);
            txv_status=vw.findViewById(R.id.txv_status);
            img_view=vw.findViewById(R.id.img_view);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        RecyclerView.ViewHolder viewHolder;
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_appointments,parent,false);
        viewHolder=new OriginalViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  OriginalViewHolder){
            final OriginalViewHolder view=(OriginalViewHolder) holder;
            final AppointmentsModel data=list.get(position);

            view.txv_appointmentID.setText("Appointment No: "+data.getAppointmentID());
            view.txv_name.setText(data.getName());
            view.txv_familyMember.setText("Family member: "+data.getFamilyMember());
            view.txv_dob.setText("DOB: "+data.getDob());
            view.txv_gender.setText(data.getGender());
            view.txv_date.setText("Date: "+data.getAppointmentDate());
            view.txv_status.setText("Status: "+data.getAppointmentStatus());

            view.img_view.setOnClickListener(v->{
                in=new Intent(cnxt, AppointmentDetails.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("appointmentID", data.getAppointmentID());
                in.putExtra("familyMember",data.getFamilyMember());
                in.putExtra("name",data.getName());
                in.putExtra("gender",data.getGender());
                in.putExtra("dob",data.getDob());
                in.putExtra("appointmentDate",data.getAppointmentDate());
                in.putExtra("appointmentStatus",data.getAppointmentStatus());
                in.putExtra("appointmentDetails",data.getAppointmentDetails());
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
                    ArrayList<AppointmentsModel> filteredList = new ArrayList<>();

                    for (AppointmentsModel androidVersion : list) {

                        if (androidVersion.getFamilyMember().toLowerCase().contains(charString)
                                ||androidVersion.getName().toLowerCase().contains(charString)
                           ||androidVersion.getAppointmentID().toLowerCase().contains(charString)) {

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
                list = (ArrayList<AppointmentsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
