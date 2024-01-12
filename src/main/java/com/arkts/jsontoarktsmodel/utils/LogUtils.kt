package com.arkts.jsontoarktsmodel.utils

import com.arkts.jsontoarktsmodel.GlobalConfig

object LogUtils {
    @JvmStatic
    fun log(tag: String, msg: String) {
        if (!GlobalConfig.DEBUG) return
        println("[JsonToArk]  $tag : $msg")
    }
}