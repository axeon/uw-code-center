package uw.code.center.service.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * swagger文档解析器。
 */
public class SwaggerParser {


    public static final String PACKAGE_INFO = "$-package-info-$";
    private static final Logger log = LoggerFactory.getLogger(SwaggerParser.class);
    /**
     * api分组列表。
     */
    public Set<ApiGroupInfo> apiGroupInfoList = new LinkedHashSet<>();
    /**
     * api分类信息。
     */
    public Set<ApiCatalogInfo> apiCatalogInfoList = new LinkedHashSet<>();
    /**
     * 项目名，使用-命名规则。
     */
    private String projectName;
    /**
     * API名，使用驼峰命名规则。
     */
    private String apiName;
    /**
     * 错误消息列表。
     */
    private List<String> messageList = new ArrayList<>();
    /**
     * api列表。
     */
    private List<ApiInfo> apiInfoList = new ArrayList<>();
    /**
     * schema列表。
     */
    private List<SchemaInfo> schemaInfoList = new ArrayList<>();
    /**
     * 用于检测排查的schemaNameSet。
     */
    private final Set<String> schemaNameSet = new HashSet<>();

    public static void main(String[] args) {
        SwaggerParser swaggerParser = new SwaggerParser();
        swaggerParser.parse("http://192.168.88.21:20050/v3/api-docs/saas");
        System.out.println(swaggerParser.getApiName());
        for (ApiInfo apiInfo : swaggerParser.getApiInfoList()) {
            System.out.println(apiInfo);
        }
        for (SchemaInfo schemaInfo : swaggerParser.getSchemaInfoList()) {
            System.out.println(schemaInfo);
        }
        for (ApiGroupInfo apiGroupInfo : swaggerParser.getApiGroupInfoList()) {
            System.out.println(apiGroupInfo);
        }
    }

