package com.jtschwartz.proxytoggle

import com.intellij.notification.*
import com.intellij.openapi.project.Project


object Notification {
	fun settingsChange(content: String, project: Project? = null) {
		NotificationGroupManager.getInstance().getNotificationGroup("Proxy Setting Toggle Notifications")
			.createNotification(content, NotificationType.IDE_UPDATE)
			.notify(project)
	}
}