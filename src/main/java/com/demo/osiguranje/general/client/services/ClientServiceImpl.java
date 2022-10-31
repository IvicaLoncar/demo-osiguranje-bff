package com.demo.osiguranje.general.client.services;

import com.github.fge.jsonpatch.JsonPatch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.enums.MessagesPoint;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.model.DataValidation;
import com.demo.framework.general.core.services.DataValidationService;
import com.demo.framework.general.core.util.ApplicationMessageUtil;
import com.demo.framework.general.core.util.CompareUtil;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;
import com.demo.framework.general.core.util.CustomMessage;
import com.demo.framework.general.core.util.CustomMetadata;
import com.demo.framework.general.core.util.DataValidationUtil;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.osiguranje.general.client.DAO.ClientDAO;
import com.demo.osiguranje.general.client.model.Client;
import com.demo.osiguranje.general.client.services.ext.ClientServiceExt;


@Service
public class ClientServiceImpl implements ClientService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);


  private List<ApplicationMessage> applicationMessages;


  @Autowired
  private DataValidationService dataValidationService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private ClientServiceExt clientServiceExt;

  @Autowired
  private ClientDAO clientDAO;



  public ClientServiceImpl()
  {
    if (this.applicationMessages == null) this.applicationMessages = new ArrayList<ApplicationMessage>();

    // clientID
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BU200",
        "hr-hr",
        "Slog sa id * više ne postoji u bazi podataka.");

    // clientID
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BU200",
        "en-us",
        "Record with id * doesnt exists in a database.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU300",
        "hr-hr",
        "* je obavezan podatak.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU300",
        "en-us",
        "* is mandatory.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
        "hr-hr",
        "* se već koristi.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
        "en-us",
        "* already exists.");

    // gsm
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1300",
        "hr-hr",
        "* je obavezan podatak.");

    // gsm
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1300",
        "en-us",
        "* is mandatory.");

    // email
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1400",
        "hr-hr",
        "* je obavezan podatak.");

    // email
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1400",
        "en-us",
        "* is mandatory.");

    // oib
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1500",
        "hr-hr",
        "* je obavezan podatak.");

    // oib
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1500",
        "en-us",
        "* is mandatory.");

    // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2400",
        "hr-hr",
        "* je obavezan.");

    // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2400",
        "en-us",
        "* is mandatory.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2500",
        "hr-hr",
        "Aktivno je obavezno.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2500",
        "en-us",
        "Active is mandatory.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.AI2500",
        "hr-hr",
        "Slog je uspješno kreiran.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.AU2500",
        "hr-hr",
        "Slog je uspješno promijenjen.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.client.ClientServiceImpl.AD2500",
        "hr-hr",
        "Slog je uspješno obrisan.");
  }


  @Override
  public CustomData<CustomDummy> handleMessages(Client client, MessagesPoint messagesPoint)
  {
    return this.handleMessages(client.unpivot(), messagesPoint);
  }


  @Override
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedClient, MessagesPoint messagesPoint)
  {
    CustomData<CustomDummy> messagesStatus = new CustomData<CustomDummy>();

    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<DataValidation>> dataValidations = this.dataValidationService.get(1, 1000, null, null);
    List<DataValidation> dataValidationBetweenPoints = null;

    StringBuilder preparedMessage = null;

    boolean checkPassed = true;

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 300);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_300",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedClient.getValueAsString("name") == null || "".equals(unpivotedClient.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Naziv")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1200);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup300_1200",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1200: UNIQUE-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* se već koristi.},{locale:en-us,definition:* already exists.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* se već koristi.", "Naziv");
        if (unpivotedClient.getValueAsString("name") == null ||"".equals(unpivotedClient.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU1200: SKIPPED, messsage: " + preparedMessage.toString());
          checkPassed = false;
        }
        if (checkPassed)
        {
          CustomData<List<Client>> clientsList = null;
          if (messagesHandler.proceed())
          {
            messagesHandler.addMessages(
                 "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
                  (clientsList = this.clientDAO.select(1, 1, "name_" + unpivotedClient.getValueAsString("name"), null, null, null)));
          }
          if (clientsList == null || clientsList.getData() == null || clientsList.getData().size() > 0)
          {
            checkPassed = false;
          }
          if (messagesHandler.proceed())
          {
            if (checkPassed)
            {
              LOGGER.info("BIU1200: PASSED, messsage: " + preparedMessage.toString());
            }
            if (messagesHandler.proceed() && checkPassed == false)
            {
              LOGGER.info("BIU1200: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
              messagesHandler.addMessage(
                "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
                new CustomMessage()
                  .applicationMessages(this.applicationMessages)
                  .customMessageLevel(CustomMessageLevel.Error)
                  .markForRollback(true)
                  .parameter("Naziv")
              );
            }
          }
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1200, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1200_1300",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("gsm"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Mobitel");
        if (unpivotedClient.getValueAsString("gsm") == null || "".equals(unpivotedClient.getValueAsString("gsm").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Mobitel")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 1400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1300_1400",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:email,javaName:email,javaType:String,modificators:[],constraints:[BIU1400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("email"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Email");
        if (unpivotedClient.getValueAsString("email") == null || "".equals(unpivotedClient.getValueAsString("email").trim()))
        {
          LOGGER.info("BIU1400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1400",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Email")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1400: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1400, 1500);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1400_1500",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:oib,javaName:oib,javaType:String,modificators:[],constraints:[BIU1500],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("oib"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "OIB");
        if (unpivotedClient.getValueAsString("oib") == null || "".equals(unpivotedClient.getValueAsString("oib").trim()))
        {
          LOGGER.info("BIU1500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("OIB")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1500: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1500, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1500_2400",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedClient.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2400",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Datum od")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU2400: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 2400, 2500);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup2400_2500",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedClient.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU2500: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 2500, 10000);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup2500_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 200);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_200",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BU200: NOTFOUND-ERROR-STOP-ROLLBACK  => [{locale:hr-hr,definition:Slog sa id ClientServiceImpl.java ClientService.java ext više ne postoji u bazi podataka.},{locale:en-us,definition:Record with id ClientServiceImpl.java ClientService.java ext doesnt exists in a database.}]
      // {dbName:klijent_id,javaName:clientID,javaType:Long,modificators:[],constraints:[BU200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("clientID"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Slog sa id * više ne postoji u bazi podataka.", unpivotedClient.getValueAsLong("clientID"));
        if (unpivotedClient.getValueAsLong("clientID") != null)
        {
          CustomData<List<Client>> clientsList = this.clientDAO.select(0, 1, "clientID_" + unpivotedClient.getValueAsLong("clientID"), null, null, null);
          if (clientsList.getData().size() == 0)
          {
            checkPassed = false;
            messagesHandler.setResourceNotFound(true);
          }
        }
        if (checkPassed)
        {
          LOGGER.info("BU200: PASSED, messsage: " + preparedMessage.toString());
        }
        else
        {
          LOGGER.info("BU200: FAILED, level: ERROR, rollback: YES, stop: YES, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BU200",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter(unpivotedClient.getValueAsLong("clientID"))
          );
          messagesHandler.setProceed(false);
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 200, 300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup200_300",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedClient.getValueAsString("name") == null || "".equals(unpivotedClient.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Naziv")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1200);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup300_1200",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1200: UNIQUE-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* se već koristi.},{locale:en-us,definition:* already exists.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* se već koristi.", "Naziv");
        if (unpivotedClient.getValueAsString("name") == null ||"".equals(unpivotedClient.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU1200: SKIPPED, messsage: " + preparedMessage.toString());
          checkPassed = false;
        }
        if (checkPassed)
        {
          CustomData<List<Client>> clientsList = null;
          if (messagesHandler.proceed())
          {
            messagesHandler.addMessages(
                 "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
                  (clientsList = this.clientDAO.select(1, 1, "name_" + unpivotedClient.getValueAsString("name") + ",clientID!_" + unpivotedClient.getValueAsLong("clientID"), null, null, null)));
          }
          if (clientsList == null || clientsList.getData() == null || clientsList.getData().size() > 0)
          {
            checkPassed = false;
          }
          if (messagesHandler.proceed())
          {
            if (checkPassed)
            {
              LOGGER.info("BIU1200: PASSED, messsage: " + preparedMessage.toString());
            }
            if (messagesHandler.proceed() && checkPassed == false)
            {
              LOGGER.info("BIU1200: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
              messagesHandler.addMessage(
                "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1200",
                new CustomMessage()
                  .applicationMessages(this.applicationMessages)
                  .customMessageLevel(CustomMessageLevel.Error)
                  .markForRollback(true)
                  .parameter("Naziv")
              );
            }
          }
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1200, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1200_1300",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("gsm"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Mobitel");
        if (unpivotedClient.getValueAsString("gsm") == null || "".equals(unpivotedClient.getValueAsString("gsm").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Mobitel")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 1400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1300_1400",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:email,javaName:email,javaType:String,modificators:[],constraints:[BIU1400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("email"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Email");
        if (unpivotedClient.getValueAsString("email") == null || "".equals(unpivotedClient.getValueAsString("email").trim()))
        {
          LOGGER.info("BIU1400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1400",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Email")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1400: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1400, 1500);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1400_1500",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:oib,javaName:oib,javaType:String,modificators:[],constraints:[BIU1500],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("oib"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "OIB");
        if (unpivotedClient.getValueAsString("oib") == null || "".equals(unpivotedClient.getValueAsString("oib").trim()))
        {
          LOGGER.info("BIU1500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU1500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("OIB")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1500: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1500, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup1500_2400",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedClient.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2400",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Datum od")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU2400: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 2400, 2500);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup2400_2500",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedClient.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedClient.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.BIU2500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU2500: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 2500, 10000);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup2500_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 5000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
      
      
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 5000, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_5000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.AI2500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Success)
              .markForRollback(false)
          );
      }
      
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup5000_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.AU2500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Success)
              .markForRollback(false)
          );
      }
      
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }      
      
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.services.ClientServiceImpl.messageGroup0_10000",
          this.clientServiceExt.handleMessages(unpivotedClient, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.client.ClientServiceImpl.AD2500",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Success)
              .markForRollback(false)
          );
      }
    }

    messagesStatus.appendMessages(messagesHandler);

    return messagesStatus;
  }


  @Override
  public CustomData<Client> get(Long id)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<Client>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.get.messagesSelect",
          (fetchedRows = this.clientDAO.select(0, 1, "clientID_" + Long.toString(id), null, null, null)));
    }

    CustomData<Client> returnRow = new CustomData<Client>();

    if (fetchedRows != null)
    {
    	if (fetchedRows.getData() != null && fetchedRows.getData().size() > 0)
    	{
    		returnRow.setData(fetchedRows.getData().get(0));
    	}
    	else
    	{
    		messagesHandler.setResourceNotFound(true);
    	}
    }

    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<List<Client>> get(
  		Integer pageNumber, 
  		Integer rowPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.get.messagesBeforeSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.BeforeSelect));
    }

    CustomData<List<Client>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.get.messagesSelect",
          (fetchedRows = this.clientDAO.select(
          		pageNumber, 
          		rowPerPage, 
          		filter, 
          		sort,
          		includeColumns,
          		excludeColumns)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.get.messagesAfterSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.AfterSelect));
    }

    CustomData<List<Client>> returnRows = new CustomData<List<Client>>();

    if (fetchedRows != null)
    {
 	    returnRows.setMetadata(fetchedRows.getMetadata());
    	returnRows.setData(fetchedRows.getData());
    }

    returnRows.appendMessages(messagesHandler);

    return returnRows;
  }


  @Override
  public CustomData<Client> post(Client client)
  {
    return this.post(client, HandleTransaction.NO);
  }


  @Override
  public CustomData<Client> post(Client client, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.services.ClientServiceImpl.post.messagesBeforeInsert",
      		this.handleMessages(client, MessagesPoint.BeforeInsert));
    }

    CustomData<Long> lastId = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.services.ClientServiceImpl.post.messagesGetLastId",
          (lastId = this.clientDAO.getLastId()));
      client.setClientID(lastId.getData() + 1);
    }

    CustomData<Client> insertedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.services.ClientServiceImpl.post.messagesInsert",
          (insertedRowStatus = this.clientDAO.insert(client)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.services.ClientServiceImpl.post.messagesAfterInsert",
      		this.handleMessages(client, MessagesPoint.AfterInsert));
    }

    if (handleTransaction == HandleTransaction.YES)
    {
      if (messagesHandler.markedForRollback())
      {
        System.out.println("rollback trans");
        transactionManager.rollback(transactionStatus);
      }
      else
      {
        System.out.println("commit trans");
        transactionManager.commit(transactionStatus);
      }
    }

    if (insertedRowStatus == null)
    {
	    insertedRowStatus = new CustomData<Client>();
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<Client> put(Client client)
  {
    return this.put(client, HandleTransaction.NO);
  }


  @Override
  public CustomData<Client> put(Client client, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.put.messagesBeforeUpdate",
      		this.handleMessages(client, MessagesPoint.BeforeUpdate));
    }

    CustomData<Client> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.put.messagesUpdate",
          (updatedRowStatus = this.clientDAO.update(client.unpivot())));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.put.messagesAfterUpdate",
      		this.handleMessages(client, MessagesPoint.AfterUpdate));
    }

    CustomData<Client> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.put.messagesRefetchRow",
      		(refetchedRow = this.get(client.getClientID())));
    }

    if (handleTransaction == HandleTransaction.YES)
    {
      if (messagesHandler.markedForRollback())
      {
        transactionManager.rollback(transactionStatus);
      }
      else
      {
        transactionManager.commit(transactionStatus);
      }
    }

    CustomData<Client> returnRow = new CustomData<Client>();

    if (refetchedRow != null) returnRow.setData(refetchedRow.getData());
    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<Client> patch(Long id, JsonPatch jsonPatch)
  {
    return this.patch(id, jsonPatch, HandleTransaction.NO);
  }


  @Override
  public CustomData<Client> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    if (handleTransaction == HandleTransaction.YES)
    {
      if (messagesHandler.markedForRollback())
      {
        transactionManager.rollback(transactionStatus);
      }
      else
      {
        transactionManager.commit(transactionStatus);
      }
    }

    UnpivotedData unpivotedData = new UnpivotedData(id, jsonPatch, this.clientDAO.getDataColumnDefinitions());

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.patch.messagesBeforeUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.BeforeUpdate));
    }

    CustomData<Client> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.patch.messagesUpdate",
          (updatedRowStatus = this.clientDAO.update(unpivotedData)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.patch.messagesAfterUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.AfterUpdate));
    }

    CustomData<Client> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.client.ClientServiceImpl.patch.messagesRefetchRow",
      		(refetchedRow = this.get(id)));
    }

    CustomData<Client> returnRow = new CustomData<Client>();

    if (refetchedRow != null) returnRow.setData(refetchedRow.getData());
    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<CustomDummy> delete(Long id)
  {
    return this.delete(id, HandleTransaction.NO);
  }


  @Override
  public CustomData<CustomDummy> delete(Long id, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    if (handleTransaction == HandleTransaction.YES)
    {
      if (messagesHandler.markedForRollback())
      {
        transactionManager.rollback(transactionStatus);
      }
      else
      {
        transactionManager.commit(transactionStatus);
      }
    }

    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("clientID", "Long", id);

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.ClientServiceImpl.delete.messagesBeforeDelete",
          this.handleMessages(unpivotedData, MessagesPoint.BeforeDelete));
    }

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.ClientServiceImpl.delete.messagesDelete",
          (deletedRowStatus = this.clientDAO.delete(id)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.ClientServiceImpl.delete.messagesAfterDelete",
          this.handleMessages(unpivotedData, MessagesPoint.AfterDelete));
    }

    CustomData<CustomDummy> returnRow = new CustomData<CustomDummy>();

    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<List<Client>> deleteAndGet(CustomData<Client> requestData)
  {
    return this.deleteAndGet(requestData, HandleTransaction.NO);
  }


  @Override
  public CustomData<List<Client>> deleteAndGet(CustomData<Client> requestData, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("clientID", "Long", requestData.getData().getClientID());

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.ClientServiceImpl.deleteAndGet.messagesDelete",
          (deletedRowStatus = this.delete(requestData.getData().getClientID())));
    }

    CustomData<List<Client>> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.client.ClientServiceImpl.deleteAndGet.messagesDelete",
          (refetchedRow
              = this.get(
                  requestData.getMetadata().getPageNumber(), 
                  requestData.getMetadata().getRowsPerPage(),
                  requestData.getMetadata().getFilter(),
                  requestData.getMetadata().getSort(),
                  requestData.getMetadata().getIncludeColumns(),
                  requestData.getMetadata().getExcludeColumns())));
    }

    if (handleTransaction == HandleTransaction.YES)
    {
      if (messagesHandler.markedForRollback())
      {
        transactionManager.rollback(transactionStatus);
      }
      else
      {
        transactionManager.commit(transactionStatus);
      }
    }

    CustomData<List<Client >> returnRow = new CustomData<List<Client>>();

    if (refetchedRow != null)
    {
      returnRow.setData(refetchedRow.getData());
      returnRow.setMetadata(refetchedRow.getMetadata());
    }
    returnRow.appendMessages(messagesHandler);

    if (returnRow.getMetadata() == null) returnRow.setMetadata(new CustomMetadata());

    return returnRow;
  }


}
