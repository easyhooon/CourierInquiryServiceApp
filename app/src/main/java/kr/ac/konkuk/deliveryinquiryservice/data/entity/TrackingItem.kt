package kr.ac.konkuk.deliveryinquiryservice.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

//Room @Embedded keyword

// 혹시 다른 화사인데 같은 송장 번호를 가질 수 있으므로 회사 코드까지 PK 를 두개로 설정
// Shipping Company 내부의 code 에 접근하기 위해 @Embedded keyword
// Embedded 가 없으면 해당 data class 를 이 data class  내부에 두어야 하니까
// 클래스는 분리하되 불러올 수 있도록 하는게 @Embedded
@Parcelize
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String,
    @Embedded val company: ShippingCompany
) : Parcelable
