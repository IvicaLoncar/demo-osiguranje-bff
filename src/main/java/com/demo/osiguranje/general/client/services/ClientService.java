package com.demo.osiguranje.general.client.services;

import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.model.Client;


@Component
public interface ClientService
{
  public CustomData<CustomDummy> handleMessages(Client client, MessagesPoint messagesPoint);

  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedClient, MessagesPoint messagesPoint);

  public CustomData<Client> get(Long id);

  public CustomData<List<Client>> get(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns);

  public CustomData<Client> post(Client client);

  public CustomData<Client> post(Client client, HandleTransaction handleTransaction);

  public CustomData<Client> put(Client client);

  public CustomData<Client> put(Client client, HandleTransaction handleTransaction);

  public CustomData<Client> patch(Long id, JsonPatch jsonPatch);

  public CustomData<Client> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction);

  public CustomData<List<Client>> deleteAndGet(CustomData<Client> requestData);

  public CustomData<List<Client>> deleteAndGet(CustomData<Client> requestData, HandleTransaction handleTransaction);

}
