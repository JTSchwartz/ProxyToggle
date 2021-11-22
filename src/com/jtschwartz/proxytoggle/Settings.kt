package com.jtschwartz.proxytoggle

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.*
import com.intellij.util.ui.FormBuilder
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JPanel

@State(
	name = "com.jtschwartz.proxytoggle.SettingsState",
	storages = [Storage("ProxyToggleSettings.xml")]
      )
class ProxyToggleSettingsState: PersistentStateComponent<ProxyToggleSettingsState> {
	companion object {
		fun getInstance(): ProxyToggleSettingsState? = ApplicationManager.getApplication().getService(ProxyToggleSettingsState::class.java)
	}
	var useAutomaticProxy = false
	var useManualProxy = true
	var useNoProxy = true
	
	override fun getState(): ProxyToggleSettingsState = this
	
	override fun loadState(state: ProxyToggleSettingsState) {
		XmlSerializerUtil.copyBean(state, this)
	}
}

class ProxyToggleSettingsComponent {
	private val label = JBLabel("Which proxy settings should be used in rotation:")
	val useNoProxy = JBCheckBox("No proxy settings")
	val useAutomaticProxy = JBCheckBox("Automatic proxy settings")
	val useManualProxy = JBCheckBox("Manual proxy settings")
	val panel: JPanel = FormBuilder.createFormBuilder()
		.addComponent(label, 1)
		.addComponent(useNoProxy, 1)
		.addComponent(useAutomaticProxy, 1)
		.addComponent(useManualProxy, 1)
		.addComponentFillVertically(JPanel(), 0)
		.panel
	
}

class ProxyToggleSettingsConfigurable: Configurable {
	private var settings: ProxyToggleSettingsComponent? = ProxyToggleSettingsComponent()
	
	override fun createComponent(): JComponent {
		settings = ProxyToggleSettingsComponent()
		return settings!!.panel
	}
	
	override fun isModified(): Boolean {
		settings?: return false
		ProxyToggleSettingsState.getInstance()?.let { state ->
			return settings!!.useAutomaticProxy.isSelected != state.useAutomaticProxy || settings!!.useManualProxy.isSelected != state.useManualProxy || settings!!.useNoProxy.isSelected != state.useManualProxy
		}
		return false
	}
	
	override fun apply() {
		settings?: return
		ProxyToggleSettingsState.getInstance()?.let { state ->
			state.useAutomaticProxy = settings!!.useAutomaticProxy.isSelected
			state.useManualProxy = settings!!.useManualProxy.isSelected
			state.useNoProxy = settings!!.useNoProxy.isSelected
		}
	}
	
	override fun reset() {
		settings?: return
		ProxyToggleSettingsState.getInstance()?.let { state ->
			settings!!.useAutomaticProxy.isSelected = state.useAutomaticProxy
			settings!!.useManualProxy.isSelected = state.useManualProxy
			settings!!.useNoProxy.isSelected = state.useNoProxy
		}
	}
	
	override fun disposeUIResources() {
		settings = null
	}
	
	@Nls(capitalization = Nls.Capitalization.Title)
	override fun getDisplayName(): String = "Proxy Toggle"
	
	override fun getPreferredFocusedComponent(): JComponent {
		if (settings ==  null) settings = ProxyToggleSettingsComponent()
		return settings!!.useAutomaticProxy
	}
	
}