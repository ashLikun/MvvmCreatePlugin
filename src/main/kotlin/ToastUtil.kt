import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import javax.swing.JComponent

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/20　19:55
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
internal object ToastUtil {
    fun make(jComponent: JComponent?, type: MessageType?, text: String?) {
        JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(text!!, type, null)
            .setFadeoutTime(7500)
            .createBalloon()
            .show(RelativePoint.getCenterOf(jComponent!!), Balloon.Position.above)
    }

    fun make(project: Project?, type: MessageType?, text: String?) {
        val statusBar = WindowManager.getInstance().getStatusBar(project!!)
        make(statusBar.component, type, text)
    }
}