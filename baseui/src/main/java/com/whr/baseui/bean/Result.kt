package com.whr.baseui.bean

class Result<T> {

    /**
     * 接口响应
     * code : 200
     * message :
     * data : {"status":"open"}
     */

    var code: Int = 0
    var message: String? = null
    var result: T? = null
    var exception: String? = null
    var exception_msg: String? = null
    var ok: Boolean = true
}