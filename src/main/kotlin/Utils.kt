import com.intellij.ide.actions.CreateFileAction
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.w3c.dom.Element
import java.io.*
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　15:25
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 * PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "RouterPath.kt", GlobalSearchScope.allScope(project));
 * //mainPath + "/AndroidManifest.xml"
 * FilenameIndex.getFilesByName(project,"AndroidManifest.xml", GlobalSearchScope.allScope(project),true);
 * PsiFile[] xxx = FilenameIndex.getFilesByName(project, mainPath + "AndroidManifest.xml", GlobalSearchScope.allScope(project));
 * PsiClass[] psiClass = PsiShortNamesCache.getInstance(project).get("RouterPath", GlobalSearchScope.allScope(project));
 *
 *
 * //((PsiJavaFile) psiClass[0].getContainingFile()).getPackageName()
 * //PsiUtil.getPackageName(psiClass[0])
 * //        XmlDocument document = XmlFile.get.getDocument();
 * //        if (document != null && document.getRootTag() != null) {
 * //            XmlTag rootTag = document.getChildren();
 * //            rootTag.getAttribute(attrName).setValue(attrValue);//set value for exists attr.
 * //            rootTag.setAttribute(name,value);//add a new attr and setting value
 * //        }
 *
 *
 * PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "AndroidManifest.xml", GlobalSearchScope.projectScope(project));
 * for (PsiFile f :
 * psiFiles) {
 * if (f.getVirtualFile().getPath().contains(mainPath)) {
 * //本模块的文件
 * XmlDocument document = ((XmlFile) f).getDocument();
 * if (document != null && document.getRootTag() != null) {
 * XmlTag rootTag = document.getRootTag();
 * XmlTag application = rootTag.findFirstSubTag("application");
 *
 *
 * XmlTag xmlTag = application.createChildTag("activity", "", "", true);
 * application.addSubTag(xmlTag, true);
 *
 *
 * }
 * }
 * }
 */
internal object Utils {
    /**
     * 大写字母前面加上_   并且转成小写
     */
    @JvmStatic
    fun activityToLayout(text: String): String {
        var answer = ""
        val findupper = text.toCharArray()
        for (i in findupper.indices) {
            //是否是大写字母
            if (findupper[i] >= 65.toChar() && findupper[i] <= 91.toChar()) {
                answer += "_" + Character.toLowerCase(findupper[i]) // adding only uppercase
            } else {
                answer += findupper[i]
            }
        }
        if (!answer.startsWith("_")) {
            answer = "_$answer"
        }
        return answer
    }

    /**
     * 大写字母前面加上_   全部转换成大写
     */
    fun routerPath(text: String): String {
        var answer = ""
        val findupper = text.toCharArray()
        for (i in findupper.indices) {
            //是否是大写字母
            if (findupper[i] >= 65.toChar() && findupper[i] <= 91.toChar()) {
                answer += "_" + findupper[i] // adding only uppercase
            } else {
                answer += findupper[i]
            }
        }
        if (!answer.startsWith("_")) {
            answer = "_$answer"
        }
        return answer.uppercase()
    }

