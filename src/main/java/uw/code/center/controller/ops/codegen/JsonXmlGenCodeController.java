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
import org.springframework.web.util.UriUtils;
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
import uw.code.center.util.ZipUtils;
import uw.common.util.SystemClock;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * JSON/XML 驱动的 Java VO 生成接口。
 * <p>
 * 接受原始 JSON 或 XML 文本（XML 校验已禁用 DOCTYPE/外部实体，防 XXE），由 {@link VoCodeGenTools} 生成 VO 类。
 * 支持单文件文本生成与批量 ZIP 上传→ZIP 下载两种模式。
 * </p>
 */
@RestController
@Tag(name = "json和xml代码生成", description = "json和xml代码生成")
@RequestMapping("/ops/codegen/jsonxml")
@MscPermDeclare(user = UserType.OPS)
public class JsonXmlGenCodeController {

    private static final Logger log = LoggerFactory.getLogger(JsonXmlGenCodeController.class);

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");

    private final ObjectMapper objectMapper;

    public JsonXmlGenCodeController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 生成单个 VO 代码。
     * <p>
     * 自动探测 {@link JsonXmlRequestParam#getText()} 的文本类型（JSON 或 XML），文本非法时返回提示文案。
     * 注意：入参中的 {@code textType} 字段当前未使用，类型由服务端探测得出。
     * </p>
     *
     * @param requestParam 生成请求（含文本、注解开关等）
     * @return 生成的 Java 源码字符串；文本非 JSON/XML 时返回错误提示
     * @throws Exception XML 解析等过程中的异常
     */
    @PostMapping("/genCode")
    @Operation(summary = "生成单个VO代码的文本", description = "生成单个VO代码的文本")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public String genCode(@Parameter(description = "生成单个VO代码的请求体") @RequestBody JsonXmlRequestParam requestParam) throws Exception {

        // 使用getTextType()的返回值确定实际文本类型，避免大小写不一致
        String actualType = getTextType(requestParam.getText());
        if (StringUtils.isBlank(actualType)) {
            return "传入的文本不是正确的json或者xml格式!";
        }

        // 生成VO所需要的配置
        GenerationConfig generationConfig = new GenerationConfig();

        // 内容
        generationConfig.setGenerationText(requestParam.getText());

        if ("JSON".equals(actualType)) {
            generationConfig.setGenerationType(GenerationType.JSON);
        } else {
            generationConfig.setGenerationType(GenerationType.XML);
        }
        // 生成的VO里面属性的注解风格
        generationConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
        generationConfig.setAuthor("axeon");
        generationConfig.setGenerateJavaDoc(true);
        generationConfig.setTakeSwagger(requestParam.isTakeSwagger());

        // 生成的VO代码
        StringBuilder voText = VoCodeGenTools.generator(generationConfig);
        return voText.toString();
    }

    /**
     * 上传 ZIP 批量生成 VO 并打包下载。
     * <p>
     * 读取 ZIP 内所有 {@code .json}/{@code .xml} 文件（UTF-8），逐个生成 Java VO，再打包为 ZIP 下载。
     * entry 名称经 {@link ZipUtils#safeEntryName(String)} 校验。
     * </p>
     *
     * @param response HTTP 响应，用于写入 ZIP 流
     * @param file     上传的 ZIP 文件
     * @throws IOException 读取/写入失败
     */
    @ResponseAdviceIgnore
    @PostMapping("/downloadCode")
    @Operation(summary = "批量下载java的VO代码", description = "根据上传的文件批量生成后,打zip包下载java的VO代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public void downloadCode(HttpServletResponse response, @Parameter(description = "json或者xml的文件的zip压缩包") @RequestParam MultipartFile file) throws IOException {

        // 上传的文件是json 或者 xml 的文件的zip压缩包
        // 统一使用 UTF-8 字符集解析压缩包内的文件名，避免 Windows 默认 GBK 导致读取异常
        Map<String, byte[]> fileMap = new HashMap<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(file.getInputStream()), StandardCharsets.UTF_8)) {
            //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
            ZipEntry ze;
            //循环遍历
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }
                // entry 名称做 Zip Slip 防护，非法/路径穿越一律跳过
                String entryName = ZipUtils.safeEntryName(ze.getName());
                if (entryName == null || (!entryName.endsWith("json") && !entryName.endsWith("xml"))) {
                    continue;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                //读取
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipInputStream.read(buffer)) > -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                // 读取到的单个文件流
                InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());

                // 文件名称：取路径最后一段作为基础名，避免目录前缀干扰，也避免数组越界
                String fullName = entryName;
                int slashIdx = fullName.lastIndexOf('/');
                if (slashIdx >= 0) {
                    fullName = fullName.substring(slashIdx + 1);
                }
                // 去掉后缀 得到最终的文件名字
                String name = fullName.contains(".") ? fullName.split("\\.")[0] : fullName;

                GenerationConfig generationConfig = new GenerationConfig();
                generationConfig.setGenerateJavaDoc(true);
                generationConfig.setTakeSwagger(true);
                generationConfig.setObjectName(name);
                // 生成的VO里面属性的注解风格
                generationConfig.setAnnotationStyle(AnnotationStyle.JACKSON2);
                //
                if (entryName.endsWith("json")) {
                    String text = objectMapper.writeValueAsString(objectMapper.readTree(stream));
                    generationConfig.setGenerationText(text);
                    generationConfig.setGenerationType(GenerationType.JSON);
                } else {
//                    String text = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream).toString();
                    String text = streamToString(stream);
                    generationConfig.setGenerationText(text);
                    generationConfig.setGenerationType(GenerationType.XML);
                }

                StringBuilder generator = VoCodeGenTools.generator(generationConfig);
                String fileName = name + ".java";
                fileMap.put(fileName, generator.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        // 完成VO生成 后面就是生成.java文件 打zip包下载
        if (fileMap.size() > 0) {
            //设置文件下载格式
            response.setContentType("application/x-download; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + UriUtils.encode("自动生成VO代码", StandardCharsets.UTF_8) + "_" + dateFormat.format(SystemClock.nowDate()) + ".zip");
            try (OutputStream outputStream = response.getOutputStream(); ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
                for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
                    ZipEntry zipEntry = ZipUtils.safeEntry(entry.getKey());
                    if (zipEntry == null) {
                        continue;
                    }
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(entry.getValue());
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.flush();
                zipOutputStream.finish();
            }
        }
    }

    /**
     * 校验传入的文本是否为 JSON 或 XML 格式。
     * <p>
     * XML 解析已禁用 DOCTYPE 与外部实体，防御 XXE。
     * </p>
     *
     * @param text 待校验文本
     * @return 合法时返回 {@code "JSON"} 或 {@code "XML"}，否则返回 null
     */
    public String getTextType(String text) {
        try {
            objectMapper.readTree(text);
            return "JSON";
        } catch (IOException ignored) {
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.newDocumentBuilder().parse(new InputSource(new StringReader(text)));
            return "XML";
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 将输入流转换为 UTF-8 字符串。
     *
     * @param inputStream 输入流
     * @return 字符串内容
     * @throws IOException 读取失败
     */
    public String streamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

}
