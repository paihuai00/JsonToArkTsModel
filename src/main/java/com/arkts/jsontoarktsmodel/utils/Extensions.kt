package com.arkts.jsontoarktsmodel.utils

import com.google.gson.Gson
import com.google.gson.JsonElement

/**
 * 扩展函数
 */

fun String.isJSONObject(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return jsonElement.isJsonObject
}

fun String.isJSONArray(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return jsonElement.isJsonArray
}


/**
 * 判断是否为json
 */
fun String.isJSON(): Boolean {
    return isJSONObject() || isJSONArray()
}


/**
 * JSON Schema 是一种用于描述 JSON 文档结构和验证其有效性的规范。
 * 它定义了 JSON 文档的结构、数据类型和约束，允许开发者在文档级别上指定规则，以确保数据的一致性和有效性。
 * JSON Schema 是一种被广泛用于验证和文档化 JSON 数据的标准。
 */
fun String.isJSONSchema(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return if (jsonElement.isJsonObject) {
        with(jsonElement.asJsonObject) {
            has("\$schema")
        }
    } else {
        false
    }
}



