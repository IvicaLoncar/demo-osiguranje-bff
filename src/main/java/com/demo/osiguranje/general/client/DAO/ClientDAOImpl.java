package com.demo.osiguranje.general.client.DAO;


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
import com.demo.osiguranje.general.client.model.Client;


@Repository
public class ClientDAOImpl implements ClientDAO
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientDAOImpl.class);

  @Value("${daoMethod.select.fetchNumberOfRows.countExpression}")
  private String fetchNumberOfRowsCountExpression;

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private List<DataColumnDefinition> dataColumnDefinitions = null;


  @Autowired
  public ClientDAOImpl(DataSource dataSource)
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
  public CustomData<List<Client>> select(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<Client>> selectedRowsStatus = new CustomData<List<Client>>();

    List<CustomMetadata> clientsMetadata = null;
    List<Client> clients = null;
    
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
      clientsMetadata =
        this.jdbcTemplate.query(
            "select " + fetchNumberOfRowsCountExpression + " as number_of_records "
          + "  from klijenti "
          + " where 1 = 1 "
          + (filterColumns.get("clientID") != null ? "and klijent_ID = ? " : "")
        	+ (filterColumns.get("name") != null ? "and upper(naziv) like '%' || upper(?) || '%'" : "")
          ,
          new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
              int paramOrder = 1;
              if (filterColumns.get("clientID") != null ) {
              	preparedStatement.setLong(paramOrder++, Long.parseLong(filterColumns.get("clientID")));
              }
              if (filterColumns.get("name") != null ) {
              	preparedStatement.setString(paramOrder++, filterColumns.get("name"));
              }
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
      
      if (clientsMetadata.size() > 0 && clientsMetadata.get(0).getNumberOfRows() > 0)
      {
      	String tableTLA = "kli";

        DBUtil.prepareColumnList(
            columnList,
            this.getDataColumnDefinitions(),
            includeColumns,
            excludeColumns,
            selectedColumns
          );

        selectQuery
	    		= "select " 
          + columnList.toString()
	          .replace(",", "," + '\n')
	    			.replace(tableTLA + ".datum_od", "parsedatetime(formatdatetime(" + tableTLA + ".datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od")
	    			.replace(tableTLA + ".datum_do", "parsedatetime(formatdatetime(" + tableTLA + ".datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_do")
	    			.replace(tableTLA + ".datum_kreiranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja")
	    			.replace(tableTLA + ".datum_azuriranja", "parsedatetime(formatdatetime(" + tableTLA + ".datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja")          
          + "  from klijenti " + tableTLA
          + " where 1 = 1 "
          + "limit ? "
          + "offset ? ";
        
        System.out.println("select q: " + selectQuery.toString());
        
        /*
          "select klijent_id, "
            + "       tip_klijenta, "
            + "       naziv, "
            + "       mobitel, "
            + "       email, "
            + "       oib, "
            + "       maticni_broj, "
            + "       parsedatetime(formatdatetime(datum_od, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_od, "
            + "       parsedatetime(formatdatetime(datum_do, 'yyyy.MM.dd') || ' UTC', 'yyyy.MM.dd z') as datum_do, "
            + "       aktivno, "
            + "       parsedatetime(formatdatetime(datum_kreiranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_kreiranja, "
            + "       kreirao, "
            + "       parsedatetime(formatdatetime(datum_azuriranja, 'yyyy.MM.dd HH:mm:ss') || ' UTC', 'yyyy.MM.dd HH:mm:ss z') as datum_azuriranja, "
            + "       azurirao "
            + "  from klijenti "
            + " where 1 = 1 "
            + (filterColumns.get("clientID") != null ? "and klijent_ID = ? " : "")
            + (filterColumns.get("name") != null ? "and upper(naziv) like '%' || upper(?) || '%'" : "")            
            + "limit ? "
            + "offset ? "
         
         */

        clients =
          this.jdbcTemplate.query(
            selectQuery,
            new PreparedStatementSetter() {
              public void setValues(PreparedStatement preparedStatement) throws SQLException {
                int paramOrder = 1;
                if (filterColumns.get("clientID") != null ) {
                	preparedStatement.setLong(paramOrder++, Long.parseLong(filterColumns.get("clientID")));
                }
                if (filterColumns.get("name") != null ) {
                	preparedStatement.setString(paramOrder++, filterColumns.get("name"));
                }
                preparedStatement.setInt(paramOrder++, rowsPerPage == null ? 100 : rowsPerPage);
                preparedStatement.setInt(paramOrder++, pageNumber == null ? 0 : pageNumber * (rowsPerPage == null ? 100 : rowsPerPage));
              }
            },
            new RowMapper<Client>() {
              public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
                Client client = new Client();
                client.setClientID(selectedColumns.get("klijent_id") == false || rs.getObject("klijent_id") == null || rs.wasNull() ? null : rs.getLong("klijent_id"));
                client.setClientType(selectedColumns.get("tip_klijenta") == false || rs.getObject("tip_klijenta") == null || rs.wasNull() ? null : rs.getString("tip_klijenta"));
                client.setName(selectedColumns.get("naziv") == false || rs.getObject("naziv") == null || rs.wasNull() ? null : rs.getString("naziv"));
                client.setGsm(selectedColumns.get("mobitel") == false || rs.getObject("mobitel") == null || rs.wasNull() ? null : rs.getString("mobitel"));
                client.setEmail(selectedColumns.get("email") == false || rs.getObject("email") == null || rs.wasNull() ? null : rs.getString("email"));
                client.setOib(selectedColumns.get("oib") == false || rs.getObject("oib") == null || rs.wasNull() ? null : rs.getString("oib"));
                client.setMbr(selectedColumns.get("maticni_broj") == false || rs.getObject("maticni_broj") == null || rs.wasNull() ? null : rs.getString("maticni_broj"));
                client.setDateFrom(selectedColumns.get("datum_od") == false || rs.getObject("datum_od") == null || rs.wasNull() ? null : rs.getTimestamp("datum_od").toInstant());
                client.setDateTo(selectedColumns.get("datum_do") == false || rs.getObject("datum_do") == null || rs.wasNull() ? null : rs.getTimestamp("datum_do").toInstant());
                client.setActive(selectedColumns.get("aktivno") == false || rs.getObject("aktivno") == null || rs.wasNull() ? null : rs.getString("aktivno").equalsIgnoreCase("Y"));
                client.setCreateDate(selectedColumns.get("datum_kreiranja") == false || rs.getObject("datum_kreiranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_kreiranja").toInstant());
                client.setCreateUser(selectedColumns.get("kreirao") == false || rs.getObject("kreirao") == null || rs.wasNull() ? null : rs.getString("kreirao"));
                client.setUpdateDate(selectedColumns.get("datum_azuriranja") == false || rs.getObject("datum_azuriranja") == null || rs.wasNull() ? null : rs.getTimestamp("datum_azuriranja").toInstant());
                client.setUpdateUser(selectedColumns.get("azurirao") == false || rs.getObject("azurirao") == null || rs.wasNull() ? null : rs.getString("azurirao"));
                return client;
              }
        });
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientDAOImpl.select.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + ClientDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM pageNumber: " + (pageNumber != null ? pageNumber.toString() : "null"));
      LOGGER.info(" METHOD PARAM rowsPerPage: " + (rowsPerPage != null ? rowsPerPage.toString() : "null"));
      LOGGER.info(" METHOD PARAM filterDefinition: " + (filter != null ? filter.toString() : "null"));
      LOGGER.info(" METHOD PARAM sort: " + (sort != null ? sort : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    selectedRowsStatus.setMetadata(clientsMetadata != null && clientsMetadata.size() > 0 ? clientsMetadata.get(0) : new CustomMetadata());
    selectedRowsStatus.setData(clients != null ? clients : new ArrayList<Client>());

    selectedRowsStatus.appendMessages(messagesHandler);

    return selectedRowsStatus;
  }


  @Override
  public CustomData<Client> insert(Client client)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<Client> insertedRowStatus = new CustomData<Client>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = 
        this.jdbcTemplate.update
        (
            "insert into klijenti " 
          + "( "
          + "  klijent_id, "
          + "  tip_klijenta, "
          + "  naziv, "
          + "  mobitel, "
          + "  email, "
          + "  oib, "
          + "  maticni_broj, "
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
          + "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "  localtimestamp, "
          + "  ?, "
          + "  null, "
          + "  null "
          + ") ",
          client.getClientID(),
          client.getClientType(),
          client.getName(),
          client.getGsm(),
          client.getEmail(),
          client.getOib(),
          client.getMbr(),
          client.getDateFrom(),
          client.getDateTo(),
          (client.getActive() ? "Y" : "N"),
          MDC.get("userInfo.username")
        );
      if (rowsAffected > 0) insertedRowStatus.setData(client);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientDAOImpl.insert.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + ClientDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM client: " + (client != null ? client.toString() : "null"));
      LOGGER.info(" STACK TRACE:");
      LogUtil.extractStackTraceToList(e).forEach((value) -> { LOGGER.info("  " + value); });
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<Client> update(UnpivotedData unpivotedClient)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<Client> updatedRowStatus = new CustomData<Client>();

    int rowsAffected = 0;

    try
    {
      UpdateParameters updateParameters = new UpdateParameters(unpivotedClient, this.getDataColumnDefinitions(), MDC.get("userInfo.username"));

      if (updateParameters.getColumnFields().length() == 0)
      {
        messagesHandler.setPatchContentMissing(true);
        messagesHandler.addMessage(
          "com.demo.osiguranje.general.client.ClientDAOImpl.update.patchContentMissing",
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
              "update klijenti " 
            + "   set "
            + updateParameters.getColumnFields() 
            + "       datum_azuriranja = localtimestamp, " 
            + "       azurirao = ? " 
            + " where klijent_id = ?  ", 
            updateParameters.getColumnValues(),
            updateParameters.getSqlTypes() 
          ); 
        if (rowsAffected > 0) updatedRowStatus.setData(new Client(unpivotedClient));
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientDAOImpl.update.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + ClientDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
      LOGGER.info(" METHOD PARAM unpivotedClient: " + (unpivotedClient != null ? new Client(unpivotedClient).toString() : "null"));
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
      rowsAffected = jdbcTemplate.update("delete from klijenti where klijent_id = ? ", id);
      if (rowsAffected == 0) messagesHandler.setResourceNotFound(true);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.demo.osiguranje.general.client.ClientDAOImpl.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
      
      LOGGER.info("EXCEPTION: " + e.toString());
      LOGGER.info(" METHOD: " + ClientDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
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
       lastId = (Long)jdbcTemplate.queryForObject("select max(klijent_id) from klijenti ", Long.class, new Object[] {});
    }
    catch (Exception e)
    {
       messagesHandler.addMessage(
         "com.demo.osiguranje.general.client.ClientDAOImpl.getLastId.exception",
         new CustomMessage()
           .customMessageLevel(CustomMessageLevel.Fatal)
           .exception(e)
           .markForRollback(true)
       );

       LOGGER.info("EXCEPTION: " + e.toString());
       LOGGER.info(" METHOD: " + ClientDAOImpl.class.getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName());
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

    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientID # Long # kli # klijent_id # clientID # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "clientType # String # kli # tip_klijenta # clientType # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "name # String # kli # naziv # name # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "gsm # String # kli # mobitel # gsm # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "email # String # kli # email # email # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "oib # String # kli # oib # oib # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "mbr # String # kli # maticni_broj # mbr # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateFrom # Instant # kli # datum_od # dateFrom # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateTo # Instant # kli # datum_do # dateTo # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "active # Boolean # kli # aktivno # active # true # true # ", "CONVERT_STRING_TO_BOOLEAN#boolean||string#true||Y#false||N"));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createDate # Instant # kli # datum_kreiranja # createDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createUser # String # kli # kreirao # createUser # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateDate # Instant # kli # datum_azuriranja # updateDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateUser # String # kli # azurirao # updateUser # true # true # "));

  }


}
