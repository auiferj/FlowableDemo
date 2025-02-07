package com.bobo.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 候选人组
 */
public class Test10 {
    @Test
    public void deploy(){
        //获取ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("请假流程-候选人组.bpmn20.xml")
                .name("请假流程-候选人组")
                .deploy();
        System.out.println("deploy.getId() = " + deployment.getId());
        System.out.println("deploy.getName() = " + deployment.getName());
    }

    @Test
    public void runProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        IdentityService identityService = processEngine.getIdentityService();
        List<Group> list = identityService.createGroupQuery().list();

        //给流程实例中的UEL表达式赋值
        Map<String, Object> variables = new HashMap<>();
        //在实际应用中，这里是让用户从查出的组（上面list）中选一个组。
        variables.put("g1",list.get(0).getId());
        runtimeService.startProcessInstanceById("hodiday-group:1:17504",variables);
    }

    /**
     * 根据登录的用户查询可拾取的任务
     */
    @Test
    public void queryTaskCandidate_Group(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();
        //找到当前用户所在的组
        Group group = identityService.createGroupQuery().groupMember("邓彪").singleResult();
        //找到当前用户所在组的任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionId("hodiday-group:1:17504")
                .taskCandidateGroup(group.getId())
                .list();
        for (Task task : list) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
        }
    }

    /**
     * 拾取任务
     */
    @Test
    public void claimTaskCandidate(){
        String userId = "邓彪";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();
        //找到当前用户所在的组
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();
        //找到当前用户所在组的任务
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("hodiday-group:1:17504")
                .taskCandidateGroup(group.getId())
                .singleResult();
        if(task != null) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
            //拾取任务
            taskService.claim(task.getId(),userId);
        }
    }

    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("hodiday-group:1:17504")
                .taskAssignee("邓彪")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
        }
    }
}
