package com.demo.framework.general.core.DAO;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.enums.OperationStatus;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.DBUtil;
import com.demo.framework.general.core.util.DataColumnDefinition;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.framework.general.core.util.ParameterValue;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.framework.general.core.util.UpdateParameters;


@Repository
public class ApplicationMessageDAOImpl implements ApplicationMessageDAO
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMessageDAOImpl.class);


  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private List<DataColumnDefinition> dataColumnDefinitions = new ArrayList<DataColumnDefinition>();


  @Autowired
  public ApplicationMessageDAOImpl(DataSource dataSource)
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(this.dataSource);

    this.populateDataColumnDefinitions();
  }


  @Override
  public List<DataColumnDefinition> getDataColumnDefinitions()
  {
    return this.dataColumnDefinitions;
  }


  @Override
  public CustomData<List<ApplicationMessage>> select(Integer pageNumber, Integer rowsPerPage, String filter, String sort)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<ApplicationMessage>> selectedRowsStatus = new CustomData<List<ApplicationMessage>>();

    List<ApplicationMessage> applicationMessages = null;

    try
    {
      applicationMessages =
        this.jdbcTemplate.query(
            "select aplikacijska_poruka_id, "
          + "       grupa, "
          + "       naziv, "
          + "       jezik, "
          + "       tekst_poruke, "
          + "       datum_od, "
          + "       datum_do, "
          + "       aktivno, "
          + "       datum_kreiranja, "
          + "       kreirao, "
          + "       datum_azuriranja, "
          + "       azurirao "
          + "  from aplikacijske_poruke "
          + " where 1 = 1 "
      //    + (filterDefinition == null || filterDefinition.getWhereClause().isEmpty() ? "" : "and (" + filterDefinition.getWhereClause() + ")")
          + "limit ? "
          + "offset ? ",
          new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
              int paramOrder = 1;

            /*  if (filterDefinition != null && filterDefinition.getParameters() != null) 
              {
                for (int i = 0; i < filterDefinition.getParameters().size(); i++)
                {
                  ParameterValue paramValue = filterDefinition.getParameters().get(i);
                  while (paramValue != null)
                  {
                    if (paramValue.getDataType().equalsIgnoreCase("Long")) 
                    {
                      preparedStatement.setLong(paramOrder++, Long.parseLong(paramValue.getValue()));
                    }
                    if (paramValue.getDataType().equalsIgnoreCase("String")) 
                    {
                      preparedStatement.setString(paramOrder++, paramValue.getValue());
                    }
                    break;
                  }
                }
              }*/
              preparedStatement.setInt(paramOrder++, rowsPerPage == null ? 100 : rowsPerPage);
              preparedStatement.setInt(paramOrder++, pageNumber == null ? 0 : (pageNumber - 1) * (rowsPerPage == null ? 100 : rowsPerPage));
            }
          },
          new RowMapper<ApplicationMessage>() {
            public ApplicationMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
              ApplicationMessage applicationMessage = new ApplicationMessage();
              applicationMessage.setApplicationMessageId(rs.getLong("aplikacijska_poruka_id"));
              applicationMessage.setGroup(rs.getString("grupa"));
              applicationMessage.setName(rs.getString("naziv"));
              applicationMessage.setLocale(rs.getString("jezik"));
              applicationMessage.setMessageText(rs.getString("tekst_poruke"));
              applicationMessage.setDateFrom(rs.getTimestamp("datum_od").toInstant());
              applicationMessage.setDateTo(rs.getTimestamp("datum_do").toInstant());
              applicationMessage.setActive(rs.getString("aktivno"));
              applicationMessage.setCreateDate(rs.getTimestamp("datum_kreiranja").toInstant());
              applicationMessage.setCrateUser(rs.getString("kreirao"));
              applicationMessage.setUpdateDate(rs.getTimestamp("datum_azuriranja").toInstant());
              applicationMessage.setUpdateUser(rs.getString("azurirao"));
              return applicationMessage;
            }
      });
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageDAOImpl.select.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
    }

    selectedRowsStatus.setData(applicationMessages != null ? applicationMessages : new ArrayList<ApplicationMessage>());

    selectedRowsStatus.appendMessages(messagesHandler);

    return selectedRowsStatus;
  }


  @Override
  public CustomData<ApplicationMessage> insert(ApplicationMessage applicationMessage)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<ApplicationMessage> insertedRowStatus = new CustomData<ApplicationMessage>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = 
        this.jdbcTemplate.update
        (
            "insert into aplikacijske_poruke " 
          + "( "
          + "  aplikacijska_poruka_id, "
          + "  grupa, "
          + "  naziv, "
          + "  jezik, "
          + "  tekst_poruke, "
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
          + "  ?, ?, ?, ?, ?, ?, ?, ?, "
          + "  localtimestamp, "
          + "  ?, "
          + "  null, "
          + "  null "
          + ") ",
          applicationMessage.getApplicationMessageId(),
          applicationMessage.getGroup(),
          applicationMessage.getName(),
          applicationMessage.getLocale(),
          applicationMessage.getMessageText(),
          applicationMessage.getDateFrom(),
          applicationMessage.getDateTo(),
          applicationMessage.getActive(),
          MDC.get("userInfo.username")
        );
      if (rowsAffected > 0) insertedRowStatus.setData(applicationMessage);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageDAOImpl.insert.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<ApplicationMessage> update(UnpivotedData unpivotedApplicationMessage)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<ApplicationMessage> updatedRowStatus = new CustomData<ApplicationMessage>();

    int rowsAffected = 0;

    try
    {
      UpdateParameters updateParameters = new UpdateParameters(unpivotedApplicationMessage, dataColumnDefinitions, MDC.get("userInfo.username"));

      if (updateParameters.getColumnFields().length() == 0)
      {
        messagesHandler.setPatchContentMissing(true);
        messagesHandler.addMessage(
          "com.example.demo.general.framework.ApplicationMessageDAOImpl.update.patchContentMissing",
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
              "update aplikacijske_poruke " 
            + "   set "
            + updateParameters.getColumnFields() 
            + "       datum_azuriranja = localtimestamp, " 
            + "       azurirao = ? " 
            + " where aplikacijska_poruka_id = ?  ", 
            updateParameters.getColumnValues(),
            updateParameters.getSqlTypes() 
          ); 
        if (rowsAffected > 0) updatedRowStatus.setData(new ApplicationMessage(unpivotedApplicationMessage));
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageDAOImpl.update.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
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
      rowsAffected = jdbcTemplate.update("delete from aplikacijske_poruke where aplikacijska_poruka_id = ? ", id);
      if (rowsAffected == 0) messagesHandler.setResourceNotFound(true);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.ApplicationMessageDAOImpl.delete.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
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
       lastId = (Long)jdbcTemplate.queryForObject("select max(aplikacijska_poruka_id) from aplikacijske_poruke ", Long.class, new Object[] {});
    }
    catch (Exception e)
    {
       messagesHandler.addMessage(
         "com.example.demo.general.framework.ApplicationMessageDAOImpl.getLastId.exception",
         new CustomMessage()
           .customMessageLevel(CustomMessageLevel.Fatal)
           .exception(e)
           .markForRollback(true)
       );
    }

    fetchLastIdStatus.setData(lastId == null ? 0L : lastId);

    fetchLastIdStatus.appendMessages(messagesHandler);

    return fetchLastIdStatus;
  }


  private void populateDataColumnDefinitions()
  {
    int columnOrder = 1;

    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "applicationMessageId # Long # aplikacijska_poruka_id # applicationMessageId # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "group # String # grupa # group # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "name # String # naziv # name # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "locale # String # jezik # locale # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "messageText # String # tekst_poruke # messageText # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateFrom # Date # datum_od # dateFrom # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateTo # Date # datum_do # dateTo # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "active # String # aktivno # active # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createDate # Timestamp # datum_kreiranja # createDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "crateUser # String # kreirao # crateUser # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateDate # Timestamp # datum_azuriranja # updateDate # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateUser # String # azurirao # updateUser # true # true # "));

  }


}
