package com.example.android.movieapp.utilities;

import com.example.android.movieapp.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewJsonUtils {

    public static List<Review> getReviewModelsFromJson(String reviewsJsonStr) throws JSONException{

        final String TRAILER_RESULTS = "results";

        final String RESULT_ID = "id";
        final String RESULT_KEY = "key";

        List<Review> parsedReviewsData;

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        JSONArray reviewArray = reviewsJson.getJSONArray(TRAILER_RESULTS);

        parsedReviewsData = new ArrayList<>();

        for (int i = 0; i < reviewArray.length(); i++) {
            Review reviewModel = new Review();
            JSONObject reviewJson = reviewArray.getJSONObject(i);

            reviewModel.setId(RESULT_ID);

            parsedReviewsData.add(reviewModel);
        }

        return parsedReviewsData;
    }
}
