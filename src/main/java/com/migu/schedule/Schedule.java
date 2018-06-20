package com.migu.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.Task;
import com.migu.schedule.info.TaskContainer;
import com.migu.schedule.info.TaskInfo;

/*
 *类名和方法不能修改
 */
public class Schedule {

  private ConcurrentHashMap<Integer, Integer> nodeMap = new ConcurrentHashMap();
  private TreeMap<Integer, Integer> taskMap = new TreeMap();

  //每个结点和所对应的consumption
  private List<TaskContainer> containList = new ArrayList<TaskContainer>();
  private List<Task> taskList = new ArrayList();
  private List<Task> originalTastList = new ArrayList();

  public int init() {
    if (nodeMap != null) nodeMap.clear();
    if (taskMap != null) taskMap.clear();
    if (taskMap != null) taskMap.clear();
    if (containList != null) containList.clear();
    if (taskList != null) taskList.clear();
    if (originalTastList != null) originalTastList.clear();
    return ReturnCodeKeys.E001;
  }

  public int registerNode(int nodeId) {
    if (nodeId < 0) return ReturnCodeKeys.E004;
    if (nodeMap.get(nodeId) != null) return ReturnCodeKeys.E005;
    nodeMap.put(nodeId, nodeId);
    containList.add(new TaskContainer(nodeId));

    return ReturnCodeKeys.E003;
  }

  public int unregisterNode(int nodeId) {
    if (nodeMap.get(nodeId) == null) return ReturnCodeKeys.E007;
    nodeMap.remove(nodeMap);
    return ReturnCodeKeys.E006;
  }

  public int addTask(int taskId, int consumption) {
    if (taskId <= 0) return ReturnCodeKeys.E009;
    if (taskMap.get(taskId) != null) return ReturnCodeKeys.E010;
    taskMap.put(taskId, consumption);
    originalTastList.add(new Task(taskId, consumption));
    return ReturnCodeKeys.E008;
  }

  public int deleteTask(int taskId) {
    if (taskId <= 0) return ReturnCodeKeys.E009;
    if (taskMap.get(taskId) == null) return ReturnCodeKeys.E012;
    taskMap.remove(taskId);
    return ReturnCodeKeys.E011;
  }

  public int scheduleTask(int threshold) {
    for (Map.Entry<Integer, Integer> entry : taskMap.entrySet()) {
      taskList.add(new Task(entry.getKey(), entry.getValue()));
    }

    //对所有任务进行排序，从大到小
    Collections.sort(
        taskList,
        new Comparator<Task>() {
          public int compare(Task o1, Task o2) {
            return o2.getConsumption().compareTo(o1.getConsumption());
          }
        });
    //将任务从大到小分配给结点，
    sortTask();
    return ReturnCodeKeys.E013;
  }

  /** 将任务从大到小分配给结点，分给容器内consumption最小的 */
  private void sortTask() {

    for (int i = 0; i < taskList.size(); i++) {
      int minConsumptionContainer = findMinConsumptionContainer();
      for (TaskContainer c : containList) {
        if (c.getNodeId() == minConsumptionContainer) c.addToContainer(taskList.get(i));
      }
    }

    //对容器内的
    Collections.sort(
        containList,
        new Comparator<TaskContainer>() {
          public int compare(TaskContainer o1, TaskContainer o2) {
            if (o1.getTotalConsumption() == o2.getTotalConsumption())
              return o1.getTaskNOSum().compareTo(o2.getTaskNOSum());
            else return o1.getTotalConsumption().compareTo(o2.getTotalConsumption());
          }
        });
    
    //将node结点进行排序，塞入到容器内
    ArrayList<Integer> nodeLists = new ArrayList();
    
    for(Map.Entry<Integer, Integer> entry: nodeMap.entrySet()){
      nodeLists.add(entry.getKey());
    }
    Collections.sort(nodeLists);
    
    for(int i=0; i< containList.size();i++){
      TaskContainer t = containList.get(i);
      t.setNodeId(nodeLists.get(i));
      containList.set(i, t);
    }
    
    
    
    
    
    
  }

  private int findMinConsumptionContainer() {
    int sum = 2100000000;
    int minNodeId = 0;
    for (TaskContainer c : containList) {
      if (c.getTotalConsumption() < sum) {
        sum = c.getTotalConsumption();
        minNodeId = c.getNodeId();
      }
    }

    return minNodeId;
  }

  public int queryTaskStatus(List<TaskInfo> tasks) {
    for(Task t:originalTastList){
      TaskInfo taskInfo  = new TaskInfo();
      taskInfo.setTaskId(t.getTaskId());
      tasks.add(taskInfo);
    }
    for(TaskInfo taskInfo: tasks){
      int nodeId = queryNodeIdInResult(taskInfo.getTaskId());
      taskInfo.setNodeId(nodeId);
    }
    return ReturnCodeKeys.E015;
  }
  
  /**
   * 在结果中查询taskId属于哪个结点
   *
   * @return
   */
  private int queryNodeIdInResult(int taskId) {
    for (TaskContainer c : containList) {
      for (Task t : c.getTaskList()) {
        if (taskId == t.getTaskId()) return c.getNodeId();
      }
    }
    return 0;
  }
  
  

  public static void main(String[] args) {
    Schedule schedule = new Schedule();

    int actual = schedule.init();
    schedule.registerNode(7);
    schedule.registerNode(1);
    schedule.registerNode(6);

    schedule.addTask(1, 2);
    schedule.addTask(2, 14);
    schedule.addTask(3, 4);
    schedule.addTask(4, 16);
    schedule.addTask(5, 6);
    schedule.addTask(6, 5);
    schedule.addTask(7, 3);

    actual = schedule.scheduleTask(10);
  }
}
