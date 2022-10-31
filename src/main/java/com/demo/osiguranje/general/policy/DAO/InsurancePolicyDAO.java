package com.demo.osiguranje.general.policy.DAO;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.policy.model.InsurancePolicy;


@Component
public interface InsurancePolicyDAO
{
  public List<DataColumnDefinition> getDataColumnDefinitions();

  public CustomData<List<InsurancePolicy>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,      
  		List<String> includeColumns,
      List<String> excludeColumns
  );  		

  public CustomData<InsurancePolicy> insert(InsurancePolicy insuranceType);

  public CustomData<InsurancePolicy> update(UnpivotedData unpivotedInsurancePolicy);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<Long> getLastId();

}
