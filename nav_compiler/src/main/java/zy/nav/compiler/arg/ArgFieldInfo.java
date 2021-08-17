package zy.nav.compiler.arg;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import zy.nav.annotation.Arg;
import zy.nav.annotation.FieldType;
import zy.nav.compiler.Utils;

import static zy.nav.compiler.Constants.BOOLEAN;
import static zy.nav.compiler.Constants.BYTE;
import static zy.nav.compiler.Constants.CHAR;
import static zy.nav.compiler.Constants.DOUBLE;
import static zy.nav.compiler.Constants.FLOAT;
import static zy.nav.compiler.Constants.INTEGER;
import static zy.nav.compiler.Constants.LONG;
import static zy.nav.compiler.Constants.SHORT;
import static zy.nav.compiler.Constants.STRING;

class ArgFieldInfo {

    final String fieldName;

    final String argName;

    final TypeName type;

    final int typeKind;

    ArgFieldInfo(Element element) {
        fieldName = element.getSimpleName().toString();
        String varArgName = element.getAnnotation(Arg.class).value();
        argName = Utils.isEmpty(varArgName) ? fieldName : varArgName;
        type = ClassName.get(element.asType());
        typeKind = typeExchange(element);
    }

    private int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case BYTE:
                return FieldType.BYTE.ordinal();
            case SHORT:
                return FieldType.SHORT.ordinal();
            case INTEGER:
                return FieldType.INT.ordinal();
            case LONG:
                return FieldType.LONG.ordinal();
            case FLOAT:
                return FieldType.FLOAT.ordinal();
            case DOUBLE:
                return FieldType.DOUBLE.ordinal();
            case BOOLEAN:
                return FieldType.BOOLEAN.ordinal();
            case CHAR:
                return FieldType.CHAR.ordinal();
            case STRING:
                return FieldType.STRING.ordinal();
            default:
                return FieldType.OBJECT.ordinal();
        }
    }

}
