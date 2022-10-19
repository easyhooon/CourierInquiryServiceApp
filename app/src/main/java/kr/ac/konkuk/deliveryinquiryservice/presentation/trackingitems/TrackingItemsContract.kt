package kr.ac.konkuk.deliveryinquiryservice.presentation.trackingitems

import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingInformation
import kr.ac.konkuk.deliveryinquiryservice.data.entity.TrackingItem
import kr.ac.konkuk.deliveryinquiryservice.presentation.BasePresenter
import kr.ac.konkuk.deliveryinquiryservice.presentation.BaseView

class TrackingItemsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter : BasePresenter {

        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}