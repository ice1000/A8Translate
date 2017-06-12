package org.a8sport.translate.bean

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.util.containers.HashMap
import com.intellij.util.xmlb.XmlSerializerUtil
import org.a8sport.translate.main.PREFIX_NAME

/**
 * Created by ice1000 on 2017/4/13.

 * @author ice1000
 */
@State(name = PREFIX_NAME, storages = arrayOf(Storage(file = StoragePathMacros.APP_CONFIG + "/a8translate_cache.xml")))
class LocalDataBean : PersistentStateComponent<LocalDataBean> {
	private val storage = HashMap<String, String>(30)

	override fun getState() = this

	override fun loadState(localDataBean: LocalDataBean) = XmlSerializerUtil.copyBean(localDataBean, this)
}
