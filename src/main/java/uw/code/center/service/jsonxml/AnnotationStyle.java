package uw.code.center.service.jsonxml;

/**
 * 注解类型
 *
 * 
 * @since 2017/9/13
 */
public enum AnnotationStyle {

    /**
     * Jackson 2.x
     *
     * @see <a
     * href="https://github.com/FasterXML/jackson-annotations">https://github.com/FasterXML/jackson-annotations</a>
     */
    JACKSON2,

    /**
     * JAXB
     */
    JAXB,

    /**
     *
     */
    XSTREAM,

    /**
     * No-op style, adds no annotations at all.
     */
    NONE,
}
