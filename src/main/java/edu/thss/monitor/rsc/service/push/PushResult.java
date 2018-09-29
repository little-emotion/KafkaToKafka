package edu.thss.monitor.rsc.service.push;
import java.io.Serializable;
import java.util.*;

public class PushResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7120037656783111425L;

	String deviceId;
	
	String taskId;
	
	long gatherTime;
	
	long sendTime;
	
	long receiveTime;
	
	long storeTime;
	
	Map<String, Map<Long, String>> pushList;
	
	Map<String, Map<String, String>> configs;
	
	List<String> userList;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(long gatherTime) {
		this.gatherTime = gatherTime;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public long getStoreTime() {
		return storeTime;
	}

	public void setStoreTime(long storeTime) {
		this.storeTime = storeTime;
	}

	public Map<String, Map<Long, String>> getPushList() {
		return pushList;
	}

	public void setPushList(Map<String, Map<Long, String>> pushList) {
		this.pushList = pushList;
	}

	public Map<String, Map<String, String>> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Map<String, String>> configs) {
		this.configs = configs;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
