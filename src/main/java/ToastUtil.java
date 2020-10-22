import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/20　19:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
class ToastUtil {
    public static void make(JComponent jComponent, MessageType type, String text) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(jComponent), Balloon.Position.above);
    }


    public static void make(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        make(statusBar.getComponent(), type, text);
    }
}
