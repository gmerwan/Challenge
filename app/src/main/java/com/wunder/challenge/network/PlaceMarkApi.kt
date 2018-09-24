package com.wunder.challenge.network

import com.wunder.challenge.model.CarsResponse
import io.reactivex.Observable
import retrofit2.http.GET


/**
 * The interface which provides methods to get result of webservices
 */
interface PlaceMarkApi {
    /**
     * Get the list of the place marks from the API
     */
    @GET("wunderbucket/locations.json")
    fun getPlaceMarks(): Observable<CarsResponse>
}