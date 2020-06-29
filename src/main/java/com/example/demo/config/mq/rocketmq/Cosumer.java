package com.example.demo.config.mq.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

//消费者
@Component
public class Cosumer {
    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public  Cosumer() throws MQClientException {
        //设置消费者组
        consumer = new DefaultMQPushConsumer(consumerGroup);
        //设置server地址
        consumer.setNamesrvAddr(RocketMQConfig.NAME_SERVER);
        //设置消费策略
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //订阅哪个主题
        consumer.subscribe(RocketMQConfig.TOPIC, "*");

        consumer.registerMessageListener( new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    Message msg = msgs.get(0);
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

                    String topic = msg.getTopic();
                    String body = new String(msg.getBody(), "utf-8");
                    String tags = msg.getTags();
                    String keys = msg.getKeys();
                    System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
                    //返回此标志代表接收成功,之后会删除此条信息
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    //代表这条信息没有被消费
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        //开启消费者
        consumer.start();
        System.out.println("consumer start ...");
    }

}
