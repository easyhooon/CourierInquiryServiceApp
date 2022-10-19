package kr.ac.konkuk.deliveryinquiryservice.presentation

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT
}