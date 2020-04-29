package com.delav.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQSender {
	
	private JmsTemplate jmsTemplate;
	  
	public ActiveMQSender(JmsTemplate jmsTmeplate) {
	    this.jmsTemplate = jmsTmeplate; 
	}
	
	public void sendMsg(String destinationName, String messageText) {
	    sendMsg(destinationName, messageText, null);
	}
	  
	public void sendMsg(String destinationName, final String messageText, final String systemId) {
	    this.jmsTemplate.send(destinationName, new MessageCreator() {
	    	public Message createMessage(Session session) throws JMSException {
		        TextMessage message = session.createTextMessage();
		        message.setText(messageText);
		        if ((systemId != null) && (systemId.length() > 0)) {
		        	message.setStringProperty("systemcode", systemId);
		        }
		        return message;
		    }
	    });
	}
	  
	public void sendDelayMsg(String destinationName, final String messageText, final long delayMillis) {
	    this.jmsTemplate.send(destinationName, new MessageCreator() {
	    	public Message createMessage(Session session) throws JMSException {
		        TextMessage message = session.createTextMessage();
		        message.setText(messageText);
		        message.setLongProperty("AMQ_SCHEDULED_DELAY", delayMillis);
		        return message;
	        }
	    });
	}
	  
	public JmsTemplate getJmsTemplate() {
	    return this.jmsTemplate;
	}
}

