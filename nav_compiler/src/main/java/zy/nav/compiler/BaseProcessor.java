package zy.nav.compiler;

import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class BaseProcessor extends AbstractProcessor {

    private static final String KEY_MODULE_NAME = "MODULE_NAME";

    private static final String KEY_DOC_DIR = "DOC_DIR";

    protected Elements elementUtils;

    protected Filer filer;

    private Types typeUtils;

    protected String moduleName;

    protected String originalModuleName;

    protected String docDir;

    protected Logger logger;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
        typeUtils = env.getTypeUtils();
        logger = new Logger(env.getMessager());
        Map<String, String> options = env.getOptions();
        if (options != null && !options.isEmpty()) {
            originalModuleName = options.get(KEY_MODULE_NAME);
            docDir = options.get(KEY_DOC_DIR);
        }
        if (!Utils.isEmpty(originalModuleName)) {
            moduleName = originalModuleName.replaceAll("[^0-9a-zA-Z_]+", "");
            logger.i("config MODULE_NAME is ---> " + moduleName);
            logger.i("config DOC_DIR is ---> " + docDir);
        } else {
            throw new IllegalArgumentException("Please config annotationProcessorOptions");
        }
    }

    boolean isSubtype(TypeElement element, String parentType) {
        return typeUtils.isSubtype(element.asType(),
                elementUtils.getTypeElement(parentType).asType());
    }

    protected boolean isActivity(TypeElement element) {
        return isSubtype(element, Constants.TYPE_ACTIVITY);
    }

    protected boolean isFragment(TypeElement element) {
        return isSubtype(element, Constants.TYPE_FRAGMENT);
    }
}
