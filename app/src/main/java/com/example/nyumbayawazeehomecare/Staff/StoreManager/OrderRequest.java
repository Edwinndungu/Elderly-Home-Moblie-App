package com.example.nyumbayawazeehomecare.Staff.StoreManager;

import static com.example.nyumbayawazeehomecare.ConstFiles.Urls.URL_ORDER_REQUEST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nyumbayawazeehomecare.ConstFiles.SessionHandler;
import com.example.nyumbayawazeehomecare.ConstFiles.UserModel;
import com.example.nyumbayawazeehomecare.R;
import com.example.nyumbayawazeehomecare.Staff.StoreManager.Adaptor.AdaptorOrdersRequests;
import com.example.nyumbayawazeehomecare.Staff.StoreManager.Model.OrdersRequestsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRequest extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<OrdersRequestsModel> list;
    private AdaptorOrdersRequests adaptor;
    private TextView txv_status;
    private SessionHandler session;
    private UserModel user;
    private String requestStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        txv_status=findViewById(R.id.txv_status);

        session=new SessionHandler(this);
        user=session.getUserDetails();

        Intent in=getIntent();
        requestStatus=in.getStringExtra("requestStatus");
        getSupportActionBar().setSubtitle(in.getStringExtra("requestStatus"));
        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        getBookings();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                adaptor.getFilter().filter(newText);

                return true;
            }

        });
    }
    private void getBookings(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_ORDER_REQUEST,
                response -> {
                    Log.e("RESPONSE",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("1")){
                            list.clear();
                            JSONArray jsonArray= jsonObject.getJSONArray("details");
                            for(int i =0; i<jsonArray.length();i++){
                                JSONObject jsn=jsonArray.getJSONObject(i);
                                String requestID=jsn.getString("requestID");
                                String name=jsn.getString("name");
                                String requestDate=jsn.getString("requestDate");
                                String requestStatus=jsn.getString("requestStatus");

                                OrdersRequestsModel model=new OrdersRequestsModel( requestID,  name,  requestDate,  requestStatus);
                                list.add(model);
                            }
                            adaptor=new AdaptorOrdersRequests(getApplicationContext(),list);
                            recyclerView.setAdapter(adaptor);
                            progressBar.setVisibility(View.GONE);
                            txv_status.setVisibility(View.GONE);
                        }else{
                            String message=jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            txv_status.setText(message);
                            txv_status.setVisibility(View.VISIBLE);
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
                params.put("requestStatus",requestStatus);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}