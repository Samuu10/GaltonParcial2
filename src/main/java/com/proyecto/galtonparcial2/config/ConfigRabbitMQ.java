package com.proyecto.galtonparcial2.config;

import com.proyecto.galtonparcial2.util.Receiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Clase que representa la configuración de RabbitMQ
@Configuration
public class ConfigRabbitMQ {

    //Variable que representa el nombre de la cola
    static final String queueName = "componentQueue";

    //Metodo que crea la cola
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    //Metodo que crea el contenedor del mensaje
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    //Metodo que crea el adaptador del mensaje
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    //Metodo que crea el template de RabbitMQ
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //Un template de RabbitMQ es un componente que facilita la publicación de mensajes en una cola
        return new RabbitTemplate(connectionFactory);
    }
}