package com.demo.osiguranje.general.policy.DAO;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.hql.internal.ast.tree.SelectClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.enums.OperationStatus;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.CustomMetadata;
import com.demo.framework.general.core.util.DBUtil;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.LogUtil;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.framework.general.core.util.ParameterValue;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.framework.general.core.util.UpdateParameters;
import com.demo.osiguranje.general.policy.model.InsurancePolicy;


@Repository
public class InsurancePolicyDAOImpl implements InsurancePolicyDAO
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyDAOImpl.class);

  @Value("${daoMethod.select.fetchNumberOfRows.countExpression}")
  private String fetchNumberOfRowsCountExpression;

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private List<DataColumnDefinition> dataColumnDefinitions = null;


  @Autowired
  public InsurancePolicyDAOImpl(DataSource dataSource)
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(this.dataSource);
  }


  @Override
  public List<DataColumnDefinition> getDataColumnDefinitions()
  {
    if (this.dataColumnDefinitions == null || this.dataColumnDefinitions.size() < 1)
    {
      this.populateDataColumnDefinitions();
    }
    return this.dataColumnDefinitions;
  }


  @Override
  public CustomData<List<InsurancePolicy>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,      
  		List<String> includeColumns,
      List<String> excludeColumns)  		
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<InsurancePolicy>> selectedRowsStatus = new CustomData<List<InsurancePolicy>>();

    List<CustomMetadata> insurancePoliciesMetadata = null;
    List<InsurancePolicy> insurancePolicies = null;
    
    StringBuilder columnList = new StringBuilder();
    String selectQuery = "";
    Map<String, Boolean> selectedColumns = new HashMap<String, Boolean>();

    Map<String, String> filterColumns = new HashMap<String, String>();

    if (filter != null && !filter.trim().equals(""))
    {
	    Arrays.asList(filter.split(",")).forEach((column) -> {
	    	filterColumns.put(column.split("_")[0], column.split("_")[1]); 
	    });
    }
    
    try
    {
      insurancePoliciesMetadata =
        this.jdbcTemplate.query(
            "select " + fetchNumberOfRowsCountExpression + " as number_of_records "
          + "  from police "
          + " where 1 = 1 "
          ,
          new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
              int paramOrder = 1;
            }
          },
          new RowMapper<CustomMetadata>() {
            public CustomMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
              CustomMetadata customMetadata = new CustomMetadata();
              customMetadata.setNumberOfRows(rs.getObject("number_of_records") == null || rs.wasNull() ? null : rs.getLong("number_of_records"));
              customMetadata.setPageNumber(pageNumber == null ? 0 : pageNumber);
              customMetadata.setRowsPerPage(rowsPerPage == null ? 100 : rowsPerPage);
              return customMetadata;
            }
      });
      
      if (insurancePoliciesMetadata.size() > 0 && insurancePoliciesMetadata.get(0).getNumberOfRows() > 0)
      {
      	String tableTLA = "pol";

        DBUtil.prepareColumnList(
            columnList,
            this.getDataColumnDefinitions(),
            includeColumns,
            excludeColumns,
            selectedColumns
          );
        
       // System.out.println("col defs: " + this.getDataColumnDefinitions().toString());        
       // System.out.println("selecte columns: " + selectedColumns.toString());

        selectQuery
	    		= "select " 
          + columnList.toString()
	          .replace(",", "," + '\n')
	          .replace("vrsta_osiguranja_naziv", "vro.naziv       as vrsta_osiguranja_naziv")
	          .replace("klijent_naziv_ugovaratelj", "kli_ph.naziv       as klijent_naziv_ugovaratelj")
	          .replace("klijent_naziv_osiguranik", "kli_is.naziv       as klijent_naziv_osiguranik")
	    			.replace(tableTLA + ".datum_od", "parsedatetime(formatdatetime(" + tableTLA + ".datum_od, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_od")
	    			.replace(tableTLA + ".datum_do", "parsedatetime(formatdatetime(" + tableTLA + ".datum_do, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_do")
	    			.replace(tableTLA + ".datum_kreiranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja")
	    			.replace(tableTLA + ".datum_azuriranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja")
          + "  from police " + tableTLA
          + "  left outer join vrste_osiguranja vro on (vro.vrsta_osiguranja_id = " + tableTLA + ".vrsta_osiguranja_id) "
          + "  left outer join klijenti kli_ph on (kli_ph.klijent_id = " + tableTLA + ".klijent_id_ugovaratelj) "
          + "  left outer join klijenti kli_is on (kli_is.klijent_id = " + tableTLA + ".klijent_id_osiguranik) "
          + " where 1 = 1 "
          + "limit ? "
          + "offset ? ";
        
        //System.out.println(" query: " + selectQuery);
        /*
         
          "select pol.polica_id, "
            + "       pol.vrsta_osiguranja_id, "
            + "       vro.naziv    as vrsta_osiguranja_naziv,"
            + "       pol.klijent_id_ugovaratelj, "
            + "       kli_ph.naziv     as klijent_naziv_ugovaratelj, "
            + "       pol.klijent_id_osiguranik, "
            + "       kli_is.naziv    as klijent_naziv_osiguranik, "
            + "       parsedatetime(formatdatetime(pol.datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od, "
            + "       parsedatetime(formatdatetime(pol.datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_do, "
            + "       pol.aktivno, "
            + "       parsedatetime(formatdatetime(pol.datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja, "
            + "       pol.kreirao, "
            + "       parsedatetime(formatdatetime(pol.datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja, "
            + "       pol.azurirao "
            + "  from police pol "
            + "  left outer join vrste_osiguranja vro on (vro.vrsta_osiguranja_id = pol.vrsta_osiguranja_id) "
            + "  left outer join klijenti kli_ph on (kli_ph.klijent_id = pol.klijent_id_ugovaratelj) "
            + "  left outer join klijenti kli_is on (kli_is.klijent_id = pol.klijent_id_osiguranik) "
            + " where 1 = 1 "
            + "limit ? "
            + "offset ? ",
         */

        insurancePolicies =
          this.jdbcTemplate.query(
            selectQuery,
            new PreparedStatementSetter() {
              public void setValues(PreparedStatement preparedStatement) throws SQLException {
                int paramOrder = 1;
                preparedStatement.setInt(paramOrder++, rowsPerPage == null ? 100 : rowsPerPage);
                preparedStatement.setInt(paramOrder++, pageNumber == null ? 0 : pageNumber * (rowsPerPage == null ? 100 : rowsPerPage));
              }
            },
            new RowMapper<InsurancePolicy>() {
              public InsurancePolicy mapRow(ResultSet rs, int rowNum) throws SQLException {
              	InsurancePolicy insurancePolicy = new InsurancePolicy();
                insurancePolicy.setInsurancePolicyID(selectedColumns.get("polica_id") == false || rs.getObject("polica_id") == null || rs.wasNull() ? null : rs.getLong("polica_id"));
                insurancePolicy.setInsuranceTypeID(selectedColumns.get("vrsta_osiguranja_id") == false || rs.getObject("vrsta_osiguranja_id") == null || rs.wasNull() ? null : rs.getLong("vrsta_osiguranja_id"));
                insurancePolicy.setInsuranceTypeName(selectedColumns.get("vrsta_osiguranja_naziv") == false || rs.getObject("vrsta_osiguranja_naziv") == null || rs.wasNull() ? null : rs.getString("vrsta_osiguranja_naziv"));
                insurancePolicy.setClientIDPolicyHolder(selectedColumns.get("klijent_id_ugovaratelj") == false || rs.getObject("klijent_id_ugovaratelj") == null || rs.wasNull() ? null : rs.getLong("klijent_id_ugovaratelj"));
                insurancePolicy.setClientNamePolicyHolder(selectedColumns.get("klijent_naziv_ugovaratelj") == false || rs.getObject("klijent_naziv_ugovaratelj") == null || rs.wasNull() ? null : rs.getString("klijent_naziv_ugovaratelj"));
                insurancePolicy.setClientIDInsured(selectedColumns.get("klijent_id_osiguranik") == false || rs.getObject("klijent_id_osiguranik") == null || rs.wasNull() ? null : rs.getLong("klijent_id_osiguranik"));
                insurancePolicy.setClientNameInsured(selectedColumns.get("klijent_naziv_osiguranik") == false || rs.getObject("klijent_naziv_osiguranik") == null || rs.wasNull() ? null : rs.getString("klijent_naziv_osiguranik"));
                insurancePolicy.setDateFrom(selectedColumns.get("datum_od") == false || rs.getObject("datum_od") == null || rs.wasNull() ? null : rs.getTimestamp("datum_od").toInstant());
                insurancePolicy.setDateTo(selectedColumns.get("datum_do") == false || rs.getObject("datum_do") == null || rs.wasNull() ? null : rs.getTimestamp("datum_do").toInstant());
                insurancePolicy.setActive(selectedColumns.get("aktivno") == false || rs.getObject("aktivno") == null || rs.wasNull() ? null : rs.getString("aktivno").equalsIgnoreCase("Y"));
                insurancePolicy.setCreateDate(selectedColumns.get("datum_kreiranja") == false || rs.getObject("datum_kreiranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_kreiranja").toInstant());
                insurancePolicy.setCreateUser(selectedColumns.get("kreirao") == false || rs.getObject("kreirao") == null || rs.wasNull() ? null : rs.getString("kreirao"));
                insurancePolicy.setUpdateDate(selectedColumns.get("datum_azuriranja") == false || rs.getObject("datum_azuriranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_azuriranja").toInstant());
                insurancePolicy.setUpdateUser(selectedColumns.get("azurirao") == false || rs.getObject("azurirao") == null || rs.wasNull() ? null : rs.getString("azurirao"));
                return insurancePolicy;
              }
        });
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.select.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsurancePolicyDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM pageNumber: " + (pageNumber != null ? pageNumber.toString() : "null"));
      LOGGER.info(" METHOD PARAM rowsPerPage: " + (rowsPerPage != null ? rowsPerPage.toString() : "null"));
      LOGGER.info(" METHOD PARAM filterDefinition: " + (filter != null ? filter.toString() : "null"));
      LOGGER.info(" METHOD PARAM sort: " + (sort != null ? sort : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    selectedRowsStatus.setMetadata(insurancePoliciesMetadata != null && insurancePoliciesMetadata.size() > 0 ? insurancePoliciesMetadata.get(0) : new CustomMetadata());
    selectedRowsStatus.setData(insurancePolicies != null ? insurancePolicies : new ArrayList<InsurancePolicy>());

    selectedRowsStatus.appendMessages(messagesHandler);

    return selectedRowsStatus;
  }


  @Override
  public CustomData<InsurancePolicy> insert(InsurancePolicy insurancePolicy)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<InsurancePolicy> insertedRowStatus = new CustomData<InsurancePolicy>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = 
        this.jdbcTemplate.update
        (
            "insert into police " 
          + "( "
          + "  polica_id,"
          + "  vrsta_osiguranja_id, "
          + "  klijent_id_ugovaratelj, "
          + "  klijent_id_osiguranik, "
          + "  datum_od, "
          + "  datum_do, "
          + "  aktivno, "
          + "  datum_kreiranja, "
          + "  kreirao, "
          + "  datum_azuriranja, "
          + "  azurirao "
          + ") "
          + "values  "
          + "( "
          + "  ?, ?, ?, ?, ?, ?, ?, "
          + "  localtimestamp, "
          + "  ?, "
          + "  null, "
          + "  null "
          + ") ",
          insurancePolicy.getInsurancePolicyID(),
          insurancePolicy.getInsuranceTypeID(),
          insurancePolicy.getClientIDPolicyHolder(),
          insurancePolicy.getClientIDInsured(),
          insurancePolicy.getDateFrom(),
          insurancePolicy.getDateTo(),
          (insurancePolicy.getActive() ? "Y" : "N"),
          MDC.get("userInfo.username")
        );
      if (rowsAffected > 0) insertedRowStatus.setData(insurancePolicy);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.insert.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsurancePolicyDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM insurancePolicy: " + (insurancePolicy != null ? insurancePolicy.toString() : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<InsurancePolicy> update(UnpivotedData unpivotedInsurancePolicy)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<InsurancePolicy> updatedRowStatus = new CustomData<InsurancePolicy>();

    int rowsAffected = 0;

    try
    {
      UpdateParameters updateParameters = new UpdateParameters(unpivotedInsurancePolicy, this.getDataColumnDefinitions(), MDC.get("userInfo.username"));

      if (updateParameters.getColumnFields().length() == 0)
      {
        messagesHandler.setPatchContentMissing(true);
        messagesHandler.addMessage(
          "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.update.patchContentMissing",
          new CustomMessage()
            .customMessageLevel(CustomMessageLevel.Error)
            .code("100")
            .exception(new Exception("Patch content is missing or invalid"))
            .markForRollback(true)
        );
      }
      else
      {
        rowsAffected = 
          this.jdbcTemplate.update
          (
              "update police " 
            + "   set "
            + updateParameters.getColumnFields() 
            + "       datum_azuriranja = localtimestamp, " 
            + "       azurirao = ? " 
            + " where vrste_osiguranja_id = ?  ", 
            updateParameters.getColumnValues(),
            updateParameters.getSqlTypes() 
          ); 
        if (rowsAffected > 0) updatedRowStatus.setData(new InsurancePolicy(unpivotedInsurancePolicy));
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.update.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsurancePolicyDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM unpivotedInsurancePolicy: " + (unpivotedInsurancePolicy != null ? new InsurancePolicy(unpivotedInsurancePolicy).toString() : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });      
    }

    updatedRowStatus.appendMessages(messagesHandler);

    return updatedRowStatus;
  }


  @Override
  public CustomData<CustomDummy> delete(Long id)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<CustomDummy> deletedRowStatus = new CustomData<CustomDummy>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = jdbcTemplate.update("delete from police where polica_id = ? ", id);
      if (rowsAffected == 0) messagesHandler.setResourceNotFound(true);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsurancePolicyDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM id: " + (id != null ? id.toString() : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });            
    }

    deletedRowStatus.appendMessages(messagesHandler);

    return deletedRowStatus;
  }


  @Override
  public CustomData<Long> getLastId()
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<Long> fetchLastIdStatus = new CustomData<Long>();

    Long lastId = 0L;

    try
    {
       lastId = (Long)jdbcTemplate.queryForObject("select max(polica_id) from police ", Long.class, new Object[] {});
    }
    catch (Exception e)
    {
       messagesHandler.addMessage(
         "com.demo.osiguranje.general.group.InsurancePolicyDAOImpl.getLastId.exception",
         new CustomMessage()
           .customMessageLevel(CustomMessageLevel.Fatal)
           .exception(e)
           .markForRollback(true)
       );

       LOGGER.info("EXCEPTION: " + e.toString());
       LOGGER.info(" METHOD: " + InsurancePolicyDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
       LOGGER.info(" STACK TRACE:");
       LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });            
    }

    fetchLastIdStatus.setData(lastId == null ? 0L : lastId);

    fetchLastIdStatus.appendMessages(messagesHandler);

    return fetchLastIdStatus;
  }


  private void populateDataColumnDefinitions()
  {
    this.dataColumnDefinitions = new ArrayList<DataColumnDefinition>();

    int columnOrder = 1;

    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "insurancePolicyID # Long # pol # polica_id # insurancePolicyID # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "insuranceTypeID # Long # pol # vrsta_osiguranja_id # insuranceTypeID # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "insuranceTypeName # String # # vrsta_osiguranja_naziv # insuranceTypeName # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientIDPolicyHolder # Long # pol # klijent_id_ugovaratelj # clientIDPolicyHolder # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientNamePolicyHolder # String # # klijent_naziv_ugovaratelj # clientNamePolicyHolder # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientIDInsured # Long # pol # klijent_id_osiguranik # clientIDInsured # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientNameInsured # String # # klijent_naziv_osiguranik # clientNameInsured # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateFrom # Instant # pol # datum_od # dateFrom # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateTo # Instant # pol # datum_do # dateTo # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "active # Boolean # pol # aktivno # active # true # true # ", "CONVERT_STRING_TO_BOOLEAN#boolean||string#true||Y#false||N"));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createDate # Instant # pol # datum_kreiranja # createDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createUser # String # pol # kreirao # createUser # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateDate # Instant # pol # datum_azuriranja # updateDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateUser # String # pol # azurirao # updateUser # true # true # "));

  }


}
