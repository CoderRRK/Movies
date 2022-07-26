package android.example.movies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.movies.R;
import android.example.movies.data.MovieAdapter;
import android.example.movies.model.Movie;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ArrayList<Movie> movies;
    RequestQueue requestQueue;
    SearchView searchView;
    String url;
    String word;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);


        searchView = findViewById(R.id.search_view);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movies = new ArrayList<>();
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                word = query.trim();
                getMovies(word);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movies = new ArrayList<>();
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                word = newText.trim();
                getMovies(word);
                return true;
            }

        });
    }

    public void getMovies(String word) {

        url = "http://www.omdbapi.com/?apikey=21e39d79&s=" + word;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Search");
                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String posterUrl = jsonObject.getString("Poster");

                        Movie movie = new Movie();
                        movie.setTitle(title);
                        movie.setYear(year);
                        movie.setPosterUrl(posterUrl);

                        movieAdapter = new MovieAdapter(MainActivity.this, movies);
                        recyclerView.setAdapter(movieAdapter);

                        movies.add(movie);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        requestQueue.add(request);
    }

}