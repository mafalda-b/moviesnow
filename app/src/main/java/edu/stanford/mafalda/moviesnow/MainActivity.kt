package edu.stanford.mafalda.moviesnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AbsCallback
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Headers
import okhttp3.Response
import java.io.IOException

private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val NOW_PLAYING_ENDPOINT = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY"
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Make network request to fetch now playing movies
        val httpClient = AsyncHttpClient()
        // this happens in the background thread
        httpClient.get(NOW_PLAYING_ENDPOINT, object: JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i(TAG, "onSuccess $statusCode")
                // First make sure that the file has data to parse
                if (json == null) {
                    Log.w(TAG, "json is null, cannot parse data")
                    return
                }
                var allMovieTitles =""
                // Parse the data
                val moviesJsonArray = json.jsonObject.getJSONArray("results")
                Log.i(TAG, "$moviesJsonArray")
                // why do we have to create the val movieJsonObject
                for (i in 0 until moviesJsonArray.length()) {
                    val movieJsonObject = moviesJsonArray.getJSONObject(i)
                    val movieTitle = movieJsonObject.getString("title")
                    val movieDate = movieJsonObject.getString("release_date")
                    allMovieTitles += movieTitle + " released on the " + movieDate +"\n"
                }
                tvMovies.text = allMovieTitles
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure $statusCode")
            }

        })
        Log.i(TAG, "heeey!!! this is executed before the OnSuccess Method gets called and proves that it goes on the background")
        // Display all titles
    }
}
