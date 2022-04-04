package com.recifit.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.recifit.application.common.ProfileData;


public class LoginActivity extends AppCompatActivity {
    private ImageButton loginV1;
    private Button loginV2;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginV1 = findViewById(R.id.loginV1);
        loginV2 = findViewById(R.id.loginV2);


        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        session.checkAndImplicitOpen();

        loginV1.setOnClickListener(v -> {
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                Log.d(TAG, "onClick: 로그인 세션살아있음");
                // 카카오 로그인 시도 (창이 안뜬다.)
                sessionCallback.requestMe();

            } else {
                Log.d(TAG, "onClick: 로그인 세션끝남");
                // 카카오 로그인 시도 (창이 뜬다.)
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

        loginV2.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);


            Log.i(TAG,"게스트 비회원 로그인");

            new ProfileData("9999999", "게스트 비회원", "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg", "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg");

            // 프로필 (닉네임, 프로필 사진 경로)
            intent.putExtra("userId", "9999999");
            intent.putExtra("nickName", "게스트 비회원");
            intent.putExtra("thumbnail",  "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg");


            startActivity(intent);
            finish();


        });

  /*      logout.setOnClickListener(v -> {
            Log.d(TAG, "onCreate:click ");
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            super.onSessionClosed(errorResult);
                            Log.d(TAG, "onSessionClosed: " + errorResult.getErrorMessage());

                        }

                        @Override
                        public void onCompleteLogout() {
                            if (sessionCallback != null) {
                                Session.getCurrentSession().removeCallback(sessionCallback);
                            }
                            Log.d(TAG, "onCompleteLogout:logout ");
                        }
                    });
        });*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("KAKAO_API", "onActivityResult " + requestCode + ":" + resultCode + "::" + data);
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("KAKAO_API", "onActivityResult2 " + requestCode + ":" + resultCode + "::" + data);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + exception.toString(), Toast.LENGTH_SHORT).show();
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            int result = errorResult.getErrorCode();

                            if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }

                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);


                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());
                            String id = String.valueOf(result.getId());

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                // 프로필 (닉네임, 프로필 사진 경로)
                                Profile profile = kakaoAccount.getProfile();
                                new ProfileData(id, profile.getNickname(), profile.getProfileImageUrl(), profile.getThumbnailImageUrl());
                                intent.putExtra("userId", id);
                                intent.putExtra("nickName", profile.getNickname());
                                intent.putExtra("profile", profile.getProfileImageUrl());
                                intent.putExtra("thumbnail", profile.getThumbnailImageUrl());

                                // LOGGING
                                if (profile == null) {
                                    Log.d("KAKAO_API", "onSuccess:profile null ");
                                } else {
                                    Log.d("KAKAO_API", "onSuccess:getProfileImageUrl " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "onSuccess:getThumbnailImageUrl " + profile.getThumbnailImageUrl());
                                    Log.d("KAKAO_API", "onSuccess:getNickname " + profile.getNickname());
                                }
                                if (email != null) {
                                    Log.d("KAKAO_API", "onSuccess:email " + email);
                                }


                            } else {
                                Log.i("KAKAO_API", "onSuccess: kakaoAccount null");
                            }

                            startActivity(intent);
                            finish();
                        }

                    });
        }

    }
}



