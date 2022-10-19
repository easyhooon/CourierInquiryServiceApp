package kr.ac.konkuk.deliveryinquiryservice.presentation.addtrackingitem

import kr.ac.konkuk.deliveryinquiryservice.data.entity.ShippingCompany
import kr.ac.konkuk.deliveryinquiryservice.presentation.BasePresenter
import kr.ac.konkuk.deliveryinquiryservice.presentation.BaseView

class AddTrackingItemContract {

    interface View : BaseView<Presenter> {

        fun showShippingCompaniesLoadingIndicator()

        fun hideShippingCompaniesLoadingIndicator()

        fun showSaveTrackingItemIndicator()

        fun hideSaveTrackingItemIndicator()

        fun showRecommendCompanyLoadingIndicator()

        fun hideRecommendCompanyLoadingIndicator()

        fun showCompanies(companies: List<ShippingCompany>)

        fun showRecommendCompany(company: ShippingCompany)

        fun enableSaveButton()

        fun disableSaveButton()

        fun showErrorToast(message: String)

        fun finish()
    }

    interface Presenter : BasePresenter {

        var invoice: String?
        var shippingCompanies: List<ShippingCompany>?
        var selectedShippingCompany: ShippingCompany?

        // 택배사 가져옴
        fun fetchShippingCompanies()

        fun fetchRecommendShippingCompany()

        fun changeSelectedShippingCompany(companyName: String)

        // 운송장
        fun changeShippingInvoice(invoice: String)

        fun saveTrackingItem()
    }
}