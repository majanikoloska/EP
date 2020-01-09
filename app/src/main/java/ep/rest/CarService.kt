package ep.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object CarService {

    interface RestApi {

        companion object {
            const val URL = "http://10.0.2.2:8080/netbeans/EP/"
        }

        @GET("json/bazaInit.php")
        fun getAll(): Call<List<Car>>

        @DELETE("json/bazaInit.php?id_avto={id_avto}")
        fun delete(@Path("id_avto") id_avto: Int): Call<Void>

        @GET("json/bazaInit.php")
        fun get(@Query("id_avto") id_avto: Int): Call<Car>

        @FormUrlEncoded
        @POST("json/bazaInit.php")
        fun insert(@Field("marka") marka: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int,
                   @Field("opis") opis: String): Call<Void>

        @FormUrlEncoded
        @PUT("json/bazaInit.php?id_avto={id_avto}")
        fun update(@Path("id_avto") id_avto: Int,
                   @Field("marka") marka: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int,
                   @Field("opis") opis: String): Call<Void>




    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(RestApi.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(RestApi::class.java)
    }
}