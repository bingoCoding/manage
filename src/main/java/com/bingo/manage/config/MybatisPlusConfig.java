package com.bingo.manage.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.bingo.manage.common.constant.DSEnum;
import com.bingo.manage.config.properties.DruidProperties;
import com.bingo.manage.config.properties.MutiDataSourceProperties;
import com.bingo.manage.core.datascope.DataScopeInterceptor;
import com.bingo.manage.core.mutidatesource.DynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * MybatisPlus配置
 *
 * @author stylefeng
 * @Date 2017/5/20 21:58
 */
@Configuration
@EnableTransactionManagement(order = 2)//由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@MapperScan(basePackages = {"com.bingo.manage.modular.*.dao", "com.bingo.manage.common.persistence.dao"})
public class MybatisPlusConfig {

    @Autowired
    DruidProperties druidProperties;

    @Autowired
    MutiDataSourceProperties mutiDataSourceProperties;


    /**
     * 另一个数据源
     */
    private DruidDataSource bizDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        mutiDataSourceProperties.config(dataSource);
        return dataSource;
    }

    /**
     * manage的数据源
     */
    private DruidDataSource dataSourcemanage(){
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }

    /**
     * 单数据源连接池配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "manage", name = "muti-datasource-open", havingValue = "false")
    public DruidDataSource singleDatasource() {
        return dataSourcemanage();
    }

    /**
     * 多数据源连接池配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "manage", name = "muti-datasource-open", havingValue = "true")
    public DynamicDataSource mutiDataSource() {

        DruidDataSource dataSourcemanage = dataSourcemanage();
        DruidDataSource bizDataSource = bizDataSource();

        try {
            dataSourcemanage.init();
            bizDataSource.init();
        }catch (SQLException sql){
            sql.printStackTrace();
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> hashMap = new HashMap();
        hashMap.put(DSEnum.DATA_SOURCE_manage, dataSourcemanage);
        hashMap.put(DSEnum.DATA_SOURCE_BIZ, bizDataSource);
        dynamicDataSource.setTargetDataSources(hashMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSourcemanage);
        return dynamicDataSource;
    }

    /**
     * 数据范围mybatis插件
     */
    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }
}
