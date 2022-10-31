package com.demo.framework.general.core.services;

import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.UnpivotedData;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public interface ApplicationMessageService
{
  public CustomData<CustomDummy> handleMessages(ApplicationMessage applicationMessage, MessagesPoint messagesPoint);

  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedApplicationMessage, MessagesPoint messagesPoint);

  public CustomData<ApplicationMessage> get(Long id);

  public CustomData<List<ApplicationMessage>> get(Integer pageNumber, Integer rowsPerPage, String filter, String sort);

  public CustomData<ApplicationMessage> post(ApplicationMessage applicationMessage);

  public CustomData<ApplicationMessage> post(ApplicationMessage applicationMessage, HandleTransaction handleTransaction);

  public CustomData<ApplicationMessage> put(ApplicationMessage applicationMessage);

  public CustomData<ApplicationMessage> put(ApplicationMessage applicationMessage, HandleTransaction handleTransaction);

  public CustomData<ApplicationMessage> patch(Long id, JsonPatch jsonPatch);

  public CustomData<ApplicationMessage> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction);

}
