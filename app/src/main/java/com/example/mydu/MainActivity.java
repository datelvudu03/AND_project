package com.example.mydu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String usernameGet, userRepos_url;

    EditText searchedNickname;

    Button submitUserNickname;

    ListView repositoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();

        searchedNickname = findViewById(R.id.github_username);

        submitUserNickname = findViewById(R.id.search_username);
        submitUserNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameGet = searchedNickname.getText().toString();
                /*Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();*/
                requestGet(usernameGet);
                pageGet(userRepos_url);


            }
        });

    }

    public void requestGet(final String username) {
        String URL_GET = "https://api.github.com/search/users?q=" + username;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray itemsArray = response.getJSONArray("items");
                            for (int i = 0; i < itemsArray.length(); i++){
                                JSONObject item = itemsArray.getJSONObject(i);
                                String login = item.getString("login");
                                String repos_url = item.getString("repos_url");

                                if(login.equals(username)){
                                    userRepos_url = repos_url;
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void pageGet(final String url ) {
        int page = 0;
        String URL_GET = url + "?page=" + page;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray repoArray = response.getJSONArray("");
                            for (int i = 0; i < repoArray.length(); i++){
                                JSONObject item = repoArray.getJSONObject(i);
                                String name = item.getString("login");
                                String repos_url = item.getString("repos_url");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, "WIFI OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "NO WIFI OK?", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    public void toWithoutLayout (View view) {
        View tp= view.findViewById(R.id.github_username);
        tp.setVisibility(View.VISIBLE);
    }

    public void search {

    }*/
}


