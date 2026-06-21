package uw.code.center.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.code.center.entity.CodeTemplateInfo;
import uw.dao.DaoManager;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * FreeMarker 模板引擎封装。
 * <p>
 * 使用 {@link StringTemplateLoader} 将数据库中所有启用（{@code state=1}）的模板加载到内存，每条
 * {@link CodeTemplateInfo} 注册两个模板 key：{@code {id}#filename}（输出文件名模板）与 {@code {id}#body}（代码体模板）。
 * 启动时由 {@code CodeCenterAutoConfiguration} 触发 {@link #init()} 加载；模板更新接口成功后也会调用 {@link #init()} 重建缓存，实现热更新。
 * </p>
 */
public class TemplateHelper {

    private static final Logger log = LoggerFactory.getLogger(TemplateHelper.class);

    private static final DaoManager dao = DaoManager.getInstance();

    /**
     * FreeMarker 配置对象，持有所有模板的 StringTemplateLoader。
     */
    private static Configuration cfg;

    /**
     * 初始化（重建）模板缓存：新建 Configuration 与 StringTemplateLoader，并异步加载所有启用模板。
     * <p>
     * 注意：模板加载在 {@code onSuccess} 异步回调中完成，方法返回时缓存未必已就绪；
     * 此为既定设计，配合更新接口触发热更新使用。
     * </p>
     */
    public static void init() {
        // 初始化FreeMarker配置
        // 创建一个Configuration实例
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // 设置FreeMarker的模版文件位置
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(stringTemplateLoader);
        //加载模板。
        dao.list(CodeTemplateInfo.class, "select * from code_template_info where state=1").onSuccess(list -> {
            for (CodeTemplateInfo CodeTemplateInfo : list) {
                stringTemplateLoader.putTemplate(CodeTemplateInfo.getId() + "#filename", CodeTemplateInfo.getTemplateFilename());
                stringTemplateLoader.putTemplate(CodeTemplateInfo.getId() + "#body", CodeTemplateInfo.getTemplateBody());
            }
        });
    }

    /**
     * 按模板名渲染代码。
     * <p>
     * 渲染异常时仅记录日志并返回已渲染的部分内容（可能为空串）；调用方需对空结果自行判空跳过。
     * </p>
     *
     * @param templateName 模板名（如 {@code {id}#filename} 或 {@code {id}#body}）
     * @param map          模板上下文变量
     * @return 渲染结果字符串；模板不存在或渲染异常时可能返回空串
     * @throws IOException 取模板时发生的 IO 异常
     */
    @SuppressWarnings("rawtypes")
    public static String buildTemplate(String templateName, Map map) throws IOException {
        StringWriter sw = new StringWriter();
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(map, sw);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sw.toString();
    }


}
