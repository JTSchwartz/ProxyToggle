package com.jtschwartz.proxytoggle

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.net.HttpConfigurable

class ProxyToggle: AnAction() {
	private var selected = 0
	
	override fun update(event: AnActionEvent) {
		event.presentation.isEnabledAndVisible = true
	}
	
	override fun actionPerformed(event: AnActionEvent) {
		val settingsState = ProxyToggleSettingsState.getInstance() ?: return
		val rotation = arrayOf(settingsState.useNoProxy, settingsState.useAutomaticProxy, settingsState.useManualProxy)
		
		HttpConfigurable.getInstance().run {
			for (i in 1..3) {
				val next = (selected + i) % 3
				if (rotation[next]) {
					selected = next
					break
				}
			}
			
			USE_PROXY_PAC = selected == 1
			USE_HTTP_PROXY = selected == 2
			
			when (selected) {
				0 -> Notification.settingsChange("Disabled IDE proxy")
				1 -> Notification.settingsChange("Using automatic IDE proxy configuration")
				2 -> Notification.settingsChange("Using manual IDE proxy configuration")
			}
			
			ApplicationManager.getApplication().saveAll()
		}
	}
}