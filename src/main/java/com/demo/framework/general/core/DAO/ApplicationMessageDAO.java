package com.demo.framework.general.core.DAO;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.UnpivotedData;


@Component
public interface ApplicationMessageDAO
{
  public List<DataColumnDefinition> getDataColumnDefinitions();

  public CustomData<List<ApplicationMessage>> select(Integer pageNumber, Integer rowsPerPage, String filter, String sort);

  public CustomData<ApplicationMessage> insert(ApplicationMessage applicationMessage);

  public CustomData<ApplicationMessage> update(UnpivotedData unpivotedApplicationMessage);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<Long> getLastId();

}
