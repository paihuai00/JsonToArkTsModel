package com.arkts.jsontoarktsmodel.widgets

import com.arkts.jsontoarktsmodel.utils.ArkTsUtil
import com.arkts.jsontoarktsmodel.utils.LogUtils
import com.arkts.jsontoarktsmodel.utils.isJSON
import com.arkts.jsontoarktsmodel.utils.isJSONObject
import com.google.gson.*
import kotlin.reflect.full.starProjectedType

/**
 * 核心类
 */
class ArkTsMaker(val json: String) {
    private val TAG = "ArkTsMaker"

    //左右的内部jsonList
    val innerJsonList = mutableListOf<Pair<String, JsonElement>>()

    //当前正在处理的内部json
    var currentInnerElement: Pair<String, JsonElement>? = null

    val stringBuffer = StringBuffer()

    //生成class前缀
    val CLASS_BEGIN = "export interface %s {\n"
    val CLASS_END = "}\n\n\n"

    fun makeArkTsModel(): String {
        stringBuffer.setLength(0)
        if (!json.isJSON()) {
            LogUtils.log(TAG, "this is not json")
            return "this is not json"
        }
        dealJson(json)

        return stringBuffer.toString()
    }

    private fun dealJson(json: String) {
        if (json.isJSONObject()) {
            dealJsonObj(json)
        } else {
            dealJsonArray(json)
        }
    }


    private fun dealJsonObj(json: String) {
        val jsonObj = Gson().fromJson(json, JsonObject::class.java)

        // 处理 : export interface
        val beginStr = if (currentInnerElement != null) {
            String.format(CLASS_BEGIN, currentInnerElement?.first?.capitalize())
        } else {
            String.format(CLASS_BEGIN, "XxModel")
        }
        currentInnerElement = null

        stringBuffer.append(beginStr)
        jsonObj.entrySet().forEach {
            val tsTypeTriple = ArkTsUtil.getTsType(it.key, it.value)
            LogUtils.log(TAG, "key: ${it.key}, value: ${it.value} , tsType = ${tsTypeTriple.toString()}")

            if (tsTypeTriple.first) {
                innerJsonList.add(Pair(it.key, it.value))
                if (it.value.isJsonObject) {
                    stringBuffer.append("${it.key}: ${it.key.capitalize()};")
                } else if (it.value.isJsonArray) {
                    stringBuffer.append("${it.key}: ${ArkTsUtil.getJsonArrayType(it.key.capitalize(), it.value)};")
                }
                stringBuffer.append("\n")
            } else {
                // 添加这样的结构
                stringBuffer.append("${it.key}: ${tsTypeTriple.second};")
                stringBuffer.append("\n")
            }

        }
        stringBuffer.append(CLASS_END)


        LogUtils.log(TAG, "dealJsonObj $stringBuffer")

        // 递归处理内部json
        if (innerJsonList.isNotEmpty()) {
            currentInnerElement = innerJsonList.removeAt(0)
            currentInnerElement?.let {
                dealJson(it.second.toString())
            }
        }
    }

    private fun dealJsonArray(json: String) {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)

        if (jsonArray.isJsonArray && !jsonArray.isEmpty) {
            /**
             * list 类型, 遍历
             *
             * 解析最长的obj即可
             */
            val largestJsonObject = jsonArray.filterIsInstance<JsonObject>().maxByOrNull { it.entrySet().size }
            largestJsonObject?.let {
                dealJsonObj(it.toString())
            }

//            if (largestJsonObject == null && jsonArray[0].isJsonPrimitive) {
//                LogUtils.log(TAG, "json array 中的元素为常见变量")
//                val normalType = ArkTsUtil.getJsonPrimitiveType(jsonArray[0] as JsonPrimitive)
//                if (currentInnerElement != null) {
//                    stringBuffer.append(String.format("${currentInnerElement?.first}: $normalType[];"))
//                    currentInnerElement = null
//                }
//            }


        } else {
            LogUtils.log(TAG, "this is not json array")
        }

    }


}