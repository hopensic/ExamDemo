package com.migu.schedule.info;

import java.util.ArrayList;
import java.util.List;

public class TaskContainer {
  private int nodeId;
  private Integer totalConsumption = 0;
  private int taskNum = 0;
  private Integer taskNOSum = 0;

  private List<Task> taskList = new ArrayList<Task>();

  public TaskContainer(int nodeId) {
    this.nodeId = nodeId;
  }

  public int getNodeId() {
    return nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getTotalConsumption() {
    return totalConsumption;
  }

  public void setTotalConsumption(Integer totalConsumption) {
    this.totalConsumption = totalConsumption;
  }

  public int getTaskNum() {
    return taskNum;
  }

  public void setTaskNum(int taskNum) {
    this.taskNum = taskNum;
  }

  public void addToContainer(Task t) {
    taskList.add(t);
    taskNum++;
    taskNOSum += t.getTaskId();
    totalConsumption += t.getConsumption();
  }

  public Integer getTaskNOSum() {
    return taskNOSum;
  }

  public void setTaskNOSum(Integer taskNOSum) {
    this.taskNOSum = taskNOSum;
  }

  public List<Task> getTaskList() {
    return taskList;
  }

  public void setTaskList(List<Task> taskList) {
    this.taskList = taskList;
  }
}
