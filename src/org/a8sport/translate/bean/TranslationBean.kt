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

	inner class BasicBean {
		var usPhonetic: String? = null
		var phonetic: String? = null
		var ukPhonetic: String? = null
		var explains: List<String>? = null
	}

	inner class WebBean {
		var key: String? = null
		var value: List<String>? = null
	}


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
	private val phonetic: String?
		get() {
			if (basic == null) return null

			var phonetic: String? = null
			val usPhonetic = basic!!.usPhonetic
			val ukPhonetic = basic!!.ukPhonetic
			if (usPhonetic == null && ukPhonetic == null) {

				phonetic = "发音：[" + basic!!.phonetic + "]；"
			} else {
				if (usPhonetic != null) phonetic = "美式：[$usPhonetic]；"
				if (ukPhonetic != null) {
					if (phonetic == null) phonetic = ""
					phonetic = phonetic + "英式：[" + ukPhonetic + "]；"
				}
			}
			println(phonetic)
			return phonetic
		}

	/**
	 * 获取翻译
	 */
	private val explains: String?
		get() {
			if (basic == null) return null
			var result: StringBuilder? = null
			val explains = basic!!.explains
			if (explains!!.isNotEmpty()) {
				result = StringBuilder()
				for (explain in explains) result.append(explain).append("\n")
			}
			return if (result != null) result.toString() else null
		}

	/**
	 * 获取直接的翻译结果
	 */
	private val translationResult: String?
		get() {
			if (translation == null) return null
			var result: StringBuilder? = null
			if (translation!!.isNotEmpty()) {
				result = StringBuilder()
				for (i in translation!!.indices) {
					val keyword = translation!![i]
					if (i < translation!!.size - 1)
						result.append(keyword).append("，")
					else
						result.append(keyword).append("；")
				}
			}
			return if (result != null) result.toString() else null
		}

	/**
	 * 获取网络翻译结果
	 */
	private val webResult: String?
		get() {
			if (web == null) {
				return null
			}
			var result: StringBuilder? = null
			if (web!!.isNotEmpty()) {
				result = StringBuilder()
				for (webBean in web!!) {
					val key = webBean.key
					result.append(key).append("：")

					val value = webBean.value
					for (i in value!!.indices) {
						val keyword = value[i]
						if (i < value.size - 1)
							result.append(keyword).append("，")
						else
							result.append(keyword)
					}

					result.append("\n")
				}
			}


			return if (result != null) result.toString() else null
		}

	private val isSentence: Boolean
		get() = query!!.trim { it <= ' ' }.contains(" ")

	/**
	 * 结果
	 */
	override fun toString(): String {
		val string = StringBuilder()
		if (errorCode != SUCCESS) {
			string.append("错误代码：$errorCode\n$errorMessage")
		} else {
			var translation = translationResult
			if (translation != null) {
				translation = translation.substring(0, translation.length - 1)
				if (translation != query)
					string.append(if (isSentence) "${translationResult!!}\n" else "$query：$translationResult\n")
			}
			if (null != phonetic) string.append("${phonetic!!}\n")
			if (explains != null) string.append(explains)
			if (webResult != null) string.append("网络释义：\n$webResult")
		}
		if (string.isBlank()) return "你选的内容：$query\n\n抱歉,翻译不了..."
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
