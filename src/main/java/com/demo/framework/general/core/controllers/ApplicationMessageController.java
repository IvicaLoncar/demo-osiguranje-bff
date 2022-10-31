package com.demo.framework.general.core.controllers;


import java.util.List;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.services.ApplicationMessageService;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.HttpStatusUtil;
import com.demo.framework.general.core.util.MessagesHandler;
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


@RestController
public class ApplicationMessageController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMessageController.class);


  @Autowired
  private ApplicationMessageService applicationMessageService;



  @RequestMapping(value = "/api/general/framework/application-messages/{id}", method = RequestMethod.GET)
  public ResponseEntity<CustomData<ApplicationMessage>> get(
    	@PathVariable("id") Long id)
  {
  	CustomData<ApplicationMessage> applicationMessage = null;

  	try
  	{
  	  applicationMessage = this.applicationMessageService.get(id);
  	}
  	catch (Exception e)
  	{
  		applicationMessage = new CustomData<ApplicationMessage>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.example.demo.general.framework.ApplicationMessageController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  applicationMessage.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<ApplicationMessage>>(applicationMessage, HttpStatusUtil.resolveGet(applicationMessage));
  }


  @RequestMapping(value = "/api/general/framework/application-messages", method = RequestMethod.GET)
  public ResponseEntity<CustomData<List<ApplicationMessage>>> get(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage,
    	@RequestParam(name = "filter", required = false) String filter,
    	@RequestParam(name = "sort", required = false) String sort)
  {
  	CustomData<List<ApplicationMessage>> applicationMessages = null;

  	try
  	{
  		applicationMessages = this.applicationMessageService.get(pageNumber, rowPerPage, filter, sort);
  	}
  	catch (Exception e)
  	{
  		applicationMessages = new CustomData<List<ApplicationMessage>>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.example.demo.general.framework.ApplicationMessageController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  applicationMessages.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<List<ApplicationMessage>>>(applicationMessages, HttpStatusUtil.resolveGet(applicationMessages));
  }


  @RequestMapping(value="/api/general/framework/application-messages", method = RequestMethod.POST)
  public ResponseEntity<CustomData<ApplicationMessage>> post(@RequestBody ApplicationMessage requestData)
  {
  	CustomData<ApplicationMessage> applicationMessage = null;

    try 
    {
      applicationMessage = this.applicationMessageService.post(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	applicationMessage = new CustomData<ApplicationMessage>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageController.post.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      applicationMessage.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<ApplicationMessage>>(applicationMessage, HttpStatusUtil.resolvePost(applicationMessage));
  }


  @RequestMapping(value="/api/general/framework/application-messages/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CustomData<ApplicationMessage>> put(
          @PathVariable("id") Long id,
          @RequestBody ApplicationMessage requestData)
  {
  	CustomData<ApplicationMessage> applicationMessage = null;

    try 
    {
      requestData.setApplicationMessageId(id);
      applicationMessage = this.applicationMessageService.put(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	applicationMessage = new CustomData<ApplicationMessage>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageController.put.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      applicationMessage.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<ApplicationMessage>>(applicationMessage, HttpStatusUtil.resolvePut(applicationMessage));
  }


  @RequestMapping(value="/api/general/framework/application-messages/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  public ResponseEntity<CustomData<ApplicationMessage>> patch(
          @PathVariable("id") Long id,
          @RequestBody JsonPatch jsonPatch)
  {
    CustomData<ApplicationMessage> applicationMessage = null;

    try
    {
      applicationMessage = this.applicationMessageService.patch(id, jsonPatch, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	applicationMessage = new CustomData<ApplicationMessage>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageController.patch.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      applicationMessage.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<ApplicationMessage>>(applicationMessage, HttpStatusUtil.resolvePatch(applicationMessage));
  }


  @RequestMapping(value="/api/general/framework/application-messages/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomData<CustomDummy>> delete(
      @PathVariable Long id)
  {
    CustomData<CustomDummy> deleteStatus = null;

    try
    {
    	deleteStatus = this.applicationMessageService.delete(id);
    }
    catch (Exception e)
    {
    	deleteStatus = new CustomData<CustomDummy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageController.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      deleteStatus.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<CustomDummy>>(deleteStatus, HttpStatusUtil.resolveDelete(deleteStatus));
  }


}
