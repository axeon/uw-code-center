package uw.code.center.controller.open;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.code.center.dto.CodeTemplateGroupQueryParam;
import uw.code.center.entity.CodeTemplateGroup;
import uw.code.center.entity.CodeTemplateInfo;
import uw.common.response.ResponseData;
import uw.dao.DaoManager;
import uw.common.data.PageList;

/**
 * 测试接口
 */
@RestController
@RequestMapping("/open/dao")
@Tag(name = "测试接口")
@Profile({"debug","dev"})
public class DaoController {

    DaoManager dao = DaoManager.getInstance();

    /**
     * 测试查询模板分组列表。
     *
     * @return 模板分组分页结果
     */
    @GetMapping("/list")
    public ResponseData<PageList<CodeTemplateGroup>> list() {
        return dao.list(CodeTemplateGroup.class, new CodeTemplateGroupQueryParam());
    }

    /**
     * 测试按 ID 加载模板内容。
     *
     * @param id 主键 ID
     * @return 模板内容实体
     */
    @GetMapping("/load")
    public ResponseData<CodeTemplateInfo> load(@RequestParam long id) {
        return dao.load(CodeTemplateInfo.class, id);
    }

}
