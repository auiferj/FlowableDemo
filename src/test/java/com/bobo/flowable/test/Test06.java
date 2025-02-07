package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Test06 {
    @Test
    public void testDeploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("HolidayListener.bpmn20.xml")
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("HolidayListenerKey:1:55004");
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
                .processInstanceId("57501")
                .taskAssignee("小明")
                .singleResult();
        //完成任务
        taskService.complete(task.getId());

    }
}
