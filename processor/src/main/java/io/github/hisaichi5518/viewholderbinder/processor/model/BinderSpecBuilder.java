package io.github.hisaichi5518.viewholderbinder.processor.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import io.github.hisaichi5518.viewholderbinder.annotation.ViewHolder;

public class BinderSpecBuilder {

    private final Element classElement;
    private List<Element> viewHolderElements = new ArrayList<>();

    public BinderSpecBuilder(Element classElement) {
        this.classElement = classElement;
    }

    public void addViewHolderElements(List<Element> elements) {
        viewHolderElements.addAll(elements);
    }

    public TypeSpec build() {
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(buildLayoutInflaterFieldSpec());
        fieldSpecs.add(buildAdapterFieldSpec());

        List<MethodSpec> methodSpecs = new ArrayList<>();
        methodSpecs.add(buildConstructorMethodSpec());
        methodSpecs.add(buildCreatorMethodSpec());
        methodSpecs.add(buildBinderMethodSpec());

        return buildTypeSpec(fieldSpecs, methodSpecs);
    }

    private FieldSpec buildLayoutInflaterFieldSpec() {
        return FieldSpec.builder(LayoutInflater.class, "layoutInflater", Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private FieldSpec buildAdapterFieldSpec() {
        return FieldSpec.builder(ClassName.get(classElement.asType()), "adapter", Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private MethodSpec buildConstructorMethodSpec() {
        return MethodSpec.constructorBuilder()
                .addParameter(ClassName.get(Context.class), "context")
                .addParameter(ClassName.get(classElement.asType()), "adapter")
                .addStatement("this.layoutInflater = $L.from(context)", ClassName.get(LayoutInflater.class))
                .addStatement("this.adapter = adapter")
                .build();
    }

    private MethodSpec buildCreatorMethodSpec() {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SuppressLint.class)
                .addMember("value", "$S", "ResourceType")
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("create")
                .addParameter(ViewGroup.class, "parent")
                .addParameter(Integer.class, "viewType")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(annotationSpec)
                .returns(ClassName.get("android.support.v7.widget.RecyclerView", "ViewHolder"))
                .beginControlFlow("switch (viewType)");

        for (Element element : viewHolderElements) {
            ViewHolder annotation = element.getAnnotation(ViewHolder.class);

            builder.beginControlFlow("case $L:", annotation.viewType())
                    .addStatement("return new $L(layoutInflater.inflate($L, parent, false))",
                            element.asType(),
                            annotation.layout())
                    .endControlFlow();
        }

        builder.beginControlFlow("default: ")
                .addStatement("throw new $L($S)", ClassName.get(IllegalArgumentException.class), "not match viewType")
                .endControlFlow()
                .endControlFlow();

        return builder.build();
    }

    private MethodSpec buildBinderMethodSpec() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addParameter(ClassName.get("android.support.v7.widget.RecyclerView", "ViewHolder"), "holder")
                .addParameter(Integer.class, "position")
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("switch (holder.getItemViewType())");

        for (Element element : viewHolderElements) {
            ViewHolder annotation = element.getAnnotation(ViewHolder.class);

            builder.beginControlFlow("case $L:", annotation.viewType())
                    .addStatement("adapter.$L(($L) holder, position)",
                            element.getEnclosingElement().getSimpleName(),
                            element.asType())
                    .addStatement("break")
                    .endControlFlow();
        }

        builder.beginControlFlow("default: ")
                .addStatement("throw new $L($S)", ClassName.get(IllegalArgumentException.class), "not match viewType")
                .endControlFlow()
                .endControlFlow();

        return builder.build();
    }

    private TypeSpec buildTypeSpec(List<FieldSpec> fieldSpecs, List<MethodSpec> methodSpecs) {
        return TypeSpec.classBuilder(classElement.getSimpleName().toString() + "ViewHolderBinder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fieldSpecs)
                .addMethods(methodSpecs)
                .build();
    }
}
