package com.demo.framework.general.core.services;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.framework.general.core.DAO.DataValidationDAO;
import com.demo.framework.general.core.enums.CheckUniqueResult;
import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.UnpivotedData;


@Service
public class DataValidationExtServiceImpl implements DataValidationExtService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DataValidationExtServiceImpl.class);


  @Autowired
  private DataValidationDAO dataValidationDAO;


  @Override
  public CustomData<CustomDummy> handleExtMessages(UnpivotedData unpivotedDataValidation, List<DataValidation> dataValidationBetweenPoints, MessagesPoint messagesPoint)
  {
    return null;
  }


}