    /**
     * 获取和创建目录
     * IdeView ideView = LangDataKeys.IDE_VIEW.getData(event.getDataContext());
     *
     * @param dir
     * @param dirName
     * @return
     */
    fun getSubDir(dir: PsiDirectory, dirName: String): PsiDirectory {
        return if (dir.name === "contract") {
            if (dirName === "contract") {
                dir
            } else {
                CreateFileAction.findOrCreateSubdirectory(dir.parentDirectory!!, dirName)
            }
        } else {
            CreateFileAction.findOrCreateSubdirectory(dir, dirName)
        }
    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     */
    fun getMainPackageName(project: Project): String {
        var package_name = ""
        val dbf = DocumentBuilderFactory.newInstance()
        try {
            val db = dbf.newDocumentBuilder()
            val doc = db.parse(project.basePath + "/App/src/main/AndroidManifest.xml")
            val nodeList = doc.getElementsByTagName("manifest")
            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i)
                val element = node as Element
                package_name = element.getAttribute("package")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return package_name
    }

    /**
     * 替换模板中字符
     *
     * @param content
     * @return
     */
    fun dealTemplateContent(project: Project, data: InputContentData, content: String): String {
        var content = content
        val routerPath = data.routerPath
        if (content.contains("\${packageName}")) {
            content = content.replace("\${packageName}", data.packge)
        }
        if (content.contains("\${httpCallbackHandle}")) {
            content = content.replace("\${httpCallbackHandle}", getFilePackage(project, "HttpUiHandle.kt", false))
        }
        if (content.contains("\${showToast}")) {
            content = content.replace("\${showToast}", getFilePackage(project, "HttpExtend.kt", true) + ".showToast")
        }
        if (content.contains("\${RouterPathPackage}")) {
            content = content.replace("\${RouterPathPackage}", getFilePackage(project, "RouterPath.kt", false))
        }
        if (content.contains("\${vmName}")) {
            content = content.replace("\${vmName}", data.viewMode)
        }
        if (content.contains("\${mvvmDesc}")) {
            content = content.replace("\${mvvmDesc}", data.desc)
        }
        if (content.contains("\${mData}")) {
            content = content.replace("\${mData}", data.currDate())
        }
        if (content.contains("\${RouterPath}")) {
            content = content.replace("\${RouterPath}", routerPath)
        }
        if (content.contains("\${activityClass}")) {
            content = content.replace("\${activityClass}", data.view)
        }
        if (content.contains("\${fragmentClass}")) {
            content = content.replace("\${fragmentClass}", data.view)
        }
        if (content.contains("\${layoutName}")) {
            content = content.replace("\${layoutName}", data.layout)
        }


        //list
        if (content.contains("\${BaseListViewModel}")) {
            content = content.replace("\${BaseListViewModel}", getFilePackage(project, "BaseListViewModel.kt", false))
        }
        if (content.contains("\${BaseSuperListFragment}")) {
            content =
                content.replace("\${BaseSuperListFragment}", getFilePackage(project, "BaseSuperListFragment.kt", false))
        }
        if (content.contains("\${BaseSuperListActivity}")) {
            content =
                content.replace("\${BaseSuperListActivity}", getFilePackage(project, "BaseSuperListActivity.kt", false))
        }
        if (content.contains("\${showEmpty}")) {
            content =
                content.replace("\${showEmpty}", getFilePackage(project, "ActivityExtend.kt", true) + ".showEmpty")
        }
        //viewbinding
        if (content.contains("\${viewBinding}")) {
            content = content.replace("\${viewBinding}", data.getViewBindingClass())
        }
        return content
    }

    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     */
    fun readTemplateFile(ob: Any, fileName: String): String {
        var input: InputStream = ob.javaClass.getResourceAsStream("/template/mvvm/$fileName")
        var content = ""
        try {
            content = String(readStream(input), Charset.forName("utf-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content
    }

    /**
     * 读取数据
     *
     * @param inputStream
     */
    @Throws(IOException::class)
    fun readStream(inputStream: InputStream): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (outputStream != null) {
                outputStream.close()
            }
            inputStream?.close()
        }
        return outputStream.toByteArray()
    }

    /**
     * 生成
     *
     * @param content   类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    fun writeToFile(content: String?, classPath: String, className: String) {
        try {
            val floder = File(classPath)
            if (!floder.exists()) {
                floder.mkdirs()
            }
            val file = File("$classPath/$className")
            if (!file.exists()) {
                file.createNewFile()
            }
            val write = OutputStreamWriter(FileOutputStream(file), "UTF-8")
            val bw = BufferedWriter(write)
            bw.write(content)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * @param project
     * @param fileName
     * @param isRemoveFilename 包名是不是不带文件名结尾
     * @return
     */
    fun getFilePackage(project: Project, fileName: String, isRemoveFilename: Boolean): String {
        val psiFiles = FilenameIndex.getFilesByName(
            project, fileName, GlobalSearchScope.projectScope(
                project
            )
        )
        return if (psiFiles != null && psiFiles.size > 0) {
            if (isRemoveFilename) {
                getPackageName(psiFiles[0].virtualFile).replace(".$fileName", "")
            } else {
                val pack = getPackageName(psiFiles[0].virtualFile)
                if (pack.endsWith(".kt")) {
                    pack.substring(0, pack.length - 3)
                } else {
                    pack
                }
            }
        } else ""
    }

    /**
     * 生成RouterPath 的常量
     */
    fun addRouterPath(project: Project, virtualFile: VirtualFile, data: InputContentData) {
        val modeName = getModeName(project, virtualFile)
        //直播列表
        //    const val FRAGMENT_LIVE_LIST = "/live/fragment/list"
        val content =
            "\n    //${data.desc}\n    const val ${data.routerPath} = \"/${modeName}/${if (data.isActivity) "activity" else "fragment"}/${data.name.lowercase()}\""

        val psiFiles = FilenameIndex.getFilesByName(
            project, "RouterPath.kt", GlobalSearchScope.projectScope(
                project
            )
        )
        for (f in psiFiles) {
            val currentVFile = f.virtualFile
            //            if (currentVFile.getPath().contains(mainPath)) {//本模块的文件
            var randomAccessFile: RandomAccessFile? = null
            val manifestPath = currentVFile.path
            try {
                randomAccessFile = RandomAccessFile(manifestPath, "rw")
                //追加文件后续的内容
                val houxuContent = StringBuilder()
                // 每一行的内容
                var line = ""
                //反向查找
                var pos = randomAccessFile.length()
                while (pos > 0) {
                    //使光标向前移动一位
                    pos--
                    randomAccessFile.seek(pos)
                    if (randomAccessFile.readLine() == "}") {
                        randomAccessFile.seek(pos)
                        break
                    }
                }
                while (randomAccessFile.readLine()?.also { line = it } != null) {
                    houxuContent.append("\n$line")
                }
                randomAccessFile.seek(randomAccessFile.length() - houxuContent.length + 1)
                houxuContent.insert(0, "$content")
                randomAccessFile.write(houxuContent.toString().toByteArray(charset("utf-8")))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    randomAccessFile?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 生成RouterJump 的常量
     */
    fun addRouterJump(project: Project, virtualFile: VirtualFile, data: InputContentData) {
        val modeName = getModeName(project, virtualFile)
        val content = "\n    /**\n" +
                "     * 启动${data.desc}页面\n" +
                "     */\n" +
                "    fun start${data.name}() = start(path = RouterPath.${data.routerPath})"
        val psiFiles = FilenameIndex.getFilesByName(
            project, "RouterJump.kt", GlobalSearchScope.projectScope(
                project
            )
        )
        psiFiles.forEach {
            val currentVFile = it.virtualFile
            //            if (currentVFile.getPath().contains(mainPath)) {//本模块的文件
            var randomAccessFile: RandomAccessFile? = null
            val manifestPath = currentVFile.path
            try {
                randomAccessFile = RandomAccessFile(manifestPath, "rw")
                //追加文件后续的内容
                val houxuContent = StringBuilder()
                // 每一行的内容
                var line = ""
                //反向查找
                var pos = randomAccessFile.length()
                while (pos > 0) {
                    //使光标向前移动一位
                    pos--
                    randomAccessFile.seek(pos)
                    if (randomAccessFile.readLine() == "}") {
                        randomAccessFile.seek(pos)
                        break
                    }
                }
                while (randomAccessFile.readLine()?.also { line = it } != null) {
                    houxuContent.append("\n$line")
                }
                randomAccessFile.seek(randomAccessFile.length() - houxuContent.length + 1)
                houxuContent.insert(0, "$content")
                randomAccessFile.write(houxuContent.toString().toByteArray(charset("utf-8")))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    randomAccessFile?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }

    /**
     * 将Activity注册到AndroidManifest
     */
    fun registerToManifest(project: Project, content: String, mainPath: String) {
        val psiFiles = FilenameIndex.getFilesByName(
            project, "AndroidManifest.xml", GlobalSearchScope.projectScope(
                project
            )
        )
        for (f in psiFiles) {
            val currentVFile = f.virtualFile
            if (currentVFile.path.contains(mainPath)) { //本模块的文件
                var randomAccessFile: RandomAccessFile? = null
                val manifestPath = currentVFile.path
                try {
                    randomAccessFile = RandomAccessFile(manifestPath, "rw")
                    //追加文件后续的内容
                    val houxuContent = StringBuilder()
                    var isZhaoDao = false
                    // 每一行的内容
                    var line = ""
                    while (randomAccessFile.readLine()?.also { line = it } != null) {
                        // 找到application节点的末尾
                        if (!isZhaoDao)
                            isZhaoDao = line.contains("</application>")
                        if (isZhaoDao) {
                            // 在application节点最后插入新创建的activity节点
                            houxuContent.append("\n$line")
                        }
                    }
//                    houxuContent.delete(0, 1)
                    randomAccessFile.seek(randomAccessFile.length() - houxuContent.length)
                    houxuContent.insert(0, "\n$content")
                    randomAccessFile.write(houxuContent.toString().toByteArray(charset("utf-8")))
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        randomAccessFile?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun getVfParent(virtualFile: VirtualFile?, parentName: String): VirtualFile? {
        if (virtualFile == null) {
            return null
        }
        return if (virtualFile.name == parentName) {
            virtualFile
        } else {
            getVfParent(virtualFile.parent, parentName)
        }
    }

    fun getPackageName(virtualFile: VirtualFile): String {
        var javaAndKotlin = getVfParent(virtualFile, "java")
        if (javaAndKotlin == null) {
            javaAndKotlin = getVfParent(virtualFile, "kotlin")
        }
        return if (javaAndKotlin != null) {
            var subStr = virtualFile.path.replace(javaAndKotlin.path, "")
            if (subStr.startsWith("/")) {
                subStr = subStr.substring(1)
            }
            subStr.replace("/", ".")
        } else {
            getResPath(virtualFile.parent)
        }
    }

    fun getResPath(virtualFile: VirtualFile?): String {
        val src = getVfParent(virtualFile, "src")
        if (src != null) {
            val children = src.children
            for (child in children) {
                if (child.name == "main") {
                    val children2 = child.children
                    for (child2 in children2) {
                        if (child2.name == "res") {
                            return child2.path
                        }
                    }
                }
            }
        }
        return ""
    }

    fun getMainPath(virtualFile: VirtualFile?): String {
        val src = getVfParent(virtualFile, "src")
        if (src != null) {
            val children = src.children
            for (child in children) {
                if (child.name == "main") {
                    return child.path
                }
            }
        }
        return ""
    }

    fun getModeName(project: Project, virtualFile: VirtualFile): String {
        val module = ModuleUtil.findModuleForFile(virtualFile, project)
        return try {
            //baseproject.component_common.main
            val module_ = module?.name?.split(".")?.find { it.startsWith("module_") || it.startsWith("component_") }
            if (!module_.isNullOrBlank()) {
                module_.replace("module_", "").replace("component_", "")
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 驼峰命名
     */
    fun tuofeng(str: String): String {
        val result = StringBuffer()
        val split = str.split("_")
        split.forEachIndexed { index, s ->
            val sarr = s.toCharArray()
            if (sarr.isNotEmpty()) {
                sarr[0] = sarr[0].uppercaseChar()
                result.append(sarr)
            }
        }
        return result.toString()
    }
}