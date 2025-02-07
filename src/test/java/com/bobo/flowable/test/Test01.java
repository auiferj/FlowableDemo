package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test01 {
    /**
     * 获取流程引擎对象
     */
    @Test
    public void testProcessEngine() {
        /**
         * 1.获取ProcessEngineConfiguratiion对象
         *
         * ProcessEngine是核心对象
         * 类似JDBC中的Connection对象
         * 类似mybatis中SqlSessionFactory对象
         * 通过配置得到ProcessEngine对象
         * ProcessEngineConfiguration有多个实现，这里通过StandaloneProcessEngineConfiguration获取配置对象。
         * standalone：单独的，独立的
         */
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();

        /**
         * 2.配置
         * 数据库的连接信息
         * 这一部分的信息其实可以放到属性文件中。
         */
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable_learn?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC&nullCatalogMeansCurrent=true");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        /**
         * 3.
         * 如果数据库中的表结构不存在就新建
         * 先要在数据库中创建数据库
         */
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        /**
         * 4.通过ProcessEngineConfiuration构建我们需要的ProcessEngine
         */
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("processEngine = " + processEngine);
    }

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
    public void testDeploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                //关联要部署的流程文件
                .addClasspathResource("holiday-request.bpmn20.xml")
                .name("请假流程")
                //执行流程的部署操作
                .deploy();
        System.out.println("deployment.getId()="+deployment.getId());
        System.out.println("deployment.getName()="+deployment.getName());
    }

    /**
     * 查询流程定义的信息
     */
    @Test
    public void testDeployQuery(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                //通过id来查询
                .deploymentId("1")
                //单个结果
                .singleResult();
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getId() = " + processDefinition.getId());
    }

    /**
     * 删除已经部署的流程定义
     */
    @Test
    public void testDeployDelete(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //删除部署的流程，第一个参数是id，如果部署的流程启动了，就不允许删除
        //processEngine.getRepositoryService().deleteDeployment("1");
        //第二个参数是级联删除，如果流程启动了，也可以删除，相关的任务一并会被删除掉
        processEngine.getRepositoryService().deleteDeployment("5001",true);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testRunProcess(){
        ProcessEngine processEngine = configuration.buildProcessEngine();

        //通过RuntimeService启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee","zhangsan");
        variables.put("length",3);
        variables.put("description","休息休息");
        //启动流程实例
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        System.out.println("holidayRequest.getProcessDefinitionId() = " + holidayRequest.getProcessDefinitionId());
        System.out.println("holidayRequest.getDeploymentId() = " + holidayRequest.getDeploymentId());
        System.out.println("holidayRequest.getBusinessKey() = " + holidayRequest.getBusinessKey());
        System.out.println("holidayRequest.getProcessDefinitionKey() = " + holidayRequest.getProcessDefinitionKey());
        System.out.println("holidayRequest.getProcessDefinitionName() = " + holidayRequest.getProcessDefinitionName());
        System.out.println("holidayRequest.getName() = " + holidayRequest.getName());
        System.out.println("holidayRequest.getProcessInstanceId() = " + holidayRequest.getProcessInstanceId());
        System.out.println("holidayRequest.getProcessVariables() = " + holidayRequest.getProcessVariables());
        System.out.println("holidayRequest.getActivityId() = " + holidayRequest.getActivityId());

    }

    /**
     * 测试任务查询
     */
    @Test
    public void testQueryTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                //指定查询的流程，因为有很多流程，有请假的，审批的等，这里指定查询请假相关流程有哪些任务
                .processDefinitionKey("holidayRequest")
                //指定这个任务的处理人
                .taskAssignee("zhangsan")
                .list();
        for (Task task : list) {
            //流程定义id
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            //任务的名称
            System.out.println("task.getName() = " + task.getName());
            //任务的处理人
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getDescription() = " + task.getDescription());
            System.out.println("task.getId() = " + task.getId());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //找到任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("zhangsan")
                .singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved",false);
        //完成任务
        taskService.complete(task.getId(),variables);

    }

    /**
     * 获取流程任务的历史数据
     */
    @Test
    public void testHistory(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        //获取活跃过的流程实例的历史查询对象
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                //这个流程定义id从act_hi_actinst中获取
                .processDefinitionId("holidayRequest:1:10003")
                //状态是完成的
                .finished()
                //根据结束时间排序
                .orderByHistoricActivityInstanceEndTime()
                //升序
                .asc()
                .list();
        for (HistoricActivityInstance history : list) {
            System.out.println("history.getActivityId() = " + history.getActivityId());
            System.out.println("history.getActivityName() = " + history.getActivityName());
            System.out.println("history.getActivityType() = " + history.getActivityType());
            System.out.println("history.getAssignee() = " + history.getAssignee());
            System.out.println("history.getDurationInMillis() = " + history.getDurationInMillis());
            System.out.println("---------------------------------");
        }
    }

}
