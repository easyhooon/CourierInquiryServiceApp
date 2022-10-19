package kr.ac.konkuk.deliveryinquiryservice

import android.app.Application
import androidx.work.Configuration
import kr.ac.konkuk.deliveryinquiryservice.di.appModule
import kr.ac.konkuk.deliveryinquiryservice.work.AppWorkerFactory
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TrackerApplication: Application(), Configuration.Provider {

    private val workerFactory: AppWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) {
                    Level.DEBUG
                } else {
                    Level.NONE
                }
            )
            // androidContext 를 넘겨줌
            androidContext(this@TrackerApplication)
            // 어떤 모듈로 시작할 것인지 지정
            modules(appModule)
        }
    }

    // 기존의 사용하는 일반적인 workerFactory 가 아닌 자체적으로 구현한 AppWorkerFactory 를 통해 Factory 를 생성하게
    // WorkManager 2.1.0 이후로 부터 변경된 구현 방식
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) {
                    android.util.Log.DEBUG
                } else {
                    android.util.Log.INFO
                }
            )
            .setWorkerFactory(workerFactory)
            .build()
}