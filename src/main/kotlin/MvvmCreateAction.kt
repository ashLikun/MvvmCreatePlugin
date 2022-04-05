import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　10:58
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class MvvmCreateAction : AnAction() {
    private lateinit var project: Project

    //当前需要在哪个文件夹下面创建文件
    private var packageName = ""
    lateinit var event: AnActionEvent

    //当前的文件路径 D:/likun/inteij/AndroidTest/app/src/main/java/com/example/androidtest
    //资源文件路径
    var resPath = ""

    //mainPath
    var mainPath = ""
    lateinit var virtualFile: VirtualFile
    override fun actionPerformed(e: AnActionEvent) {
        project = e.getData(PlatformDataKeys.PROJECT)!!
        event = e
        virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE)!!
        packageName = Utils.getPackageName(virtualFile)
        resPath = Utils.getResPath(virtualFile)
        mainPath = Utils.getMainPath(virtualFile)
        init()
    }

    /**
     * 初始化Dialog
     */
    private fun init() {
        val myDialog = MvvmDialog(
            packageName,
            Utils.getModeName(project, virtualFile)
        ) { data: InputContentData -> WriteCommandAction.runWriteCommandAction(project) { createClassFiles(data) } }
        myDialog.pack()
        myDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(project))
        myDialog.isVisible = true
    }

    /**
     * 生成类文件
     */
    private fun createClassFiles(data: InputContentData) {
        if (data.isActivity) {
            createClassFile(CodeType.Activity, data)
            createClassFile(CodeType.Manifest, data)
        } else {
            createClassFile(CodeType.Fragment, data)
        }
        createClassFile(CodeType.ViewModel, data)
        createClassFile(CodeType.Layout, data)
    }

    /**
     * 生成mvp框架代码
     *
     * @param codeType 类型
     */
    private fun createClassFile(codeType: CodeType, data: InputContentData) {
        var fileName = ""
        var content: String? = ""
        var basePath = if (data.isViewBinding) "/binding" else "/layoutid"
        if (data.isList) {
            basePath = "$basePath/list"
        }
        when (codeType) {
            CodeType.Activity -> {
                fileName = "${basePath}/MainActivity.kt.ftl"
                content = Utils.readTemplateFile(this, fileName)
                content = Utils.dealTemplateContent(project, data, content)
                Utils.writeToFile(content, virtualFile.path + "/view/activity/", data.view + ".kt")
                Utils.addRouterPath(project, virtualFile, data)
            }
            CodeType.Fragment -> {
                fileName = "${basePath}/MainFragment.kt.ftl"
                content = Utils.readTemplateFile(this, fileName)
                content = Utils.dealTemplateContent(project, data, content)
                Utils.writeToFile(content, virtualFile.path + "/view/fragment/", data.view + ".kt")
                Utils.addRouterPath(project, virtualFile, data)
            }
            CodeType.Layout -> {
                fileName = if (data.isList) "activity_main_list.xml.ftl" else "activity_main.xml.ftl"

                content = Utils.readTemplateFile(this, fileName)
                content = Utils.dealTemplateContent(project, data, content)
                Utils.writeToFile(content, "$resPath/layout/", data.layout + ".xml")
            }
            CodeType.ViewModel -> {
                fileName = "${basePath}/MainViewModel.kt.ftl"
                content = Utils.readTemplateFile(this, fileName)
                content = Utils.dealTemplateContent(project, data, content)
                Utils.writeToFile(content, virtualFile.path + "/viewmodel/", data.viewMode + ".kt")
            }
            CodeType.Manifest -> {
                fileName = "AndroidManifest.xml.ftl"
                content = Utils.readTemplateFile(this, fileName)
                content = Utils.dealTemplateContent(project, data, content)
                //合并清单文件
                Utils.registerToManifest(project, content, mainPath)
            }
        }
    }
}