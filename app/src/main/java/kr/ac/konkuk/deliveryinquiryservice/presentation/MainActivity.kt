package kr.ac.konkuk.deliveryinquiryservice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import kr.ac.konkuk.deliveryinquiryservice.R
import kr.ac.konkuk.deliveryinquiryservice.databinding.ActivityMainBinding
import kr.ac.konkuk.deliveryinquiryservice.work.TrackingCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initWorker()
    }

    private fun initView() {
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
        // navigation graph 에 설정한 label 이 toolbar 에 업데이트 되도록
        // androidx toolbar 만 적용됨, 기본 toolbar x
        binding.toolbar.setupWithNavController(navigationController)
    }

    // Activity 에서 Worker 관련 작업 실행
    private fun initWorker() {
        val workerStartTime = Calendar.getInstance()
        workerStartTime.set(Calendar.HOUR_OF_DAY, 16)
        // 최초 시작을 지연
        // 오후 한시에 앱을 켰을 경우 지정한 시각 까지 지연 (3시간) 후 반복
        // initial Delay 가 음수면 바로 노티가 시작하기 때문에 간단하게 테스트할때는 숫자를 변경해줌으로써 가능
        val initialDelay = workerStartTime.timeInMillis - System.currentTimeMillis()
        val dailyTrackingCheckedRequest =
            // 주기적인 WorkerRequest 생성
            // 반복 주기 최소 15분
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                // .setInitialDelay(0, TimeUnit.MILLISECONDS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    // 재시도 정책
                .setBackoffCriteria(
                    // 실패했을 경우 몇초 뒤에 다시 시도
                    // EXPONENTIAL 재시도할때 마다 그 기다리는 시간이 증가
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(this)
                // 반복되는 작업을 중복되지 않게 이름으로 구분
                // 이미 존재하면 KEEP <- 새로운 것으로 대체하지 않고 기존 것을 유지,
                // 시작할때마다 initWorker() 가 호출되지만 이미 등록된 Worker 가 있으면 갈아끼워지지 않음
            .enqueueUniquePeriodicWork(
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyTrackingCheckedRequest
            )
    }
}