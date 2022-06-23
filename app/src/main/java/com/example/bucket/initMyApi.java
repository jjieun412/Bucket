package com.example.bucket;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface initMyApi {

    //getLoginResponse 함수로 LoginRequest.java에 정의해준 데이터들을 서버 Body에 보낸 후 LoginResponse로 데이터를 받겠다는 의미
    // 로그인
    @Headers("Authorization:your auth token")
    @POST("user/login")
    Call<JsonObject> getLoginResponse(@Body LoginRequest loginRequest);

    // 회원가입
    @Multipart
    @POST("user/signup")
    Call<JsonObject> getJoinResponse(@Part("nickname") String nickname,
                                     @Part("email") String email,
                                     @Part("password") String password,
                                     @Part("tags") String tags,
                                     @Part MultipartBody.Part picture);

    // 닉네임 중복 확인
    @POST("user/nickname/check_duplicate")
    Call<JsonObject> getNicknameResponse(@Body NicknameRequest nicknameRequest);

    // 이메일 중복 확인
    @POST("user/email/check_duplicate")
    Call<JsonObject> getEmailResponse(@Body EmailRequest emailRequest);


    //비밀번호 찾기 시 인증코드 전송
    @POST("user/password/send_code")
    Call<JsonObject> getSendCodeResponse(@Body SendCodeRequest sendCodeRequest);

    //비밀번호 찾기 시 인증코드 인증
    @POST("user/password/verify_code")
    Call<JsonObject> getVerifyCodeResponse(@Body VerifyCodeRequest verifyCodeRequest);

    // 새 비밀번호 생성
    @POST("user/password/create_pswd")
    Call<JsonObject> getNewPWsetResponse(@Body NewPWsetRequest newPWsetRequest);


    // 버킷리스트 작성-> date
    @Multipart
    @POST("/test/bucketlist")
    Call<JsonObject> getWriteBucketResponse(@Header("Authorization") String token,
                                            @Part("startDate") String startDate,
                                            @Part("endDate") String endDate,
                                            @Part("title") String title,
                                            @Part("content") String content,
                                            @Part("isVisible") Integer isVisible,
                                            @Part("detailPlans") String detailplans,
                                            @Part MultipartBody.Part picture,
                                            @Part("tags") String tags);

    // 버킷리스트 작성-> rule
    @Multipart
    @POST("/test/bucketlist")
    Call<JsonObject> getWriteBucketResponse2(@Header("Authorization") String token,
                                             @Part("startDate") String startDate,
                                             @Part("endDate") String endDate,
                                             @Part("title") String title,
                                             @Part("content") String content,
                                             @Part("isVisible") Integer isVisible,
                                             @Part("detailPlans") String detailplans,
                                             @Part MultipartBody.Part picture,
                                             @Part("tags") String tags);


    // 버킷리스트 추천 (관심카테고리에 등록된 것)
    @GET("/test/bucketlist/recommendation")
    Call<JsonObject> getRecommendResponse(@Header("Authorization") String token);


    // 버킷리스트 검색 (키워드가 태그나 제목에 매칭되어야 함)
    @GET("/test/bucketlist/search")
    Call<JsonObject> getSearchResponse(@Header("Authorization") String token,
                                       @Query("keyword") String keyword);

    // 내가 작성한 버킷리스트 리스트로 보여주기
    @GET("/test/bucketlist/my")
    Call<JsonObject> getMyBKListResponse(@Header("Authorization") String token);


    // 버킷리스트 상세조회 (나의 버킷 확인 리스트에서 클릭시 상세보기페이지)
    @GET("/test/bucketlist")
    Call<JsonObject> getBKListDetailResponse(@Header("Authorization") String token,
                                             @Query("bucketId") Integer bucketId);

    // 버킷리스트 세부계획 달성여부 판단
    @PUT("/test/bucketlist/detailplan/achieved")
    Call<JsonObject> getBKDetailAchievedResponse(@Header("Authorization") String token,
                                                 @Query("bucketId") Integer bucketId,
                                                 @Query("orderNumb") Integer orderNumb,
                                                 @Query("achieved") Integer achieved);


    // 버킷리스트 하트 누르기
    @POST("/test/bucketlist/heart")
    Call<JsonObject> getBKListHeartFullResponse(@Header("Authorization") String token,
                                                @Query("bucketId") Integer bucketId);

    // 버킷리스트 하트 누르기 취소
    @DELETE("/test/bucketlist/heart")
    Call<JsonObject> getBKListHeartNullResponse(@Header("Authorization") String token,
                                                @Query("bucketId") Integer bucketId);

    // 하트누른 버킷리스트만 따로 조회하는 것
    @GET("/test/bucketlist/search/heart")
    Call<JsonObject> getBKListHeartOnlyResponse(@Header("Authorization") String token);



    // 버킷리스트 수정
    @Multipart
    @PUT("/test/bucketlist")
    Call<JsonObject> getEditBucketResponse(@Header("Authorization") String token,
                                             @Part("startDate") String startDate,
                                             @Part("endDate") String endDate,
                                             @Part("title") String title,
                                             @Part("content") String content,
                                             @Part("isVisible") Integer isVisible,
                                             @Part("detailPlans") String detailplans,
                                             @Part MultipartBody.Part picture,
                                             @Part("bucketId") Integer bucketId,
                                             @Part("tags") String tags);

    // 버킷리스트 수정
    @Multipart
    @PUT("/test/bucketlist")
    Call<JsonObject> getEditBucketResponse2(@Header("Authorization") String token,
                                           @Part("startDate") String startDate,
                                           @Part("endDate") String endDate,
                                           @Part("title") String title,
                                           @Part("content") String content,
                                           @Part("isVisible") Integer isVisible,
                                           @Part("detailPlans") String detailplans,
                                           @Part MultipartBody.Part picture,
                                           @Part("bucketId") Integer bucketId,
                                           @Part("tags") String tags);

    // 버킷리스트 삭제
    @DELETE("/test/bucketlist")
    Call<JsonObject> getMyBKListDeleteResponse(@Header("Authorization") String token,
                                               @Query("bucketId") Integer bucketId);

    // 버킷리스트 달성 인증
    @Multipart
    @POST("/test/bucketlist/authenticate")
    Call<JsonObject> getMyBKListAchievedResponse(@Header("Authorization") String token,
                                                 @Part("bucketId") Integer bucketId,
                                                 @Part MultipartBody.Part pict);

    // 버킷리스트 후기 쓰고 등록하기 (사진 최대 10장) -> 달성인증 완료해야만 가능
    @Multipart
    @POST("/test/bucketlist/review")
    Call<JsonObject> getMyBKListReviewResponse(@Header("Authorization") String token,
                                               @Part("bucketId") Integer bucketId,
                                               @Part("content") String content,
                                               @Part MultipartBody.Part picture);


    // 버킷리스트 인증이 안될 때 운영자에게 알리는 것 (문의하기)
    @Multipart
    @POST("/test/bucketlist/verifyrequest")
    Call<JsonObject> getServiceAskResponse(@Header("Authorization") String token,
                                           @Part("bucketId") Integer bucketId,
                                           @Part("content") String content,
                                           @Part MultipartBody.Part picture);

    // 문의한 내역 확인 (문의내역)
    @GET("/test/bucketlist/verifyrequest")
    Call<JsonObject> getServiceAskListResponse(@Header("Authorization") String token);


    // 문의한 내역 삭제
    @DELETE("/test/bucketlist/verifyrequest")
    Call<JsonObject> getServiceAskListDeleteResponse(@Header("Authorization") String token,
                                                     @Query("bucketId") Integer bucketId,
                                                     @Query("requestDate") String requestDate);



    // 유저 정보 보여주기
    @GET("/test/user")
    Call<JsonObject> getMySetPageResponse(@Header("Authorization") String token);

    // 유저 정보 수정
    @Multipart
    @PUT("/test/user")
    Call<JsonObject> getMySetPageEditResponse(@Header("Authorization") String token,
                                              @Part("tags") String tags,
                                              @Part("nickname") String nickname,
                                              @Part MultipartBody.Part picture);

    // 유저 포인트 조회
    @GET("/test/user/point")
    Call<JsonObject> getPointResponse(@Header("Authorization") String token);

    // 유저 포인트 지급 내역
    @GET("/test/user/point/details")
    Call<JsonObject> getPointListResponse(@Header("Authorization") String token);



    // 유저 포인트 랭킹 가져오기
    @GET("/test/user/rankings")
    Call<JsonObject> getPointRankResponse(@Header("Authorization") String token);

}

