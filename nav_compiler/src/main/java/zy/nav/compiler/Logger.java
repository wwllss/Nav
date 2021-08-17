package zy.nav.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author zhangyuan
 * created on 2018/5/4.
 */
public class Logger {

    private final Messager messager;

    Logger(Messager messager) {
        this.messager = messager;
    }

    void i(String str, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(str, args));
    }

    public void e(String str, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(str, args));
    }

}
