package com.example.axel10.douban;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.MovieAdapter;
import Bean.MovieBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView mTextMessage;
    private Context mContext;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String url;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);

                    url = "https://api.douban.com/v2/movie/in_theaters?count=10";
                    initView(url);

                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);

                    url = "https://api.douban.com/v2/movie/coming_soon?count=10";
                    initView(url);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);

                    url = "https://api.douban.com/v2/movie/top250?count=10";
                    initView(url);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.mContext = this;


        //----------------------listView------------------------------

        String url = "https://api.douban.com/v2/movie/in_theaters?count=10";
        initView(url);





    }


    private void initView(String url){
        final ListView mv_news = findViewById(R.id.list_view);
//
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                System.out.println("我是异步线程,线程Id为:" + Thread.currentThread().getId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String res = response.body().string();
                            ArrayList<MovieBean> arrayList = new ArrayList<>();

//                            List<MovieBean> movieBeanList = JSON.parseArray(res, MovieBean.class);
//                            ArrayList<MovieBean> arrayList = new ArrayList<>(movieBeanList);

                            JSONObject o = JSON.parseObject(res);
                            JSONArray subjects = o.getJSONArray("subjects");
                            for (int i = 0;i<subjects.size();i++){
                                JSONObject jsonObject = subjects.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                MovieBean movieBean = new MovieBean();

                                JSONArray genres = jsonObject.getJSONArray("genres");
                                List<String> strings = genres.toJavaList(String.class);

//                                JSONArray directors = jsonObject.getJSONArray("directors");
//                                String url = directors.getJSONObject(0).getString("alt");


                                movieBean.title = title;
                                movieBean.content = join(strings.toArray(),",");
                                movieBean.url = jsonObject.getString("alt");
                                movieBean.thumbnail = jsonObject.getJSONObject("images").getString("small");

                                arrayList.add(movieBean);
                                System.out.println(1);
                            }

                            MovieAdapter adapter = new MovieAdapter(arrayList, mContext);
                            mv_news.setAdapter(adapter);
                            mv_news.setOnItemClickListener((AdapterView.OnItemClickListener) mContext);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MovieBean movieBean = (MovieBean) adapterView.getItemAtPosition(i);
        String url = movieBean.url;
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }


    public static String join( Object[] o , String flag ){
        StringBuffer str_buff = new StringBuffer();

        for(int i=0 , len=o.length ; i<len ; i++){
            str_buff.append( String.valueOf( o[i] ) );
            if(i<len-1)str_buff.append( flag );
        }

        return str_buff.toString();
    }
}
