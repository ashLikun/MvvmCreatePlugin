import com.intellij.ide.actions.CreateFileAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　15:25
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 * PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "RouterPath.kt", GlobalSearchScope.allScope(project));
 * //mainPath + "/AndroidManifest.xml"
 * FilenameIndex.getFilesByName(project,"AndroidManifest.xml", GlobalSearchScope.allScope(project),true);
 * PsiFile[] xxx = FilenameIndex.getFilesByName(project, mainPath + "AndroidManifest.xml", GlobalSearchScope.allScope(project));
 * PsiClass[] psiClass = PsiShortNamesCache.getInstance(project).get("RouterPath", GlobalSearchScope.allScope(project));
 * <p>
 * //((PsiJavaFile) psiClass[0].getContainingFile()).getPackageName()
 * //PsiUtil.getPackageName(psiClass[0])
 * //        XmlDocument document = XmlFile.get.getDocument();
 * //        if (document != null && document.getRootTag() != null) {
 * //            XmlTag rootTag = document.getChildren();
 * //            rootTag.getAttribute(attrName).setValue(attrValue);//set value for exists attr.
 * //            rootTag.setAttribute(name,value);//add a new attr and setting value
 * //        }
 * <p>
 * PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "AndroidManifest.xml", GlobalSearchScope.projectScope(project));
 * for (PsiFile f :
 * psiFiles) {
 * if (f.getVirtualFile().getPath().contains(mainPath)) {
 * //本模块的文件
 * XmlDocument document = ((XmlFile) f).getDocument();
 * if (document != null && document.getRootTag() != null) {
 * XmlTag rootTag = document.getRootTag();
 * XmlTag application = rootTag.findFirstSubTag("application");
 * <p>
 * XmlTag xmlTag = application.createChildTag("activity", "", "", true);
 * application.addSubTag(xmlTag, true);
 * <p>
 * }
 * }
 * }
 */
class Utils {

    /**
     * 大写字母前面加上_   并且转成小写
     */
    public static String activityToLayout(String text) {
        String answer = "";
        char[] findupper = text.toCharArray();
        for (int i = 0; i < findupper.length; i++) {
            //是否是大写字母
            if (findupper[i] >= 65 && findupper[i] <= 91) {
                answer += "_" + Character.toLowerCase(findupper[i]); // adding only uppercase
            } else {
                answer += findupper[i];
            }
        }
        if (!answer.startsWith("_")) {
            answer = "_" + answer;
        }
        return answer;
    }

    /**
     * 大写字母前面加上_   全部转换成大写
     */
    public static String routerPath(String text) {
        String answer = "";
        char[] findupper = text.toCharArray();
        for (int i = 0; i < findupper.length; i++) {
            //是否是大写字母
            if (findupper[i] >= 65 && findupper[i] <= 91) {
                answer += "_" + findupper[i]; // adding only uppercase
            } else {
                answer += findupper[i];
            }
        }
        if (!answer.startsWith("_")) {
            answer = "_" + answer;
        }
        return answer.toUpperCase();
    }

