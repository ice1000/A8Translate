package org.a8sport.translate.main

import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NonNls
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/**
 * Created by ice1000 on 2017/4/13.

 * @author ice1000
 */
const val PREFIX_NAME = "A8Translate"

object LocalData {
	private val p = Properties()
	// private val f = File(System.getProperty("user.home") + "/a8temp.properties")

	init {
		// if (!f.exists()) f.createNewFile()
		// p.load(FileReader(f))
	}

	fun store(@NonNls key: String, @NonNls value: String) {
		p.put(StringUtils.uncapitalize(key), value)
		save()
	}

	fun clear() {
		p.clear()
		save()
	}

	private fun save() = Unit // p.store(FileWriter(f), "Created by a8translate")

	fun read(@NonNls key: String): String? = p.getProperty(StringUtils.uncapitalize(key))
}
