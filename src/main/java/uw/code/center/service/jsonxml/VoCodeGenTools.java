package uw.code.center.service.jsonxml;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * 描述: 根据传入的xml 或者 json 生成VO对象
 *
 */
public class VoCodeGenTools {

    private static final Logger logger = LoggerFactory.getLogger( VoCodeGenTools.class);


    private JSONTokener jsonTokener;

    /**
     * 是否引入了List
     */
    private boolean importList = false;

    /**
     * 引了那些Java对象,如java.util.Date,方便之后支持对象类型
     */
    private Set<String> importTypeSet = Sets.newHashSet();

    /**
     * 对象的名称 同时也是文件的名称
     */
    private String objectName;

    private String mainProName;

    private Map<String, JavaType> mainProp;

    private List<Json2JavaVo> json2JavaVoList = Lists.newArrayList();

    private Set<String> typeSet = Sets.newHashSet();

    /**
     * 换行符
     */
    private final static String lineSeparator = System.getProperty("line.separator");

    /**
     * 默认四个空格对齐
     */
    private final static String propW = Strings.padEnd("", 4, ' ');

    /**
     * 类型分隔器
     */
    private final static Splitter splitter = Splitter.on("$");

    private static Date now = new Date();

    private GenerationConfig generationConfig;

    private AnnotationStyle annotationStyle = AnnotationStyle.JACKSON2;

    private Predicate<Json2JavaVo> findPredicate = new Predicate<Json2JavaVo>() {
        @Override
        public boolean apply(Json2JavaVo input) {
            return input.getPropName().equals(mainProName);
        }
    };

    public VoCodeGenTools(GenerationConfig config, String objectName, String json) {
        this.generationConfig = config;
        this.annotationStyle = config.getAnnotationStyle();
        this.objectName = objectName;
        this.jsonTokener = new JSONTokener(json);
    }

    /**
     * 是否需要驼峰式命名
     *
     * @return
     */
    private boolean needCamel() {
        return !this.generationConfig.isCamel();
    }

    // 调用入参
//    GenerationConfig config = new GenerationConfig();
//        config.setGenerationText("{\n" +
//                "\"packages\":[],\n" +
//                "\"zone_packages\":\n" +
//                "[{\"ticket_type\":[{\"ticket_type\":\"113\",\"ticket_count\":1,\"trans_num\":1,\"refund_num\":0}],\n" +
//                "\"zone_group\":\"武汉园博会\",\"ticket_key\":\"6242644229\"}],\n" +
//                "\"order_nbr\":\"10000000052017012400230687\",\"order_amount\":\"\",\n" +
//                "\"order_nbr_original\":\"16272596\",\"order_contacts\":\"测试\",\n" +
//                "\"contacts_tel\":\"15997963291\",\"order_remark\":\"\",\"use_date\":\"20170128\",\"order_status\":\"10\"\n" +
//                "}");
//        config.setAnnotationStyle(AnnotationStyle.JACKSON2);
//        config.setGenerationType(GenerationType.JSON);
//        config.setAuthor("awei");
//        config.setComment("hehe");
//        config.setObjectName("orderDetials");
//        config.setGenerationType(GenerationType.JSON);
//        config.setPackageName("com.zwy.example");
//        config.setGenerateJavaDoc(true);
//        config.setCamel(true);
//    // 生成的VO
//    StringBuilder generator = VOCodeGenTools.generator(config);

    /**
     * 生成式入口
     *
     * @param config 这个对象里面包含了 源数据的一些信息 以及对于生成的VO的对象的规范设置
     * @throws IOException
     */
    public static StringBuilder generator(GenerationConfig config) {
        // 获取传入文本是什么类型
        GenerationType sourceType = config.getGenerationType();
        logger.debug("开始生成VO...");
        VoCodeGenTools generator = null;

        if (sourceType == GenerationType.JSON) {
            generator = new VoCodeGenTools(config, config.getObjectName(), config.getGenerationText());
        } else {
            JSONObject xmlJSONObj = XML.toJSONObject(config.getGenerationText());
            String json = xmlJSONObj.toString();
            generator = new VoCodeGenTools(config, config.getObjectName(), json);
        }
        StringBuilder text = generator.run();
        if (null == text || text.isEmpty()) {
            throw new RuntimeException("生成VO对象失败!");
        }
//        logger.debug(text.toString());
        logger.debug("生成VO结束...");
        return text;
    }

