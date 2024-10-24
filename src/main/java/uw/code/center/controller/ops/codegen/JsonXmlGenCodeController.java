package uw.code.center.controller.ops.codegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.annotation.ResponseAdviceIgnore;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.code.center.dto.JsonXmlRequestParam;
import uw.code.center.service.jsonxml.AnnotationStyle;
import uw.code.center.service.jsonxml.GenerationConfig;
import uw.code.center.service.jsonxml.GenerationType;
import uw.code.center.service.jsonxml.VoCodeGenTools;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 描述:
 *
 */
@RestController
@Tag(name = "json和xml代码生成", description = "json和xml代码生成")
@RequestMapping("/ops/codegen/jsonxmlGenCode")
@MscPermDeclare(user = UserType.OPS)
public class JsonXmlGenCodeController {

    private static final Logger log = LoggerFactory.getLogger( JsonXmlGenCodeController.class );

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");

    private final ObjectMapper objectMapper;

    public JsonXmlGenCodeController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 生成代码
     *
     * @param requestParam
     * @return
     * @throws Exception
     */
    @PostMapping("/genCode")
    @Operation(summary = "生成单个VO代码的文本", description = "生成单个VO代码的文本")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public String genCode(@Parameter(description = "生成单个VO代码的请求体") @RequestBody JsonXmlRequestParam requestParam) throws Exception {

        // 校验传入的是 json 或者 xml
        if (StringUtils.isBlank( getTextType( requestParam.getText() ) )) {
            return "传入的文本不是正确的json或者xml格式!";
        }

        // 生成VO所需要的配置
        GenerationConfig generationConfig = new GenerationConfig();

        // 内容
        generationConfig.setGenerationText( requestParam.getText() );

        if ("json".equals( requestParam.getTextType() )) {
            generationConfig.setGenerationType( GenerationType.JSON );
        } else if ("xml".equals( requestParam.getTextType() )) {
            generationConfig.setGenerationType( GenerationType.XML );
        } else {
            return "传入的文本类型不正确!";
        }
        // 生成的VO里面属性的注解风格
        generationConfig.setAnnotationStyle( AnnotationStyle.JACKSON2 );
        generationConfig.setAuthor( "axeon" );
        generationConfig.setGenerateJavaDoc( true );
        generationConfig.setTakeSwagger( requestParam.isTakeSwagger() );

        // 生成的VO代码
        StringBuilder voText = VoCodeGenTools.generator( generationConfig );
        return voText.toString();
    }

    /**
     * 上传zip 批量生成VO 再打包下载代码。
     *
     * @param response
     * @param file
     * @throws IOException
     */
    @ResponseAdviceIgnore
    @PostMapping("/downloadCode")
    @Operation(summary = "批量下载java的VO代码", description = "根据上传的文件批量生成后,打zip包下载java的VO代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public void downloadCode(HttpServletResponse response, @Parameter(name = "file", description = "json或者xml的文件的zip压缩包") @RequestParam("file") MultipartFile file) throws IOException {

        // 上传的文件是json 或者 xml 的文件的zip压缩包
        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream( new BufferedInputStream( file.getInputStream() ), Charset.forName( "GBK" ) );
        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze;
        Map<String, byte[]> fileMap = new HashMap<>();
        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (!ze.isDirectory() && (ze.toString().endsWith( "json" ) || ze.toString().endsWith( "xml" ))) {
                //读取
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipInputStream.read( buffer )) > -1) {
                    outputStream.write( buffer, 0, len );
                }
                outputStream.flush();
                // 读取到的单个文件流
                InputStream stream = new ByteArrayInputStream( outputStream.toByteArray() );

                // 文件名称
                // 去掉压缩包的前缀名字
                String fullName = ze.getName().split( "/" )[1];
                // 去掉后缀 得到最终的文件名字
                String name = fullName.contains( "." ) ? fullName.split( "\\." )[0] : fullName;

                GenerationConfig generationConfig = new GenerationConfig();
                generationConfig.setGenerateJavaDoc( true );
                generationConfig.setTakeSwagger( true );
                generationConfig.setObjectName( name );
                // 生成的VO里面属性的注解风格
                generationConfig.setAnnotationStyle( AnnotationStyle.JACKSON2 );
                //
                if (ze.toString().endsWith( "json" )) {
                    String text = objectMapper.writeValueAsString( objectMapper.readTree( stream ) );
                    generationConfig.setGenerationText( text );
                    generationConfig.setGenerationType( GenerationType.JSON );
                } else if (ze.toString().endsWith( "xml" )) {
//                    String text = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream).toString();
                    String text = streamToString( stream );
                    generationConfig.setGenerationText( text );
                    generationConfig.setGenerationType( GenerationType.XML );
                }

                StringBuilder generator = VoCodeGenTools.generator( generationConfig );
                String fileName = name + ".java";
                fileMap.put( fileName, generator.toString().getBytes( StandardCharsets.UTF_8 ) );
            }
        }
        //一定记得关闭流
        zipInputStream.closeEntry();

        // 完成VO生成 后面就是生成.java文件 打zip包下载
        if (fileMap.size() > 0) {
            //设置文件下载格式
            response.setContentType( "application/x-download; charset=utf-8" );
            response.setHeader( "Content-Disposition", "attachment; filename=" + URLEncoder.encode( "自动生成VO代码", "utf-8" ) + "_" + dateFormat.format( new Date() ) + ".zip" );
            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream( outputStream );

            for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
                zipOutputStream.putNextEntry( new ZipEntry( entry.getKey() ) );
                zipOutputStream.write( entry.getValue() );
                zipOutputStream.closeEntry();
            }

            zipOutputStream.flush();
            zipOutputStream.finish();
            zipOutputStream.close();
            outputStream.close();
        }
    }

    /**
     * 校验传入的文本是否是json或者xml格式
     *
     * @param text
     * @return
     */
    public String getTextType(String text) {
        try {
            objectMapper.readTree( text );
            return "JSON";
        } catch (IOException ignored) {
        }
        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new InputSource( new StringReader( text ) ) );
            return "XML";
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 将输入流转换为字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String streamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read( buffer )) != -1) {
            result.write( buffer, 0, length );
        }
        return result.toString( StandardCharsets.UTF_8 );
    }

}
