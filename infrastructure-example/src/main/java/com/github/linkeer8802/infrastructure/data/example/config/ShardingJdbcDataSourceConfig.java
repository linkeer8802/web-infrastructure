package com.github.linkeer8802.infrastructure.data.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author: weird
 * @date: 2018/12/4
 */
@Configuration
public class ShardingJdbcDataSourceConfig {


//    @Bean("shardingDataSource")
//    @Qualifier("shardingDataSource")
//    public DataSource dataSource() throws SQLException {
//        // 配置真实数据源
//        Map<String, DataSource> dataSourceMap = new HashMap<>();
//
//        // 配置第一个数据源
//        DruidDataSource dataSource1 = new DruidDataSource();
//        dataSource1.setUrl("jdbc:mysql://10.10.1.227:3307/test?useUnicode=true&characterEncoding=UTF-8");
//        dataSource1.setUsername("root");
//        dataSource1.setPassword("123456");
//        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource1.setInitialSize(2);
//        dataSource1.setMaxActive(20);
//        dataSource1.setMinIdle(0);
//        dataSource1.setMaxWait(60000);
//        dataSource1.setValidationQuery("SELECT 1");
//        dataSource1.setTestOnBorrow(false);
//        dataSource1.setTestWhileIdle(true);
//        dataSource1.setPoolPreparedStatements(false);
//        dataSourceMap.put("ds0", dataSource1);
//
//        // 配置第二个数据源
//        DruidDataSource dataSource2 = new DruidDataSource();
//        dataSource2.setUrl("jdbc:mysql://10.10.1.227:3308/test?useUnicode=true&characterEncoding=UTF-8");
//        dataSource2.setUsername("root");
//        dataSource2.setPassword("123456");
//        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource2.setInitialSize(2);
//        dataSource2.setMaxActive(20);
//        dataSource2.setMinIdle(0);
//        dataSource2.setMaxWait(60000);
//        dataSource2.setValidationQuery("SELECT 1");
//        dataSource2.setTestOnBorrow(false);
//        dataSource2.setTestWhileIdle(true);
//        dataSource2.setPoolPreparedStatements(false);
//        dataSourceMap.put("ds1", dataSource2);
//
//        // 配置Order表规则
//        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
//        orderTableRuleConfig.setLogicTable("t_student");
//        orderTableRuleConfig.setActualDataNodes("ds${0..1}.t_student0");
//
//        // 配置分库 + 分表策略
//        orderTableRuleConfig.setDatabaseShardingStrategyConfig(
//                new InlineShardingStrategyConfiguration("id", "ds${id % 2}"));
////        orderTableRuleConfig.setTableShardingStrategyConfig(
////                new InlineShardingStrategyConfiguration("order_id", "t_order${order_id % 2}"));
//
//        // 配置分片规则
//        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
//        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
//
//
//        // 获取数据源对象
//        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());
//        return dataSource;
//    }

//    @Bean("jdbcTemplate")
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    @Bean
//    public PlatformTransactionManager txManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
