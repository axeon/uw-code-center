package uw.code.center.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import uw.auth.service.dto.EnumStruct;

/**
 * 模板组类型。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Schema(implementation = EnumStruct.class, title = "模板组类型", description = "模板组类型")
public enum TemplateGroupType {

    Database(1, "DataBase类型"),

    Swagger(2, "Swagger类型");


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

    TemplateGroupType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static TemplateGroupType findByValue(int value) {
        for (TemplateGroupType e : TemplateGroupType.values()) {
            if (value == e.value) {
                return e;
            }
        }
        return null;
    }

}
