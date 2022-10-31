package com.demo.osiguranje.general.client.services.ext;


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
public interface ClientServiceExt
{
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedClient, List<DataValidation> dataValidationBetweenPoints, MessagesPoint messagesPoint);


}
