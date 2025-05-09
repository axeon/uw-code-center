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
 * 模板Helper类。
 * 使用freemarker作为模板引擎。
 */
public class TemplateHelper {

    private static final Logger log = LoggerFactory.getLogger(TemplateHelper.class);

    private static final DaoManager dao = DaoManager.getInstance();

    /**
     * Configuration对象.
     */
    private static Configuration cfg;

    /**
     * 初始化模板配置.
     *
     * @throws Exception 异常
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
     * 生成代码.
     *
     * @param templateName TemplateName
     * @param map          Map对象
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
