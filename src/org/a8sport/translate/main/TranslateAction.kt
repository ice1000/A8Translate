package org.a8sport.translate.main

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.JBColor
import org.a8sport.translate.bean.TranslationBean
import org.a8sport.translate.net.HttpUtils
import org.a8sport.translate.net.TranslateCallBack
import java.awt.Color

/**
 * Created by Pinger on 2016/12/10.
 * 翻译插件动作
 *
 *
 * 需要完成的逻辑有三段：
 * 第一：获取选中的单词
 * 第二：联网查询选中的单词意思，返回json，然后解析
 * 第三：弹出PopupWindow，显示结果

 * @author ice1000
 * *
 * @author Pinger
 */

class TranslateAction : AnAction() {
	private lateinit var editor: Editor
	private var latestClickTime = 0L  // 上一次的点击时间

	override fun actionPerformed(e: AnActionEvent) {
		if (!isFastClick(1000)) performTranslation(e)
	}

	/**
	 * 执行翻译
	 */
	private fun performTranslation(e: AnActionEvent) {
		/* 第一步 --> 选中单词 */
		// 获取动作编辑器
		editor = e.getData(PlatformDataKeys.EDITOR) ?: return

		// 获取选择模式对象
		val model = editor.selectionModel

		// 选中文字
		val selectedText = model.selectedText ?: return
		if (selectedText.isBlank()) return

		/* 第二步 ---> API查询 */
		HttpUtils.requestNetData(selectedText, object : TranslateCallBack<TranslationBean>() {
			override fun onSuccess(result: TranslationBean) = showPopupWindow(result.toString())
			override fun onFailure(message: String) = showPopupWindow(message)
			override fun onError(message: String) = showPopupWindow(message)
		})
	}

	/**
	 * 第三步 --> 弹出对话框

	 * @param result string result
	 */
	private fun showPopupWindow(result: String) {
		ApplicationManager.getApplication().invokeLater {
			JBPopupFactory.getInstance()
					.createHtmlTextBalloonBuilder(result, A8_ICON, JBColor(Color(186, 238, 186), Color(73, 117, 73)), null)
					.setFadeoutTime(8000)
					.createBalloon()
					.show(JBPopupFactory.getInstance().guessBestPopupLocation(editor), Balloon.Position.below)
		}
	}

	/**
	 * 屏蔽多次选中
	 */
	private fun isFastClick(timeMillis: Long): Boolean {
		val begin = System.currentTimeMillis()
		val end = begin - latestClickTime
		if (end in 1..(timeMillis - 1)) return true
		latestClickTime = begin
		return false
	}

}

val A8_ICON = IconLoader.getIcon("/icons/a8.png")
