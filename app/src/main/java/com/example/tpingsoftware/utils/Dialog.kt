package com.example.tpingsoftware.utils

class Dialog(
    var title:String? = null,
    var description:String?= null,
    var result:TypeDialog? = null
)

enum class TypeDialog {
    GO_TO_HOME,
    DISMISS,
    ON_HOLD,
    GO_TO_LOGIN
}