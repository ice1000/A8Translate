package org.a8sport.translate.bean;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.containers.HashMap;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.a8sport.translate.main.LocalData;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by ice1000 on 2017/4/13.
 *
 * @author ice1000
 */
@SuppressWarnings("deprecation")
@State(name = LocalData.PREFIX_NAME, storages = {
		@Storage(
				file = StoragePathMacros.APP_CONFIG + "/a8translate_cache.xml"
		)})
public class LocalDataBean implements PersistentStateComponent<LocalDataBean> {
	private Map<String, String> storage = new HashMap<>(30);

	@Nullable
	@Override
	public LocalDataBean getState() {
		return this;
	}

	@Override
	public void loadState(LocalDataBean localDataBean) {
		XmlSerializerUtil.copyBean(localDataBean, this);
	}
}
