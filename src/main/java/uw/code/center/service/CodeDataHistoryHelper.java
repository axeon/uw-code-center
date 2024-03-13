package uw.code.center.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.auth.service.AuthServiceHelper;
import uw.code.center.dto.CodeDataHistoryQueryParam;
import uw.code.center.entity.CodeDataHistory;
import uw.dao.DaoFactory;
import uw.dao.DataEntity;
import uw.dao.DataList;
import uw.dao.TransactionException;
import uw.httpclient.http.ObjectMapper;
import uw.httpclient.json.JsonInterfaceHelper;
import uw.httpclient.exception.DataMapperException;

import java.util.Date;


/**
 * Saas公用的历史记录存储Helper。
 * 注意：此方法中直接使用dao是为了提升效率。
 */
public class CodeDataHistoryHelper {

    private static final Logger log = LoggerFactory.getLogger( CodeDataHistoryHelper.class );

    private static final DaoFactory dao = DaoFactory.getInstance();

    private static final ObjectMapper mapper = JsonInterfaceHelper.JSON_CONVERTER;

    /**
     * 获得历史记录列表。
     *
     * @return
     */
    public static DataList<CodeDataHistory> getHistoryList(CodeDataHistoryQueryParam queryParam) throws TransactionException {
        return dao.list( CodeDataHistory.class, queryParam );
    }

    /**
     * 保存历史记录。
     *
     * @param entityId
     * @param entityData
     */
    public static void saveHistory(long entityId, Object entityData) {
        CodeDataHistory history = new CodeDataHistory();
        history.setId( dao.getSequenceId( CodeDataHistory.class ) );
        history.setEntityId( entityId );
        history.setEntityClass( entityData.getClass().getSimpleName() );
        if (AuthServiceHelper.getContextToken() != null) {
            history.setSaasId( AuthServiceHelper.getSaasId() );
            history.setMchId( AuthServiceHelper.getMchId() );
            history.setUserId( AuthServiceHelper.getUserId() );
            history.setUserType( AuthServiceHelper.getUserType() );
            history.setGroupId( AuthServiceHelper.getGroupId() );
            history.setUserName( AuthServiceHelper.getUserName() );
            history.setNickName( AuthServiceHelper.getNickName() );
            history.setRealName( AuthServiceHelper.getRealName() );
            history.setUserIp( AuthServiceHelper.getRemoteIp() );
        }
        history.setCreateDate( new Date() );
        try {
            history.setEntityData( mapper.toString( entityData ) );
            if (entityData instanceof DataEntity de) {
                history.setEntityUpdateInfo( de.GET_UPDATED_INFO() );
            }
        } catch (DataMapperException e) {
            log.error( e.getMessage(), e );
        }
        history.setCreateDate( new Date() );
        try {
            dao.save( history );
        } catch (TransactionException e) {
            log.error( e.getMessage(), e );
        }
    }


}
