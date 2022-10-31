package com.demo.osiguranje.general.policy.services.ext;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.framework.general.core.enums.CheckUniqueResult;
import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.DAO.ClientDAO;
import com.demo.osiguranje.general.client.model.Client;


@Service
public class InsurancePolicyServiceExtImpl implements InsurancePolicyServiceExt
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyServiceExtImpl.class);


  @Autowired
  private ClientDAO clientDAO;


  @Override
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsurancePolicy, List<DataValidation> dataValidationBetweenPoints, MessagesPoint messagesPoint)
  {
    return null;
  }


}
