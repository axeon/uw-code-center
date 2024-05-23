package uw.code.center.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 模板类型。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Schema(title = "模板类型", description = "模板组类型")
public enum TemplateType {


    DbEntity(11, "DbEntity代码"),

    DbCommon(19, "Db通用代码"),

    VueApi(21, "vueApi代码"),

    VueRouter(22, "vueRouter代码"),

    VuePage(23, "vuePage代码"),

    VueI18N(24, "VueI18N代码"),

    VueCommon(29, "vue通用代码"),

    Jmeter(31, "jmeter模板");

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
