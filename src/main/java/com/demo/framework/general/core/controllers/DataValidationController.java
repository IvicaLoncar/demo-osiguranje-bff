package com.demo.framework.general.core.controllers;


import java.util.List;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.services.DataValidationService;
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
public class DataValidationController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DataValidationController.class);


  @Autowired
  private DataValidationService dataValidationService;



  @RequestMapping(value = "/api/general/framework/data-validation/{id}", method = RequestMethod.GET)
  public ResponseEntity<CustomData<DataValidation>> get(
    	@PathVariable("id") Long id)
  {
  	CustomData<DataValidation> dataValidation = null;

  	try
  	{
  	  dataValidation = this.dataValidationService.get(id);
  	}
  	catch (Exception e)
  	{
  		dataValidation = new CustomData<DataValidation>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.example.demo.general.framework.DataValidationController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  dataValidation.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<DataValidation>>(dataValidation, HttpStatusUtil.resolveGet(dataValidation));
  }


  @RequestMapping(value = "/api/general/framework/data-validation", method = RequestMethod.GET)
  public ResponseEntity<CustomData<List<DataValidation>>> get(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage,
    	@RequestParam(name = "filter", required = false) String filter,
    	@RequestParam(name = "sort", required = false) String sort)
  {
  	CustomData<List<DataValidation>> dataValidations = null;

  	try
  	{
  		dataValidations = this.dataValidationService.get(pageNumber, rowPerPage, filter, sort);
  	}
  	catch (Exception e)
  	{
  		dataValidations = new CustomData<List<DataValidation>>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.example.demo.general.framework.DataValidationController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  dataValidations.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<List<DataValidation>>>(dataValidations, HttpStatusUtil.resolveGet(dataValidations));
  }


  @RequestMapping(value="/api/general/framework/data-validation", method = RequestMethod.POST)
  public ResponseEntity<CustomData<DataValidation>> post(@RequestBody DataValidation requestData)
  {
  	CustomData<DataValidation> dataValidation = null;

    try 
    {
      dataValidation = this.dataValidationService.post(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	dataValidation = new CustomData<DataValidation>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationController.post.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      dataValidation.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<DataValidation>>(dataValidation, HttpStatusUtil.resolvePost(dataValidation));
  }


  @RequestMapping(value="/api/general/framework/data-validation/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CustomData<DataValidation>> put(
          @PathVariable("id") Long id,
          @RequestBody DataValidation requestData)
  {
  	CustomData<DataValidation> dataValidation = null;

    try 
    {
      requestData.setDataValidationId(id);
      dataValidation = this.dataValidationService.put(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	dataValidation = new CustomData<DataValidation>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationController.put.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      dataValidation.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<DataValidation>>(dataValidation, HttpStatusUtil.resolvePut(dataValidation));
  }


  @RequestMapping(value="/api/general/framework/data-validation/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  public ResponseEntity<CustomData<DataValidation>> patch(
          @PathVariable("id") Long id,
          @RequestBody JsonPatch jsonPatch)
  {
    CustomData<DataValidation> dataValidation = null;

    try
    {
      dataValidation = this.dataValidationService.patch(id, jsonPatch, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	dataValidation = new CustomData<DataValidation>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationController.patch.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      dataValidation.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<DataValidation>>(dataValidation, HttpStatusUtil.resolvePatch(dataValidation));
  }


  @RequestMapping(value="/api/general/framework/data-validation/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomData<CustomDummy>> delete(
      @PathVariable Long id)
  {
    CustomData<CustomDummy> deleteStatus = null;

    try
    {
    	deleteStatus = this.dataValidationService.delete(id);
    }
    catch (Exception e)
    {
    	deleteStatus = new CustomData<CustomDummy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationController.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      deleteStatus.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<CustomDummy>>(deleteStatus, HttpStatusUtil.resolveDelete(deleteStatus));
  }


}
