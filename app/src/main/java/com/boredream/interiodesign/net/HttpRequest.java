package com.boredream.interiodesign.net;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.interiodesign.base.BaseEntity;
import com.boredream.interiodesign.constants.CommonConstants;
import com.boredream.interiodesign.entity.Community;
import com.boredream.interiodesign.entity.HouseType;
import com.boredream.interiodesign.entity.OpenDate;
import com.boredream.interiodesign.entity.User;
import com.boredream.interiodesign.utils.UserInfoKeeper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class HttpRequest {
    // Bmob
//    public static final String HOST = "https://api.bmob.cn";
//    public static final String FILE_HOST = "http://file.bmob.cn/";
//
//    public static final String APP_ID_NAME = "X-Bmob-Application-Id";
//    public static final String API_KEY_NAME = "X-Bmob-REST-API-Key";
//    public static final String SESSION_TOKEN_KEY = "X-Bmob-Session-Token";
//
//    public static final String APP_ID_VALUE = "06e70a02d2950057ac4c5153460b06b2";
//    public static final String API_KEY_VALUE = "9d2e47ead033bdbe516b3b7277f31f5a";

    // LeanCloud
    public static final String HOST = "https://api.leancloud.cn";
    public static final String FILE_HOST = "";

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    public static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "xXxN4uWNOWRxUXcbXd4NS1YA-gzGzoHsz";
    private static final String API_KEY_VALUE = "nfAi2yBqPYqWPopLHcPyFhJu";


    private static Retrofit retrofit;
    private static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    static {
        // OkHttpClient
        httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getToken())
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.networkInterceptors().add(new StethoInterceptor()); // stetho 浏览器调试工具

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public interface BmobService {
        // 登录用户
        @GET("/1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机验证注册
        @POST("/1/users")
        Observable<User> userRegist(
                @Body User user);

        // 忘记密码重置
        @PUT("/1/resetPasswordBySmsCode/{smsCode}")
        Observable<Object> resetPasswordBySmsCode(
                @Path("smsCode") String smsCode,
                @Body Map<String, Object> password);

        // 旧密码修改新密码
        @POST(" /1/updateUserPassword/{objectId}")
        Observable<User> updateUserPassword(
                @Path("smsCode") String smsCode,
                @Body UpdatePswRequest updatePswRequest);

        // 根据昵称搜索用户
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getUserByName(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 动态图收藏用户列表
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getGifFavUsers(
                @Query("where") String where);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getCurrentUser(
                @Path("objectId") String userId);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getUserById(
                @Path("objectId") String userId);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<BaseEntity> updateUserById(
                @Path("objectId") String userId,
                @Body Map<String, Object> updateInfo);

        // 上传图片接口
        @POST("/1/files/{fileName}")
        Observable<FileUploadResponse> fileUpload(
                @Path("fileName") String fileName,
                @Body RequestBody image);

        // 查询app更新信息
        @GET("/1/classes/AppUpdateInfo")
        Observable<ListResponse<AppUpdateInfo>> getAppUpdateInfo();

        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String fileUrl);

        ////////
        // 云函数
        @POST("/1/functions/hello")
        Observable<BaseEntity> cloudHello(
                @Body Object entity);

        @GET("/1/functions/scrapy")
        Observable<BaseEntity> cloudScrapy();

        ////////

        // 添加小区
        @POST("/1/classes/Community")
        Observable<BaseEntity> addCommunity(
                @Body Community entity);

        // 修改小区
        @PUT("/1/classes/Community/{objectId}")
        Observable<BaseEntity> updateCommunity(
                @Path("objectId") String objectId,
                @Body Map<String, Object> updateInfo);

        // 查询小区
        @GET("/1/classes/Community")
        Observable<ListResponse<Community>> getCommunity(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 添加户型
        @POST("/1/classes/HouseType")
        Observable<BaseEntity> addHouseType(
                @Body HouseType entity);

        // 修改户型
        @PUT("/1/classes/HouseType/{objectId}")
        Observable<BaseEntity> updateHouseType(
                @Path("objectId") String objectId,
                @Body Map<String, Object> updateInfo);

        // 查询户型
        @GET("/1/classes/HouseType")
        Observable<ListResponse<HouseType>> getHouseType(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询户型
        @GET("/1/classes/HouseType")
        Observable<ListResponse<HouseType>> getHouseTypeOrder(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("order") String order,
                @Query("include") String include);

        // 添加开盘时间
        @POST("/1/classes/OpenDate")
        Observable<BaseEntity> addOpenDate(
                @Body OpenDate entity);

        // 修改开盘时间
        @PUT("/1/classes/OpenDate/{objectId}")
        Observable<BaseEntity> updateOpenDate(
                @Path("objectId") String objectId,
                @Body Map<String, Object> updateInfo);

        // 查询开盘时间
        @GET("/1/classes/OpenDate")
        Observable<ListResponse<OpenDate>> getOpenDate(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询开盘时间-排序
        @GET("/1/classes/OpenDate")
        Observable<ListResponse<OpenDate>> getOpenDateOrder(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include,
                @Query("order") String order);
    }

    public static BmobService getApiService() {
        BmobService service = retrofit.create(BmobService.class);
        return service;
    }

    /**
     * 查询小区
     */
    public static Observable<ListResponse<Community>> getCommunity(
            int page, String area, ArrayList<String> communityTypes) {
        BmobService service = getApiService();

        StringBuilder sbTypes = new StringBuilder();
        for (int i = 0; communityTypes != null && i < communityTypes.size(); i++) {
            if (i > 0) {
                sbTypes.append(", ");
            }
            sbTypes.append("\"").append(communityTypes.get(i)).append("\"");
        }

        String where;
        if (!TextUtils.isEmpty(area) && TextUtils.isEmpty(sbTypes.toString())) {
            where = "{\"area\":\"" + area + "\"}";
        } else if (TextUtils.isEmpty(area) && !TextUtils.isEmpty(sbTypes.toString())) {
            where = "{\"community_types\":{\"$all\":[" + sbTypes.toString() + "]}}";
        } else if (!TextUtils.isEmpty(area) && !TextUtils.isEmpty(sbTypes.toString())) {
            where = "{\"$and\":[" +
                    "{\"area\":\"" + area + "\"}," +
                    "{\"community_types\":{\"$all\":[" + sbTypes.toString() + "]}}," +
                    "]}";
        } else {
            // 条件都为空，则查询全部课程
            where = "{}";
        }
        return service.getCommunity(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "include");
    }

    /**
     * 查询小区
     *
     * @param page
     * @param community_id 小区类型:
     */
    public static Observable<ListResponse<Community>> getCommunity(int page, String community_id) {
        BmobService service = getApiService();
        String whereCommunity_id = "{}";
        if (!TextUtils.isEmpty(community_id)) {
            whereCommunity_id = "{\"community_id\":\"" + community_id + "\"}";
        }
        String where = whereCommunity_id;
        return service.getCommunity(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);
    }

    /**
     * 查询户型
     */
    public static Observable<ListResponse<HouseType>> getHouseType(int page, String size, String type) {
        BmobService service = getApiService();

        String where;
        if (!TextUtils.isEmpty(size) && TextUtils.isEmpty(type)) {
            size = size.replace("㎡", "");
            where = getTypeWhere(size);
        } else if (TextUtils.isEmpty(size) && !TextUtils.isEmpty(type)) {
            type = type.replace("及以上", "");
            where = "{\"typeName\":{\"$regex\":\".*" + type + ".*\"}}";
        } else if (!TextUtils.isEmpty(size) && !TextUtils.isEmpty(type)) {
            size = size.replace("㎡", "");
            String sizeWhere = getTypeWhere(size);

            type = type.replace("及以上", "");
            String typeWhere = "{\"typeName\":{\"$regex\":\".*" + type + ".*\"}}";

            where = "{\"$and\":[" +
                    sizeWhere + "," +
                    typeWhere +
                    "]}";
        } else {
            // 条件都为空，则查询全部课程
            where = "{}";
        }

        return service.getHouseType(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "community");
    }

    private static String getTypeWhere(String size) {
        String where;
        if (size.contains("以下")) {
            float sizeNum = Float.parseFloat(size.replace("以下", ""));
            where = "{\"size\":{\"$lt\":" + sizeNum + "}}";
        } else if (size.contains("以上")) {
            float sizeNum = Float.parseFloat(size.replace("以上", ""));
            where = "{\"size\":{\"$gte\":" + sizeNum + "}}";
        } else {
            float startSizeNum = Float.parseFloat(size.split("-")[0]);
            float endSizeNum = Float.parseFloat(size.split("-")[1]);
            where = "{\"size\":{\"$gte\":" + startSizeNum + ", \"$lt\":" + endSizeNum + "}}";
        }
        return where;
    }

    /**
     * 查询户型
     */
    public static Observable<ListResponse<HouseType>> getHouseTypeTest(int page) {
        BmobService service = getApiService();
        String order = "size";
        return service.getHouseTypeOrder(300,
                (page - 1) * 300, order, "community");
    }

    /**
     * 查询开盘时间
     */
    public static Observable<ListResponse<OpenDate>> getOpenDate(int page) {
        BmobService service = getApiService();
        String where = "{}";
        return service.getOpenDate(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "community");
    }

    /**
     * 查询开盘时间
     */
    public static Observable<ListResponse<OpenDate>> getOpenDate(int page, String order) {
        BmobService service = getApiService();
        String where = "{}";
        return service.getOpenDateOrder(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "community", order);
    }

    //////////////////////////////

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public static Observable<User> login(String username, String password) {
        BmobService service = getApiService();
        return service.login(username, password)
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.saveLoginData(user.getObjectId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 使用token自动登录
     *
     * @param loginData size为2的数组, 第一个为当前用户id, 第二个为当前用户token
     */
    public static Observable<User> loginByToken(final String[] loginData) {
        BmobService service = getApiService();
        // 这种自动登录方法其实是使用token去再次获取当前账号数据
        return service.getUserById(loginData[0])
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // TODO 获取用户信息接口不会返回token
                        user.setSessionToken(loginData[1]);
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.saveLoginData(user.getObjectId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 根据昵称模糊搜索用户,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索昵称
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<User>> getUserByName(String searchKey, int page) {
        BmobService service = getApiService();
        String where = "{\"username\":{\"$regex\":\"" + searchKey + ".*\"}}";
        return service.getUserByName(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 上传图片
     *
     * @param call    上传成功回调
     * @param context
     * @param uri     图片uri
     * @param reqW    上传图片需要压缩的宽度
     * @param reqH    上传图片需要压缩的高度
     * @param call
     */
    public static void fileUpload(final Context context, Uri uri, int reqW, int reqH, final Subscriber<FileUploadResponse> call) {
        final BmobService service = getApiService();
        final String filename = "avatar_" + System.currentTimeMillis() + ".jpg";

        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        Glide.with(context).load(uri).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(reqW, reqH) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // 上传图片
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), resource);

                        Observable<FileUploadResponse> observable = service.fileUpload(filename, requestBody);
                        ObservableDecorator.decorate(context, observable)
                                .subscribe(call);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        call.onError(new Throwable("图片解析失败"));
                    }
                });
    }

}

