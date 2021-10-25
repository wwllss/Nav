package zy.nav.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import zy.nav.annotation.Route;
import zy.nav.doc.NavDoc;
import zy.nav.doc.RouteDoc;

public class RouteProcessor extends BaseProcessor {

    private static final String INTERFACE_ACTIVITY_REGISTER = "ActivityRegister";

    private static final String INTERFACE_FRAGMENT_REGISTER = "FragmentRegister";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<TypeElement> activitySet = new LinkedHashSet<>();
        Set<TypeElement> fragmentSet = new LinkedHashSet<>();
        for (Element element : env.getElementsAnnotatedWith(Route.class)) {
            parseClassSet(element, activitySet, fragmentSet);
        }
        try {
            if (!activitySet.isEmpty()) {
                new RouteWriter(activitySet, INTERFACE_ACTIVITY_REGISTER, "activities")
                        .generate().writeTo(filer);
            }
            if (!fragmentSet.isEmpty()) {
                new RouteWriter(fragmentSet, INTERFACE_FRAGMENT_REGISTER, "fragments")
                        .generate().writeTo(filer);
            }
        } catch (IOException e) {
            logger.e("generate ActivityRegister or FragmentRegister file error --- " + e.getMessage());
        }
        return false;
    }

    private class RouteWriter {

        private final Set<TypeElement> classSet;

        private final String interfaceName;

        private final String paramName;

        private RouteWriter(Set<TypeElement> classSet, String interfaceName, String paramName) {
            this.classSet = classSet;
            this.interfaceName = interfaceName;
            this.paramName = paramName;
        }

        private JavaFile generate() {
            return JavaFile.builder(Constants.GENERATE_PACKAGE_NAME + "." + moduleName, createType())
                    .addFileComment("Generated code. Do not modify!")
                    .build();
        }

        private TypeSpec createType() {
            return TypeSpec.classBuilder(Character.toUpperCase(moduleName.charAt(0))
                    + moduleName.substring(1) + "$$" + interfaceName)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, interfaceName))
                    .addMethod(createRegisterMethod())
                    .build();
        }

        private MethodSpec createRegisterMethod() {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("register")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterizedTypeName.get(Map.class, String.class, String.class),
                            paramName);
            List<RouteDoc> docList = new ArrayList<>();
            for (TypeElement element : classSet) {
                String[] urlArr = element.getAnnotation(Route.class).value();
                for (String url : urlArr) {
                    try {
                        String path = new URI(url).getPath();
                        Name binaryName = elementUtils.getBinaryName(element);
                        builder.addStatement(paramName + ".put($S, $S)", path,
                                binaryName/*element.getQualifiedName()*/);

                        RouteDoc doc = new RouteDoc();
                        doc.setRoute(path);
                        doc.setClassName(binaryName.toString());
                        doc.setComment(elementUtils.getDocComment(element));
                        docList.add(doc);
                    } catch (URISyntaxException e) {
                        logger.e(e.getMessage());
                    }
                }
            }
            NavDoc.doc(docList, docDir + "/nav/" + paramName + "_" + originalModuleName + ".json");
            return builder.build();
        }
    }

    private void parseClassSet(Element element, Set<TypeElement> activitySet, Set<TypeElement> fragmentSet) {
        TypeElement typeElement = (TypeElement) element;
        if (hasError(typeElement)) {
            return;
        }
        if (isActivity(typeElement)) {
            activitySet.add(typeElement);
        }
        if (isFragment(typeElement)) {
            fragmentSet.add(typeElement);
        }
    }

    private boolean hasError(TypeElement element) {
        Name qualifiedName = element.getQualifiedName();
        if (!isActivity(element) && !isFragment(element)) {
            logger.e("element %s must be a subtype of %s or %s",
                    qualifiedName,
                    Constants.TYPE_ACTIVITY, Constants.TYPE_FRAGMENT);
            return true;
        }
        String[] urlArr = element.getAnnotation(Route.class).value();
        if (urlArr.length == 0) {
            logger.e("Route annotation must have value on class %s", qualifiedName);
            return true;
        }
        for (String url : urlArr) {
            try {
                if (!url.startsWith("/")) {
                    logger.e("%s,This url must startWith '/'. %s", url, qualifiedName);
                    return true;
                }
                URI uri = new URI(url);
                if (uri.isOpaque()) {
                    logger.e("%s,This url isn't a hierarchical URI. %s", url, qualifiedName);
                    return true;
                }
                if (Utils.isEmpty(uri.getPath())) {
                    logger.e("%s,error url with empty path on %s", url, qualifiedName);
                    return true;
                }
            } catch (URISyntaxException e) {
                logger.e("%s,error url on class %s" + e.getMessage(), qualifiedName);
                return true;
            }
        }
        return false;
    }
}
