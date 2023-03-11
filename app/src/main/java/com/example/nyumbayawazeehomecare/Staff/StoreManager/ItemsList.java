package com.example.nyumbayawazeehomecare.Staff.StoreManager;

import static com.example.nyumbayawazeehomecare.ConstFiles.Urls.URL_APPROVE_DONATION;
import static com.example.nyumbayawazeehomecare.ConstFiles.Urls.URL_CONFIRM_DONATION;
import static com.example.nyumbayawazeehomecare.ConstFiles.Urls.URL_ITEMS_DONATED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nyumbayawazeehomecare.Donors.Adaptor.AdaptorDonationItems;
import com.example.nyumbayawazeehomecare.Donors.Model.ItemModel;
import com.example.nyumbayawazeehomecare.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsList extends AppCompatActivity {
    private TextView txv_donationID,txv_date,
            txv_name,txv_status;
    private ProgressBar progressBar;
    private Button btn_confirm;
    private String donationID;
    private List<ItemModel> list;
    private AdaptorDonationItems adaptor;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        txv_date=findViewById(R.id.txv_date);
        txv_status=findViewById(R.id.txv_status);
        txv_name=findViewById(R.id.txv_name);
        txv_donationID=findViewById(R.id.txv_donationID);
        btn_confirm=findViewById(R.id.btn_confirm);
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);

        btn_confirm.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        Intent in=getIntent();
        donationID=in.getStringExtra("donationID");
        txv_donationID.setText("Donation No "+in.getStringExtra("donationID"));
        txv_date.setText("Donation date "+in.getStringExtra("donationDate"));
        txv_status.setText("Status "+in.getStringExtra("donationStatus"));
        txv_name.setText(in.getStringExtra("name"));

        if(in.getStringExtra("donationStatus").equals("Delivered")){
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("Confirm delivery");
        }

        if(in.getStringExtra("donationStatus").equals("Pending approval")){
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("Approve donation");
        }

        btn_confirm.setOnClickListener(v->{
                if(in.getStringExtra("donationStatus").equals("Pending approval")) {
                    approve();
                }else if(in.getStringExtra("donationStatus").equals("Delivered")){
                    confirm();
                }
        }
        );

        items();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void approve() {
        btn_confirm.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_APPROVE_DONATION,
                response -> {

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("1")){
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("E "," "+e.toString());
                        btn_confirm.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }, error -> {
            error.printStackTrace();
            Log.e("ERROR"," "+error.toString());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            btn_confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("donationID",donationID);
                Log.e("PARAMS "," "+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void confirm() {
        btn_confirm.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_CONFIRM_DONATION,
                response -> {

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("1")){
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("E "," "+e.toString());
                        btn_confirm.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }, error -> {
            error.printStackTrace();
            Log.e("ERROR"," "+error.toString());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            btn_confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("donationID",donationID);
                Log.e("PARAMS "," "+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void items(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_ITEMS_DONATED,
                response -> {
                    Log.e("RESPONSE",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("1")){

                            JSONArray jsonArray= jsonObject.getJSONArray("details");
                            for(int i =0; i<jsonArray.length();i++){
                                JSONObject jsn=jsonArray.getJSONObject(i);
                                String itemName=jsn.getString("itemName");
                                String quantity=jsn.getString("quantity");

                                ItemModel model=new ItemModel( itemName,quantity);
                                list.add(model);
                            }
                            adaptor=new AdaptorDonationItems(getApplicationContext(),list);
                            recyclerView.setAdapter(adaptor);
                            progressBar.setVisibility(View.GONE);

                        }else{
                            String message=jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("ERROR e",e.toString());
                    }

                }, error -> {
            error.printStackTrace();
            Log.e("ERROR",error.toString());
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("donationID",donationID);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}