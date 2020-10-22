import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;


/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　10:58
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class MvvmCreateAction extends AnAction {
    private Project project;
    //当前需要在哪个文件夹下面创建文件
    private String packageName = "";
    AnActionEvent event;
    //当前的文件路径 D:/likun/inteij/AndroidTest/app/src/main/java/com/example/androidtest
    //资源文件路径
    String resPath = "";
    //mainPath
    String mainPath = "";
    VirtualFile virtualFile;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        event = e;
        virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        packageName = Utils.getPackageName(virtualFile);
        resPath = Utils.getResPath(virtualFile);
        mainPath = Utils.getMainPath(virtualFile);

        init();
    }


    /**
     * 初始化Dialog
     */
    private void init() {
        MvvmDialog myDialog = new MvvmDialog(packageName, Utils.getModeName(project, virtualFile), (data) -> {
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    createClassFiles(data);
                }
            });
        });
        myDialog.pack();
        myDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(project));
        myDialog.setVisible(true);
    }

    /**
     * 生成类文件
     */
    private void createClassFiles(InputContentData data) {
        if (data.isActivity) {
            createClassFile(CodeType.Activity, data);
            createClassFile(CodeType.Manifest, data);
        } else {
            createClassFile(CodeType.Fragment, data);
        }
        createClassFile(CodeType.ViewModel, data);
        createClassFile(CodeType.Layout, data);
    }

    /**
     * 生成mvp框架代码
     *
     * @param codeType 类型
     */
    private void createClassFile(CodeType codeType, InputContentData data) {
        String fileName = "";
        String content = "";
        switch (codeType) {
            case Activity:
                fileName = "MainActivity.kt.ftl";
                content = Utils.readTemplateFile(this, fileName);
                content = Utils.dealTemplateContent(project, data, content);
                Utils.writeToFile(content, virtualFile.getPath() + "/view/activity/", data.view + ".kt");
                Utils.addRouterPath(project, virtualFile, data);
                break;
            case Fragment:
                fileName = "MainFragment.kt.ftl";
                content = Utils.readTemplateFile(this, fileName);
                content = Utils.dealTemplateContent(project, data, content);
                Utils.writeToFile(content, virtualFile.getPath() + "/view/fragment/", data.view + ".kt");
                Utils.addRouterPath(project, virtualFile, data);
                break;
            case Layout:
                fileName = "activity_main.xml.ftl";
                content = Utils.readTemplateFile(this, fileName);
                content = Utils.dealTemplateContent(project, data, content);
                Utils.writeToFile(content, resPath + "/layout/", data.layout + ".xml");
                break;
            case ViewModel:
                fileName = "MainViewModel.kt.ftl";
                content = Utils.readTemplateFile(this, fileName);
                content = Utils.dealTemplateContent(project, data, content);
                Utils.writeToFile(content, virtualFile.getPath() + "/viewmodel/", data.viewMode + ".kt");
                break;
            case Manifest:
                fileName = "AndroidManifest.xml.ftl";
                content = Utils.readTemplateFile(this, fileName);
                content = Utils.dealTemplateContent(project, data, content);
                //合并清单文件
                Utils.registerToManifest(project, content, mainPath);
                break;
        }
    }

}
