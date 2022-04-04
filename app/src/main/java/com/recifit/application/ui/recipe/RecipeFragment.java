package com.recifit.application.ui.recipe;



import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.recifit.application.MainActivity;
import com.recifit.application.R;
import com.recifit.application.WriteActivity;
import com.recifit.application.common.ProfileData;
import com.recifit.application.databinding.FragmentMylistBinding;
import com.recifit.application.databinding.FragmentRecipeBinding;
import com.recifit.application.ui.mylist.MyListAdapter;
import com.recifit.application.ui.mylist.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecipeFragment extends Fragment {

    // 로그에 사용할 TAG 변수
    final private String TAG = getClass().getSimpleName();
    private FragmentRecipeBinding binding;


    // 사용할 컴포넌트 선언
    ListView listView;
    private String userId;
    private String todayDate;
    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        long now = System.currentTimeMillis();
        Date date2 = new Date(now);

        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WriteActivity.class);
                Toast.makeText(getActivity(), "오늘의 레시피 등록!",Toast.LENGTH_SHORT).show();
                intent.putExtra("userId", ProfileData.getUserId());
                startActivity(intent);

            }
        });

            // 컴포넌트 초기화

        listView = (ListView) root.findViewById(R.id.listView);

        MyListAdapter adapter = new MyListAdapter();
        listView.setAdapter(adapter);


        userId = ProfileData.getUserId();

        //DB에서 레시피 불러오기
        WriteActivity.myDBHelper myHelper = new WriteActivity.myDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from groupTBL;", null);

//        String string1 = "Movie title" + System.lineSeparator();
//        String string2 = "Director" + System.lineSeparator();
//        String string3 = "Released Year" + System.lineSeparator();
        String string1 = "";
        String string2 = "";
        String string3 = "";

//        string1 += "------------"+System.lineSeparator();
//        string2 += "------------"+System.lineSeparator();
//        string3 += "------------"+System.lineSeparator();

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:MM");
        todayDate = sdf2.format(date2);
        cursor.moveToLast();
        cursor.moveToNext();
        while (cursor.moveToPrevious()){
                string1 = cursor.getString(0);
                string2 = cursor.getString(1);
                string3 = cursor.getString(2);
                adapter.addDBItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.thumbnail2), string3, "database", string1, string2, "좋아요: " + 0, "조회수: " + 0, "  등록일 = " + todayDate);
                }

//        binding.textView4.setText(string1);
//        binding.textView5.setText(string2);
//        binding.textView6.setText(string3);

        cursor.close();
        sqlDB.close();


        try {

            // 결과물 JSONArray 형태
            String test = "[{\"id\":\"a\",\"profileImage\":1,\"date\":\"2021-11-30 12:11\",\"title\":\"스타벅스 오레오 프라푸치노\\uD83D\\uDE2D \",\"content\":\"바닐라 크림 프라푸치노에 자바칩 2번 갈아넣기 \n 에스프레스 휘핑 추가에 초코 드리즐 추가하기\\uD83D\\uDE2D \",\"like\":1500,\"view\":1896}," +
                    "{\"id\":\"b\",\"profileImage\":2,\"date\":\"2021-11-28 15:25\",\"title\":\"에그마요 꿀조합 소스\",\"content\":\"빵: 플렛브래드, \n 재료추가: 베이컨 추가(무조건!), 치주추가 : 아메리칸 치즈, 야채: 취향대로 (할라피뇨 빼고! - 안어울림), 소스: 스위트 칠리 + 스위트 어니언)\",\"like\":256,\"view\":810}," +
                    "{\"id\":\"c\",\"profileImage\":3,\"date\":\"2021-11-18 18:25\",\"title\":\"베스킨라빈스 할로윈 조합\",\"content\":\"초콜릿 무스, 초코나무 숲, 엄마는 외계인, 아몬드 봉봉\n 초코덕후들은 놓칠 수 없는 조합이죠! \n진한 초코 맛에 씹는 맛까지 최강!!\",\"like\":876,\"view\":1265},"+
                    "{\"id\":\"c\",\"profileImage\":4,\"date\":\"2021-11-11 11:25\",\"title\":\"공차 초콜렛밀크티\",\"content\":\"진하고 풍부한 초코맛을 느끼고 싶은 분에게 추천! \n 얼음 보통, 당도 70%, 밀크폼 추가, 타피오카펄 추가\",\"like\":659,\"view\":1653}]"
                    ;

            JSONArray jsonArray = new JSONArray(test);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.optString("title");
                String content = jsonObject.optString("content");
                String date = jsonObject.optString("date");
                int profileImage = Integer.parseInt(jsonObject.optString("profileImage"));
                int like = Integer.parseInt(jsonObject.optString("like"));
                int view = Integer.parseInt(jsonObject.optString("view"));

                String id = jsonObject.optString("id");




                switch (profileImage) {
                    case 1 : adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.character2),ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.recipe1), id, title, content, "좋아요: "+like,"조회수: "+view, "  등록일 = "+date);
                        break;
                    case 2 : adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.character3),ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.recipe2), id, title, content, "좋아요: "+like,"조회수: "+view, "  등록일 = "+date);
                        break;
                    case 3 : adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.character4),ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.recipe3), id, title, content, "좋아요: "+like,"조회수: "+view, "  등록일 = "+date);
                        break;
                    default : adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.thumbnail),ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.recipe4), id, title, content, "좋아요: "+like,"조회수: "+view, "  등록일 = "+date);
                       break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View root, int position, long l) {
                MyListView item = (MyListView) adapterView.getItemAtPosition(position);


            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}