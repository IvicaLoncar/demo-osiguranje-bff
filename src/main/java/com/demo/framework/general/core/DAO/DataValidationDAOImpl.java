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
import com.demo.framework.general.core.model.DataValidation;
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
public class DataValidationDAOImpl implements DataValidationDAO
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DataValidationDAOImpl.class);


  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private List<DataColumnDefinition> dataColumnDefinitions = new ArrayList<DataColumnDefinition>();


  @Autowired
  public DataValidationDAOImpl(DataSource dataSource)
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
  public CustomData<List<DataValidation>> select(Integer pageNumber, Integer rowsPerPage, String filter, String sort)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<DataValidation>> selectedRowsStatus = new CustomData<List<DataValidation>>();

    List<DataValidation> dataValidations = null;

    try
    {
      dataValidations =
        this.jdbcTemplate.query(
            "select validacija_podataka_id, "
          + "       puni_naziv_klase, "
          + "       metoda, "
          + "       tocka, "
          + "       poredak, "
          + "       naziv_validacije, "
          + "       razina_poruke	, "
          + "       grupa_poruke, "
          + "       naziv_poruke, "
          + "       rollback, "
          + "       datum_od, "
          + "       datum_do, "
          + "       aktivno, "
          + "       vrijeme_kreiranja, "
          + "       kreirao, "
          + "       vrijeme_azuriranja, "
          + "       azurirao "
          + "  from validacije_podataka "
          + " where 1 = 1 "
       //   + (filterDefinition == null || filterDefinition.getWhereClause().isEmpty() ? "" : "and (" + filterDefinition.getWhereClause() + ")")
          + "limit ? "
          + "offset ? ",
          new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
              int paramOrder = 1;

              /*if (filterDefinition != null && filterDefinition.getParameters() != null) 
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
          new RowMapper<DataValidation>() {
            public DataValidation mapRow(ResultSet rs, int rowNum) throws SQLException {
              DataValidation dataValidation = new DataValidation();
              dataValidation.setDataValidationId(rs.getLong("validacija_podataka_id"));
              dataValidation.setFullClassName(rs.getString("puni_naziv_klase"));
              dataValidation.setMethodName(rs.getString("metoda"));
              dataValidation.setMessagePoint(rs.getString("tocka"));
              dataValidation.setItemPosition(rs.getInt("poredak"));
              dataValidation.setValidationName(rs.getString("naziv_validacije"));
              dataValidation.setApplicationMessageLevel(rs.getString("razina_poruke	"));
              dataValidation.setApplicationMessageGroup(rs.getString("grupa_poruke"));
              dataValidation.setApplicationMessageName(rs.getString("naziv_poruke"));
              dataValidation.setRollback(rs.getString("rollback"));
              dataValidation.setDateFrom(rs.getTimestamp("datum_od").toInstant());
              dataValidation.setDateTo(rs.getTimestamp("datum_do").toInstant());
              dataValidation.setActive(rs.getString("aktivno"));
              dataValidation.setCreateTime(rs.getTimestamp("vrijeme_kreiranja").toInstant());
              dataValidation.setCrateUser(rs.getString("kreirao"));
              dataValidation.setUpdateTime(rs.getTimestamp("vrijeme_azuriranja").toInstant());
              dataValidation.setUpdateUser(rs.getString("azurirao"));
              return dataValidation;
            }
      });
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationDAOImpl.select.exception",
        new CustomMessage()
          .customMessageLevel(CustomMessageLevel.Fatal)
          .exception(e)
          .markForRollback(true)
      );
    }

    selectedRowsStatus.setData(dataValidations != null ? dataValidations : new ArrayList<DataValidation>());

    selectedRowsStatus.appendMessages(messagesHandler);

    return selectedRowsStatus;
  }


  @Override
  public CustomData<DataValidation> insert(DataValidation dataValidation)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<DataValidation> insertedRowStatus = new CustomData<DataValidation>();

    int rowsAffected = 0;

    try
    {
      rowsAffected = 
        this.jdbcTemplate.update
        (
            "insert into validacije_podataka " 
          + "( "
          + "  validacija_podataka_id, "
          + "  puni_naziv_klase, "
          + "  metoda, "
          + "  tocka, "
          + "  poredak, "
          + "  naziv_validacije, "
          + "  razina_poruke	, "
          + "  grupa_poruke, "
          + "  naziv_poruke, "
          + "  rollback, "
          + "  datum_od, "
          + "  datum_do, "
          + "  aktivno, "
          + "  vrijeme_kreiranja, "
          + "  kreirao, "
          + "  vrijeme_azuriranja, "
          + "  azurirao "
          + ") "
          + "values  "
          + "( "
          + "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "  localtimestamp, "
          + "  ?, "
          + "  null, "
          + "  null "
          + ") ",
          dataValidation.getDataValidationId(),
          dataValidation.getFullClassName(),
          dataValidation.getMethodName(),
          dataValidation.getMessagePoint(),
          dataValidation.getItemPosition(),
          dataValidation.getValidationName(),
          dataValidation.getApplicationMessageLevel(),
          dataValidation.getApplicationMessageGroup(),
          dataValidation.getApplicationMessageName(),
          dataValidation.getRollback(),
          dataValidation.getDateFrom(),
          dataValidation.getDateTo(),
          dataValidation.getActive(),
          MDC.get("userInfo.username")
        );
      if (rowsAffected > 0) insertedRowStatus.setData(dataValidation);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationDAOImpl.insert.exception",
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
  public CustomData<DataValidation> update(UnpivotedData unpivotedDataValidation)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<DataValidation> updatedRowStatus = new CustomData<DataValidation>();

    int rowsAffected = 0;

    try
    {
      UpdateParameters updateParameters = new UpdateParameters(unpivotedDataValidation, dataColumnDefinitions, MDC.get("userInfo.username"));

      if (updateParameters.getColumnFields().length() == 0)
      {
        messagesHandler.setPatchContentMissing(true);
        messagesHandler.addMessage(
          "com.example.demo.general.framework.DataValidationDAOImpl.update.patchContentMissing",
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
              "update validacije_podataka " 
            + "   set "
            + updateParameters.getColumnFields() 
            + "       datum_azuriranja = localtimestamp, " 
            + "       azurirao = ? " 
            + " where validacija_podataka_id = ?  ", 
            updateParameters.getColumnValues(),
            updateParameters.getSqlTypes() 
          ); 
        if (rowsAffected > 0) updatedRowStatus.setData(new DataValidation(unpivotedDataValidation));
      }
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationDAOImpl.update.exception",
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
      rowsAffected = jdbcTemplate.update("delete from validacije_podataka where validacija_podataka_id = ? ", id);
      if (rowsAffected == 0) messagesHandler.setResourceNotFound(true);
    }
    catch (Exception e)
    {
      messagesHandler.addMessage(
        "com.example.demo.general.framework.DataValidationDAOImpl.delete.exception",
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
       lastId = (Long)jdbcTemplate.queryForObject("select max(validacija_podataka_id) from validacije_podataka ", Long.class, new Object[] {});
    }
    catch (Exception e)
    {
       messagesHandler.addMessage(
         "com.example.demo.general.framework.DataValidationDAOImpl.getLastId.exception",
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

    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dataValidationId # Long # validacija_podataka_id # dataValidationId # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "fullClassName # String # puni_naziv_klase # fullClassName # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "methodName # String # metoda # methodName # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "messagePoint # String # tocka # messagePoint # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "itemPosition # Integer # poredak # itemPosition # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "validationName # String # naziv_validacije # validationName # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "applicationMessageLevel # String # razina_poruke	 # applicationMessageLevel # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "applicationMessageGroup # String # grupa_poruke # applicationMessageGroup # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "applicationMessageName # String # naziv_poruke # applicationMessageName # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "rollback # String # rollback # rollback # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateFrom # Date # datum_od # dateFrom # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "dateTo # Date # datum_do # dateTo # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "active # String # aktivno # active # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "createTime # Timestamp # vrijeme_kreiranja # createTime # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "crateUser # String # kreirao # crateUser # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateTime # Timestamp # vrijeme_azuriranja # updateTime # true # true # "));
    this.dataColumnDefinitions.add(DataColumnDefinition.create(columnOrder++, "updateUser # String # azurirao # updateUser # true # true # "));

  }


}
