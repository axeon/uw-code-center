package uw.code.center.service.jsonxml;

import uw.common.util.SystemClock;

import java.util.Map;

/**
 * VO 生成过程中的一个内部类描述（对应 JSON 中的一个对象节点）。
 * <p>
 * 每个 Json2JavaVo 记录一个嵌套对象的属性名、生成的类名、属性列表，以及用于去重的父级 ID 与自身 ID。
 * {@link VoCodeGenTools} 会递归收集所有嵌套对象为 Json2JavaVo，最终输出为 Java 源码中的 static inner class。
 * </p>
 *
 * @since 2017/9/13
 */
public class Json2JavaVo {

    /**
     * 该对象在父节点中的属性名。
     */
    private String propName;

    /**
     * 生成的内部类名。
     */
    private String className = null;

    /**
     * 该类的属性列表（属性名 → 类型信息）。
     */
    private Map<String, JavaType> propMap;

    /**
     * 父级 Json2JavaVo 的 ID，根对象为 0。
     */
    private long prvId;

    /**
     * 自身唯一 ID，使用系统时钟生成，用于类型去重标识。
     */
    private long id = SystemClock.now();

    /**
     * 构造一个 VO 描述。
     *
     * @param propName  在父节点中的属性名
     * @param className 生成的内部类名
     * @param prvId     父级 ID（根对象传 0）
     */
    public Json2JavaVo(String propName, String className, long prvId) {
        this.propName = propName;
        this.className = className;
        this.prvId = prvId;
    }

    /**
     * 获取属性名。
     *
     * @return 属性名
     */
    public String getPropName() {
        return propName;
    }

    /**
     * 设置属性名。
     *
     * @param propName 属性名
     */
    public void setPropName(String propName) {
        this.propName = propName;
    }

    /**
     * 获取内部类名。
     *
     * @return 内部类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置内部类名。
     *
     * @param className 内部类名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取属性列表。
     *
     * @return 属性列表
     */
    public Map<String, JavaType> getPropMap() {
        return propMap;
    }

    /**
     * 设置属性列表。
     *
     * @param propMap 属性列表
     */
    public void setPropMap(Map<String, JavaType> propMap) {
        this.propMap = propMap;
    }

    /**
     * 获取父级 ID。
     *
     * @return 父级 ID
     */
    public long getPrvId() {
        return prvId;
    }

    /**
     * 获取自身 ID。
     *
     * @return 自身 ID
     */
    public long getId() {
        return id;
    }

    /**
     * 设置自身 ID。
     *
     * @param id 自身 ID
     */
    public void setId(long id) {
        this.id = id;
    }
}
