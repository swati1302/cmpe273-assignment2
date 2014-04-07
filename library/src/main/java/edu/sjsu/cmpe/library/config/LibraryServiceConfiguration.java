package edu.sjsu.cmpe.library.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class LibraryServiceConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String stompQueueName;


	@NotEmpty
    @JsonProperty
    private String apolloPassword;
    
    @NotEmpty
    @JsonProperty
    private String apolloHost;
    
    @JsonProperty
    private int apolloPort;
    
    @NotEmpty
    @JsonProperty
    private String apolloUser;
    
    /**
	 * @return the apolloUser
	 */
	public String getApolloUser() {
		return apolloUser;
	}

	/**
	 * @param apolloUser the apolloUser to set
	 */
	public void setApolloUser(String apolloUser) {
		this.apolloUser = apolloUser;
	}

	/**
	 * @return the apolloPassword
	 */
	public String getApolloPassword() {
		return apolloPassword;
	}

	

	/**
	 * @return the apolloHost
	 */
	public String getApolloHost() {
		return apolloHost;
	}

	/**
	 * @param apolloHost the apolloHost to set
	 */
	public void setApolloHost(String apolloHost) {
		this.apolloHost = apolloHost;
	}

	/**
	 * @return the apolloPort
	 */
	public int getApolloPort() {
		return apolloPort;
	}
	/**
	 * @param apolloPassword the apolloPassword to set
	 */
	public void setApolloPassword(String apolloPassword) {
		this.apolloPassword = apolloPassword;
	}
	
    
    @NotEmpty
    @JsonProperty
    private String stompTopicName;

    /**
     * @return the stompQueueName
     */
    public String getStompQueueName() {
	return stompQueueName;
    }
    /**
     * @param stompTopicName
     *            the stompTopicName to set
     */
    public void setStompTopicName(String stompTopicName) {
	this.stompTopicName = stompTopicName;
    }
    /**
     * @param stompQueueName
     *            the stompQueueName to set
     */
    public void setStompQueueName(String stompQueueName) {
	this.stompQueueName = stompQueueName;
    }
    /**
	 * @param apolloPort the apolloPort to set
	 */
	public void setApolloPort(int apolloPort) {
		this.apolloPort = apolloPort;
	}
    /**
     * @return the stompTopicName
     */
    public String getStompTopicName() {
	return stompTopicName;
    }

    
}
