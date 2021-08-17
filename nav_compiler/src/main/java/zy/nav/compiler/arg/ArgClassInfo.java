package zy.nav.compiler.arg;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class ArgClassInfo {

    final boolean isActivity;

    final TypeName targetType;

    final ClassName injectorType;

    ArgClassInfo parentClassInfo;

    Set<ArgFieldInfo> fieldInfoSet;

    ArgClassInfo(TypeElement enclosingElement, boolean isActivity) {
        this.isActivity = isActivity;
        TypeMirror typeMirror = enclosingElement.asType();
        targetType = TypeName.get(typeMirror);
        String packageName = MoreElements.getPackage(enclosingElement).getQualifiedName().toString();
        String className = enclosingElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        injectorType = ClassName.get(packageName, className + "$$ArgsInjector");
        fieldInfoSet = new LinkedHashSet<>();
    }
}
