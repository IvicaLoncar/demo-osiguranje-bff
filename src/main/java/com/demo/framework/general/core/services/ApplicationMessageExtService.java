package com.demo.framework.general.core.services;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.enums.CheckUniqueResult;
import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.UnpivotedData;


@Component
public interface ApplicationMessageExtService
{
  public CustomData<CustomDummy> handleExtMessages(UnpivotedData unpivotedApplicationMessage, List<DataValidation> dataValidationBetweenPoints, MessagesPoint messagesPoint);


}
