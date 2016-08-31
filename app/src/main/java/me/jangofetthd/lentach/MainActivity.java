package me.jangofetthd.lentach;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<VKApiPost> vkPosts;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        vkBackgroundLoading();
                    }
                });

        //Posts
        recyclerView = (RecyclerView) findViewById(R.id.feedview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        vkPosts = new ArrayList<>();

        NewsFeedRecyclerViewAdapter adapter = new NewsFeedRecyclerViewAdapter(vkPosts, this);
        recyclerView.setAdapter(adapter);

        vkBackgroundLoading();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class VkAsyncPostsLoading extends AsyncTask<Void, Void, Void>{

        int[] groups;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefresh.setRefreshing(true);

            groups = getResources().getIntArray(R.array.groups);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            vkPosts.clear();

            for (int group : groups) {
                Map<String, Object> params = new ArrayMap<>(3);
                params.put("owner_id", Integer.toString(group));
                params.put("offset", "0");
                params.put("count", "20");

                VKRequest request = VKApi.wall().get(new VKParameters(params));

                request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        try {
                            JSONObject jsonObject = response.json.getJSONObject("response");
                            Log.w("JSON", "Vk response json: "+jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                vkPosts.add(new VKApiPost(object));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        super.attemptFailed(request, attemptNumber, totalAttempts);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }

                    @Override
                    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                        super.onProgress(progressType, bytesLoaded, bytesTotal);
                    }
                });
            }

            Collections.sort(vkPosts, new VkPostsComparator());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getAdapter().notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }
    }

    private void vkBackgroundLoading(){
        new VkAsyncPostsLoading().execute();
    }


    class VkPostsComparator implements Comparator<VKApiPost>{

        @Override
        public int compare(VKApiPost first, VKApiPost second) {
            return (int) (second.date-first.date);
        }
    }

}