    /**
     * 获取和创建目录
     * IdeView ideView = LangDataKeys.IDE_VIEW.getData(event.getDataContext());
     *
     * @param dir
     * @param dirName
     * @return
     */
    public static PsiDirectory getSubDir(PsiDirectory dir, String dirName) {
        if (dir.getName() == "contract") {
            if (dirName == "contract") {
                return dir;
            } else {
                return CreateFileAction.findOrCreateSubdirectory(dir.getParentDirectory(), dirName);
            }
        } else {
            return CreateFileAction.findOrCreateSubdirectory(dir, dirName);
        }
    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     */
    public static String getMainPackageName(Project project) {
        String package_name = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/App/src/main/AndroidManifest.xml");
            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                package_name = element.getAttribute("package");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return package_name;
    }

    /**
     * 替换模板中字符
     *
     * @param content
     * @return
     */
    public static String dealTemplateContent(Project project, InputContentData data, String content) {
        String routerPath = data.getRouterPath();
        if (content.contains("${packageName}")) {
            content = content.replace("${packageName}", data.packge);
        }
        if (content.contains("${httpCallbackHandle}")) {
            content = content.replace("${httpCallbackHandle}", getFilePackage(project, "HttpCallbackHandle.kt", false));
        }
        if (content.contains("${showToast}")) {
            content = content.replace("${showToast}", getFilePackage(project, "HttpExtend.kt", true) + ".showToast");
        }
        if (content.contains("${RouterPathPackage}")) {
            content = content.replace("${RouterPathPackage}", getFilePackage(project, "RouterPath.kt", false));
        }
        if (content.contains("${vmName}")) {
            content = content.replace("${vmName}", data.viewMode);
        }
        if (content.contains("${mvvmDesc}")) {
            content = content.replace("${mvvmDesc}", data.desc);
        }
        if (content.contains("${mData}")) {
            content = content.replace("${mData}", data.currDate());
        }
        if (content.contains("${RouterPath}")) {
            content = content.replace("${RouterPath}", routerPath);
        }
        if (content.contains("${activityClass}")) {
            content = content.replace("${activityClass}", data.view);
        }
        if (content.contains("${fragmentClass}")) {
            content = content.replace("${fragmentClass}", data.view);
        }
        if (content.contains("${layoutName}")) {
            content = content.replace("${layoutName}", data.layout);
        }

        //list
        if (content.contains("${BaseListViewModel}")) {
            content = content.replace("${BaseListViewModel}", getFilePackage(project, "BaseListViewModel.kt", false));
        }
        if (content.contains("${BaseSuperListFragment}")) {
            content = content.replace("${BaseSuperListFragment}", getFilePackage(project, "BaseSuperListFragment.kt", false));
        }
        if (content.contains("${BaseSuperListActivity}")) {
            content = content.replace("${BaseSuperListActivity}", getFilePackage(project, "BaseSuperListActivity.kt", false));
        }
        return content;
    }


    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     */
    public static String readTemplateFile(Object ob, String fileName) {
        InputStream in = null;
        in = ob.getClass().getResourceAsStream("/template/mvvm/" + fileName);
        String content = "";
        try {
            content = new String(Utils.readStream(in), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 读取数据
     *
     * @param inputStream
     */
    public static byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * 生成
     *
     * @param content   类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    public static void writeToFile(String content, String classPath, String className) {
        try {
            File floder = new File(classPath);
            if (!floder.exists()) {
                floder.mkdirs();
            }

            File file = new File(classPath + "/" + className);
            if (!file.exists()) {
                file.createNewFile();
            }

            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            BufferedWriter bw = new BufferedWriter(write);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param project
     * @param fileName
     * @param isRemoveFilename 包名是不是不带文件名结尾
     * @return
     */
    public static String getFilePackage(Project project, String fileName, boolean isRemoveFilename) {
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.projectScope(project));
        if (psiFiles != null && psiFiles.length > 0) {
            if (isRemoveFilename) {
                return getPackageName(psiFiles[0].getVirtualFile()).replace("." + fileName, "");
            } else {
                String pack = getPackageName(psiFiles[0].getVirtualFile());
                if (pack.endsWith(".kt")) {
                    return pack.substring(0, pack.length() - 3);
                } else {
                    return pack;
                }
            }
        }
        return "";
    }

    /**
     * 生成RouterPath 的常量
     */
    public static void addRouterPath(Project project, VirtualFile virtualFile, InputContentData data) {
        String modeName = Utils.getModeName(project, virtualFile);
        //直播列表
        //    const val FRAGMENT_LIVE_LIST = "/live/fragment/list"
        StringBuilder content = new StringBuilder();
        content.append("    //");
        content.append(data.desc);
        content.append("\n    const val ");
        content.append(data.getRouterPath());
        content.append(" = \"");
        content.append("/");
        content.append(modeName);
        content.append("/");
        content.append(data.isActivity ? "activity" : "fragment");
        content.append("/");
        content.append(data.name.toLowerCase());
        content.append("\"");

        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "RouterPath.kt", GlobalSearchScope.projectScope(project));
        for (PsiFile f : psiFiles) {
            VirtualFile currentVFile = f.getVirtualFile();
//            if (currentVFile.getPath().contains(mainPath)) {//本模块的文件

            RandomAccessFile randomAccessFile = null;
            String manifestPath = currentVFile.getPath();
            try {
                randomAccessFile = new RandomAccessFile(manifestPath, "rw");
                //追加文件后续的内容
                StringBuilder houxuContent = new StringBuilder();
                // 每一行的内容
                String line = "";
                boolean isReadContains = false;
                while ((line = randomAccessFile.readLine()) != null) {
                    // 找到application节点的末尾
                    if (line.trim().equals("}")) {
                        isReadContains = true;
                    }
                    if (isReadContains) {
                        houxuContent.append("\n" + line);
                    }
                }
                randomAccessFile.seek(randomAccessFile.length() - houxuContent.length());
                houxuContent.insert(0, content + "\n");
                randomAccessFile.write(houxuContent.toString().getBytes("utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将Activity注册到AndroidManifest
     */
    public static void registerToManifest(Project project, String content, String mainPath) {
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "AndroidManifest.xml", GlobalSearchScope.projectScope(project));
        for (PsiFile f : psiFiles) {
            VirtualFile currentVFile = f.getVirtualFile();
            if (currentVFile.getPath().contains(mainPath)) {//本模块的文件

                RandomAccessFile randomAccessFile = null;
                String manifestPath = currentVFile.getPath();
                try {
                    randomAccessFile = new RandomAccessFile(manifestPath, "rw");
                    //追加文件后续的内容
                    StringBuilder houxuContent = new StringBuilder();
                    // 每一行的内容
                    String line = "";
                    boolean isReadContains = false;
                    while ((line = randomAccessFile.readLine()) != null) {
                        // 找到application节点的末尾
                        if (line.contains("</application>")) {
                            // 在application节点最后插入新创建的activity节点
                            isReadContains = true;
                        }
                        if (isReadContains) {
                            houxuContent.append("\n" + line);
                        }
                    }
                    houxuContent.delete(0, 1);
                    randomAccessFile.seek(randomAccessFile.length() - houxuContent.length());
                    houxuContent.insert(0, content + "\n");
                    randomAccessFile.write(houxuContent.toString().getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    public static VirtualFile getVfParent(VirtualFile virtualFile, String parentName) {
        if (virtualFile == null) {
            return null;
        }
        if (virtualFile.getName().equals(parentName)) {
            return virtualFile;
        } else {
            return getVfParent(virtualFile.getParent(), parentName);
        }
    }

    public static String getPackageName(VirtualFile virtualFile) {
        VirtualFile javaAndKotlin = getVfParent(virtualFile, "java");
        if (javaAndKotlin == null) {
            javaAndKotlin = getVfParent(virtualFile, "kotlin");
        }
        if (javaAndKotlin != null) {
            String subStr = virtualFile.getPath().replace(javaAndKotlin.getPath(), "");
            if (subStr.startsWith("/")) {
                subStr = subStr.substring(1);
            }
            return subStr.replace("/", ".");
        } else {
            return getResPath(virtualFile.getParent());
        }
    }

    public static String getResPath(VirtualFile virtualFile) {
        VirtualFile src = getVfParent(virtualFile, "src");
        if (src != null) {
            VirtualFile[] children = src.getChildren();
            for (VirtualFile child : children) {
                if (child.getName().equals("main")) {
                    VirtualFile[] children2 = child.getChildren();
                    for (VirtualFile child2 : children2) {
                        if (child2.getName().equals("res")) {
                            return child2.getPath();
                        }
                    }
                }
            }
        }
        return "";
    }

    public static String getMainPath(VirtualFile virtualFile) {
        VirtualFile src = getVfParent(virtualFile, "src");
        if (src != null) {
            VirtualFile[] children = src.getChildren();
            for (VirtualFile child : children) {
                if (child.getName().equals("main")) {
                    return child.getPath();
                }
            }
        }
        return "";
    }

    public static String getModeName(Project project, VirtualFile virtualFile) {
        Module module = ModuleUtil.findModuleForFile(virtualFile, project);
        try {
            int aa = module.getName().lastIndexOf(".");
            if (aa != -1) {
                String name = module.getName().substring(aa + 1);
                if (name.startsWith("module_")) {
                    return name.replace("module_", "");
                } else {
                    return "";
                }
            } else {
                return module.getName();
            }

        } catch (Exception e) {
            return "";
        }
    }
}
