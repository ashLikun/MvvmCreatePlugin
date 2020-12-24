import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　13:34
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
class InputContentData {
    //是否是Activity,其他情况就是Fragment
    boolean isActivity;
    String name;
    String desc;
    //activity或者Fragment
    String view;
    String layout;
    String viewMode;
    String packge;
    //是否是列表
    boolean isList = false;

    public String currDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date().getTime());
    }

    public String getRouterPath() {
        return isActivity ? "ACTIVITY" + Utils.routerPath(name) : "FRAGMENT" + Utils.routerPath(name);
    }
}
