package uw.code.center.controller.ops.codegen;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.annotation.ResponseAdviceIgnore;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.code.center.constant.TemplateInfoType;
import uw.code.center.entity.CodeTemplateGroup;
import uw.code.center.entity.CodeTemplateInfo;
import uw.code.center.service.swagger.ApiGroupInfo;
import uw.code.center.service.swagger.ApiInfo;
import uw.code.center.service.swagger.SchemaInfo;
import uw.code.center.service.swagger.SwaggerParser;
import uw.code.center.template.TemplateHelper;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@Tag(name = "swagger代码生成", description = "swagger代码生成")
@RequestMapping("/ops/codegen/swaggerGenCode")
@MscPermDeclare(user = UserType.OPS)
public class SwaggerGenCodeController {

    private final DaoFactory dao = DaoFactory.getInstance();

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /**
     * 批量下载vue3代码。
     *
     * @param templateGroupId
     */
    @ResponseAdviceIgnore
    @GetMapping("/downloadCodeForVue3")
    @Operation(summary = "批量下载VUE3代码", description = "批量下载Vue3代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public void downloadCodeForVue3(HttpServletResponse response, @Parameter(description = "模板组Id") @RequestParam long templateGroupId, String swaggerUrl) throws IOException, TransactionException {
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, templateGroupId );
        DataList<CodeTemplateInfo> ctList = dao.list( CodeTemplateInfo.class, "select * from code_template_info where group_id=? and state=1", new Object[]{templateGroupId} );

