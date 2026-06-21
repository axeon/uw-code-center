package uw.code.center.service.jsonxml;

/**
 * JSON/XML 转 Java VO 的生成配置。
 * <p>
 * 描述一次 VO 生成所需的全部输入与输出规范，包括数据源类型/内容、对象命名、注解风格、
 * 包路径、是否生成 JavaDoc、是否附加 Swagger 注解、作者与注释等。由 {@link VoCodeGenTools#generator(GenerationConfig)} 消费。
 * </p>
 */
public class GenerationConfig {
    /**
     * 生成源的类型：JSON 或 XML。
     */
    private GenerationType generationType = GenerationType.JSON;

    /**
     * 生成源内容，JSON 或 XML 格式的原始文本。
     */
    private String generationText;

    /**
     * 生成的对象名（类名）。JSON 场景下使用；XML 场景下默认取根节点名， camelCase。
     */
    private String objectName = "GenerationObject";

    /**
     * 生成代码的注解风格，目前支持 {@link AnnotationStyle#JACKSON2}、{@link AnnotationStyle#JAXB}、{@link AnnotationStyle#XSTREAM}。
     */
    private AnnotationStyle annotationStyle = AnnotationStyle.JACKSON2;

    /**
     * 是否保持源命名（不强制下划线转驼峰）。{@code false} 表示将下划线命名转为驼峰，{@code true} 表示直接使用源命名。
     */
    private boolean camel;

    /**
     * 生成代码的包路径。
     */
    private String packageName = "com.zwy.generation";

    /**
     * 是否生成 JavaDoc 注释。
     */
    private boolean generateJavaDoc = true;

    /**
     * 属性上是否携带 Swagger3（springdoc）的 {@code @Schema} 注解；为 true 时会追加对应的 import。
     */
    private boolean takeSwagger;

    /**
     * 生成作者，写入 JavaDoc 的 {@code @author}。
     */
    private String author = "axeon";

    /**
     * 生成注释，写入类级 JavaDoc。
     */
    private String comment = "自动生成";


    /**
     * 获取生成源类型。
     *
     * @return 生成源类型
     */
    public GenerationType getGenerationType() {
        return generationType;
    }

    /**
     * 设置生成源类型。
     *
     * @param generationType 生成源类型
     */
    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }

    /**
     * 获取生成源内容。
     *
     * @return 生成源内容
     */
    public String getGenerationText() {
        return generationText;
    }

    /**
     * 设置生成源内容。
     *
     * @param generationText 生成源内容
     */
    public void setGenerationText(String generationText) {
        this.generationText = generationText;
    }

    /**
     * 获取对象名。
     *
     * @return 对象名
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * 设置对象名。
     *
     * @param objectName 对象名
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * 获取注解风格。
     *
     * @return 注解风格
     */
    public AnnotationStyle getAnnotationStyle() {
        return annotationStyle;
    }

    /**
     * 设置注解风格。
     *
     * @param annotationStyle 注解风格
     */
    public void setAnnotationStyle(AnnotationStyle annotationStyle) {
        this.annotationStyle = annotationStyle;
    }

    /**
     * 是否保持源命名。
     *
     * @return {@code true} 保持源命名，{@code false} 下划线转驼峰
     */
    public boolean isCamel() {
        return camel;
    }

    /**
     * 设置是否保持源命名。
     *
     * @param camel {@code true} 保持源命名，{@code false} 下划线转驼峰
     */
    public void setCamel(boolean camel) {
        this.camel = camel;
    }

    /**
     * 获取包路径。
     *
     * @return 包路径
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置包路径。
     *
     * @param packageName 包路径
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 是否生成 JavaDoc。
     *
     * @return {@code true} 生成
     */
    public boolean isGenerateJavaDoc() {
        return generateJavaDoc;
    }

    /**
     * 设置是否生成 JavaDoc。
     *
     * @param generateJavaDoc {@code true} 生成
     */
    public void setGenerateJavaDoc(boolean generateJavaDoc) {
        this.generateJavaDoc = generateJavaDoc;
    }

    /**
     * 获取作者。
     *
     * @return 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者。
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取注释。
     *
     * @return 注释
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置注释。
     *
     * @param comment 注释
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 是否附加 Swagger 注解。
     *
     * @return {@code true} 附加
     */
    public boolean isTakeSwagger() {
        return takeSwagger;
    }

    /**
     * 设置是否附加 Swagger 注解。
     *
     * @param takeSwagger {@code true} 附加
     */
    public void setTakeSwagger(boolean takeSwagger) {
        this.takeSwagger = takeSwagger;
    }

}
