package com.jason.knife.annotation;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class MyProcessor  extends AbstractProcessor {
    private Filer filer;
    //初始化处理器
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // Filer是个接口，支持通过注解处理器创建新文件
        filer = processingEnv.getFiler();
    }
    //处理器的主函数 处理注解
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
//            //获取最里面的节点
//            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//            String packageName = enclosingElement.getQualifiedName().toString();
//            packageName =packageName .substring(0, packageName.lastIndexOf("."));
//            String className = enclosingElement.getSimpleName().toString();
//            String typeMirror = element.asType().toString();
//
//            //注解的值
//            int annotationValue = element.getAnnotation(BindView.class).value();
//            String name = element.getSimpleName().toString();
//            TypeName type = TypeName.get(enclosingElement.asType());//此元素定义的类型
//            if (type instanceof ParameterizedTypeName) {
//                type = ((ParameterizedTypeName) type).rawType;
//            }
//
//            System.out.println("typeMirror:"+typeMirror);//被注解的对象的类型
//            System.out.println("packageName:"+packageName);//包名
//            System.out.println("className:"+className);//类名
//            System.out.println("annotationValue:"+annotationValue);//注解传过来的参数
//            System.out.println("name:"+name);//被注解的对象的名字
//            System.out.println("type:"+type);//当前被注解对象所在类的完整路径
//
//            ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");
//            //创建代码块
//            CodeBlock.Builder builder = CodeBlock.builder()
//                    .add("target.$L = ", name);//$L是占位符，会把后面的name参数拼接大$L所在的地方
//            builder.add("($L)source.findViewById($L)", typeMirror , annotationValue);
//
//            // 创建main方法
//            MethodSpec methodSpec = MethodSpec.constructorBuilder()
//                    .addModifiers(Modifier.PUBLIC)
//                    .addParameter(type,"target")
//                    .addParameter(ClassName.get("android.view", "View"),"source")
//                    .addStatement("$L", builder.build())
//                    .build();
//
//            // 创建类
//            TypeSpec helloWorld = TypeSpec.classBuilder(bindingClassName.simpleName())
//                    .addModifiers(Modifier.PUBLIC)
//                    .addMethod(methodSpec)
//                    .build();
//
//            try {
//                // 生成文件
//                JavaFile javaFile = JavaFile.builder("com.chs.annotationtest", helloWorld)
//                        .build();
//                //　将文件写出
//                javaFile.writeTo(System.out);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return true;
    }
    //指定处理器处理哪个注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
//        annotataions.add(BindView.class.getCanonicalName());
        return annotataions;
    }
    //指定使用的java的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}

