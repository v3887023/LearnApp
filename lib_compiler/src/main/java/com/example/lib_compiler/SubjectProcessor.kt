package com.example.lib_compiler

import com.example.lib_annotations.Subject
import com.example.lib_annotations.entity.SubjectEntity
import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class SubjectProcessor : AbstractProcessor() {
    var filer: Filer? = null
    var messager:Messager?=null

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        filer = p0?.filer
        messager = p0?.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Subject::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {

        val entityType = SubjectEntity::class.java
        val className = ParameterizedTypeName.get(ArrayList::class.java, entityType)
        val fieldSpec = FieldSpec.builder(className, "subjectEntities")
            .addModifiers(Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
            .initializer("new \$T()", className)
            .build()

        val codeBlockBuilder = CodeBlock.builder()
        val annotationType = Subject::class.java
        if (roundEnvironment != null) {
            for (element in roundEnvironment.getElementsAnnotatedWith(annotationType)) {
                val subject = element.getAnnotation(annotationType)
                try {
                    codeBlockBuilder.addStatement(
                            "subjectEntities.add(new \$T(\$S, \$S, \$S, ${subject.isTest}))",
                            TypeName.get(entityType),
                            element.toString(),
                            subject.title,
                            subject.description
                    )
                } catch (e: Exception) {
                    messager?.printMessage(Diagnostic.Kind.ERROR, e.message, element)
                }
            }
        }

        val methodSpec = MethodSpec.methodBuilder("getSubjectEntities")
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
            .returns(className)
            .addStatement("return subjectEntities")
            .build()

        val typeSpec = TypeSpec.classBuilder("SubjectProvider")
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
            .addField(fieldSpec)
            .addStaticBlock(codeBlockBuilder.build())
            .addMethod(methodSpec)
            .build()

        try {
            val javaFile = JavaFile.builder("com.example", typeSpec)
                .addFileComment("Auto generated, do not edit!!!")
                .build()
            javaFile.writeTo(filer)
        } catch (ignored: Exception) {

        }

        return true
    }
}