package com.demo.osiguranje.general.policy.services;

import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.policy.model.InsurancePolicy;


@Component
public interface InsurancePolicyService
{
  public CustomData<CustomDummy> handleMessages(InsurancePolicy insurancePolicy, MessagesPoint messagesPoint);

  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsurancePolicy, MessagesPoint messagesPoint);

  public CustomData<InsurancePolicy> get(Long id);

  public CustomData<List<InsurancePolicy>> get(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns);

  public CustomData<InsurancePolicy> post(InsurancePolicy insurancePolicy);

  public CustomData<InsurancePolicy> post(InsurancePolicy insurancePolicy, HandleTransaction handleTransaction);

  public CustomData<InsurancePolicy> put(InsurancePolicy insurancePolicy);

  public CustomData<InsurancePolicy> put(InsurancePolicy insurancePolicy, HandleTransaction handleTransaction);

  public CustomData<InsurancePolicy> patch(Long id, JsonPatch jsonPatch);

  public CustomData<InsurancePolicy> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction);

  public CustomData<List<InsurancePolicy>> deleteAndGet(CustomData<InsurancePolicy> requestData);

  public CustomData<List<InsurancePolicy>> deleteAndGet(CustomData<InsurancePolicy> requestData, HandleTransaction handleTransaction);

}
