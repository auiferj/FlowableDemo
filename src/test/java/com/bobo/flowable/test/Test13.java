package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 包容网关
 */
public class Test13 {
    @Test
    public void deploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("请假流程-包容网关.bpmn20.xml")
                .name("请假流程-包容网关")
                .deploy();
        System.out.println("deploy.getId() = " + deployment.getId());
        System.out.println("deploy.getName() = " + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        //给流程定义中的UEL表达式赋值
        variables.put("num",5);
        runtimeService.startProcessInstanceById("holiday-inclusive:1:67504",variables);
    }

    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-inclusive:1:67504")
                .taskAssignee("zhangsan")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("张三完成任务");
        }
    }

    @Test
    public void completeTaskI1(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-inclusive:1:67504")
                .taskAssignee("i1")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("i1完成任务");
        }
    }

    @Test
    public void completeTaskI2(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-inclusive:1:67504")
                .taskAssignee("i2")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("i1完成任务");
        }
    }

    @Test
    public void setVariables(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("num",4);
        //执行id在act_re_execution中
        runtimeService.setVariables("47503",variables);
    }
}
