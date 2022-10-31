package com.demo.osiguranje.general.policy.services.ext;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.enums.CheckUniqueResult;
import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.model.Client;


@Component
public interface InsurancePolicyServiceExt
{
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsurancePolicy, List<DataValidation> dataValidationBetweenPoints, MessagesPoint messagesPoint);


}
