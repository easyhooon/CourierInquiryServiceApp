package kr.ac.konkuk.deliveryinquiryservice.di

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kr.ac.konkuk.deliveryinquiryservice.BuildConfig
import kr.ac.konkuk.deliveryinquiryservice.data.api.SweetTrackerApi
import kr.ac.konkuk.deliveryinquiryservice.data.api.Url
import kr.ac.konkuk.deliveryinquiryservice.data.db.AppDatabase
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingInformation
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingItem
import kr.ac.konkuk.deliveryinquiryservice.data.preference.PreferenceManager
import kr.ac.konkuk.deliveryinquiryservice.data.preference.SharedPreferenceManager
import kr.ac.konkuk.deliveryinquiryservice.data.repository.ShippingCompanyRepository
import kr.ac.konkuk.deliveryinquiryservice.data.repository.ShippingCompanyRepositoryImpl
import kr.ac.konkuk.deliveryinquiryservice.data.repository.TrackingItemRepository
import kr.ac.konkuk.deliveryinquiryservice.data.repository.TrackingItemRepositoryImpl
import kr.ac.konkuk.deliveryinquiryservice.presentation.addtrackingitem.AddTrackingItemContract
import kr.ac.konkuk.deliveryinquiryservice.presentation.addtrackingitem.AddTrackingItemFragment
import kr.ac.konkuk.deliveryinquiryservice.presentation.addtrackingitem.AddTrackingItemPresenter
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackinghistory.TrackingHistoryContract
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackinghistory.TrackingHistoryFragment
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackinghistory.TrackingHistoryPresenter
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackingitems.TrackingItemsContract
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackingitems.TrackingItemsFragment
import kr.ac.konkuk.deliveryinquiryservice.presentation.trackingitems.TrackingItemsPresenter
import kr.ac.konkuk.deliveryinquiryservice.work.AppWorkerFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    // single<TrackingItemRepository> { TrackingItemRepositoryStub() }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }

    // Work
    single { AppWorkerFactory(get(), get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource(), get()) }
    }

    // view 는 getSource() 로 처리
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemContract.Presenter> {
            AddTrackingItemPresenter(
                getSource(),
                get(),
                get()
            )
        }
    }

    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (trackingItem: TrackingItem, trackingInformation: TrackingInformation) ->
            TrackingHistoryPresenter(getSource(), get(), trackingItem, trackingInformation)
        }
    }
}