package uw.code.center.service.jsonxml;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Java 类型信息，承载 VO 生成中单个属性的类型、描述与集合语义。
 * <p>
 * 维护三组静态映射：原生类型集合（用于判定是否需要默认值/导包）、需要导包的包装类型（如 {@code Date} → {@code java.util.Date}）、
 * 以及基本类型与装箱类型的双向映射（用于拆箱/装箱）。
 * </p>
 *
 * @since 2017/10/10
 */
public class JavaType {

    /**
     * 原生类型集合：这些类型无需导包，且 {@link #defaultValue()} 返回 null。String 为方便处理也归入此集合。
     */
    private final static Map<String, String> PRIMITIVE_TYPE_MAP =
            new ImmutableMap.Builder<String, String>()
                    .put("byte", "")
                    .put("int", "")
                    .put("short", "")
                    .put("long", "")
                    .put("float", "")
                    .put("double", "")
                    .put("boolean", "")
                    .put("char", "")
                    .put("String", "") // 把String归为原始类型方便处理
                    .build();

    /**
     * 需要导包的类型映射：类型名 → 全限定导入路径。
     */
    private final static Map<String, String> NEED_IMPORT_TYPE =
            new ImmutableMap.Builder<String, String>()
                    .put("Date", "java.util.Date").build();

    /**
     * 装箱类型：基本类型 ↔ 包装类型的双向映射。
     */
    private final static ImmutableBiMap<String, String> BOXED_TYPE_MAP =
            new ImmutableBiMap.Builder<String, String>()
                    .put("byte", "Byte")
                    .put("int", "Integer")
                    .put("short", "Short")
                    .put("long", "Long")
                    .put("float", "Float")
                    .put("double", "Double")
                    .put("boolean", "Boolean")
                    .put("char", "Character")
                    .build();

    /**
     * 类型名。
     */
    private String type;

    /**
     * 类型描述（取自 JSON 属性值或 $ 分隔的描述部分）。
     */
    private String description;

    /**
     * 是否为列表（数组）语义。
     */
    private final boolean isList;

    /**
     * 构造普通类型。
     *
     * @param type        类型名
     * @param description 类型描述
     */
    public JavaType(String type, String description) {
        this.type = type;
        this.description = description;
        this.isList = false;
    }

    /**
     * 构造类型，可选拆箱（用于把解析得到的包装类型还原为基本类型）。
     *
     * @param type        类型名
     * @param description 类型描述
     * @param isUnBox     {@code true} 时将包装类型拆为基本类型
     */
    public JavaType(String type, String description, boolean isUnBox) {
        if (isUnBox){
            if (BOXED_TYPE_MAP.containsValue(type))
                this.type = BOXED_TYPE_MAP.inverse().get(type);
        } else {
            this.type = type;
        }
        this.description = description;
        this.isList = false;
    }

    /**
     * 构造类型，可指定列表语义。
     *
     * @param type        元素类型名
     * @param isList      是否列表
     * @param description 类型描述
     */
    public JavaType(String type, boolean isList, String description) {
        this.type = type;
        this.description = description;
        this.isList = isList;
    }

    /**
     * 获取类型名；若为列表语义则返回 {@code List<元素类型>}。
     *
     * @return 类型名或列表类型表达式
     */
    public String getType() {
        return isArray() ? "List<" + this.type + ">" : this.type;
    }

    /**
     * 获取类型描述。
     *
     * @return 类型描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 是否需要导包（当前仅 {@code Date} 需要）。
     *
     * @return {@code true} 需要导包
     */
    public boolean needImport() {
        return NEED_IMPORT_TYPE.containsKey(this.type);
    }

    /**
     * 返回该类型的默认初始值表达式；原生类型返回 null。
     *
     * @return 默认初始值表达式，或 null
     */
    public String defaultValue() {
        if (!isPrimitive()) {
            if (!isList)
                return String.format("new %s()", this.type);
            else
                return String.format("new ArrayList<%s>()", this.type);
        }
        return null;
    }

    /**
     * 获取需要导包类型的全限定导入路径。
     *
     * @return 导入路径，无需导包时返回 null
     */
    public String getImportPath() {
        return NEED_IMPORT_TYPE.get(this.type);
    }

    /**
     * 是否为原生类型（含 String）。
     *
     * @return {@code true} 为原生类型
     */
    public boolean isPrimitive() {
        return PRIMITIVE_TYPE_MAP.containsKey(this.type);
    }

    /**
     * 是否为列表（数组）语义。
     *
     * @return {@code true} 为列表
     */
    public boolean isArray() {
        return isList;
    }

    /**
     * 将基本类型装箱为包装类型；非基本类型原样返回。
     *
     * @return 装箱后的类型名
     */
    public String box() {
        if (BOXED_TYPE_MAP.containsKey(type))
            return BOXED_TYPE_MAP.get(type);
        return type;
    }

    /**
     * 将包装类型拆箱为基本类型；非包装类型原样返回。
     *
     * @return 拆箱后的类型名
     */
    public String unbox() {
        if (BOXED_TYPE_MAP.containsValue(type))
            return BOXED_TYPE_MAP.inverse().get(type);
        return type;
    }
}
