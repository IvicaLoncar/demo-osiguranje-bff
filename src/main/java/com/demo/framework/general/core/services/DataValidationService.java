package com.demo.framework.general.core.services;

import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.UnpivotedData;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public interface DataValidationService
{
  public CustomData<CustomDummy> handleMessages(DataValidation dataValidation, MessagesPoint messagesPoint);

  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedDataValidation, MessagesPoint messagesPoint);

  public CustomData<DataValidation> get(Long id);

  public CustomData<List<DataValidation>> get(Integer pageNumber, Integer rowsPerPage, String filter, String sort);

  public CustomData<DataValidation> post(DataValidation dataValidation);

  public CustomData<DataValidation> post(DataValidation dataValidation, HandleTransaction handleTransaction);

  public CustomData<DataValidation> put(DataValidation dataValidation);

  public CustomData<DataValidation> put(DataValidation dataValidation, HandleTransaction handleTransaction);

  public CustomData<DataValidation> patch(Long id, JsonPatch jsonPatch);

  public CustomData<DataValidation> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction);

}
