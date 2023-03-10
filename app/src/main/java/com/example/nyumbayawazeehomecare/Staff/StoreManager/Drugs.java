package com.example.nyumbayawazeehomecare.Staff.StoreManager;

import static com.example.nyumbayawazeehomecare.ConstFiles.Urls.URL_DRUG_STOCK;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nyumbayawazeehomecare.R;
import com.example.nyumbayawazeehomecare.Staff.StoreManager.Adaptor.AdaptorDrugs;
import com.example.nyumbayawazeehomecare.Staff.StoreManager.Model.DrugsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Drugs extends AppCompatActivity {
    private ProgressBar progressBar;
    private List<DrugsModel> list;
    private AdaptorDrugs adaptor;
    private RecyclerView recyclerView;
    private TextView txv_status;
    private Button btn_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs);


        txv_status=findViewById(R.id.txv_status);
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        btn_request=findViewById(R.id.btn_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        btn_request.setOnClickListener(v->{
            Intent in=new Intent(this,Suppliers.class);
            startActivity(in);
        });

        drugs();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void drugs(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_DRUG_STOCK,
                response -> {
                    Log.e("RESPONSE",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("1")){

                            JSONArray jsonArray= jsonObject.getJSONArray("details");
                            for(int i =0; i<jsonArray.length();i++){
                                JSONObject jsn=jsonArray.getJSONObject(i);
                                String drugName=jsn.getString("drugName");
                                String stock=jsn.getString("stock");

                                DrugsModel model=new DrugsModel(   drugName,  stock);
                                list.add(model);
                            }
                            adaptor=new AdaptorDrugs(getApplicationContext(),list);
                            recyclerView.setAdapter(adaptor);
                            progressBar.setVisibility(View.GONE);
                            txv_status.setVisibility(View.GONE);

                        }else{
                            String message=jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            txv_status.setText(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("ERROR e",e.toString());
                    }

                }, error -> {
            error.printStackTrace();
            Log.e("ERROR",error.toString());
        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("donationID",donationID);
//                return params;
//            }
//        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}