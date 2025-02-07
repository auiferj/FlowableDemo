package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate{
    /**
     * 这是flowable中的触发器
     * 在bpmn20.xml中定义之后，如果触发，就会执行
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        //触发执行的逻辑
        System.out.println("不好意思，你的请假申请被拒绝了，请安心工作。");
    }
}