        //设置文件下载格式
        response.setContentType( "application/x-download; charset=utf-8" );
        response.setHeader( "Content-Disposition", "attachment; filename=" + URLEncoder.encode( codeTemplateGroup.getGroupName(), "utf-8" ) + "_" + dateFormat.format( new Date() ) +
                ".zip" );
        OutputStream outputStream = response.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream( outputStream );
        String[] swaggers = swaggerUrl.split( "," );
        for (String swagger : swaggers) {
            SwaggerParser swaggerParser = new SwaggerParser();
            swaggerParser.parse( swagger );
            //拼参数
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "author", "axeon" );
            map.put( "date", new Date() );
            map.put( "projectName", swaggerParser.getProjectName() );
            map.put( "apiName", swaggerParser.getApiName() );
            map.put( "apiInfoList", swaggerParser.getApiInfoList() );
            map.put( "schemaInfoList", swaggerParser.getSchemaInfoList() );
            map.put( "apiGroupInfoList", swaggerParser.getApiGroupInfoList() );
            map.put( "apiCatalogInfoList", swaggerParser.getApiCatalogInfoList() );
            map.put( "messageList", swaggerParser.getMessageList() );
            for (CodeTemplateInfo ct : ctList) {
                //对于页面类型，要特别处理。
                if (ct.getTemplateType() == TemplateInfoType.VUE_PAGE.getValue()) {
                    for (ApiGroupInfo apiGroupInfo : swaggerParser.getApiGroupInfoList()) {
                        map.put( "apiGroupInfo", apiGroupInfo );
                        map.remove( "functionList" );
                        map.remove( "functionSave" );
                        map.remove( "functionUpdate" );
                        map.remove( "functionDelete" );
                        //找出增删改查列相关信息，并赋值。
                        for (ApiInfo apiInfo : apiGroupInfo.getApiInfoList()) {

                            if (apiInfo.getPath().endsWith( "/list" )) {
                                map.put( "functionList", apiInfo.getFunction() );
                                String objType = null;
                                if (apiInfo.getParameterInfoList() != null && apiInfo.getParameterInfoList().size() > 0) {
                                    objType = apiInfo.getParameterInfoList().get( 0 ).getType();
                                }
                                if (apiInfo.getRequestInfo() != null) {
                                    objType = apiInfo.getRequestInfo().getType();
                                }
                                if (objType != null) {
                                    for (SchemaInfo schemaInfo : swaggerParser.getSchemaInfoList()) {
                                        if (schemaInfo.getName().equals( objType )) {
                                            map.put( "queryInfo", schemaInfo );
                                            break;
                                        }
                                    }
                                }
                                ApiInfo.ResponseInfo responseInfo = apiInfo.getResponseInfo();
                                if (responseInfo != null) {
                                    String responseType = responseInfo.getType();
                                    if (responseType.startsWith( "ResponseData<" )) {
                                        responseType = responseType.substring( 13, responseType.length() - 1 );
                                    }
                                    if (responseType.startsWith( "DataList<" )) {
                                        responseType = responseType.substring( 9, responseType.length() - 1 );
                                    }
                                    if (responseType.startsWith( "ESDataListScroll<" )) {
                                        responseType = responseType.substring( 17, responseType.length() - 1 );
                                    }
                                    if (responseType.startsWith( "ESDataList<" )) {
                                        responseType = responseType.substring( 11, responseType.length() - 1 );
                                    }
                                    if (StringUtils.isNotBlank( responseType )) {
                                        for (SchemaInfo schemaInfo : swaggerParser.getSchemaInfoList()) {
                                            if (schemaInfo.getName().equals( responseType )) {
                                                map.put( "schemaInfo", schemaInfo );
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else if (apiInfo.getPath().endsWith( "/save" )) {
                                map.put( "functionSave", apiInfo.getFunction() );
                            } else if (apiInfo.getPath().endsWith( "/update" )) {
                                map.put( "functionUpdate", apiInfo.getFunction() );
                            } else if (apiInfo.getPath().endsWith( "/enable" )) {
                                map.put( "functionEnable", apiInfo.getFunction() );
                            } else if (apiInfo.getPath().endsWith( "/disable" )) {
                                map.put( "functionDisable", apiInfo.getFunction() );
                            } else if (apiInfo.getPath().endsWith( "/delete" )) {
                                map.put( "functionDelete", apiInfo.getFunction() );
                                map.put( "functionDeleteParameterInfoList", apiInfo.getParameterInfoList() );
                            }
                        }
                        //如果连list方法都没有，直接返回，不再生成vue page代码。
                        if (!map.containsKey( "functionList" )) {
                            continue;
                        }
                        String fileName = TemplateHelper.buildTemplate( ct.getId() + "#filename", map );
                        String fileBody = TemplateHelper.buildTemplate( ct.getId() + "#body", map );
                        zipOutputStream.putNextEntry( new ZipEntry( fileName ) );
                        zipOutputStream.write( fileBody.getBytes( StandardCharsets.UTF_8 ) );
                        zipOutputStream.closeEntry();
                    }
                } else {
                    String fileName = TemplateHelper.buildTemplate( ct.getId() + "#filename", map );
                    String fileBody = TemplateHelper.buildTemplate( ct.getId() + "#body", map );
                    zipOutputStream.putNextEntry( new ZipEntry( fileName ) );
                    zipOutputStream.write( fileBody.getBytes( StandardCharsets.UTF_8 ) );
                    zipOutputStream.closeEntry();
                }
            }
        }
        zipOutputStream.flush();
        zipOutputStream.finish();
        zipOutputStream.close();
    }

    /**
     * 批量下载代码。
     *
     * @param templateGroupId
     */
    @ResponseAdviceIgnore
    @GetMapping("/downloadCodeForJmeter")
    @Operation(summary = "批量下载Jmeter代码", description = "批量下载Jmeter代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public void downloadCodeForJmeter(HttpServletResponse response, @Parameter(description = "模板组Id") @RequestParam long templateGroupId, String swaggerUrl) throws IOException, TransactionException {
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, templateGroupId );
        DataList<CodeTemplateInfo> ctList = dao.list( CodeTemplateInfo.class, "select * from code_template_info where group_id=? and state=1", new Object[]{templateGroupId} );
        //设置文件下载格式
        response.setContentType( "application/octet-stream;charset=UTF-8" );
        response.setHeader( "Content-Disposition", "attachment; filename=" + URLEncoder.encode( codeTemplateGroup.getGroupName(), "utf-8" ) + "_" + dateFormat.format( new Date() ) +
                ".zip" );
        response.setHeader( "Access-Control-Expose-Headers", "Content-Disposition" );
        OutputStream outputStream = response.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream( outputStream );

        String[] swaggers = swaggerUrl.split( "," );
        for (String swagger : swaggers) {
            SwaggerParser swaggerParser = new SwaggerParser();
            swaggerParser.parse( swagger );
            //拼参数
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "author", "axeon" );
            map.put( "date", new Date() );
            map.put( "projectName", swaggerParser.getProjectName() );
            map.put( "apiName", swaggerParser.getApiName() );
            map.put( "apiInfoList", swaggerParser.getApiInfoList() );
            map.put( "schemaInfoList", swaggerParser.getSchemaInfoList() );
            map.put( "apiGroupInfoList", swaggerParser.getApiGroupInfoList() );
            map.put( "apiCatalogInfoList", swaggerParser.getApiCatalogInfoList() );
            map.put( "messageList", swaggerParser.getMessageList() );
            for (CodeTemplateInfo ct : ctList) {
                String fileName = TemplateHelper.buildTemplate( ct.getId() + "#filename", map );
                String fileBody = TemplateHelper.buildTemplate( ct.getId() + "#body", map );
                zipOutputStream.putNextEntry( new ZipEntry( fileName ) );
                zipOutputStream.write( fileBody.getBytes( StandardCharsets.UTF_8 ) );
                zipOutputStream.closeEntry();
            }
        }
        zipOutputStream.flush();
        zipOutputStream.finish();
        zipOutputStream.close();
    }

}
