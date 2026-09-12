package com.minikafka.annotation;

import com.minikafka.core.MiniKafkaBroker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class MiniKafkaListenerAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final MiniKafkaBroker broker;

    public MiniKafkaListenerAnnotationBeanPostProcessor(MiniKafkaBroker broker) {
        this.broker = broker;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            MiniKafkaListener listenerAnnotation = method.getAnnotation(MiniKafkaListener.class);
            if (listenerAnnotation != null) {
                String topic = listenerAnnotation.topic();
                broker.subscribe(topic, message -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, bean, message);
                });
            }
        }
        return bean;
    }
}
