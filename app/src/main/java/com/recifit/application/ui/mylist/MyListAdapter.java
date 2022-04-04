package com.recifit.application.ui.mylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recifit.application.R;

import java.io.File;
import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장
    private ArrayList<MyListView> myListViewArrayList = new ArrayList<MyListView>() ;

    public MyListAdapter() { }

    // Adapter에 사용되는 데이터의 개수
    @Override
    public int getCount() {
        return myListViewArrayList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_list_view, parent, false);
        }

        // 화면에 표시될 View
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        ImageView recipeImageView = (ImageView) convertView.findViewById(R.id.imageView2);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleText);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.contentText);
        TextView likeTextView = (TextView) convertView.findViewById(R.id.likeText);
        TextView viewTextView = (TextView) convertView.findViewById(R.id.viewText);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateText);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MyListView myListView = myListViewArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(myListView.getIcon());
        String urlString = myListView.getImgUrl().toString();
        if(!urlString.equals("null")) {
            recipeImageView.setImageURI(myListView.getImgUrl());
        } else {
            recipeImageView.setImageDrawable(myListView.getRecipe());
        }
        titleTextView.setText(myListView.getTitle());
        contentTextView.setText(myListView.getContent());
        likeTextView.setText(myListView.getLike());
        viewTextView.setText(myListView.getView());
        dateTextView.setText(myListView.getDate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return myListViewArrayList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(Drawable icon,Drawable recipe, String id, String title, String content, String like, String view, String date) {
        MyListView item = new MyListView();

        item.setIcon(icon);
        item.setRecipe(recipe);
        item.setId(id);
        item.setTitle(title);
        item.setContent(content);
        item.setLike(like);
        item.setView(view);
        item.setDate(date);

        myListViewArrayList.add(item);
    }
    // 아이템 데이터 추가를 위한 함수
    public void addDBItem(Drawable icon,String recipe, String id, String title, String content, String like, String view, String date) {
        MyListView item = new MyListView();

        item.setIcon(icon);
        item.setImgUrl(recipe);
        item.setId(id);
        item.setTitle(title);
        item.setContent(content);
        item.setLike(like);
        item.setView(view);
        item.setDate(date);

        myListViewArrayList.add(item);
    }
}
