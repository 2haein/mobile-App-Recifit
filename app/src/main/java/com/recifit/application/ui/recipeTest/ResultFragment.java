package com.recifit.application.ui.recipeTest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.recifit.application.MainActivity;
import com.recifit.application.R;
import com.recifit.application.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {
    FragmentResultBinding binding;
    final private String TAG = getClass().getSimpleName();
    int count;
    TextView title, explanation, explanation2;

    MainActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        title = (TextView) root.findViewById(R.id.title);
        explanation = (TextView) root.findViewById((R.id.myResult));
        explanation2 = (TextView) root.findViewById((R.id.myResult2));

        Bundle bundle = getArguments();
        if(bundle != null){
            count = bundle.getInt("key");
            Log.i(TAG, "tag값" + count);
        }


        if(count >= 5){
            explanation.setText("위에서 해당하는 사항이 5개 이상이에요.");
            explanation2.setText("나트륨 섭취량 위험 수준이니, 식습관을 개선하는 것이 좋아요.");
        }
        else{
            explanation.setText("회원님은 나트륨 적정 상태입니다.");
            explanation2.setText("앞으로도 이대로 지켜주세요");
        }


        return root;
    }

}
