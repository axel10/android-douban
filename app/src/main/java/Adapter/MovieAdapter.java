package Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.axel10.douban.R;

import java.util.ArrayList;

import Bean.MovieBean;
import views.MyImageView;

public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieBean> movieList;
    private Context context;

    public MovieAdapter(ArrayList<MovieBean> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view;
        if (convertView!=null){
            view = convertView;
        }else{
            view = View.inflate(context, R.layout.list_item,null);
        }
        TextView view_title = view.findViewById(R.id.tv_title);
        TextView view_content = view.findViewById(R.id.item_content);
        MyImageView imageView = view.findViewById(R.id.miv_img);

        MovieBean movieBean = movieList.get(i);
        view_title.setText(movieBean.title);
        view_content.setText(movieBean.content);
        imageView.setImageUrl(movieBean.thumbnail);

        return view;
    }
}
