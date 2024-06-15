package uw.code.center.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 模板类型。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Schema(title = "模板类型", description = "模板组类型")
public enum TemplateType {


    DB_COMMON(10, "DB通用代码"),

    DB_ENTITY(11, "DBEntity代码"),

    DB_CONTROLLER(12, "DBController代码"),

    DB_DTO(13, "DBDto代码"),

    VUE_COMMON(20, "vue通用代码"),

    VUE_API(21, "vueApi代码"),

    VUE_ROUTER(22, "vueRouter代码"),

    VUE_PAGE(23, "vuePage代码"),

    VUE_I18N(24, "VueI18N代码"),

    JMETER(31, "Jmeter模板");

    /**
     * 参数值
     */
    @Schema(title = "数值", description = "数值")
    private int value;

    /**
     * 参数信息。
     */
    @Schema(title = "名称", description = "名称")
    private String label;


    TemplateType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static TemplateType findByValue(int value) {
        for (TemplateType e : TemplateType.values()) {
            if (value == e.value) {
                return e;
            }
        }
        return null;
    }

}
