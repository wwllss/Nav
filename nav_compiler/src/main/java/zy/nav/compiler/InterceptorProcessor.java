package zy.nav.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import zy.nav.annotation.Intercept;
import zy.nav.doc.InterceptorDoc;
import zy.nav.doc.NavDoc;

public class InterceptorProcessor extends BaseProcessor {

    private static final String INTERFACE_INTERCEPTOR_REGISTER = "InterceptorRegister";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Intercept.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<TypeElement> classSet = new LinkedHashSet<>();
        for (Element element : env.getElementsAnnotatedWith(Intercept.class)) {
            parseClassSet(element, classSet);
        }
        try {
            if (!classSet.isEmpty()) {
                generate(classSet).writeTo(filer);
            }
        } catch (IOException e) {
            logger.e("generate ServiceRegister file error --- " + e.getMessage());
        }
        return false;
    }

    private JavaFile generate(Set<TypeElement> classSet) {
        return JavaFile.builder(Constants.GENERATE_PACKAGE_NAME + "." + moduleName, createType(classSet))
                .addFileComment("Generated code. Do not modify!")
                .build();
    }

    private TypeSpec createType(Set<TypeElement> classSet) {
        return TypeSpec.classBuilder(Character.toUpperCase(moduleName.charAt(0))
                + moduleName.substring(1) + "$$" + INTERFACE_INTERCEPTOR_REGISTER)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, INTERFACE_INTERCEPTOR_REGISTER))
                .addMethod(createRegisterMethod(classSet))
                .build();
    }

    private MethodSpec createRegisterMethod(Set<TypeElement> classSet) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("register")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(
                        ClassName.get(Map.class),
                        ClassName.get(String.class),
                        ParameterizedTypeName.get(Map.class, Integer.class, String.class)),
                        "interceptors");
        builder.addStatement("Map<Integer, String> interceptorMap");
        List<InterceptorDoc> docList = new ArrayList<>();
        for (TypeElement element : classSet) {
            String[] route = element.getAnnotation(Intercept.class).route();
            for (String token : route) {
                builder.addStatement("interceptorMap = interceptors.get($S)", token);
                builder.beginControlFlow("if (interceptorMap == null)");
                builder.addStatement("interceptorMap = new $T<>()", TypeName.get(HashMap.class));
                builder.addStatement("interceptors.put($S, interceptorMap)", token);
                builder.endControlFlow();
                int priority = element.getAnnotation(Intercept.class).priority();
                Name binaryName = elementUtils.getBinaryName(element);
                builder.addStatement("interceptorMap.put($L, $S)", priority, binaryName/*element.getQualifiedName()*/);

                InterceptorDoc doc = new InterceptorDoc();
                doc.setRoute(token);
                doc.setInterceptor(binaryName.toString());
                docList.add(doc);
            }
        }
        NavDoc.doc(docList, docDir + "/nav/interceptor_" + originalModuleName + ".json");
        return builder.build();
    }

    private void parseClassSet(Element element, Set<TypeElement> classSet) {
        if (hasError((TypeElement) element)) {
            return;
        }
        classSet.add((TypeElement) element);
    }

    private boolean hasError(TypeElement element) {
        String interceptorInterface = Constants.PACKAGE_NAME + "." + Constants.TYPE_INTERCEPTOR;
        if (!isSubtype(element, interceptorInterface)) {
            logger.e("element %s must be a subtype of %s",
                    element.getQualifiedName(), interceptorInterface);
            return true;
        }
        Set<Modifier> modifiers = element.getModifiers();
        Name binaryName = elementUtils.getBinaryName(element);
        Name simpleName = element.getSimpleName();
        if (binaryName.toString().contains("$" + simpleName)
                && !modifiers.contains(Modifier.STATIC)
                && !modifiers.contains(Modifier.PUBLIC)) {
            logger.e("element %s is inner class, must be public static",
                    element.getQualifiedName());
            return true;
        }
        int priority = element.getAnnotation(Intercept.class).priority();
        if (priority < 0) {
            logger.e("Annotation %s priority value must > 0，on %s",
                    Intercept.class.getCanonicalName(), element.getQualifiedName());
            return true;
        }
        return false;
    }
}
