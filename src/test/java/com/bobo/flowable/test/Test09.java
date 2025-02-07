package com.bobo.flowable.test;

import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.junit.Test;

import java.util.List;

/**
 * 用户和组的维护
 */
public class Test09 {
    @Test
    public void createUser(){
        //维护用户
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过IdentityService完成用户和组的管理
        //这个过程相当于在身份管理应用程序中，通过表单创建用户
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.newUser("田佳");
        user.setFirstName("tian");
        user.setLastName("jia");
        user.setEmail("tianjia@qq.com");
        identityService.saveUser(user);

    }

    @Test
    public void createGroup(){
        //维护用户
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //创建group对象，并指定相关信息
        Group group = identityService.newGroup("group2");
        group.setName("开发部");
        group.setType("2");
        //保存组
        identityService.saveGroup(group);

    }

    /**
     * 将用户分配给对应的组
     */
    @Test
    public void userGroup(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //根据组id找到组对象
        Group group1 = identityService.createGroupQuery().groupId("group1").singleResult();
        List<User> list = identityService.createUserQuery().list();
        for (User user : list) {
            System.out.println("user = " + user.getId());
            //将用户分配给对应的组
            identityService.createMembership(user.getId(),group1.getId());
        }
    }
}
