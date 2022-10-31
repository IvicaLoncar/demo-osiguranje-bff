package com.demo.osiguranje.general.client.controllers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.fge.jsonpatch.JsonPatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.osiguranje.general.client.services.ClientService;
import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.HttpStatusUtil;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.osiguranje.general.client.model.Client;


@RestController
public class ClientController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);


  @Autowired
  private ClientService clientService;



  @RequestMapping(value = "/api/osiguranje/general/client/clients/{id}", method = RequestMethod.GET)
  public ResponseEntity<CustomData<Client>> get(
    	@PathVariable("id") Long id)
  {
  	CustomData<Client> client = null;

  	try
  	{
  	  client = this.clientService.get(id);
  	}
  	catch (Exception e)
  	{
  		client = new CustomData<Client>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.client.ClientController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  client.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<Client>>(client, HttpStatusUtil.resolveGet(client));
  }


  @RequestMapping(value = "/api/osiguranje/general/client/clients", method = RequestMethod.GET)
  public ResponseEntity<CustomData<List<Client>>> get(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage,
    	@RequestParam(name = "filter", required = false) String filter,
    	@RequestParam(name = "sort", required = false) String sort,
    	@RequestParam(name = "include-columns", required = false) String includeColumns,
    	@RequestParam(name = "exclude-columns", required = false) String excludeColumns,
    	@RequestParam(name = "domain", required = false) String domain)
  {
  	CustomData<List<Client>> clients = null;

  	try
  	{
  		clients = this.clientService.get(
          pageNumber,
          rowPerPage,
          filter,
          sort,
          (includeColumns == null || includeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(includeColumns.split(","))),
          (excludeColumns == null || excludeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(excludeColumns.split(","))));
  		clients.getMetadata().setDomain(domain);
  	}
  	catch (Exception e)
  	{
  		clients = new CustomData<List<Client>>();
  		clients.getMetadata().setDomain(domain);
  		
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.client.ClientController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  clients.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<List<Client>>>(clients, HttpStatusUtil.resolveGet(clients));
  }


  @RequestMapping(value="/api/osiguranje/general/client/clients", method = RequestMethod.POST)
  public ResponseEntity<CustomData<Client>> post(@RequestBody CustomData<Client> requestData)
  {
  	CustomData<Client> client = null;

    try 
    {
      client = this.clientService.post(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	client = new CustomData<Client>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientController.post.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      client.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<Client>>(client, HttpStatusUtil.resolvePost(client));
  }


  @RequestMapping(value="/api/osiguranje/general/client/clients/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CustomData<Client>> put(
          @PathVariable("id") Long id,
          @RequestBody CustomData<Client> requestData)
  {
  	CustomData<Client> client = null;

    try 
    {
      requestData.getData().setClientID(id);
      client = this.clientService.put(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	client = new CustomData<Client>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientController.put.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      client.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<Client>>(client, HttpStatusUtil.resolvePut(client));
  }


  @RequestMapping(value="/api/osiguranje/general/client/clients/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  public ResponseEntity<CustomData<Client>> patch(
          @PathVariable("id") Long id,
          @RequestBody JsonPatch jsonPatch)
  {
    CustomData<Client> client = null;

    try
    {
      client = this.clientService.patch(id, jsonPatch, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	client = new CustomData<Client>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientController.patch.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      client.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<Client>>(client, HttpStatusUtil.resolvePatch(client));
  }


  @RequestMapping(value="/api/osiguranje/general/client/clients/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomData<CustomDummy>> delete(
      @PathVariable Long id)
  {
    CustomData<CustomDummy> deleteStatus = null;

    try
    {
    	deleteStatus = this.clientService.delete(id);
    }
    catch (Exception e)
    {
    	deleteStatus = new CustomData<CustomDummy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientController.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      deleteStatus.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<CustomDummy>>(deleteStatus, HttpStatusUtil.resolveDelete(deleteStatus));
  }


  @RequestMapping(value="/api/osiguranje/general/client/clients/delete-and-get", method = RequestMethod.POST)
  public ResponseEntity<CustomData<List<Client>>> deleteAndGet(@RequestBody CustomData<Client> requestData)
  {
    CustomData<List<Client>> clients = null;

    try 
    {
      clients = this.clientService.deleteAndGet(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
      clients = new CustomData<List<Client>>();
      MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
          "com.demo.osiguranje.general.client.ClientController.deleteAndGet.exception",
          new CustomMessage()
            .customMessageLevel(CustomMessageLevel.Fatal)
            .exception(e)
      );
      clients.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<List<Client>>>(clients, HttpStatusUtil.resolveDelete(clients));
  }


}
