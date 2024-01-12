package com.arkts.jsontoarktsmodel.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

object ArkTsUtil {

    /**
     * 1. boolean ： true 需要继续遍历
     */
    fun getTsType(key: String, jsonElement: JsonElement): Triple<Boolean, String, JsonElement> {

        //需要继续遍历
        if (jsonElement.isJsonObject || jsonElement.isJsonArray) {
            return Triple(true, key, jsonElement)
        } else if (jsonElement.isJsonPrimitive) {
            val jsonPrimitive = jsonElement as JsonPrimitive
            return if (jsonPrimitive.isString) {
                Triple(false, "string", jsonElement)
            } else if (jsonPrimitive.isNumber) {
                Triple(false, "number", jsonElement)
            } else if (jsonPrimitive.isBoolean) {
                Triple(false, "boolean", jsonElement)
            } else {
                Triple(false, "any", jsonElement)
            }
        }
        return Triple(false, "any", jsonElement)
    }


    fun getJsonPrimitiveType(jsonPrimitive: JsonPrimitive): String {
        return if (jsonPrimitive.isString) {
            "string"
        } else if (jsonPrimitive.isNumber) {
            "number"
        } else if (jsonPrimitive.isBoolean) {
            "boolean"
        } else {
            "any"
        }
    }

    /**
     * 获取 数组 中的具体类型
     */
    fun getJsonArrayType(key: String, jsonElement: JsonElement): String {
        if (jsonElement.isJsonObject) {
            return "$key[]"
        }

        if (jsonElement.isJsonArray && !(jsonElement as JsonArray).isEmpty) {
            val temp = jsonElement.asJsonArray[0]
            if (temp.isJsonObject) {
                return "$key[]"
            }
            if (temp.isJsonPrimitive) {
                val jsonPrimitive = temp as JsonPrimitive
                return if (jsonPrimitive.isString) {
                    "string[]"
                } else if (jsonPrimitive.isNumber) {
                    "number[]"
                } else if (jsonPrimitive.isBoolean) {
                    "boolean[]"
                } else {
                    "any[]"
                }
            }

        }

        return "any[]"
    }
}