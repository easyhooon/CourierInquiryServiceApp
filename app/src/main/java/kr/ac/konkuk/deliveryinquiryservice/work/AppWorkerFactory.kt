package kr.ac.konkuk.deliveryinquiryservice.work

import androidx.work.DelegatingWorkerFactory
import kotlinx.coroutines.CoroutineDispatcher
import kr.ac.konkuk.deliveryinquiryservice.data.repository.TrackingItemRepository

class AppWorkerFactory(
    trackingItemRepository: TrackingItemRepository,
    dispatcher: CoroutineDispatcher
) : DelegatingWorkerFactory() {

    init {
        addFactory(TrackingCheckWorkerFactory(trackingItemRepository, dispatcher))
    }
}