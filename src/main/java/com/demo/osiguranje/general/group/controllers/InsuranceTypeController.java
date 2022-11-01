package com.demo.osiguranje.general.group.controllers;


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

import com.demo.osiguranje.general.group.services.InsuranceTypeService;
import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.HttpStatusUtil;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.osiguranje.general.group.model.InsuranceType;


@RestController
public class InsuranceTypeController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceTypeController.class);


  @Autowired
  private InsuranceTypeService insuranceTypeService;



  @RequestMapping(value = "/api/osiguranje/general/insurance-type/insurance-types/{id}", method = RequestMethod.GET)
  public ResponseEntity<CustomData<InsuranceType>> get(
    	@PathVariable("id") Long id)
  {
  	CustomData<InsuranceType> insuranceType = null;

  	try
  	{
  	  insuranceType = this.insuranceTypeService.get(id);
  	}
  	catch (Exception e)
  	{
  		insuranceType = new CustomData<InsuranceType>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  insuranceType.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<InsuranceType>>(insuranceType, HttpStatusUtil.resolveGet(insuranceType));
  }


  @RequestMapping(value = "/api/osiguranje/general/insurance-type/insurance-types", method = RequestMethod.GET)
  public ResponseEntity<CustomData<List<InsuranceType>>> get(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage,
    	@RequestParam(name = "filter", required = false) String filter,
    	@RequestParam(name = "sort", required = false) String sort,
    	@RequestParam(name = "include-columns", required = false) String includeColumns,
    	@RequestParam(name = "exclude-columns", required = false) String excludeColumns,
    	@RequestParam(name = "domain", required = false) String domain)
  {
  	CustomData<List<InsuranceType>> insuranceTypes = null;

  	try
  	{
  		insuranceTypes = this.insuranceTypeService.get(
          pageNumber,
          rowPerPage,
          filter,
          sort,
          (includeColumns == null || includeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(includeColumns.split(","))),
          (excludeColumns == null || excludeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(excludeColumns.split(","))));
  		insuranceTypes.getMetadata().setDomain(domain == null ? "" : domain);
  	}
  	catch (Exception e)
  	{
  		insuranceTypes = new CustomData<List<InsuranceType>>();
  		insuranceTypes.getMetadata().setDomain(domain == null ? "" : domain);
  		
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  insuranceTypes.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<List<InsuranceType>>>(insuranceTypes, HttpStatusUtil.resolveGet(insuranceTypes));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-type/insurance-types", method = RequestMethod.POST)
  public ResponseEntity<CustomData<InsuranceType>> post(@RequestBody CustomData<InsuranceType> requestData)
  {
  	CustomData<InsuranceType> insuranceType = null;

    try 
    {
      insuranceType = this.insuranceTypeService.post(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insuranceType = new CustomData<InsuranceType>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.post.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insuranceType.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsuranceType>>(insuranceType, HttpStatusUtil.resolvePost(insuranceType));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-type/insurance-types/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CustomData<InsuranceType>> put(
          @PathVariable("id") Long id,
          @RequestBody CustomData<InsuranceType> requestData)
  {
  	CustomData<InsuranceType> insuranceType = null;

    try 
    {
      requestData.getData().setInsuranceTypeID(id);
      insuranceType = this.insuranceTypeService.put(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insuranceType = new CustomData<InsuranceType>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.put.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insuranceType.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsuranceType>>(insuranceType, HttpStatusUtil.resolvePut(insuranceType));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-type/insurance-types/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  public ResponseEntity<CustomData<InsuranceType>> patch(
          @PathVariable("id") Long id,
          @RequestBody JsonPatch jsonPatch)
  {
    CustomData<InsuranceType> insuranceType = null;

    try
    {
      insuranceType = this.insuranceTypeService.patch(id, jsonPatch, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insuranceType = new CustomData<InsuranceType>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.patch.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insuranceType.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsuranceType>>(insuranceType, HttpStatusUtil.resolvePatch(insuranceType));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-type/insurance-types/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomData<CustomDummy>> delete(
      @PathVariable Long id)
  {
    CustomData<CustomDummy> deleteStatus = null;

    try
    {
    	deleteStatus = this.insuranceTypeService.delete(id);
    }
    catch (Exception e)
    {
    	deleteStatus = new CustomData<CustomDummy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      deleteStatus.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<CustomDummy>>(deleteStatus, HttpStatusUtil.resolveDelete(deleteStatus));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-type/insurance-types/delete-and-get", method = RequestMethod.POST)
  public ResponseEntity<CustomData<List<InsuranceType>>> deleteAndGet(@RequestBody CustomData<InsuranceType> requestData)
  {
    CustomData<List<InsuranceType>> insuranceTypes = null;

    try 
    {
      insuranceTypes = this.insuranceTypeService.deleteAndGet(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
      insuranceTypes = new CustomData<List<InsuranceType>>();
      MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeController.deleteAndGet.exception",
          new CustomMessage()
            .customMessageLevel(CustomMessageLevel.Fatal)
            .exception(e)
      );
      insuranceTypes.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<List<InsuranceType>>>(insuranceTypes, HttpStatusUtil.resolveDelete(insuranceTypes));
  }


}
