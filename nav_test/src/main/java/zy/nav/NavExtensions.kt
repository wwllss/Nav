package zy.nav

/**
 * created by zhangyuan on 2020/5/7
 */
fun Nav.scan(packageName: String = "") {
    val context = delegate.request.context()
    val fileNameSet = ClassUtils.getFileNameByPackageName(context) {
        it.startsWith(packageName) && it.contains("$$") && it.endsWith("Register")
    }
    if (fileNameSet.isNullOrEmpty()) {
        return
    }
    fileNameSet.filterNotNull().forEach {
        NavRegistry.register(it)
    }
}