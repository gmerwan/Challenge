package com.wunder.challenge.ui.placemark

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.wunder.challenge.R
import com.wunder.challenge.base.BaseViewModel
import com.wunder.challenge.model.PlaceMark
import com.wunder.challenge.model.PlaceMarkDao
import com.wunder.challenge.network.PlaceMarkApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaceMarkListViewModel(private val placeMarkDao: PlaceMarkDao): BaseViewModel(){
    @Inject
    lateinit var placeMarkApi: PlaceMarkApi
    val placeMarkListAdapter: PlaceMarkListAdapter = PlaceMarkListAdapter()
    lateinit var placeMarks: List<PlaceMark>

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPlaceMarks() }

    private lateinit var subscription: Disposable

    init{
        loadPlaceMarks()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadPlaceMarks(){
        subscription = Observable.fromCallable { placeMarkDao.all }
                .concatMap {
                    dbPlaceMarkList ->
                    if(dbPlaceMarkList.isEmpty()) {
                        placeMarkApi.getPlaceMarks().concatMap {
                            apiPlaceMarkList -> placeMarkDao.insertAll(*apiPlaceMarkList.placemarks.toTypedArray())
                            Observable.just(apiPlaceMarkList.placemarks)
                        }
                    } else
                        Observable.just(dbPlaceMarkList)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePlaceMarkListStart() }
                .doOnTerminate { onRetrievePlaceMarkListFinish() }
                .subscribe(
                        { result -> onRetrievePlaceMarkListSuccess(result) },
                        { error -> onRetrievePlaceMarkListError(error) }
                )
    }

    private fun onRetrievePlaceMarkListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePlaceMarkListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePlaceMarkListSuccess(placeMarkList:List<PlaceMark>){
        placeMarkListAdapter.updatePlaceMarkList(placeMarkList)
        placeMarks = placeMarkList
    }

    private fun onRetrievePlaceMarkListError(error: Throwable){
        errorMessage.value = R.string.place_mark_error
        Log.d("Error Message", error.message)
    }
}