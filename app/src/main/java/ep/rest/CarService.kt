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

        @get:GET("cars")
        val all: Call<List<Car>>

        @GET("cars/{id}")
        operator fun get(@Path("id") id: Int): Call<Car>

        @DELETE("books/{id}")
        fun delete(@Path("id") id: Int): Call<Void>

        @FormUrlEncoded
        @POST("cars")
        fun insert(@Field("marka") marka: String,
                   @Field("slika") slika: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int): Call<Void>

        @FormUrlEncoded
        @PUT("cars/{id}")
        fun update(@Path("id") id: Int,
                   @Field("marka") marka: String,
                   @Field("slika") slika: String,
                   @Field("cena") cena: Int,
                   @Field("aktiven") aktiven: Int): Call<Void>

        companion object {
            val URL = "http://10.0.2.2:8080/netbeans/mvc-rest/api/"
        }


    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(RestApi.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(RestApi::class.java)
    }
}