    /**
     * 准备工作 然后调用write方法生成
     *
     * @return
     */
    public StringBuilder run() {
        Object curPoint = jsonTokener.nextValue();
        JSONObject next = null;
        while (!(curPoint instanceof JSONObject)) {
            try {
                curPoint = jsonTokener.nextValue();
            } catch (JSONException e) {
                return null;
            }
        }
        next = (JSONObject) curPoint;
        mainProp = Maps.newTreeMap();
        long rootId = 0;
        while (next != JSONObject.NULL) {
            JSONArray jsonArray = next.names();
            String[] nameList = jsonArray.toList().toArray(new String[0]);
            // XML响应头元素
            if (generationConfig.getGenerationType() == GenerationType.XML) {
                if (StringUtils.isBlank(mainProName) && nameList.length > 0) {
                    mainProName = nameList[0];
                    objectName = mainProName;// XML 默认就是Camel
                }
            }
            for (int i = 0; i < nameList.length; i++) {
                String name = nameList[i];
                Object prop = next.get(name);
                JavaType javaType = null;
                if (prop instanceof JSONObject) {// 对象
                    // 继续迭代
                    javaType = new JavaType(needCamel() ? firstUpperCaseCamel(name) : firstToUpper(name), "");
                    mainProp.put(name, javaType);
                    getJavaObject(name, javaType.getType(), rootId, (JSONObject) prop);
                } else if (prop instanceof JSONArray) {//数组
                    JSONArray array = (JSONArray) prop;
                    if (array.length() == 0)
                        javaType = new JavaType("Object", "");
                    else {
                        Object object = array.get(0);
                        if (object instanceof JSONObject) {
                            javaType = new JavaType(needCamel() ? firstUpperCaseCamel(name) : firstToUpper(name), "");

                            // 登记一个类型
                            getJavaObject(name, javaType.getType(), rootId, (JSONObject) object);
                        } else {
                            javaType = getJavaType(object);
                        }
                    }
                    importList = true;
                    mainProp.put(name, new JavaType(javaType.getType(), true, ""));
                } else {
                    mainProp.put(name, getJavaType(prop));
                }
            }

            // 没到尾,接着跑
            if (jsonTokener.more()) {
                next = (JSONObject) jsonTokener.nextValue();
            } else {
                break;
            }
        }
        // 如果是生成XML,则直接以根节点做为Java的最外层类
        if (generationConfig.getGenerationType() == GenerationType.XML) {
            if (annotationStyle == AnnotationStyle.JAXB ||
                    annotationStyle == AnnotationStyle.XSTREAM ||
                    annotationStyle == AnnotationStyle.JACKSON2) {
                Json2JavaVo it = Iterators.find(json2JavaVoList.iterator(), findPredicate);
                mainProp = it.getPropMap();
                Iterators.removeIf(json2JavaVoList.iterator(), findPredicate);
            }
        }
        StringBuilder write = write();
        return write;
    }

    /**
     * 生成属性getter,setter
     *
     * @param voText
     * @param propMap
     */
    public void writeGetterSetter(StringBuilder voText, Map<String, JavaType> propMap, int indent) {
        for (Map.Entry<String, JavaType> typeEntry : propMap.entrySet()) {
            JavaType javaType = typeEntry.getValue();
            String typeName = javaType.getType();
            String name = typeEntry.getKey();
            String javaName = toCamel(name);
            String getterSetterName = new String(needCamel() ? newFirstLowerCaseCamel(name) : firstToUpper(name));
            getterSetterName = firstToUpper(getterSetterName);
            voText.append(String.format("%spublic void set%s(%s %s) {%s", Strings.repeat(propW, indent), getterSetterName, typeName, javaName, lineSeparator));
            voText.append(String.format("%sthis.%s = %s;%s%s}", Strings.repeat(propW, indent + 1), javaName, javaName, lineSeparator, Strings.repeat(propW, indent)));
            // 统一写GET上 不在Get上写注解
//            writeMethodAnnotation(AnnotationType.METHOD_GET,voText,name,javaType,indent);
            voText.append(String.format("%s%spublic %s get%s (){%s", lineSeparator, Strings.repeat(propW, indent), typeName, getterSetterName, lineSeparator));
            voText.append(String.format("%sreturn this.%s;%s%s}%s%s", Strings.repeat(propW, indent + 1), javaName, lineSeparator, Strings.repeat(propW, indent), lineSeparator, lineSeparator));
        }
    }

