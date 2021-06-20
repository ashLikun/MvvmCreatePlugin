import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2020/10/16　13:34
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class InputContentData {
    //是否是Activity,其他情况就是Fragment
    var isActivity = false
    var name: String = ""
    var desc: String = ""

    //activity或者Fragment
    var view: String = ""
    var layout: String = ""
    var viewMode: String = ""
    var packge: String = ""

    //是否是列表
    var isList = false

    //是否用ViewBinding加载View
    var isViewBinding = false
    fun currDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date().time)
    }

    val routerPath: String
        get() = if (isActivity) "ACTIVITY" + Utils.routerPath(name) else "FRAGMENT" + Utils.routerPath(name)

    fun getViewBindingClass() = Utils.tuofeng(layout)
}