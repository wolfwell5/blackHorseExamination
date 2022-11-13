package com.example.dealcontracts.constants

enum class BiddingPickupStatus(val biddingPickupResponseCode: String, val text: String) {
    ACCEPT_REQUEST("30", "您的提货申请已成功受理"),
    WILL_ACCEPT_REQUEST("31", "您的提货申请正在处理，我们将于24小时之内为您更新受理信息，请稍后查看"),
}

