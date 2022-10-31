package com.demo.framework.general.core.DAO;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.UnpivotedData;


@Component
public interface DataValidationDAO
{
  public List<DataColumnDefinition> getDataColumnDefinitions();

  public CustomData<List<DataValidation>> select(Integer pageNumber, Integer rowsPerPage, String filter, String sort);

  public CustomData<DataValidation> insert(DataValidation dataValidation);

  public CustomData<DataValidation> update(UnpivotedData unpivotedDataValidation);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<Long> getLastId();

}
