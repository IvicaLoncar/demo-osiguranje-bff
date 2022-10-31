package com.demo.osiguranje.general.group.services;

import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.model.Client;
import com.demo.osiguranje.general.group.model.InsuranceType;


@Component
public interface InsuranceTypeService
{
  public CustomData<CustomDummy> handleMessages(InsuranceType insuranceType, MessagesPoint messagesPoint);

  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsuranceType, MessagesPoint messagesPoint);

  public CustomData<InsuranceType> get(Long id);

  public CustomData<List<InsuranceType>> get(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns);

  public CustomData<InsuranceType> post(InsuranceType insuranceType);

  public CustomData<InsuranceType> post(InsuranceType insuranceType, HandleTransaction handleTransaction);

  public CustomData<InsuranceType> put(InsuranceType insuranceType);

  public CustomData<InsuranceType> put(InsuranceType insuranceType, HandleTransaction handleTransaction);

  public CustomData<InsuranceType> patch(Long id, JsonPatch jsonPatch);

  public CustomData<InsuranceType> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction);

  public CustomData<List<InsuranceType>> deleteAndGet(CustomData<InsuranceType> requestData);

  public CustomData<List<InsuranceType>> deleteAndGet(CustomData<InsuranceType> requestData, HandleTransaction handleTransaction);

}
