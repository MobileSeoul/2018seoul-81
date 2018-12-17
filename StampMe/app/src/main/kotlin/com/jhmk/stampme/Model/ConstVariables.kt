package com.jhmk.stampme.Model

object ConstVariables {
    /* Preferences Key*/
    val PREF_KEY_USER_ID = "con.jhmk.string.id"
    val PREF_KEY_USER_PW = "con.jhmk.string.pw"

    /* EventBus Key*/
    val EVENTBUS_REQUELST_LOGIN = 100
    val EVENTBUS_REQUELST_REGISTER = 101
    val EVENTBUS_REQUEST_LOGOUT = 102
    val EVENTBUS_SHOW_LOGIN = 103
    val EVENTBUS_SHOW_REGISTER = 104
    val EVENTBUS_SHOW_SETTINGS = 105
    val EVENTBUS_POP_BACKSTACK = 106
    val EVENTBUS_SEND_CURRENT_STORE = 107
    val EVENTBUS_SUCCESS_SUBMIT = 108
    val EVENTBUS_FAILED_SUBMIT = 109

    /* Handle Fragment Key*/
    val SHOW_FRAGMENT_LOGIN = 200
    val SHOW_FRAGMENT_REGISTER = 201
    val SHOW_FRAGMENT_SETTINGS = 202
    val FRAGMENT_BACKSTACK = 203

    /* Common Variables*/
    val USER_TYPE_SELLER = 0
    val USER_TYPE_BUYER = 1

    /* Shop Type*/
    val SHOP_TYPE_CAFE = "카페"
    val SHOP_TYPE_RESTRAUNT = "식당"
    val SHOP_TYPE_STORE = "편의점"
    val SHOP_TYPE_MART = "마트"
    val SHOP_TYPE_PUBLIC = "공공시설"
    val SHOP_TYPE_ETC = "기타"

    /* Select Tab*/
    val TAB_STAMPYOU = 0
    val TAB_HOME = 1
    val TAB_STAMPME = 2

    /* Dialog Type*/
    val DIALOG_TYPE_BARCODE = 1
    val DIALOG_TYPE_SUBMIT_SUCCESS = 2
    val DIALOG_TYPE_SUBMIT_FAILED = 3
}