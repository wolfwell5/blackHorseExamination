package com.example.dealcontracts.constants

enum class PaymentStatus(val paymentResponseCode: String, val text: String) {
    PAYING("00", "支付中"),
    PAYMENT_SUCCEED("10", "支付成功"),
    PAYMENT_FAILED("11", "支付失败"),
    ACCOUNT_NOT_ENOUGH("20", "余额不足，支付失败"),
    PAYMENT_EXCEPTION("21", "支付异常，请稍后重试"),
    LAST_PAYMENT_NOT_FINISHED("22", "支付异常，您的上次支付还未完成，请勿重复支付")
}


