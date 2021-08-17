package zy.nav.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;

import zy.nav.annotation.Service;
import zy.nav.doc.NavDoc;
import zy.nav.doc.ServiceDoc;
import zy.nav.doc.ServiceImplDoc;

@AutoService(Processor.class)
public class ServiceProcessor extends BaseProcessor {

    private static final String INTERFACE_SERVICE_REGISTER = "ServiceRegister";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Service.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<TypeElement> classSet = new LinkedHashSet<>();
        for (Element element : env.getElementsAnnotatedWith(Service.class)) {
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
                + moduleName.substring(1) + "$$ServiceRegister")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, INTERFACE_SERVICE_REGISTER))
                .addMethod(createRegisterMethod(classSet))
                .build();
    }

    private MethodSpec createRegisterMethod(Set<TypeElement> classSet) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("register")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(Map.class, String.class, String.class),
                        "services");
        List<ServiceDoc> docList = new ArrayList<>();
        for (TypeElement element : classSet) {
            String annotateClassName = getAnnotationClassValue(element);
            String token = element.getAnnotation(Service.class).token();
            Name binaryName = elementUtils.getBinaryName(element);
            builder.addStatement("services.put($S, $S)", key(annotateClassName, token), binaryName/*element.getQualifiedName()*/);

            ServiceDoc doc = new ServiceDoc();
            doc.setService(annotateClassName);
            doc.setComment(elementUtils.getDocComment(elementUtils.getTypeElement(annotateClassName)));
            List<ServiceImplDoc> implDocList = new ArrayList<>();
            ServiceImplDoc implDoc = new ServiceImplDoc();
            implDoc.setToken(token);
            implDoc.setImplName(binaryName.toString());
            implDoc.setComment(elementUtils.getDocComment(element));
            implDocList.add(implDoc);
            doc.setImplList(implDocList);
            docList.add(doc);
        }
        NavDoc.doc(docList, docDir + "/nav/service_" + originalModuleName + ".json");
        return builder.build();
    }

    private void parseClassSet(Element element, Set<TypeElement> classSet) {
        if (hasError((TypeElement) element)) {
            return;
        }
        classSet.add((TypeElement) element);
    }

    private boolean hasError(TypeElement element) {
        String annotateClass = getAnnotationClassValue(element);
        if (!isSubtype(element, annotateClass)) {
            logger.e("element %s must be a subtype of %s",
                    element.getQualifiedName(), annotateClass);
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
        return false;
    }

    private String getAnnotationClassValue(TypeElement element) {
        String annotateValue;
        try {
            annotateValue = element.getAnnotation(Service.class).value().getName();
        } catch (MirroredTypeException e) {
            annotateValue = e.getTypeMirror().toString();
        }
        return annotateValue;
    }


    private static String key(String className, String token) {
        if (token == null || token.isEmpty()) {
            return className;
        }
        return className + "-" + token;
    }
}
