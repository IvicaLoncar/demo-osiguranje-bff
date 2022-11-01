package com.demo.osiguranje.general.group.DAO;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
import com.demo.osiguranje.general.group.model.InsuranceType;


@Repository
public class InsuranceTypeDAOImpl implements InsuranceTypeDAO
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceTypeDAOImpl.class);

  @Value("${daoMethod.select.fetchNumberOfRows.countExpression}")
  private String fetchNumberOfRowsCountExpression;

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private List<DataColumnDefinition> dataColumnDefinitions = null;


  @Autowired
  public InsuranceTypeDAOImpl(DataSource dataSource)
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
  public CustomData<List<InsuranceType>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<InsuranceType>> selectedRowsStatus = new CustomData<List<InsuranceType>>();

    List<CustomMetadata> insuranceTypesMetadata = null;
    List<InsuranceType> insuranceTypes = null;
    
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
      insuranceTypesMetadata =
        this.jdbcTemplate.query(
            "select " + fetchNumberOfRowsCountExpression + " as number_of_records "
          + "  from vrste_osiguranja "
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
      
      if (insuranceTypesMetadata.size() > 0 && insuranceTypesMetadata.get(0).getNumberOfRows() > 0)
      {
      	String tableTLA = "vro";
      	
        DBUtil.prepareColumnList(
            columnList,
            this.getDataColumnDefinitions(),
            includeColumns,
            excludeColumns,
            selectedColumns
          );
      	
        /*System.out.println("column list " 
        		+ columnList.toString()
        			.replace(",", "," + '\n')
        			.replace("vro.datum_od", "parsedatetime(formatdatetime(vro.datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od")
        			.replace("vro.datum_do", "parsedatetime(formatdatetime(vro.datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od")
        			.replace("vro.datum_kreiranja", "parsedatetime(formatdatetime(datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja")
        			.replace("vro.datum_azuriranja", "parsedatetime(formatdatetime(datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja")
        		);*/
        
        selectQuery
	    		= "select " 
          + columnList.toString()
	          .replace(",", "," + '\n')
	    			.replace(tableTLA + ".datum_od", "parsedatetime(formatdatetime(" + tableTLA + ".datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od")
	    			.replace(tableTLA + ".datum_do", "parsedatetime(formatdatetime(" + tableTLA + ".datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_do")
	    			.replace(tableTLA + ".datum_kreiranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja")
	    			.replace(tableTLA + ".datum_azuriranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja")
          + "  from vrste_osiguranja " + tableTLA
          + " where 1 = 1 "
          + "limit ? "
          + "offset ? ";
        
        /*
           "select vrsta_osiguranja_id, "
            + "       skupina, "
            + "       naziv, "
            + "       opis, "
            + "       parsedatetime(formatdatetime(datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od, "
            + "       parsedatetime(formatdatetime(datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_do, "
            + "       aktivno, "
            + "       parsedatetime(formatdatetime(datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja, "
            + "       kreirao, "
            + "       parsedatetime(formatdatetime(datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja, "
            + "       azurirao "
            + "  from vrste_osiguranja "
            + " where 1 = 1 "
            + "limit ? "
            + "offset ? "
         
         */
        
        insuranceTypes =
          this.jdbcTemplate.query(
            selectQuery,
            new PreparedStatementSetter() {
              public void setValues(PreparedStatement preparedStatement) throws SQLException {
                int paramOrder = 1;
                preparedStatement.setInt(paramOrder++, rowsPerPage == null ? 100 : rowsPerPage);
                preparedStatement.setInt(paramOrder++, pageNumber == null ? 0 : pageNumber * (rowsPerPage == null ? 100 : rowsPerPage));
              }
            },
            new RowMapper<InsuranceType>() {
              public InsuranceType mapRow(ResultSet rs, int rowNum) throws SQLException {
              	InsuranceType insuranceType = new InsuranceType();
                insuranceType.setInsuranceTypeID(selectedColumns.get("vrsta_osiguranja_id") == false || rs.getObject("vrsta_osiguranja_id") == null || rs.wasNull() ? null : rs.getLong("vrsta_osiguranja_id"));
                insuranceType.setGroup(selectedColumns.get("skupina") == false || rs.getObject("skupina") == null || rs.wasNull() ? null : rs.getString("skupina"));
                insuranceType.setName(selectedColumns.get("naziv") == false || rs.getObject("naziv") == null || rs.wasNull() ? null : rs.getString("naziv"));
                insuranceType.setDescription(selectedColumns.get("opis") == false || rs.getObject("opis") == null || rs.wasNull() ? null : rs.getString("opis"));
                insuranceType.setDateFrom(selectedColumns.get("datum_od") == false || rs.getObject("datum_od") == null || rs.wasNull() ? null : rs.getTimestamp("datum_od").toInstant());
                insuranceType.setDateTo(selectedColumns.get("datum_do") == false || rs.getObject("datum_do") == null || rs.wasNull() ? null : rs.getTimestamp("datum_do").toInstant());
                insuranceType.setActive(selectedColumns.get("aktivno") == false || rs.getObject("aktivno") == null || rs.wasNull() ? null : rs.getString("aktivno").equalsIgnoreCase("Y"));
                insuranceType.setCreateDate(selectedColumns.get("datum_kreiranja") == false || rs.getObject("datum_kreiranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_kreiranja").toInstant());
                insuranceType.setCreateUser(selectedColumns.get("kreirao") == false || rs.getObject("kreirao") == null || rs.wasNull() ? null : rs.getString("kreirao"));
                insuranceType.setUpdateDate(selectedColumns.get("datum_azuriranja") == false || rs.getObject("datum_azuriranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_azuriranja").toInstant());
                insuranceType.setUpdateUser(selectedColumns.get("azurirao") == false || rs.getObject("azurirao") == null || rs.wasNull() ? null : rs.getString("azurirao"));
                return insuranceType;
              }
        });
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.select.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsuranceTypeDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM pageNumber: " + (pageNumber != null ? pageNumber.toString() : "null"));
      LOGGER.info(" METHOD PARAM rowsPerPage: " + (rowsPerPage != null ? rowsPerPage.toString() : "null"));
      LOGGER.info(" METHOD PARAM filterDefinition: " + (filter != null ? filter.toString() : "null"));
      LOGGER.info(" METHOD PARAM sort: " + (sort != null ? sort : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    selectedRowsStatus.setMetadata(insuranceTypesMetadata != null && insuranceTypesMetadata.size() > 0 ? insuranceTypesMetadata.get(0) : new CustomMetadata());
    selectedRowsStatus.setData(insuranceTypes != null ? insuranceTypes : new ArrayList<InsuranceType>());

    selectedRowsStatus.appendMessages(messagesHandler);

    return selectedRowsStatus;
  }


  @Override
  public CustomData<InsuranceType> insert(InsuranceType insuranceType)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<InsuranceType> insertedRowStatus = new CustomData<InsuranceType>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = 
        this.jdbcTemplate.update
        (
            "insert into vrste_osiguranja " 
          + "( "
          + "  vrsta_osiguranja_id, "
          + "  skupina, "
          + "  naziv, "
          + "  opis, "
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
          insuranceType.getInsuranceTypeID(),
          insuranceType.getGroup(),
          insuranceType.getName(),
          insuranceType.getDescription(),
          insuranceType.getDateFrom(),
          insuranceType.getDateTo(),
          (insuranceType.getActive() ? "Y" : "N"),
          MDC.get("userInfo.username")
        );
      if (rowsAffected > 0) insertedRowStatus.setData(insuranceType);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.insert.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsuranceTypeDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM insuranceType: " + (insuranceType != null ? insuranceType.toString() : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<InsuranceType> update(UnpivotedData unpivotedInsuranceType)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<InsuranceType> updatedRowStatus = new CustomData<InsuranceType>();

    int rowsAffected = 0;

    try
    {
      UpdateParameters updateParameters = new UpdateParameters(unpivotedInsuranceType, this.getDataColumnDefinitions(), MDC.get("userInfo.username"));

      if (updateParameters.getColumnFields().length() == 0)
      {
        messagesHandler.setPatchContentMissing(true);
        messagesHandler.addMessage(
          "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.update.patchContentMissing",
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
              "update vrste_osiguranja " 
            + "   set "
            + updateParameters.getColumnFields() 
            + "       datum_azuriranja = localtimestamp, " 
            + "       azurirao = ? " 
            + " where vrsta_osiguranja_id = ?  ", 
            updateParameters.getColumnValues(),
            updateParameters.getSqlTypes() 
          ); 
        if (rowsAffected > 0) updatedRowStatus.setData(new InsuranceType(unpivotedInsuranceType));
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.update.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsuranceTypeDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM unpivotedInsuranceType: " + (unpivotedInsuranceType != null ? new InsuranceType(unpivotedInsuranceType).toString() : "null"));
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
      rowsAffected = jdbcTemplate.update("delete from vrste_osiguranja where vrsta_osiguranja_id = ? ", id);
      if (rowsAffected == 0) messagesHandler.setResourceNotFound(true);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + InsuranceTypeDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
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
       lastId = (Long)jdbcTemplate.queryForObject("select max(vrsta_osiguranja_id) from vrste_osiguranja ", Long.class, new Object[] {});
    }
    catch (Exception e)
    {
       messagesHandler.addMessage(
         "com.demo.osiguranje.general.group.InsuranceTypeDAOImpl.getLastId.exception",
         new CustomMessage()
           .customMessageLevel(CustomMessageLevel.Fatal)
           .exception(e)
           .markForRollback(true)
       );

       LOGGER.info("EXCEPTION: " + e.toString());
       LOGGER.info(" METHOD: " + InsuranceTypeDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
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

    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "insuranceTypeID # Long # vro # vrsta_osiguranja_id # insuranceTypeID # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "group # String # vro # skupina # group # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "name # String # vro # naziv # name # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "description # String # vro # opis # description # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateFrom # Instant # vro # datum_od # dateFrom # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateTo # Instant # vro # datum_do # dateTo # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "active # Boolean # vro # aktivno # active # true # true # ", "CONVERT_STRING_TO_BOOLEAN#boolean||string#true||Y#false||N"));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createDate # Instant # vro # datum_kreiranja # createDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createUser # String # vro # kreirao # createUser # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateDate # Instant # vro # datum_azuriranja # updateDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateUser # String # vro # azurirao # updateUser # true # true # "));

  }


}
