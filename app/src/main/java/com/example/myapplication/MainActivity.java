package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener      {
    Button btn;
    TextView view;
    private SwipeRefreshLayout swipe;
    ArrayList<Post> myData = new ArrayList<>();
    private ActionBar actionBar;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private AdapterPostList mAdapter;
    private LinearLayoutManager linearLayoutManager;
    String user_id;
    RelativeLayout emptyView;
    DbHelperAdapter dbHelperAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PostList");
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        emptyView = findViewById(R.id.empty_relative_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterPostList(this, myData);
        setDataAdapter();
//


    }
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Rent Your Car");
    }

    private void setDataAdapter() {
        DbHelperAdapter databasehelper = new DbHelperAdapter(getApplicationContext());
        myData = databasehelper.GetAllRide();

        if (myData.size() > 0) {

            mAdapter = new AdapterPostList(this, myData);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(mAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onRefresh() {
        if (swipe.isRefreshing()) {
            if (NetworkCheck.isConnect(getApplicationContext())) {
                loadRecyclerViewData();
            } else {
                Toast.makeText(this, getResources().getString(R.string.connect_to_internet), Toast.LENGTH_LONG).show();
            }

        }
        swipe.setRefreshing(false);
    }
    private void loadRecyclerViewData() {

        String urls = "https://pixeldev.in/webservices/digital_reader/GetAllPostList.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("response");

                    dbHelperAdapter = new DbHelperAdapter(getApplicationContext());
                    dbHelperAdapter.DeleteRideTable();

                    Log.d("MyRideList", "Response: " + response);

                    if (!res.equalsIgnoreCase("failure") ) {

                        JSONArray jsonArray = new JSONArray(res);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject dataObject = jsonArray.getJSONObject(i);

                            String post_id = dataObject.getString("post_id");

                            String post_title = dataObject.getString("post_title");
                            String post_sub_title = dataObject.getString("post_sub_title");
                            String post_content = dataObject.getString("post_content");
                            String post_userid = dataObject.getString("post_userid");
                            String post_username = dataObject.getString("post_username");
                            String post_image = dataObject.getString("post_image");
                            String post_date = dataObject.getString("post_date");
                            String post_link = dataObject.getString("post_link");
                            String topics = dataObject.getString("topics");
                            String post_view = dataObject.getString("post_view");
                            String post_like = dataObject.getString("post_like");
                            String premium_flag = dataObject.getString("premium_flag");


                            dbHelperAdapter.SaveRide(new Post(post_id,post_title,post_sub_title,post_content,post_userid,
                                    post_username,post_date,post_image,post_link,topics,post_view,post_like,premium_flag));
                        }
                        setDataAdapter();

                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", user_id);

                Log.d("MyRideList", "Params: " + params);

                return params;
            }
        }*/;

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
//    public void onResume() {
//        super.onResume();
//        ((AdapterPostList) mAdapter).setOnItemClickListener(new AdapterPostList.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//
//
//                Intent detailIntent = new Intent(getApplicationContext(), RideDetails.class);
//                detailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//                detailIntent.putExtra("full_name", myData.get(position).getRide_fullname());
//                detailIntent.putExtra("user_contact_no", myData.get(position).getRide_user_contact_no());
//                detailIntent.putExtra("from_date", myData.get(position).getRide_from_date());
//                detailIntent.putExtra("to_date", myData.get(position).getRide_to_date());
//                detailIntent.putExtra("pickup_time", myData.get(position).getRide_pickup_time());
//                detailIntent.putExtra("pickup_location", myData.get(position).getRide_pickup_location());
//                detailIntent.putExtra("drop_location", myData.get(position).getRide_drop_location());
//                detailIntent.putExtra("car_no", myData.get(position).getRide_car_no());
//                detailIntent.putExtra("no_seats", myData.get(position).getRide_car_seats());
//                detailIntent.putExtra("driver_name", myData.get(position).getRide_driver_name());
//                detailIntent.putExtra("driver_contact_no", myData.get(position).getRide_driver_name());
//                detailIntent.putExtra("charges", myData.get(position).getRide_charges());
//                detailIntent.putExtra("status", myData.get(position).getStatus());
//                detailIntent.putExtra("unique_id", myData.get(position).getUnique_id());
//
//                startActivity(detailIntent);
//
//
//            }
//
//        });
//    }

}