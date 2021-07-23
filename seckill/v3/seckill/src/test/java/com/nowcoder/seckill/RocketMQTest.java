package com.nowcoder.seckill;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;
import java.util.List;

public class RocketMQTest {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        testDefaultMQProducer();
        testDefaultMQConsumer();
    }

    public static void testDefaultMQProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("simple_test_producer");
        producer.setNamesrvAddr("139.9.119.64:9876");
        producer.start();

        String topic = "simpleTest";
        String tag = "tag1";
        for (int i = 0; i < 10; i++) {
            String body = "message " + i;
            Message message = new Message(topic, tag, body.getBytes(UTF_8));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("SUCCESS: " + sendResult);
                }
                @Override
                public void onException(Throwable e) {
                    System.out.println("FAILURE: " + e.getMessage());
                }
            });
        }
    }

    public static void testDefaultMQConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("simple_test_consumer");
        consumer.setNamesrvAddr("139.9.119.64:9876");
        consumer.subscribe("simpleTest", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + ": " + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }

}
