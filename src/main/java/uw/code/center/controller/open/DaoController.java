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
import uw.common.dto.ResponseData;
import uw.dao.DaoManager;
import uw.dao.DataList;

/**
 * 测试接口
 */
@RestController
@RequestMapping("/open/dao")
@Tag(name = "测试接口")
@Profile({"test", "dev"})
public class DaoController {

    DaoManager dao = DaoManager.getInstance();

    @GetMapping("/list")
    public ResponseData<DataList<CodeTemplateGroup>> list() {
        return dao.list(CodeTemplateGroup.class, new CodeTemplateGroupQueryParam());
    }

    @GetMapping("/load")
    public ResponseData<CodeTemplateInfo> load(@RequestParam long id) {
        return dao.load(CodeTemplateInfo.class, id);
    }

}
