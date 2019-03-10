package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.SmsUtils;

import java.util.Map;

/**
 * rabbitMQ消息队列监听器
 * 消费消息,发送手机短信(验证码)
 */
@Component      //交给springIOC容器管理
@RabbitListener(queues = "sendCode")    //监听sendCode消息队列
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    /**
     * 消费短信
     * @param map
     */
    @RabbitHandler
    public void showMessage(Map<String,String> map){
        System.out.println("监听方法执行了......");
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("短信微服务:"+mobile+" --- "+code);
        try {
            //使用阿里云短信服务发送验证码
            smsUtils.sendSms(mobile,code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
