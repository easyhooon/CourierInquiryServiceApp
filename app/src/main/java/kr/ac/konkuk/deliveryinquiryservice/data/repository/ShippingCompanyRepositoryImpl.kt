package kr.ac.konkuk.deliveryinquiryservice.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.ac.konkuk.deliveryinquiryservice.data.api.SweetTrackerApi
import kr.ac.konkuk.deliveryinquiryservice.data.db.ShippingCompanyDao
import kr.ac.konkuk.deliveryinquiryservice.data.entity.ShippingCompany
import kr.ac.konkuk.deliveryinquiryservice.data.preference.PreferenceManager

class ShippingCompanyRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val shippingCompanyDao: ShippingCompanyDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        val currentTimeMillis = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMillis =
            preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        // 최근 저장된 데이터가 없거나 유효 기간을 초과한 경우
        // 새로 데이터를 가져외서 DB 의 Dao 를 통해 저장
        if (lastDatabaseUpdatedTimeMillis == null ||
            CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdatedTimeMillis)
        ) {
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()
            shippingCompanyDao.insert(shippingCompanies)
            // 데이터를 저장한 시점을 sharedPreference 에 저장
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? =
        withContext(dispatcher) {
            try {
                // 추천된 택배사 목록 들 중 가장 메이저한 회사를 반환
                trackerApi.getRecommendShippingCompanies(invoice)
                    .body()
                    ?.shippingCompanies
                        // Company code 가 낮을수록 메이저한 회사들이라 낮은 순으로
                    ?.minByOrNull { it.code.toIntOrNull() ?: Int.MAX_VALUE }
            } catch (exception: Exception) {
                null
            }
        }


    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS =
            "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7 // 일주일
    }
}