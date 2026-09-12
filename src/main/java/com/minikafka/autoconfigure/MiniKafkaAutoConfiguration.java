package com.minikafka.autoconfigure;

import com.minikafka.annotation.MiniKafkaListenerAnnotationBeanPostProcessor;
import com.minikafka.core.MiniKafkaBroker;
import com.minikafka.core.MiniKafkaTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MiniKafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MiniKafkaBroker miniKafkaBroker() {
        return new MiniKafkaBroker();
    }

    @Bean
    @ConditionalOnMissingBean
    public MiniKafkaTemplate miniKafkaTemplate(MiniKafkaBroker miniKafkaBroker) {
        return new MiniKafkaTemplate(miniKafkaBroker);
    }

    @Bean
    public MiniKafkaListenerAnnotationBeanPostProcessor miniKafkaListenerAnnotationBeanPostProcessor(MiniKafkaBroker miniKafkaBroker) {
        return new MiniKafkaListenerAnnotationBeanPostProcessor(miniKafkaBroker);
    }
}
