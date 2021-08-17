package zy.nav.service

import com.google.gson.Gson
import zy.nav.JsonMarshaller
import java.lang.reflect.Type

/**
 * created by zhangyuan on 2020/5/28
 */
class TestJsonServiceImpl : JsonMarshaller {

    private val json by lazy {
        Gson()
    }

    override fun toJson(any: Any?): String = json.toJson(any)

    override fun <T : Any?> fromJson(json: String?, type: Type?): T = this.json.fromJson(json, type)
}