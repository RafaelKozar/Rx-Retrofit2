package bakeapp.rako.githubrxjava


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by rako on 09/05/2018.
 */
interface GitHubService {
    @GET("users/{user}/starred")
    fun getStarredRepositories(@Path("user") userName: String): Observable<List<GitHubRepo>>
}