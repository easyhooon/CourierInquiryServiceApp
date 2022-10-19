package kr.ac.konkuk.deliveryinquiryservice.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingItem

@Dao
interface TrackingItemDao {

    // Room DB 에 Observable 하게 접근을 하기 위해
    // -> Data 의 변경사항이 있을 때 어떤 액션을 하기 위함
    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems(): Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    //Primary 키 기준 겹치면 무시 (새로 추가하지 않는다)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrackingItem)

    @Delete
    suspend fun delete(item: TrackingItem)
}