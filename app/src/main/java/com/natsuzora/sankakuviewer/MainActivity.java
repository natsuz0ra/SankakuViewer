package com.natsuzora.sankakuviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class  MainActivity extends AppCompatActivity {
    public static String tages = "";
    public static int page_num = 1;

    public static int is_refresh = 0;
    public static int is_searched = 0;
    public static int is_loadingmore = 0;

    private Menu mMenu;

    public static List<itemBean> mData = new ArrayList<>();
    public static RecyclerView recyclerview;
    public static ImageViewAdapter adapter;
    public static ImageViewAdapter2 adapter2;
    public static SwipeRefreshLayout swiperefreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        swiperefreshlayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        swiperefreshlayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        handlerDownPullUpdate();
    }

    @SuppressLint("HandlerLeak")
    public static Handler load_handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                if (is_refresh == 0 & is_searched == 0 & is_loadingmore == 0){
                    recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,1));

                    adapter2 = new ImageViewAdapter2(R.layout.item_list_view,mData);
                    recyclerview.setAdapter(adapter2);
                    //adapter = new ImageViewAdapter(mData);
                    //recyclerview.setAdapter(adapter);

                    //initClickListener();
                    is_searched = 1;

                    adapter2.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            is_loadingmore = 1;
                            page_num += 1;
                            catchcapi capi = new catchcapi();
                            capi.start();
                        }
                    },recyclerview);
                    adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            itemBean selectBean = (itemBean)adapter.getItem(position);
                            System.out.println(selectBean.id);
                            view.getContext().startActivity(new Intent(view.getContext(), imagePreviewActivity.class).putExtra("id",selectBean.id));
                        }
                    });
                }else if (is_refresh == 0 & is_searched == 1 & is_loadingmore == 0){
                    adapter2.notifyDataSetChanged();
                }else if (is_refresh == 0 & is_searched == 1 & is_loadingmore == 1){
                    adapter2.notifyDataSetChanged();
                    adapter2.loadMoreComplete();
                    is_loadingmore = 0;
                }else if (is_refresh == 1){
                    adapter2.notifyDataSetChanged();
                    swiperefreshlayout.setRefreshing(false);
                    is_refresh = 0;
                }
            }else{
                if (is_loadingmore == 1){
                    adapter2.loadMoreFail();
                    page_num -= 1;
                    is_loadingmore = 0;
                }else if (is_refresh == 1){
                    is_refresh = 0;
                }
            }
        }
    };

    private void handlerDownPullUpdate() {
        swiperefreshlayout.setEnabled(true);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mData.isEmpty()){
                    is_refresh = 1;
                    page_num = 1;

                    catchcapi capi = new catchcapi();
                    capi.start();
                }else{
                    swiperefreshlayout.setRefreshing(false);
                }
            }
        });
    }

    public void initClickListener(){
        adapter.setOnItemClickListener(new ImageViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,String id) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search_view,menu);
        this.mMenu = menu;
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView search_tage = (SearchView)searchItem.getActionView();
        search_tage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tages = query;
                if (is_searched == 1){
                    page_num = 1;
                }
                catchcapi capi = new catchcapi();
                capi.start();
                search_tage.clearFocus();

                if (mMenu != null) {
                    (mMenu.findItem(R.id.action_search)).collapseActionView();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
