package com.demo.osiguranje.general.client.DAO;


import java.util.List;

import org.springframework.stereotype.Component;

import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.model.Client;


@Component
public interface ClientDAO
{
  public List<DataColumnDefinition> getDataColumnDefinitions();

  public CustomData<List<Client>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,      
  		List<String> includeColumns,
      List<String> excludeColumns
  );  		

  public CustomData<Client> insert(Client client);

  public CustomData<Client> update(UnpivotedData unpivotedClient);

  public CustomData<CustomDummy> delete(Long id);

  public CustomData<Long> getLastId();

}
