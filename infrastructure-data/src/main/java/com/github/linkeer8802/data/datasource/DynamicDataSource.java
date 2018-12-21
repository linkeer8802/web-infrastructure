package com.github.linkeer8802.data.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author: weird
 * @date: 2018/12/4
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSource = DataSourceHolder.getDataSource();
        if (dataSource == null) {
            dataSource = DataSourceType.MASTER;
        }

        log.info("Using dataSource " + dataSource);

        return dataSource;
    }



    public static class DataSourceHolder {
        //线程本地环境
        private static final ThreadLocal<DataSourceType> dataSources = new ThreadLocal<DataSourceType>();

        //设置数据源
        public static void setDataSource(DataSourceType customerType) {
            dataSources.set(customerType);
        }

        //获取数据源
        public static DataSourceType getDataSource() {
            return dataSources.get();
        }

        //清除数据源
        public static void clearDataSource() {
            dataSources.remove();
        }
    }
}
