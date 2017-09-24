package com.example.android.movieapp.utilities;

import com.example.android.movieapp.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrailerJsonUtils {

    public static List<Trailer> getTrailerModelsFromJson(String trailersJsonStr) throws JSONException{

        final String TRAILER_RESULTS = "results";

        final String RESULT_ID = "id";
        final String RESULT_KEY = "key";
        final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

        List<Trailer> parsedTrailersData;

        JSONObject trailersJson = new JSONObject(trailersJsonStr);

        JSONArray trailerArray = trailersJson.getJSONArray(TRAILER_RESULTS);

        parsedTrailersData = new ArrayList<>();

        for (int i = 0; i < trailerArray.length(); i++) {
            Trailer trailerModel = new Trailer();
            JSONObject trailerJson = trailerArray.getJSONObject(i);

            String youtubeLink = BASE_YOUTUBE_URL + trailerJson.getString(RESULT_KEY);

            trailerModel.setId(RESULT_ID);
            trailerModel.setYoutubeLink(youtubeLink);

            parsedTrailersData.add(trailerModel);
        }

        return parsedTrailersData;
    }
}
