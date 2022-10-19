package kr.ac.konkuk.deliveryinquiryservice.data.repository

import kr.ac.konkuk.deliveryinquiryservice.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}