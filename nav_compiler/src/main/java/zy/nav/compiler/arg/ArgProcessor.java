package zy.nav.compiler.arg;

import static javax.lang.model.element.ElementKind.CLASS;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import zy.nav.annotation.Arg;
import zy.nav.annotation.FieldType;
import zy.nav.annotation.Route;
import zy.nav.compiler.BaseProcessor;
import zy.nav.compiler.Constants;
import zy.nav.doc.ArgDoc;
import zy.nav.doc.ArgFieldDoc;
import zy.nav.doc.NavDoc;

public class ArgProcessor extends BaseProcessor {

    private static final String INTERFACE_ARGS_INJECTOR = "ArgsInjector";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Arg.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Map<TypeElement, ArgClassInfo> classMap = new LinkedHashMap<>();
        for (Element element : env.getElementsAnnotatedWith(Arg.class)) {
            parseArg(element, classMap);
        }
        for (Map.Entry<TypeElement, ArgClassInfo> entry : classMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            ArgClassInfo classInfo = entry.getValue();
            TypeElement parentType = findParentClassInfo(typeElement, classMap);
            ArgClassInfo parentClassInfo = classMap.get(parentType);
            if (parentClassInfo != null) {
                classInfo.parentClassInfo = parentClassInfo;
            }
            try {
                generate(classInfo).writeTo(filer);
            } catch (IOException e) {
                logger.e("generate ArgsInjector file error for " + typeElement);
            }
        }
        new DocWriter().write(classMap);
        return false;
    }

    private JavaFile generate(ArgClassInfo classInfo) {
        return JavaFile.builder(classInfo.injectorType.packageName(), createType(classInfo))
                .addFileComment("Generated code. Do not modify!")
                .build();
    }

    private TypeSpec createType(ArgClassInfo classInfo) {
        TypeSpec.Builder result = TypeSpec.classBuilder(classInfo.injectorType.simpleName())
                .addModifiers(Modifier.PUBLIC);
        ArgClassInfo parentClassInfo = classInfo.parentClassInfo;
        if (parentClassInfo != null) {
            result.superclass(parentClassInfo.injectorType);
        } else {
            result.addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, INTERFACE_ARGS_INJECTOR));
        }
        result.addMethod(createInjectMethod(classInfo))
                .addMethod(createGetParamTypesMethod(classInfo));
        return result.build();
    }

    private MethodSpec createInjectMethod(ArgClassInfo classInfo) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.OBJECT, "target");
        if (classInfo.parentClassInfo != null) {
            builder.addStatement("super.inject(target)");
        }
        builder.addStatement("$T host = ($T) target",
                classInfo.targetType,
                classInfo.targetType);
        boolean isActivity = classInfo.isActivity;
        builder.addStatement("$T bundle = " + (isActivity ? "host.getIntent().getExtras()" : "host.getArguments()"),
                ClassName.bestGuess(Constants.TYPE_ANDROID_BUNDLE));
        builder.beginControlFlow("if (bundle == null)");
        builder.addStatement("return");
        builder.endControlFlow();
        builder.addStatement("$T marshaller = $T.getService($T.class)",
                ClassName.get(Constants.PACKAGE_NAME, Constants.JSON_MARSHALLER),
                ClassName.get(Constants.PACKAGE_NAME, Constants.NAV),
                ClassName.get(Constants.PACKAGE_NAME, Constants.JSON_MARSHALLER));
        for (ArgFieldInfo fieldInfo : classInfo.fieldInfoSet) {
            int typeKind = fieldInfo.typeKind;
            CodeBlock.Builder code = CodeBlock.builder();
            code.beginControlFlow("if (bundle.containsKey($S))", fieldInfo.argName);
            if (typeKind != FieldType.OBJECT.ordinal()) {
                code.add("host.$L = bundle.", fieldInfo.fieldName);
            }
            if (typeKind == FieldType.BOOLEAN.ordinal()) {
                code.add("getBoolean($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.BYTE.ordinal()) {
                code.add("getByte($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.SHORT.ordinal()) {
                code.add("getShort($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.INT.ordinal()) {
                code.add("getInt($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.LONG.ordinal()) {
                code.add("getLong($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.CHAR.ordinal()) {
                code.add("getChar($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.FLOAT.ordinal()) {
                code.add("getFloat($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.DOUBLE.ordinal()) {
                code.add("getDouble($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.STRING.ordinal()) {
                code.add("getString($S);",
                        fieldInfo.argName);
            }
            if (typeKind == FieldType.OBJECT.ordinal()) {
                code.beginControlFlow(("if (marshaller != null)"));
                code.add("host.$L = ", fieldInfo.fieldName);
                code.add("marshaller.fromJson(");
                code.add("bundle.getString($S), "
                        , fieldInfo.argName);
                code.add("new $T<$T>(){}.getType()",
                        ClassName.get(Constants.PACKAGE_NAME, Constants.TYPE_TOKEN),
                        fieldInfo.type);
                code.add(");\n");
                code.nextControlFlow("else");
                code.add("$T.e($S, \"must implements "
                                + Constants.PACKAGE_NAME + "." + INTERFACE_ARGS_INJECTOR
                                + " to inject an Object argument\");\n",
                        ClassName.bestGuess(Constants.TYPE_ANDROID_LOG),
                        Constants.NAV);
                code.endControlFlow();
            } else {
                code.add("\n");
            }
            code.nextControlFlow("else");
            code.add("$T.e($S, \"$L --- This parameter may be unnecessary\");\n",
                    ClassName.bestGuess(Constants.TYPE_ANDROID_LOG),
                    Constants.NAV,
                    fieldInfo.fieldName);
            code.endControlFlow();
            builder.addCode(code.build());
        }
        return builder.build();
    }

    private MethodSpec createGetParamTypesMethod(ArgClassInfo classInfo) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getParamTypes")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        ParameterizedTypeName typeMap = ParameterizedTypeName.get(Map.class, String.class, Integer.class);
        builder.returns(typeMap);
        if (classInfo.parentClassInfo != null) {
            builder.addStatement("$T map = super.getParamTypes()", typeMap);
        } else {
            builder.addStatement("$T map = new $T<>()", typeMap, ClassName.get(LinkedHashMap.class));
        }
        Set<ArgFieldInfo> fieldInfoSet = classInfo.fieldInfoSet;
        if (!fieldInfoSet.isEmpty()) {
            for (ArgFieldInfo fieldInfo : fieldInfoSet) {
                builder.addStatement("map.put($S, $L)", fieldInfo.argName, fieldInfo.typeKind);
            }
        }
        builder.addStatement("return map");
        return builder.build();
    }

    private void parseArg(Element element, Map<TypeElement, ArgClassInfo> classMap) {
        if (hasError(element)) {
            return;
        }
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        ArgClassInfo classInfo = classMap.get(enclosingElement);
        if (classInfo == null) {
            classMap.put(enclosingElement, classInfo = new ArgClassInfo(enclosingElement, isActivity(enclosingElement)));
        }
        classInfo.fieldInfoSet.add(new ArgFieldInfo(element));
    }

    private TypeElement findParentClassInfo(TypeElement typeElement, Map<TypeElement, ArgClassInfo> classMap) {
        TypeMirror type;
        while (true) {
            type = typeElement.getSuperclass();
            if (type.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement = (TypeElement) ((DeclaredType) type).asElement();
            if (classMap.containsKey(typeElement)) {
                return typeElement;
            }
        }
    }

    private boolean hasError(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        TypeMirror enclosingElementType = enclosingElement.asType();
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.PRIVATE)) {
            logger.e("%s.%s must not be static or private",
                    enclosingElement.getQualifiedName(), element.getSimpleName());
            return true;
        }
        if (enclosingElement.getKind() != CLASS) {
            logger.e("@%s %s may only be contained in classes. (%s.%s)",
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            return true;
        }
        if (enclosingElement.getModifiers().contains(Modifier.PRIVATE)) {
            logger.e("annotation may not be contained in private class. (%s.%s)",
                    enclosingElement.getQualifiedName(), element.getSimpleName());
            return true;
        }
        if (!isActivity(enclosingElement) && !isFragment(enclosingElement)) {
            if (enclosingElementType.getKind() != TypeKind.ERROR) {
                logger.e("class must extend from Activity or Fragment(support-v4). (%s)",
                        enclosingElement.getQualifiedName());
                return true;
            }
        }
        return false;
    }

    class DocWriter {

        void write(Map<TypeElement, ArgClassInfo> map) {
            if (map.isEmpty()) {
                return;
            }
            List<ArgDoc> docList = new ArrayList<>();
            for (Map.Entry<TypeElement, ArgClassInfo> entry : map.entrySet()) {
                TypeElement key = entry.getKey();
                String[] route = findRoute(key);
                if (route == null || route.length == 0) {
                    continue;
                }
                ArgClassInfo value = entry.getValue();
                List<ArgFieldDoc> argList = findArgList(value);
                if (argList.isEmpty()) {
                    continue;
                }
                for (String s : route) {
                    ArgDoc doc = new ArgDoc();
                    doc.setRoute(s);
                    doc.setArgList(argList);
                    docList.add(doc);
                }
            }
            NavDoc.doc(docList, docDir + "/nav/arg_" + originalModuleName + ".json");
        }

        private List<ArgFieldDoc> findArgList(ArgClassInfo argClassInfo) {
            List<ArgFieldDoc> list = new ArrayList<>();
            for (ArgFieldInfo info : argClassInfo.fieldInfoSet) {
                ArgFieldDoc doc = new ArgFieldDoc();
                doc.setParamName(info.argName);
                if (!info.argName.equals(info.fieldName)) {
                    doc.setFieldName(info.fieldName);
                }
                list.add(doc);
            }
            ArgClassInfo parentClassInfo = argClassInfo.parentClassInfo;
            return parentClassInfo == null ? list : findArgList(parentClassInfo);
        }

        private String[] findRoute(TypeElement key) {
            Route route = key.getAnnotation(Route.class);
            if (route != null) {
                return route.value();
            }
            if (!isActivity(key) && isFragment(key)) {
                return null;
            }
            return findRoute((TypeElement) ((DeclaredType) key.getSuperclass()).asElement());
        }

    }
}
