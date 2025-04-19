package otus.homework.coroutines.server

import otus.homework.coroutines.data.CatImage
import otus.homework.coroutines.data.Fact
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Response<Fact>

    @GET
    suspend fun getCatPicture(@Url url: String = "https://api.thecatapi.com/v1/images/search"): Response<List<CatImage>>
}