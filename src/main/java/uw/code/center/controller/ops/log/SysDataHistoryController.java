package uw.code.center.controller.ops.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.app.common.dto.SysDataHistoryQueryParam;
import uw.app.common.entity.SysDataHistory;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;


/**
 * 数据历史管理。
 */
@RestController
@MscPermDeclare(type = UserType.OPS)
@Tag(name = "数据历史管理", description = "数据历史管理")
@RequestMapping("/ops/log/history")
public class SysDataHistoryController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表数据历史。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    @Operation(summary = "列表数据历史", description = "列表数据历史")
    @GetMapping("/list")
    public DataList<SysDataHistory> list(SysDataHistoryQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.log(SysDataHistory.class, 0, "列表saas系统修改数据历史");
        return dao.list(SysDataHistory.class, queryParam);
    }

    /**
     * 加载数据历史。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    @Operation(summary = "加载数据历史", description = "加载数据历史")
    @GetMapping("/load")
    public SysDataHistory load(@Parameter(description = "主键ID", required = true, example = "1") @RequestParam long id) throws TransactionException {
        AuthServiceHelper.log(SysDataHistory.class, id, "加载saas系统修改数据历史");
        return dao.load(SysDataHistory.class, id);
    }

}