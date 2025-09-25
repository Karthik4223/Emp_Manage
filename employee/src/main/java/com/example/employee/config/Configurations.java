package com.example.employee.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class Configurations {
	
    @Value("${spring.data.solr.host}")
    private String SOLR_URL;

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	
	@Bean
	public SolrClient solrClient() {
		return new HttpSolrClient.Builder(SOLR_URL).build();
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return activeMQConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setPubSubDomain(false);
		return jmsTemplate;
	}
	
	@Bean
	public SingleConnectionFactory singleConnectionFactory(ConnectionFactory connectionFactory) {
	    SingleConnectionFactory scf = new SingleConnectionFactory();
	    scf.setTargetConnectionFactory(connectionFactory);
	    return scf;
	}

	
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory);
	    factory.setPubSubDomain(false); 
	    factory.setSessionTransacted(true);
	    
	    if (connectionFactory instanceof ActiveMQConnectionFactory) {
	        ActiveMQConnectionFactory amqFactory = (ActiveMQConnectionFactory) connectionFactory;
	        RedeliveryPolicy policy = new RedeliveryPolicy();
	        policy.setMaximumRedeliveries(3);
	        policy.setInitialRedeliveryDelay(2000);
	        policy.setBackOffMultiplier(2);
	        policy.setUseExponentialBackOff(true);
	        amqFactory.setRedeliveryPolicy(policy);
	    }
	    
	    return factory;
	}
	
	@Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); 
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); 
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }
	
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory,ObjectMapper redisObjectMapper){
		RedisTemplate<String,Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
	    StringRedisSerializer stringSerializer = new StringRedisSerializer();

		GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        
		template.setKeySerializer(stringSerializer);

	    template.setHashKeySerializer(stringSerializer);     
	    template.setHashValueSerializer(jacksonSerializer);  

	    template.setValueSerializer(jacksonSerializer);      

		template.afterPropertiesSet();
		
		return template;
	}
	
}