    /**
     * 将appName按照驼峰整形。
     *
     * @param text
     * @return
     */
    private static String clearAppName(String text) {
        StringBuilder sb = new StringBuilder();
        char[] data = text.toCharArray();
        boolean needClear = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != '-') {
                if (needClear) {
                    sb.append(Character.toUpperCase(data[i]));
                    needClear = false;
                } else {
                    sb.append(data[i]);
                }
            } else {
                needClear = true;
            }
        }
        return sb.toString();
    }

    /**
     * 清理属性名中的异常字符。
     *
     * @param text
     * @return
     */
    private static String cleanPropertyName(String text) {
        if (text.charAt(0) == '@') {
            text = text.substring(1);
        }
        return text;
    }

    public void parse(String swaggerUrl) {
        SwaggerParseResult result = new OpenAPIV3Parser().readLocation(swaggerUrl, null, null);
        //设置信息列表
        this.messageList = result.getMessages();
        OpenAPI openAPI = result.getOpenAPI();
        projectName = openAPI.getInfo().getTitle();
        apiName = clearAppName(openAPI.getInfo().getTitle());

        //开始处理数据类型。
        Map<String, Schema> schemaMap = openAPI.getComponents().getSchemas();
        if (schemaMap != null) {
            for (Map.Entry<String, Schema> skv : schemaMap.entrySet()) {
                //对于封装结构体，不再重复输出。
                if (skv.getKey().startsWith("ResponseData") || skv.getKey().startsWith("DataList") || skv.getKey().startsWith("ESDataListScroll") || skv.getKey().startsWith(
                        "ESDataList")) {
                    continue;
                }
                SchemaInfo schemaInfo = new SchemaInfo();
                this.schemaInfoList.add(schemaInfo);
                //获得对象名和注释。
                schemaInfo.setName(skv.getKey());
                schemaNameSet.add(schemaInfo.getName());
                schemaInfo.setTitle(skv.getValue().getTitle());
                //获得属性列表。
                List<SchemaInfo.PropertyInfo> propertyInfoList = new ArrayList<>();
                schemaInfo.setPropertyList(propertyInfoList);
                if (skv.getValue().getProperties() == null) {
                    continue;
                }
                Set<Map.Entry<String, Schema>> pset = skv.getValue().getProperties().entrySet();
                for (Map.Entry<String, Schema> pkv : pset) {
                    SchemaInfo.PropertyInfo propertyInfo = new SchemaInfo.PropertyInfo();
                    propertyInfoList.add(propertyInfo);
                    propertyInfo.setName(cleanPropertyName(pkv.getKey()));
                    propertyInfo.setTitle(pkv.getValue().getTitle());
                    propertyInfo.setType(getFullType(pkv.getValue()));
                }
            }
        }

        //开始处理api。
        Set<Map.Entry<String, PathItem>> pathSet = openAPI.getPaths().entrySet();
        for (Map.Entry<String, PathItem> pathKv : pathSet) {
            Set<Map.Entry<PathItem.HttpMethod, Operation>> opKv = pathKv.getValue().readOperationsMap().entrySet();
            for (Map.Entry<PathItem.HttpMethod, Operation> mkv : opKv) {
                ApiInfo apiInfo = new ApiInfo();
                apiInfo.setPath(pathKv.getKey());
                apiInfo.setFunction(pathToFunctionName(apiInfo.getPath()));
                apiInfo.setMethod(mkv.getKey().name());
                apiInfo.setTitle(mkv.getValue().getSummary());
                ApiInfo.ResponseInfo responseInfo = new ApiInfo.ResponseInfo();
                apiInfo.setResponseInfo(responseInfo);
                List<String> tags = mkv.getValue().getTags();
                if (tags != null && tags.size() > 0) {
                    apiInfo.setGroup(tags.get(0));
                }
                //对于一级菜单，采用特殊的处理办法。
                if (apiInfo.getGroup().equalsIgnoreCase(PACKAGE_INFO)) {
                    ApiCatalogInfo catalogInfo = new ApiCatalogInfo(pathToFunctionName(apiInfo.getPath()), apiInfo.getPath(), apiInfo.getTitle());
                    this.apiCatalogInfoList.add(catalogInfo);
                    continue;
                } else {
                    this.apiInfoList.add(apiInfo);
                }
//                //just for debug
//                if (apiInfo.getPath().equals("/ops/runner/info/list")) {
//                    System.out.println(apiInfo.getPath());
//                }
                //检查是requestBody参数
                RequestBody requestBody = mkv.getValue().getRequestBody();
                if (requestBody != null) {
                    ApiInfo.RequestInfo requestInfo = new ApiInfo.RequestInfo();
                    apiInfo.setRequestInfo(requestInfo);
                    for (Map.Entry<String, MediaType> mtKv : requestBody.getContent().entrySet()) {
                        Schema schema = mtKv.getValue().getSchema();
                        requestInfo.setTitle(schema.getTitle());
                        requestInfo.setType(getFullType(schema));
                    }
                }
                //检查表单入参
                if (mkv.getValue().getParameters() != null) {
                    List<ApiInfo.ParameterInfo> parameterInfoList = new ArrayList<>();
                    apiInfo.setParameterInfoList(parameterInfoList);
                    for (Parameter parameter : mkv.getValue().getParameters()) {
                        ApiInfo.ParameterInfo parameterInfo = new ApiInfo.ParameterInfo();
                        parameterInfoList.add(parameterInfo);
                        parameterInfo.setName(cleanPropertyName(parameter.getName()));
                        parameterInfo.setTitle(parameter.getDescription());
                        parameterInfo.setRequired(parameter.getRequired());
                        parameterInfo.setType(getFullType(parameter.getSchema()));
                    }
                }

                //检查返回值
                for (Map.Entry<String, ApiResponse> resKv : mkv.getValue().getResponses().entrySet()) {
                    if (resKv.getKey().equals("200")) {
                        if (resKv.getValue().getContent() != null) {
                            for (Map.Entry<String, MediaType> stringMediaTypeEntry : resKv.getValue().getContent().entrySet()) {
                                Schema schema = stringMediaTypeEntry.getValue().getSchema();
                                responseInfo.setTitle(schema.getTitle());
                                responseInfo.setType(responseWrapper(getFullType(schema)));
                            }
                        } else {
                            responseInfo.setType("ResponseData<void>");
                            responseInfo.setTitle("默认返回值");
                        }
                    }
                }


            }
        }

        //临时分组列表。
        Map<ApiGroupInfo, List<ApiInfo>> apiGroupInfoMap = new LinkedHashMap<>();
        //重组列表。
        for (ApiInfo apiInfo : getApiInfoList()) {
            String groupPath = getApiParentPath(apiInfo.getPath());
            ApiGroupInfo groupInfo = new ApiGroupInfo(pathToFunctionName(groupPath), groupPath, apiInfo.getGroup());
            List<ApiInfo> list = apiGroupInfoMap.computeIfAbsent(groupInfo, k -> new ArrayList<ApiInfo>());
            list.add(apiInfo);
        }
        //整理结构。
        for (Map.Entry<ApiGroupInfo, List<ApiInfo>> kv : apiGroupInfoMap.entrySet()) {
            kv.getKey().setApiInfoList(kv.getValue());
        }
        this.apiGroupInfoList = apiGroupInfoMap.keySet();
        //开始处理catalog数据。
        for (ApiCatalogInfo apiCatalogInfo : apiCatalogInfoList) {
            for (ApiGroupInfo apiGroupInfo : apiGroupInfoList) {
                if (apiGroupInfo.getPath().startsWith(apiCatalogInfo.getPath())) {
                    apiCatalogInfo.addToApiGroupInfoList(apiGroupInfo);
                }
            }
        }

    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public List<SchemaInfo> getSchemaInfoList() {
        return schemaInfoList;
    }

    public void setSchemaInfoList(List<SchemaInfo> schemaInfoList) {
        this.schemaInfoList = schemaInfoList;
    }

    /**
     * 从$ref中解析出正确的SchemaName
     *
     * @param ref
     * @return
     */
    private String getSchemeName(String ref) {
        if (StringUtils.isBlank(ref)) {
            return null;
        }
        int pos = ref.lastIndexOf('/');
        if (pos < 1) {
            return null;
        }
        return ref.substring(pos + 1);
    }

    /**
     * swagger类型转为ts类型。
     *
     * @param text
     * @return
     */
    private String convertType(String text) {
        if (text.equalsIgnoreCase("int")) {
            return "number";
        } else if (text.equalsIgnoreCase("integer")) {
            return "number";
        } else if (text.equalsIgnoreCase("int64")) {
            return "number";
        } else if (text.equalsIgnoreCase("int32")) {
            return "number";
        } else if (text.equalsIgnoreCase("long")) {
            return "number";
        } else if (text.equalsIgnoreCase("double")) {
            return "number";
        } else if (text.equalsIgnoreCase("number")) {
            return "number";
        } else if (text.equalsIgnoreCase("float")) {
            return "number";
        } else if (text.equalsIgnoreCase("boolean")) {
            return "boolean";
        } else if (text.equalsIgnoreCase("Date")) {
            return "string";
        } else if (text.equalsIgnoreCase("date-time")) {
            return "string";
        } else if (text.equalsIgnoreCase("java.util.Date")) {
            return "string";
        } else if (text.equalsIgnoreCase("String")) {
            return "string";
        } else if (text.equalsIgnoreCase("null")) {
            return "string";
        } else if (text.equalsIgnoreCase("array")) {
            return "Array";
        } else {
            return text;
        }
    }

    /**
     * 从path转化为FunctionName。
     *
     * @param text
     * @return
     */
    private String pathToFunctionName(String text) {
        StringBuilder sb = new StringBuilder();
        // 前缀替换
        char[] data = text.toCharArray();
        boolean needClear = false;
        for (int i = 1; i < data.length; i++) {
            if (data[i] != '/' && data[i] != '_') {
                if (needClear) {
                    sb.append(Character.toUpperCase(data[i]));
                    needClear = false;
                } else {
                    sb.append(data[i]);
                }
            } else {
                needClear = true;
            }
        }
        return sb.toString();
    }

    /**
     * 处理responseData的封装。
     *
     * @return
     */
    private String responseWrapper(String type) {
        if (StringUtils.isBlank(type)) {
            return "ResponseData<void>";
        }
        if (type.startsWith("ResponseData")) {
            type = convertType(type.substring(12));
        }
        if (type.startsWith("DataList")) {
            type = "DataList<" + type.substring(8) + ">";
        } else if (type.startsWith("ESDataListScroll")) {
            type = "ESDataListScroll<" + type.substring(16) + ">";
        } else if (type.startsWith("ESDataList")) {
            type = "ESDataList<" + type.substring(10) + ">";
        } else if (type.startsWith("List")) {
            String subtype = type.substring(4);
            if (schemaNameSet.contains(subtype)) {
                type = "Array<" + subtype + ">";
            }
        } else if (type.startsWith("ArrayList")) {
            String subtype = type.substring(9);
            if (schemaNameSet.contains(subtype)) {
                type = "Array<" + subtype + ">";
            }
        } else if (type.startsWith("LinkedList")) {
            String subtype = type.substring(10);
            if (schemaNameSet.contains(subtype)) {
                type = "Array<" + subtype + ">";
            }
        }

        if (type.length() == 0) {
            return "ResponseData<void>";
        } else {
            return "ResponseData<" + type + ">";
        }
    }

    public List<ApiInfo> getApiInfoList() {
        return apiInfoList;
    }

    public void setApiInfoList(List<ApiInfo> apiInfoList) {
        this.apiInfoList = apiInfoList;
    }

    /**
     * 获得api上级路径。
     *
     * @param path
     * @return
     */
    private String getApiParentPath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        int pos = path.lastIndexOf('/');
        if (pos < 1) {
            return null;
        }
        return path.substring(0, pos);
    }

    /**
     * 递归获取数据类型。
     *
     * @param schema
     * @return
     */
    public String getFullType(Schema schema) {
        String fullType = "any";
        if (schema.getType() == null) {
            fullType = (getSchemeName(schema.get$ref()));
        } else {
            if (schema.getType().equals("array")) {
                fullType = "Array<" + getFullType(schema.getItems()) + ">";
            } else if (schema.getType().equals("object")) {
                //特殊处理Map类型，当前仅支持String类型。
                if (schema instanceof MapSchema) {
                    Schema valueSchema = (Schema) schema.getAdditionalProperties();
                    if (valueSchema != null) {
                        fullType = ("Record<string, " + getFullType(valueSchema) + ">");
                    }
                }
            } else {
                fullType = (convertType(schema.getFormat() == null ? schema.getType() : schema.getFormat()));
            }
        }
        return fullType;
    }

    public Set<ApiGroupInfo> getApiGroupInfoList() {
        return apiGroupInfoList;
    }

    public void setApiGroupInfoList(Set<ApiGroupInfo> apiGroupInfoList) {
        this.apiGroupInfoList = apiGroupInfoList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    public Set<ApiCatalogInfo> getApiCatalogInfoList() {
        return apiCatalogInfoList;
    }

    public void setApiCatalogInfoList(Set<ApiCatalogInfo> apiCatalogInfoList) {
        this.apiCatalogInfoList = apiCatalogInfoList;
    }
}
