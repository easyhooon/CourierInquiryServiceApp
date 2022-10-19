package kr.ac.konkuk.deliveryinquiryservice.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import kr.ac.konkuk.deliveryinquiryservice.data.api.SweetTrackerApi
import kr.ac.konkuk.deliveryinquiryservice.data.db.TrackingItemDao
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingInformation
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingItem

class TrackingItemRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    // 현재 버전은 stateFlow 가 distinctUntilChanged 연산자 기능을 가지고 있기 때문에
    // 업데이트 될 때 같은 값이라면 filter 됨
    override val trackingItems: Flow<List<TrackingItem>> =
        trackingItemDao.allTrackingItems().distinctUntilChanged()


    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll()
                .mapNotNull { trackingItem ->
                    val relatedTrackInfo = trackerApi.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    ).body()

                    if (relatedTrackInfo?.invoiceNo.isNullOrBlank()) {
                        null
                    } else {
                        trackingItem to relatedTrackInfo!!
                    }
                }
                .sortedWith(
                    compareBy(
                        // 배송 상태별 정렬 우선, 같은 상태일 경우(배송완료) 최근순으로 정렬
                        { it.second.level },
                        { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) }
                    )
                )
        }

    override suspend fun getTrackingInformation(
        companyCode: String,
        invoice: String
    ): TrackingInformation? =
        trackerApi.getTrackingInformation(companyCode, invoice)
            .body()
            ?.sortTrackingDetailByTimeDescending()

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = withContext(dispatcher) {
        val trackingInformation = trackerApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if (!trackingInformation!!.errorMessage.isNullOrEmpty()) {
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) {
        trackingItemDao.delete(trackingItem)
    }

    private fun TrackingInformation.sortTrackingDetailByTimeDescending() =
        copy(trackingDetails = trackingDetails?.sortedByDescending { it.time ?: 0L })
}