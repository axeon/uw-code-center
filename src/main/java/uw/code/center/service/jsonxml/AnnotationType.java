package uw.code.center.service.jsonxml;

/**
 * 注解写入位置的分类，由 {@link VoCodeGenTools#writeMethodAnnotation(AnnotationType, StringBuilder, String, JavaType, int)} 区分处理。
 *
 * @since 2017/9/13
 */
public enum AnnotationType {
    /**
     * 方法级（通用）。
     */
    METHOD,
    /**
     * getter 方法上。
     */
    METHOD_GET,
    /**
     * 类级。
     */
    CLASS,
    /**
     * 属性声明处。
     */
    PROPERTY,
    /**
     * 属性上方的注解（如 {@code @JsonProperty}）。
     */
    PROPERTY_Annotation
}
