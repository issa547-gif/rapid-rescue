package com.example.rapidrescue.ui.navigation

enum class ROUTES {
    Login,
    Register,
    ForgotPassword,
    signup,
    Home,
    onboarding,
    Contacts,
    Alerts,
    Receiver;

    companion object {
        const val ReceiverWithArgs = "Receiver/{lat}/{lng}"
        fun receiverRoute(lat: Double, lng: Double) = "Receiver/$lat/$lng"
    }
}