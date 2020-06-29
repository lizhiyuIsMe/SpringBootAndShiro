package com.example.demo.controller.shiro.pub;

import com.example.demo.config.mq.rocketmq.Producer;
import com.example.demo.config.mq.rocketmq.RocketMQConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/pub")
public class TestRocketMQ {

    @Autowired
    private Producer rocketProducer;

    @RequestMapping("/testRocketMQ")
    public Object testRocketMQ(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //创建消息
        Message message=new Message(RocketMQConfig.TOPIC,"taga",("hello rocket send "+text).getBytes());
        //通过product 发送消息
        SendResult send = rocketProducer.getProducer().send(message);
        System.out.println("rocketmq 生产者发送了:"+ send);
        return new HashMap<>();
    }

}
