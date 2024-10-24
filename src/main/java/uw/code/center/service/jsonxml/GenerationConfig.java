package uw.code.center.service.jsonxml;

/**
 * 描述:
 *
 */

public class GenerationConfig {
    /**
     * 生成源的类型 xml or json
     */
    private GenerationType generationType = GenerationType.JSON;

    /**
     * 生成源的内容 xml格式 or json格式 的内容
     */
    private String generationText;

    /**
     * 生成的对象的名字 json可用 XML 默认就是Camel
     */
    private String objectName = "GenerationObject";

    /**
     * 生成Java 注解类型，目前支持JACKSON2,FASTJSON
     */
    private AnnotationStyle annotationStyle = AnnotationStyle.JACKSON2;

    /**
     * 是否为骆峰式命名  false 为是 true 为否
     */
    private boolean camel;

    /**
     * 生成包路径
     */
    private String packageName = "com.zwy.generation";

    /**
     * 是否生成Java文档注释
     */
    private boolean generateJavaDoc = true;

    /**
     * 属性上是否携带swagger注解 为true携带 因为需要导包 但是swagger2和3版本路径不一致且注解也不同 目前就默认使用swagger3的注解规范  有必要的话可以开放参数出来选择使用的swagger版本
     */
    private boolean takeSwagger;

    /**
     * 生成作者
     */
    private String author = "axeon";

    /**
     * 生成注释
     */
    private String comment = "自动生成";


    public GenerationType getGenerationType() {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }

    public String getGenerationText() {
        return generationText;
    }

    public void setGenerationText(String generationText) {
        this.generationText = generationText;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public AnnotationStyle getAnnotationStyle() {
        return annotationStyle;
    }

    public void setAnnotationStyle(AnnotationStyle annotationStyle) {
        this.annotationStyle = annotationStyle;
    }

    public boolean isCamel() {
        return camel;
    }

    public void setCamel(boolean camel) {
        this.camel = camel;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isGenerateJavaDoc() {
        return generateJavaDoc;
    }

    public void setGenerateJavaDoc(boolean generateJavaDoc) {
        this.generateJavaDoc = generateJavaDoc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public boolean isTakeSwagger() {
        return takeSwagger;
    }

    public void setTakeSwagger(boolean takeSwagger) {
        this.takeSwagger = takeSwagger;
    }

}
