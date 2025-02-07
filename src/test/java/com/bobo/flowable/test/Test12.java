package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 并行网关
 */
public class Test12 {
    @Test
    public void deploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("请假流程-并行网关.bpmn20.xml")
                .name("请假流程-并行网关")
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
        variables.put("num",3);
        runtimeService.startProcessInstanceById("holiday-parallel:1:55004",variables);
    }

    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:55004")
                .taskAssignee("zhangsan")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
        }
    }

    @Test
    public void completeTaskWangWu(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:55004")
                .taskAssignee("wangwu")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
        }
    }

    @Test
    public void completeTaskLisi(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:55004")
                .taskAssignee("lisi")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
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