    /**
     * java doc 注释
     *
     * @param voText
     * @param comment
     * @param author
     */
    public void writeJavaDoc(StringBuilder voText, String comment, String author) {
        voText.append(String.format("%s/**%s", lineSeparator, lineSeparator));
        voText.append(String.format(" * This class is auto generated by tools.%s", lineSeparator));

        if (StringUtils.isNotBlank(comment)) {// 不适合 paragraph
            voText.append(String.format(" * %s %s", comment, lineSeparator));
        }
        if (StringUtils.isNotBlank(author)) {
            voText.append(String.format(" * @author %s %s", author, lineSeparator));
        }

        voText.append(String.format(" * @since %s %s", DateFormatUtils.format(now, "yyyy-MM-dd"), lineSeparator));

        voText.append(" */");
    }

    /**
     * JavaDoc 字段描述
     *
     * @param voText
     * @param javaType
     */
    public void writePropertyJavaDoc(StringBuilder voText, JavaType javaType, int indent) {
        if (generationConfig.isGenerateJavaDoc()) {
            String description = javaType.getDescription();
            voText.append(Strings.repeat(propW, indent)).append(String.format("/**%s", lineSeparator))
                    .append(Strings.repeat(propW, indent)).append(" * ").append(StringUtils.trimToEmpty(description)).append(lineSeparator)
                    .append(Strings.repeat(propW, indent)).append(" */").append(lineSeparator);
        }
    }

    /**
     * 写注解
     *
     * @param type     - 注解类型
     * @param voText   - 输出内容
     * @param propName - 属性名称
     * @param typeName - 对应Java类型名
     * @param indent   - 当前对齐位置
     */
    public void writeMethodAnnotation(AnnotationType type, StringBuilder voText, String propName, JavaType typeName, int indent) {
        StringBuilder builder = new StringBuilder();
        String javaType = "";
        if (type != AnnotationType.CLASS)
            javaType = typeName.getType();
        switch (annotationStyle) {
            case JACKSON2:
                if (generationConfig.getGenerationType() == GenerationType.JSON) {
                    switch (type) {
                        case CLASS:
                            builder.append(lineSeparator)
                                    .append(Strings.repeat(propW, indent - 1))
                                    .append("@JsonInclude(JsonInclude.Include.NON_NULL)")
                                    .append(lineSeparator)
                                    .append(Strings.repeat(propW, indent - 1))
                                    .append("@JsonIgnoreProperties(ignoreUnknown = true)");
                            break;
                        case PROPERTY:
                            if (generationConfig.isGenerateJavaDoc()) {
                                writePropertyJavaDoc(voText, typeName, indent);
                            }
                            break;
                        case PROPERTY_Annotation:
                            builder.append(Strings.repeat(propW, indent))
                                    .append("@JsonProperty(\"").append(propName).append("\")")
                                    .append(lineSeparator);
                            break;
                        case METHOD_GET:
                            builder.append(lineSeparator).append(lineSeparator)
                                    .append(Strings.repeat(propW, indent))
                                    .append("@JsonProperty(\"").append(propName).append("\")");
                            break;
                    }
                } else { // XML
                    switch (type) {
                        case CLASS:
                            builder.append(lineSeparator)
                                    .append(Strings.repeat(propW, indent - 1))
                                    .append("@JacksonXmlRootElement(localName = \"").append(propName).append("\")")
                                    .append(lineSeparator)
                                    .append(Strings.repeat(propW, indent - 1))
                                    .append("@JsonIgnoreProperties(ignoreUnknown = true)");
                            break;
                        case PROPERTY:
                            if (generationConfig.isGenerateJavaDoc()) {
                                writePropertyJavaDoc(voText, typeName, indent);
                            }
                            break;
                        case METHOD_GET:
                            // @JacksonXmlElementWrapper(useWrapping=false)

                            if (typeName.isArray()) {
                                builder.append(lineSeparator).append(lineSeparator)
                                        .append(Strings.repeat(propW, indent))
                                        .append("@JacksonXmlElementWrapper(useWrapping=false)")
                                        .append(lineSeparator)
                                        .append(Strings.repeat(propW, indent))
                                        .append("@JacksonXmlProperty(localName=\"").append(propName).append("\")");
                            } else {
                                builder.append(lineSeparator).append(lineSeparator)
                                        .append(Strings.repeat(propW, indent))
                                        .append("@JacksonXmlProperty(localName=\"").append(propName).append("\")");
                            }
                            break;
                    }
                }
                break;
            case XSTREAM:
                switch (type) {
                    case CLASS:
                        builder.append(lineSeparator)
                                .append(Strings.repeat(propW, indent - 1))
                                .append("@XStreamAlias(\"").append(propName).append("\")");
                        break;
                    case PROPERTY:
                        if (generationConfig.isGenerateJavaDoc()) {
                            writePropertyJavaDoc(voText, typeName, indent);
                        }
                        if (typeName.isArray()) {
                            builder.append(Strings.repeat(propW, indent)).append("@XStreamImplicit")
                                    .append(lineSeparator);
                        } else {
                            builder.append(Strings.repeat(propW, indent)).append("@XStreamAlias(\"").append(propName).append("\")")
                                    .append(lineSeparator);
                        }
                        break;
                }
                break;
            case JAXB:
                switch (type) {
                    case CLASS:
                        builder.append(lineSeparator)
                                .append(Strings.repeat(propW, indent - 1))
                                .append("@XmlRootElement(name = \"").append(propName).append("\")");
                        break;
                    case PROPERTY:
                        if (generationConfig.isGenerateJavaDoc()) {
                            writePropertyJavaDoc(voText, typeName, indent);
                        }
                        break;
                    case METHOD_GET://写GET上就好了
                        builder.append(lineSeparator).append(Strings.repeat(propW, indent))
                                .append("@XmlElement(name = \"").append(propName).append("\")")
                                .append(lineSeparator);
                        break;
                }
        }
        voText.append(builder.toString());
    }

