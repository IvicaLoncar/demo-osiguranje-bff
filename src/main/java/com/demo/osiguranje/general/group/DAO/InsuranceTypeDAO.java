package com.demo.osiguranje.general.group.DAO;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.model.Client;
import com.demo.osiguranje.general.group.model.InsuranceType;


@Component
public interface InsuranceTypeDAO
{
  public List<DataColumnDefinition> getDataColumnDefinitions();

  public CustomData<List<InsuranceType>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,      
  		List<String> includeColumns,
      List<String> excludeColumns
  );

  public CustomData<InsuranceType> insert(InsuranceType insuranceType);

  public CustomData<InsuranceType> update(UnpivotedData unpivotedInsuranceType);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<Long> getLastId();

}
