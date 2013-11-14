package com.hubspot.singularity.data.history;

import java.util.Date;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.hubspot.singularity.SingularityTaskId;
import com.hubspot.singularity.data.history.SingularityRequestHistory.SingularityRequestHistoryMapper;
import com.hubspot.singularity.data.history.SingularityTaskHistory.SingularityTaskIdMapper;
import com.hubspot.singularity.data.history.SingularityTaskHistoryHelper.SingularityTaskHistoryHelperMapper;
import com.hubspot.singularity.data.history.SingularityTaskHistoryUpdate.SingularityTaskUpdateMapper;

public interface HistoryJDBI {

  @SqlUpdate("INSERT INTO requestHistory (requestName, request, createdAt, requestState, user) VALUES (:requestName, :request, :createdAt, :requestState, :user)")
  void insertRequestHistory(@Bind("requestName") String requestName, @Bind("request") byte[] request, @Bind("createdAt") Date createdAt, @Bind("requestState") String requestState, @Bind("user") String user);
  
  @SqlUpdate("INSERT INTO taskHistory (requestName, taskId, task, status, createdAt) VALUES (:requestName, :taskId, :task, :status, :createdAt)")
  void insertTaskHistory(@Bind("requestName") String requestName, @Bind("taskId") String taskId, @Bind("task") byte[] task, @Bind("status") String status, @Bind("createdAt") Date createdAt);

  @SqlUpdate("INSERT INTO taskUpdates (taskId, status, message, createdAt) VALUES (:taskId, :status, :message, :createdAt)")
  void insertTaskUpdate(@Bind("taskId") String taskId, @Bind("status") String status, @Bind("message") String message, @Bind("createdAt") Date createdAt);
  
  @Mapper(SingularityTaskUpdateMapper.class)
  @SqlQuery("SELECT status, message, createdAt FROM taskUpdates WHERE taskId = :taskId")
  List<SingularityTaskHistoryUpdate> getTaskUpdates(@Bind("taskId") String taskId);
  
  @Mapper(SingularityTaskHistoryHelperMapper.class)
  @SqlQuery("SELECT createdAt, task FROM taskHistory WHERE taskId = :taskId")
  SingularityTaskHistoryHelper getTaskHistoryForTask(@Bind("taskId") String taskId);
  
  @Mapper(SingularityTaskIdMapper.class)
  @SqlQuery("SELECT taskId FROM taskHistory WHERE requestName = :requestName")
  List<SingularityTaskId> getTaskHistoryForRequest(@Bind("requestName") String requestName);
  
  @Mapper(SingularityTaskIdMapper.class)
  @SqlQuery("SELECT taskId FROM taskHistory WHERE requestName LIKE CONCAT('%', CONCAT(:requestNameLike, '%'))")
  List<SingularityTaskId> getTaskHistoryForRequestLike(@Bind("requestNameLike") String requestNameLike);
  
  @Mapper(SingularityRequestHistoryMapper.class)
  @SqlQuery("SELECT request, createdAt, requestState, user FROM requestHistory WHERE requestName = :requestName")
  List<SingularityRequestHistory> getRequestHistory(@Bind("requestName") String requestName);
  
  @Mapper(SingularityRequestHistoryMapper.class)
  @SqlQuery("SELECT request, createdAt, requestState, user FROM requestHistory WHERE requestName LIKE CONCAT('%', CONCAT(:requestNameLike, '%'))")
  List<SingularityRequestHistory> getRequestHistoryLike(@Bind("requestNameLike") String requestNameLike);
  
  void close();

  
}
