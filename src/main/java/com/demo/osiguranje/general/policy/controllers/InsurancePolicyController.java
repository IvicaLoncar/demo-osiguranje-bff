package com.demo.osiguranje.general.policy.controllers;


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

import com.demo.osiguranje.general.policy.services.InsurancePolicyService;
import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.HttpStatusUtil;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.osiguranje.general.policy.model.InsurancePolicy;


@RestController
public class InsurancePolicyController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyController.class);


  @Autowired
  private InsurancePolicyService insurancePolicyService;



  @RequestMapping(value = "/api/osiguranje/general/insurance-policy/insurance-policies/{id}", method = RequestMethod.GET)
  public ResponseEntity<CustomData<InsurancePolicy>> get(
    	@PathVariable("id") Long id)
  {
  	CustomData<InsurancePolicy> insurancePolicy = null;

  	try
  	{
  	  insurancePolicy = this.insurancePolicyService.get(id);
  	}
  	catch (Exception e)
  	{
  		insurancePolicy = new CustomData<InsurancePolicy>();
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  insurancePolicy.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<InsurancePolicy>>(insurancePolicy, HttpStatusUtil.resolveGet(insurancePolicy));
  }


  @RequestMapping(value = "/api/osiguranje/general/insurance-policy/insurance-policies", method = RequestMethod.GET)
  public ResponseEntity<CustomData<List<InsurancePolicy>>> get(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage,
    	@RequestParam(name = "filter", required = false) String filter,
    	@RequestParam(name = "sort", required = false) String sort,
    	@RequestParam(name = "include-columns", required = false) String includeColumns,
    	@RequestParam(name = "exclude-columns", required = false) String excludeColumns,
    	@RequestParam(name = "domain", required = false) String domain)
  {
  	CustomData<List<InsurancePolicy>> insurancePolicies = null;

  	try
  	{
  		insurancePolicies = this.insurancePolicyService.get(
          pageNumber,
          rowPerPage,
          filter,
          sort,
          (includeColumns == null || includeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(includeColumns.split(","))),
          (excludeColumns == null || excludeColumns.trim().equals("") ? new ArrayList<String>() : Arrays.asList(excludeColumns.split(","))));
  		insurancePolicies.getMetadata().setDomain(domain);
  	}
  	catch (Exception e)
  	{
  		insurancePolicies = new CustomData<List<InsurancePolicy>>();
  		insurancePolicies.getMetadata().setDomain(domain);
  		
  		MessagesHandler messagesHandler = new MessagesHandler();
  	  messagesHandler.addMessage(
  	    "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.get.exception",
  	    new CustomMessage()
  	      .customMessageLevel(CustomMessageLevel.Fatal)
  	      .exception(e)
  	  );
  	  insurancePolicies.appendMessages(messagesHandler);
  	}

  	return new ResponseEntity<CustomData<List<InsurancePolicy>>>(insurancePolicies, HttpStatusUtil.resolveGet(insurancePolicies));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-policy/insurance-policies", method = RequestMethod.POST)
  public ResponseEntity<CustomData<InsurancePolicy>> post(@RequestBody CustomData<InsurancePolicy> requestData)
  {
  	CustomData<InsurancePolicy> insurancePolicy = null;

    try 
    {
      insurancePolicy = this.insurancePolicyService.post(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insurancePolicy = new CustomData<InsurancePolicy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.post.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insurancePolicy.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsurancePolicy>>(insurancePolicy, HttpStatusUtil.resolvePost(insurancePolicy));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-policy/insurance-policies/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CustomData<InsurancePolicy>> put(
          @PathVariable("id") Long id,
          @RequestBody CustomData<InsurancePolicy> requestData)
  {
  	CustomData<InsurancePolicy> insurancePolicy = null;

    try 
    {
      requestData.getData().setInsurancePolicyID(id);
      insurancePolicy = this.insurancePolicyService.put(requestData.getData(), HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insurancePolicy = new CustomData<InsurancePolicy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.put.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insurancePolicy.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsurancePolicy>>(insurancePolicy, HttpStatusUtil.resolvePut(insurancePolicy));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-policy/insurance-policies/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  public ResponseEntity<CustomData<InsurancePolicy>> patch(
          @PathVariable("id") Long id,
          @RequestBody JsonPatch jsonPatch)
  {
    CustomData<InsurancePolicy> insurancePolicy = null;

    try
    {
      insurancePolicy = this.insurancePolicyService.patch(id, jsonPatch, HandleTransaction.YES);
    }
    catch (Exception e)
    {
    	insurancePolicy = new CustomData<InsurancePolicy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.patch.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      insurancePolicy.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<InsurancePolicy>>(insurancePolicy, HttpStatusUtil.resolvePatch(insurancePolicy));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-policy/insurance-policies/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomData<CustomDummy>> delete(
      @PathVariable Long id)
  {
    CustomData<CustomDummy> deleteStatus = null;

    try
    {
    	deleteStatus = this.insurancePolicyService.delete(id);
    }
    catch (Exception e)
    {
    	deleteStatus = new CustomData<CustomDummy>();
    	MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
      );
      deleteStatus.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<CustomDummy>>(deleteStatus, HttpStatusUtil.resolveDelete(deleteStatus));
  }


  @RequestMapping(value="/api/osiguranje/general/insurance-policy/insurance-policies/delete-and-get", method = RequestMethod.POST)
  public ResponseEntity<CustomData<List<InsurancePolicy>>> deleteAndGet(@RequestBody CustomData<InsurancePolicy> requestData)
  {
    CustomData<List<InsurancePolicy>> insurancePolicies = null;

    try 
    {
      insurancePolicies = this.insurancePolicyService.deleteAndGet(requestData, HandleTransaction.YES);
    }
    catch (Exception e)
    {
      insurancePolicies = new CustomData<List<InsurancePolicy>>();
      MessagesHandler messagesHandler = new MessagesHandler();
      messagesHandler.addMessage(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyController.deleteAndGet.exception",
          new CustomMessage()
            .customMessageLevel(CustomMessageLevel.Fatal)
            .exception(e)
      );
      insurancePolicies.appendMessages(messagesHandler);
    }

    return new ResponseEntity<CustomData<List<InsurancePolicy>>>(insurancePolicies, HttpStatusUtil.resolveDelete(insurancePolicies));
  }


}
