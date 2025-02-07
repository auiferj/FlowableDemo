package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程变量
 */
public class Test07 {

    @Test
    public void deploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("出差申请单.bpmn20.xml")
                .name("出差申请单")
                .deploy();
        System.out.println("deploy.getId() = " + deployment.getId());
        System.out.println("deploy.getName() = " + deployment.getName());
    }

    @Test
    public void runProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //在启动流程实例时，创建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee0","张三");
        variables.put("assignee1","李四");
        variables.put("assignee2","王五");
        variables.put("assignee3","贾会计");
        runtimeService.startProcessInstanceById("evection:1:62504",variables);
    }

    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("65001")
                .taskAssignee("张三")
                .singleResult();
        //这里可以修改流程实例变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num",2);
        taskService.complete(task.getId(),processVariables);
    }

    /**
     * 通过taskId更新流程变量
     * 在节点被处理之前把变量更新
     */
    @Test
    public void updateVariableLocal(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("65001")
                .taskAssignee("李四")
                .singleResult();
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num",6);
        //设置了本地局部变量
        taskService.setVariablesLocal(task.getId(),processVariables);
    }

    @Test
    public void updateVariable(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("65001")
                .taskAssignee("李四")
                .singleResult();
        //在局部变量和全局变量都有的情况下，取出的是局部变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num",1);
        //这里虽然设置了全局变量，但实际上改的是局部变量
        taskService.setVariables(task.getId(),processVariables);
    }

    @Test
    public void completeTaskLisi(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("65001")
                .taskAssignee("李四")
                .singleResult();
        taskService.complete(task.getId());
    }


}
