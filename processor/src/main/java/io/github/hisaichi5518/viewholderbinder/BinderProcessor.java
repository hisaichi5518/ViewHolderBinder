package io.github.hisaichi5518.viewholderbinder;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import io.github.hisaichi5518.viewholderbinder.annotation.ViewHolder;
import io.github.hisaichi5518.viewholderbinder.model.BinderSpecBuilder;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"io.github.hisaichi5518.viewholderbinder.annotation.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BinderProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Elements elements;
    private Map<Element, List<Element>> viewHolderElements = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element viewHolderElement : roundEnv.getElementsAnnotatedWith(ViewHolder.class)) {
            if (viewHolderElement.getKind() != ElementKind.PARAMETER) {
                messager.printMessage(Diagnostic.Kind.ERROR, "@ViewHolder annotation's target is not ElementKind.PARAMETER.", viewHolderElement);
                continue;
            }

            // ElementKind.PARAMETER -> METHOD -> TYPE
            Element classElement = viewHolderElement.getEnclosingElement().getEnclosingElement();
            List<Element> elements = viewHolderElements.get(classElement);
            if (elements == null) {
                elements = new ArrayList<>();
            }
            elements.add(viewHolderElement);
            viewHolderElements.put(viewHolderElement.getEnclosingElement().getEnclosingElement(), elements);
        }

        for (Map.Entry<Element, List<Element>> entry : viewHolderElements.entrySet()) {
            BinderSpecBuilder binderSpecBuilder = new BinderSpecBuilder(entry.getKey());
            binderSpecBuilder.addViewHolderElements(entry.getValue());

            try {
                JavaFile.builder(elements.getPackageOf(entry.getKey()).getQualifiedName().toString(), binderSpecBuilder.build())
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
