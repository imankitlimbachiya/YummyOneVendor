package yummy.one.yummyonevendor.Functionality.Retrofit;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    //@FormUrlEncoded
    @POST("/v1/api/auth/vendor/signInWithPhoneNumber")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<JSONObject> signInWithPhoneNumber(@Header("ApiKey") String apiKey, @Body RequestBody body);


    @POST("/v1/api/auth/vendor/signInWithPhoneNumber")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    public void login(
            @Header("ApiKey") String apiKey, @Body RequestBody body,
            Callback<JSONObject> callback);
}
