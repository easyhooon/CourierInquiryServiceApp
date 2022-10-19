package kr.ac.konkuk.deliveryinquiryservice.data.repository

import kotlinx.coroutines.flow.Flow
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingInformation
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingItem

interface TrackingItemRepository {

    val trackingItems: Flow<List<TrackingItem>>

    // 택배 목록 가져오기
    suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>>

    // 단일 택배 정보 가져오기
    suspend fun getTrackingInformation(companyCode: String, invoice: String): TrackingInformation?

    suspend fun saveTrackingItem(trackingItem: TrackingItem)

    suspend fun deleteTrackingItem(trackingItem: TrackingItem)

}