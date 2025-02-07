package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test04 {
    @Test
    public void testSuspension_state_(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        //查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("holidayRequest:1:10003")
                //.processDefinitionKey("holidayRequest")
                .singleResult();
        //获取当前流程定义的 状态信息
        boolean suspended = processDefinition.isSuspended();
        if(suspended){
            //当前的流程被挂起了
            //激活流程
            System.out.printf("激活流程", processDefinition.getId() + ":" + processDefinition.getName());
            repositoryService.activateProcessDefinitionById("holidayRequest:1:10003");
        }else{
            //当前流程是激活状态
            //挂起流程
            System.out.printf("挂起流程", processDefinition.getId() + ":" + processDefinition.getName());
            repositoryService.suspendProcessDefinitionById("holidayRequest:1:10003");
        }
    }

    /**
     * 启动一个流程实例
     */
    @Test
    public void testRunProcess(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //通过RuntimeService来启动流程
        RuntimeService runtimeService = engine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee","张三-");
        variables.put("nrOfHolidays",4);
        variables.put("description","工作累了先出去-");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("MyHoliday:1:17504","order100002",variables);
        System.out.println("processInstance.getProcessDefinitionId()=" + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getActivityId()=" + processInstance.getActivityId());
        System.out.println("processInstance.getId()=" + processInstance.getId());
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //找到任务
        Task task = taskService.createTaskQuery()
                //因为，启动流程的时候，运行了多次，所以有多个流程实例，通过processDefinitionKey会找到多个任务
                //所以，使用流程实例id：processInstanceId，可以找到唯一的一个任务
                .processInstanceId("37501")
                .taskAssignee("user1")
                .singleResult();
        //获取当前的流程实例绑定的流程变量
        Map<String, Object> processVariables = task.getProcessVariables();
        Set<String> keys = processVariables.keySet();
        for (String key : keys) {
            System.out.println(key + ":" + processVariables.get(key));
        }
        processVariables.put("approved",true);
        processVariables.put("description","我要出去玩...");
        //完成任务
        taskService.complete(task.getId(),processVariables);

    }
}