    /**
     * 返回生成的VO字符串
     *
     * @return
     */
    public StringBuilder write() {
        StringBuilder voText = new StringBuilder();
        voText.append(String.format("package %s;%s", generationConfig.getPackageName(), lineSeparator));
        voText.append(lineSeparator);

        switch (annotationStyle) {
            case JACKSON2:
                switch (generationConfig.getGenerationType()) {
                    case JSON:
                        voText.append(String.format("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.annotation.JsonInclude;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.annotation.JsonProperty;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.annotation.JsonPropertyOrder;%s", lineSeparator));
                        break;
                    case XML:
                        voText.append(String.format("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;%s", lineSeparator));
                        voText.append(String.format("import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;%s", lineSeparator));
                        break;
                }
                break;
            case JAXB:
                voText.append(String.format("import javax.xml.bind.annotation.XmlElement;%s", lineSeparator));
                voText.append(String.format("import javax.xml.bind.annotation.XmlRootElement;%s", lineSeparator));
                break;
            case XSTREAM:
                voText.append(String.format("import com.thoughtworks.xstream.annotations.XStreamAlias;%s", lineSeparator));
                voText.append(String.format("import com.thoughtworks.xstream.annotations.XStreamImplicit;%s", lineSeparator));
                voText.append(String.format("import com.thoughtworks.xstream.annotations.XStreamAsAttribute;%s", lineSeparator));
                break;
        }

        if (importList || !importTypeSet.isEmpty())
            voText.append(lineSeparator);

        // Java类型引入
        if (importList) {
            voText.append(String.format("import java.util.List;%s", lineSeparator));
            voText.append(String.format("import java.util.ArrayList;%s", lineSeparator));
        }
        if (!importTypeSet.isEmpty()) {
            for (String importPath : importTypeSet) {
                voText.append(String.format("import %s;%s", importPath, lineSeparator));
            }
        }
        // Swagger引入
        if(this.generationConfig.isTakeSwagger()){
            voText.append(String.format("import io.swagger.v3.oas.annotations.media.Schema;%s", lineSeparator));
        }
        boolean isFirstLine = true;

        if (generationConfig.isGenerateJavaDoc())
            writeJavaDoc(voText, generationConfig.getComment(), generationConfig.getAuthor());

        // main class
        writeMethodAnnotation(AnnotationType.CLASS, voText, mainProName, null, 1);
        if(this.generationConfig.isTakeSwagger()){
            voText.append(String.format("%s@Schema(title = \"%s\", description = \"%s\")",lineSeparator,objectName,objectName));
        }
        voText.append(String.format("%spublic class %s {", lineSeparator, objectName));
        voText.append(lineSeparator);
        for (Map.Entry<String, JavaType> typeEntry : mainProp.entrySet()) {
            JavaType javaType = typeEntry.getValue();
            String typeName = javaType.getType();
            String name = typeEntry.getKey();
            String javaName = toCamel(name);
            if (isFirstLine) {
                voText.append(lineSeparator);
            }
            writeMethodAnnotation(AnnotationType.PROPERTY, voText, name, javaType, 1);
            // 属性上@JsonProperty里面的字段使用原json字段 而不是驼峰 但是writeMethodAnnotation这个方法里面会用到驼峰名字 但是调用已经被丢弃了 假如重用 可以看看这个方法里面的实现
            writeMethodAnnotation(AnnotationType.PROPERTY_Annotation, voText, name, javaType, 1);
            // Swagger引入
            if(this.generationConfig.isTakeSwagger()){
                String schema = StringUtils.isNotBlank(javaType.getDescription()) ? javaType.getDescription() : name;
                voText.append(String.format("%s@Schema(title = \"%s\", description = \"%s\")%s",propW,schema,schema, lineSeparator));
            }
            if (javaType.isPrimitive()) {
                voText.append(String.format("%sprivate %s %s;%s", propW, typeName, javaName, lineSeparator));
            } else {
                voText.append(String.format("%sprivate %s %s;%s", propW, typeName, javaName, lineSeparator));
            }
            voText.append(lineSeparator);
            isFirstLine = false;
        }
        writeGetterSetter(voText, mainProp, 1);

        // inner static class
        if (!json2JavaVoList.isEmpty()) {
            int indent = 2;// FIXME static class no need to care about inner order
            isFirstLine = true;
            for (Json2JavaVo json2JavaVo : json2JavaVoList) {
                String className = json2JavaVo.getClassName();
                String propName = json2JavaVo.getPropName();
                Map<String, JavaType> propMap = json2JavaVo.getPropMap();

                // other class
                if (!isFirstLine)
                    voText.append(lineSeparator);
                writeMethodAnnotation(AnnotationType.CLASS, voText, propName, new JavaType(className, ""), indent);
                if(this.generationConfig.isTakeSwagger()){
                    voText.append(String.format("%s%s@Schema(title = \"%s\", description = \"%s\")",lineSeparator, propW,className,className));
                }
                voText.append(String.format("%s%spublic static class %s {%s", lineSeparator, Strings.repeat(propW, indent - 1), className, lineSeparator));
                for (Map.Entry<String, JavaType> typeEntry : propMap.entrySet()) {
                    JavaType javaType = typeEntry.getValue();
                    String typeName = javaType.getType();
                    String name = typeEntry.getKey();
                    String javaName = toCamel(name);
                    writeMethodAnnotation(AnnotationType.PROPERTY, voText, name, javaType, indent);
                    writeMethodAnnotation(AnnotationType.PROPERTY_Annotation, voText, name, javaType, indent);
                    if(this.generationConfig.isTakeSwagger()){
                        String schema = StringUtils.isNotBlank(javaType.getDescription()) ? javaType.getDescription() : name;
                        voText.append(String.format("%s@Schema(title = \"%s\", description = \"%s\")%s",Strings.repeat(propW, indent),schema,schema, lineSeparator));
                    }
                    if (javaType.isPrimitive()) {
                        voText.append(String.format("%sprivate %s %s;%s", Strings.repeat(propW, indent), typeName, javaName, lineSeparator));
                    } else {
                        voText.append(String.format("%sprivate %s %s;%s", Strings.repeat(propW, indent),
                                typeName, javaName, lineSeparator));
                    }
                    voText.append(lineSeparator);
                }
                writeGetterSetter(voText, propMap, indent);
                voText.append(String.format("%s}", Strings.repeat(propW, indent - 1)));
                isFirstLine = false;
            }
        }

        voText.append(String.format("%s}", lineSeparator));
        return voText;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    private static String firstToUpper(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toUpperCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    private static String firstToLower(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }

    /**
     * 下划线转首字母大写Camel
     *
     * @param str
     * @return
     */
    private static String firstUpperCaseCamel(String str) {
        if (StringUtils.isNotBlank(str))
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
        return "";
    }

    /**
     * 下划线转首字母小写Camel
     *
     * @param str
     * @return
     */
    private static String firstLowerCaseCamel(String str) {
        if (StringUtils.isNotBlank(str))
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
        return "";
    }


    /**
     * 重写生成驼峰方法
     *
     * @param str
     * @return
     */
    private static String newFirstLowerCaseCamel(String str) {
        if (StringUtils.isNotBlank(str))
            if (str.contains("_")) {
                // 有下划线 直接转驼峰
                return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
            } else {
                if (Character.isUpperCase(str.charAt(0))) {
                    // 如果首字母是大写就转小写
                    return firstToLower(str);
                }
                return str;
            }
        return "";
    }

    /**
     * toCamel
     *
     * @param str
     * @return
     */
    private String toCamel(String str) {
        if (generationConfig.isCamel())
            return str;
        return newFirstLowerCaseCamel(str);
    }

    /**
     * 转换属性类型
     *
     * @param prop
     * @return
     */
    private JavaType getJavaType(Object prop) {
        // 由于目前json不支持注释 所以对于注释的只能放在String类型的value使用$符号进行分割 暂时只支持String类型
        if (prop instanceof String ) {
            if (((String) prop).contains("$")){
                List<String> dataList = splitter.splitToList((String) prop);
                String type = dataList.get(0);
                String description = dataList.size() > 1 ? dataList.get(dataList.size() - 1) : "";
                JavaType javaType = new JavaType("String", description);
                if (javaType.needImport()) {
                    importTypeSet.add(javaType.getImportPath());
                }
                return javaType;
            } else {
                return new JavaType("String", String.valueOf(prop));
            }
        } else {
            String name = prop.getClass().getSimpleName();
            // 使用自动拆箱创建对象 因为对象里面需要使用基本类型使用
            JavaType javaType = new JavaType(name, "",true);
            if (javaType.needImport()) {
                importTypeSet.add(javaType.getImportPath());
            }
            return javaType;
        }
//        // 如果解析时发现没有$，则默认为String类型，里面的东西都是属性描述
//        return new JavaType("String", String.valueOf(prop));
    }

    /**
     * 对象登记
     *
     * @param className
     * @param jsonObject
     * @return
     */
    private String getJavaObject(String propName, String className, long pId, JSONObject jsonObject) {
        String typeLabel = pId + className;
        if (typeSet.contains(typeLabel))
            return null;
        typeSet.add(typeLabel);
        Json2JavaVo json2JavaVo = new Json2JavaVo(propName, firstToUpper(className), pId);
        long psId = json2JavaVo.getId();
        Map<String, JavaType> propMap = Maps.newHashMap();
        Iterator<String> key = jsonObject.keys();
        while (key.hasNext()) {
            String name = String.valueOf(key.next());
            Object prop = jsonObject.get(name);
            JavaType javaType = null;
            if (prop instanceof JSONObject) {// 对象
                // 继续迭代
                javaType = new JavaType(needCamel() ? firstUpperCaseCamel(name) : firstToUpper(name), "");
                propMap.put(name, javaType);
                getJavaObject(name, javaType.getType(), psId, (JSONObject) prop);
            } else if (prop instanceof JSONArray) {//数组
                JSONArray array = (JSONArray) prop;
                if (array.length() == 0)
                    javaType = new JavaType("Object", "");
                else {
                    Object object = array.get(0);
                    if (object instanceof JSONObject) {
                        javaType = new JavaType(needCamel() ? firstUpperCaseCamel(name) : firstToUpper(name), "");
                        // 登记一个类型
                        getJavaObject(name, javaType.getType(), psId, (JSONObject) object);
                    } else {
                        javaType = getJavaType(object);
                    }
                }
                importList = true;
                propMap.put(name, new JavaType(javaType.getType(), true, ""));
            } else {
                propMap.put(name, getJavaType(prop));
            }
        }
        json2JavaVo.setPropMap(propMap);
        json2JavaVoList.add(json2JavaVo);
        return json2JavaVo.getClassName();
    }

}
