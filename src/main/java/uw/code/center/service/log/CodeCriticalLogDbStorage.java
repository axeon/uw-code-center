package uw.code.center.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import uw.auth.service.log.AuthCriticalLogStorage;
import uw.auth.service.vo.MscActionLog;
import uw.code.center.entity.CodeCritLog;
import uw.dao.DaoFactory;

import java.util.Date;

/**
 * 描述: 日志写数据库实现
 */
@Service
@Primary
public class CodeCriticalLogDbStorage implements AuthCriticalLogStorage {

    private static final Logger log = LoggerFactory.getLogger( CodeCriticalLogDbStorage.class );

    private final static DaoFactory dao = DaoFactory.getInstance();


    @Override
    public void save(MscActionLog mscActionLog) {

        CodeCritLog critLog = new CodeCritLog();
        critLog.setId( dao.getSequenceId( CodeCritLog.class ) );
        critLog.setAppInfo( mscActionLog.getAppInfo() );
        critLog.setAppHost( mscActionLog.getAppHost() );
        critLog.setSaasId( mscActionLog.getSaasId() );
        critLog.setMchId( mscActionLog.getMchId() );
        critLog.setUserId( mscActionLog.getUserId() );
        critLog.setGroupId( mscActionLog.getGroupId() );
        critLog.setUserName( mscActionLog.getUserName() );
        critLog.setUserType( mscActionLog.getUserType() );
        critLog.setObjectId( String.valueOf( mscActionLog.getObjectId() ) );
        critLog.setObjectType( mscActionLog.getObjectType() );
        critLog.setRequestBody( mscActionLog.getRequestBody() );
        critLog.setResponseBody( mscActionLog.getResponseBody() );
        critLog.setResponseMillis( mscActionLog.getResponseMillis() );
        critLog.setUri( mscActionLog.getUri() );
        critLog.setInfo( mscActionLog.getInfo() );
        critLog.setLog( mscActionLog.getLog() );
        critLog.setException( mscActionLog.getException() );
        critLog.setStatusCode( mscActionLog.getStatusCode() );
        critLog.setUserIp( mscActionLog.getUserIp() );
        critLog.setAppHost( mscActionLog.getAppHost() );
        critLog.setAppInfo( mscActionLog.getAppInfo() );
        critLog.setRequestDate( new Date() );
        try {
            dao.save( critLog );
        } catch (Exception e) {
            log.error( "关键日志保存数据库失败:" + e.getMessage(), e );
        }

    }
}
