package com.nowcoder.seckill;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

//@SpringBootApplication
public class RocketMQTestInSpring implements CommandLineRunner {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RocketMQTestInSpring.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        testProduce();
        testProduceInTransaction();
    }

    private void testProduce() throws Exception {
        for (int i = 0; i < 10; i++) {
            String destination = "springTest:tag" + (i % 2);
            Message message = MessageBuilder.withPayload("message " + i).build();
            rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("SUCCESS: " + sendResult);
                }
                @Override
                public void onException(Throwable e) {
                    System.out.println("FAILURE: " + e.getMessage());
                }
            }, 3000);
        }
    }

    // debug: DefaultRocketMQListenerContainer.handleMessage()
    @Service
    @RocketMQMessageListener(topic = "springTest",
            consumerGroup = "spring_test_consumer_0", selectorExpression = "tag0")
    private class SpringConsumer0 implements RocketMQListener<String> {

        @Override
        public void onMessage(String message) {
            System.out.println("SpringConsumer0: " + message);
        }
    }

    @Service
    @RocketMQMessageListener(topic = "springTest",
            consumerGroup = "spring_test_consumer_1", selectorExpression = "tag1")
    private class SpringConsumer1 implements RocketMQListener<String> {

        @Override
        public void onMessage(String message) {
            System.out.println("SpringConsumer1: " + message);
        }
    }

    @Service
    @RocketMQMessageListener(topic = "springTest",
            consumerGroup = "spring_test_consumer_x", selectorExpression = "*")
    private class SpringConsumerX implements RocketMQListener<String> {

        @Override
        public void onMessage(String message) {
            System.out.println("SpringConsumerX: " + message);
        }
    }

    private void testProduceInTransaction() throws Exception {
        String destination = "springTest:tagT";
        for (int i = 0; i < 10; i++) {
            Message message = MessageBuilder.withPayload("message " + i).build();
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, null);
            System.out.println(sendResult);
        }
    }

    // LocalTransactionListenerImpl
    @RocketMQTransactionListener
    private class TransactionListenerImpl implements RocketMQLocalTransactionListener {

        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("executeLocalTransaction: " + msg + ", " + arg);
//            return RocketMQLocalTransactionState.COMMIT;
//            return RocketMQLocalTransactionState.ROLLBACK;
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            // 30s
            System.out.println("checkLocalTransaction: " + msg);
            return RocketMQLocalTransactionState.COMMIT;
        }
    }

}
