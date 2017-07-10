package com.blinkfox.adept.test.datasource;

import com.blinkfox.adept.datasource.DataSourceConfig;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * BoneCP 的数据源配置类.
 * @author blinkfox on 2017-07-10.
 */
public class BonecpDataSourceConfig extends DataSourceConfig<BoneCPDataSource> {
    
    /**
     * 关闭BoneCP数据源，目的是关闭应用程序时也能确保释放掉连接池所占用的内存.
     */
    @Override
    public void close() {
        if (super.dataSource != null) {
            super.dataSource.close();
            super.dataSource = null;
        }
    }

}