package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 描述:生成单个VO代码的请求体
 *
 */
@Schema(title = "生成单个VO代码的参数", description = "生成单个VO代码的参数")
public class JsonXmlRequestParam {

    /**
     * 文本的类型,json或者xml,必传
     */
    @Schema(title = "文本的类型,json或者xml,必传", description = "文本的类型,json或者xml,必传", requiredMode = Schema.RequiredMode.REQUIRED)
    private String textType;

    /**
     * 文本内容 xml或者json的值,必传
     */
    @Schema(title = "文本内容 xml或者json的值,必传", description = "文本内容 xml或者json的值,必传", requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;

    /**
     * 属性上是否携带swagger注解 为true携带 因为需要导包 但是swagger2和3版本路径不一致且注解也不同 目前就默认使用swagger3的注解规范 有必要的话可以开放参数出来选择使用的swagger版本
     */
    @Schema(title = "属性上是否携带swagger注解 为true携带", description = "属性上是否携带swagger注解 为true携带", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean takeSwagger = true;

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTakeSwagger() {
        return takeSwagger;
    }

    public void setTakeSwagger(boolean takeSwagger) {
        this.takeSwagger = takeSwagger;
    }
}
