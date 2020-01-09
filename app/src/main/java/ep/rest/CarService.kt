package ep.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object CarService {

    interface RestApi {

        companion object {
            const val URL = "http://10.0.2.2:8080/netbeans/EP/"
        }

        @GET("json/bazaInit.php")
        fun getAll(): Call<List<Car>>

        @GET("json/bazaInit.php?id_avto={id_avto}")
        fun get(@Path("id_avto") id_avto: Int): Call<Car>

        @DELETE("json/bazaInit.php?id_avto={id_avto}")
        fun delete(@Path("id_avto") id_avto: Int): Call<Void>

        @FormUrlEncoded
        @POST("json/bazaInit.php")
        fun insert(@Field("marka") marka: String,
                   @Field("opis") opis: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int): Call<Void>

        @FormUrlEncoded
        @PUT("json/bazaInit.php?id_avto={id_avto}")
        fun update(@Path("id_avto") id_avto: Int,
                   @Field("marka") marka: String,
                   @Field("opis") opis: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int): Call<Void>




    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(RestApi.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(RestApi::class.java)
    }
}