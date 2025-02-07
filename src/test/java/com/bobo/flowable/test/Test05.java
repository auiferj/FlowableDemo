package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test05 {

    @Test
    public void testDeploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("请假流程.bpmn20.xml")
                .name("XXX公司请假流程")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());

    }

    /**
     * 启动流程实例
     */
    @Test
    public void testRun(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> variables = new HashMap<>();
        variables.put("assignee0","张三");
        variables.put("assignee1","李四");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("holiday-new:3:47504", variables);
        System.out.println("processInstance = " + processInstance);
    }

    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //找到任务
        Task task = taskService.createTaskQuery()
                //因为，启动流程的时候，运行了多次，所以有多个流程实例，通过processDefinitionKey会找到多个任务
                //所以，使用流程实例id：processInstanceId，可以找到唯一的一个任务
                .processInstanceId("50001")
                .taskAssignee("张三")
                .singleResult();
        //完成任务
        taskService.complete(task.getId());

    }
}


