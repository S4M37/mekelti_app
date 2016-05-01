package tn.iac.mobiledevelopment.mekelti.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by S4M37 on 17/04/2016.
 */
public interface RetrofitServices {

    @FormUrlEncoded
    @POST("mobile/signin")
    Call<ResponseBody> signin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("mobile/signup")
    Call<ResponseBody> signup(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("region") String region);

    @GET("user/{id_User}")
    Call<ResponseBody> getUser(@Path("id_User") int idUser);

    @FormUrlEncoded
    @POST("user/{id_User}/update")
    Call<ResponseBody> updateUserRegion(@Path("id_User") int idUser, @Field("region") String region);

    @FormUrlEncoded
    @POST("user/{id_User}/update")
    Call<ResponseBody> updateUserName(@Path("id_User") int idUser, @Field("name") String name);

    @GET("user/{id_User}/delete")
    Call<ResponseBody> deleteUser(@Path("id_User") int idUser);

    @GET("user/{id_User}/favoris")
    Call<ResponseBody> getUserFavoris(@Path("id_User") int idUser);

    @FormUrlEncoded
    @POST("user/{id_User}/favoris/add")
    Call<ResponseBody> addFavorisToUser(@Path("id_User") int idUser, @Field("id_Recette") int idRecette);

    @GET("user/{id_User}/favoris/{id_Favoris}/delete")
    Call<ResponseBody> deleteUserFavoris(@Path("id_User") int idUser, @Path("id_Favoris") int idFavoris);

    @GET("user/{id_user}/newsfeed")
    Call<ResponseBody> getNewsFeed(@Path("id_user") int id_user);
}


