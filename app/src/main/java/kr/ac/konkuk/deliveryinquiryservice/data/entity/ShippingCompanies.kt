package kr.ac.konkuk.deliveryinquiryservice.data.entity

import com.google.gson.annotations.SerializedName

data class ShippingCompanies(

    // 꼭 하나의 SerializedName 으로 쓸 필요 없음, 대체체 앱에서 더 잘 어울리는 네임으로 변경
    @SerializedName("Company", alternate = ["Recommend"])
    val shippingCompanies: List<ShippingCompany>? = null
)
