package com.bobo.flowable.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class Test02 {
    ProcessEngineConfiguration configuration = null;

    @Before
    public void before(){
        configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable_learn?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC&nullCatalogMeansCurrent=true");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    }

    /**
     * 部署流程
     */
    @Test
    public void testDeploy() {
        //获取ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream inputStream = this.getClass().getResourceAsStream("MyHoliday.bar");
        assert inputStream != null;
        Deployment deployment = repositoryService.createDeployment()
                //关联要部署的流程文件,这里由于eclipse插件问题，无法拿到bar文件，只是记录一下代码
                //ZIP或者Bar文件，
                //.addClasspathResource("MyHoliday.bar")
                .addZipInputStream(new ZipInputStream(inputStream))
                .name("XXX公司请假流程")
                //执行流程的部署操作
                .deploy();
        System.out.println("deployment.getId()="+deployment.getId());
        System.out.println("deployment.getName()="+deployment.getName());
    }
}
