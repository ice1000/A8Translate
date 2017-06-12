package org.a8sport.translate.bean

import org.jetbrains.annotations.Contract

/**
 * Created by Pinger on 2016/12/10.
 * 翻译结果javabean
 * @author ice1000
 * *
 * @author Pinger
 */
class TranslationBean {

	/*
     * translation : ["解决"]
     * basic :
     */
	/*
    {
      "us-phonetic": "rɪ'zɑlv",
      "phonetic": "rɪ'zɒlv",
      "uk-phonetic": "rɪ'zɒlv",
      "explains": [
        "n. 坚决；决定要做的事",
        "vt. 决定；溶解；使\u2026分解；决心要做\u2026",
        "vi. 解决；决心；分解"
      ]
    }
    */
	/*
     * query : resolve
     * errorCode : 0
     * web :
     */
	/*
    [
      {
        "value": [
          "决心",
          "决定",
          "解决"
        ],
        "key": "Resolve"
      },
      {
        "value": [
          "归结为",
          "使分解为",
          "分解为"
        ],
        "key": "resolve into"
      },
      {
        "value": [
          "解决分歧",
          "消除分歧"
        ],
        "key": "resolve differences"
      }
    ]
    */

	var basic: BasicBean? = null
	var query: String? = null
	var errorCode: Int = 0
	var translation: List<String>? = null
	var web: List<WebBean>? = null

	inner class BasicBean(
			var usPhonetic: String?,
			var phonetic: String?,
			var ukPhonetic: String?,
			var explains: List<String>?
	)

	inner class WebBean(
			var key: String?,
			var value: List<String>?
	)


	/**
	 * 取错误信息
	 */
	private val errorMessage: String @Contract(pure = true) get() {
		when (errorCode) {
			SUCCESS -> return "成功"
			QUERY_STRING_TOO_LONG -> return "要翻译的文本过长"
			CAN_NOT_TRANSLATE -> return "无法进行有效的翻译"
			INVALID_LANGUAGE -> return "不支持的语言类型"
			INVALID_KEY -> return "无效的key"
			NO_RESULT -> return "无词典结果"
		}
		return "你选中的是什么鬼？"
	}


	/**
	 * 获取不同语言的翻译内容
	 */
	private val phonetic: String by lazy {
		val phonetic = StringBuilder()
		basic?.usPhonetic?.let { phonetic.append("美式: [$it];") }
		basic?.ukPhonetic?.let { phonetic.append("英式: [$it];") }
		basic?.phonetic?.let { phonetic.append("发音: [$it];") }
		phonetic.toString()
	}

	/**
	 * 获取翻译
	 */
	private val explains: String by lazy {
		val result = StringBuilder()
		basic?.explains?.forEach { result.append(it).append("\n") }
		result.toString()
	}

	/**
	 * 获取直接的翻译结果
	 */
	private val translationResult: String by lazy {
		val result = StringBuilder()
		translation?.forEachIndexed { i, v -> result.append(if (0 == i) "" else ", ").append(v) }
		result.append(";").toString()
	}

	/**
	 * 获取网络翻译结果
	 */
	private val webResult: String by lazy {
		val result = StringBuilder()
		if (null != web) result.append("网络释义: \n")
		web?.forEach {
			result.append(it.key).append(": ")
			it.value?.forEachIndexed { i, v -> result.append(if (0 == i) "" else ", ").append(v) }
		}
		result.toString()
	}

	fun isSentence(query: String?) = null != query && " " in query.trim { it <= ' ' }

	/**
	 * 结果
	 */
	override fun toString(): String {
		val string = StringBuilder()
		if (SUCCESS != errorCode) {
			string.append("错误代码：$errorCode\n$errorMessage")
		} else {
			if (translationResult != query)
				string.append(if (isSentence(query)) "$translationResult\n" else "$query：$translationResult\n")
			string.append("$phonetic\n").append(explains).append(webResult)
		}
		if (string.isBlank()) return "抱歉, 你选的内容: $query\n翻译不了..."
		return string.toString()
	}
}

/**
 * 返回错误码的状态，有道返回
 */
const private val SUCCESS = 0  // 成功
const private val QUERY_STRING_TOO_LONG = 20  // 要翻译的文本过长
const private val CAN_NOT_TRANSLATE = 30  // 无法进行有效的翻译
const private val INVALID_LANGUAGE = 40   // 不支持的语言类型
const private val INVALID_KEY = 50  // 无效的key
const private val NO_RESULT = 60   // 无词典结果
const val EMPTY = "返回数据为空"
