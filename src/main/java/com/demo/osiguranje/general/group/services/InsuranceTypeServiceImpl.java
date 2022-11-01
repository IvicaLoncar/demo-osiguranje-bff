package com.demo.osiguranje.general.group.services;

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
import com.demo.osiguranje.general.group.DAO.InsuranceTypeDAO;
import com.demo.osiguranje.general.group.model.InsuranceType;
import com.demo.osiguranje.general.group.services.ext.InsuranceTypeServiceExt;


@Service
public class InsuranceTypeServiceImpl implements InsuranceTypeService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceTypeServiceImpl.class);


  private List<ApplicationMessage> applicationMessages;


  @Autowired
  private DataValidationService dataValidationService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private InsuranceTypeServiceExt insuranceTypeServiceExt;

  @Autowired
  private InsuranceTypeDAO insuranceTypeDAO;



  public InsuranceTypeServiceImpl()
  {
    if (this.applicationMessages == null) this.applicationMessages = new ArrayList<ApplicationMessage>();

    // insuranceTypeID
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BU200",
        "hr-hr",
        "Slog sa id * više ne postoji u bazi podataka.");

    // insuranceTypeID
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BU200",
        "en-us",
        "Record with id * doesnt exists in a database.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU300",
        "hr-hr",
        "* je obavezan podatak.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU300",
        "en-us",
        "* is mandatory.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU1200",
        "en-us",
        "* already exists.");

      // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2400",
        "hr-hr",
        "* je obavezan.");

    // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2400",
        "en-us",
        "* is mandatory.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2500",
        "hr-hr",
        "Aktivno je obavezno.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2500",
        "en-us",
        "Active is mandatory.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AI2500",
        "hr-hr",
        "Slog je uspješno kreiran.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AU2500",
        "hr-hr",
        "Slog je uspješno promijenjen.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AD2500",
        "hr-hr",
        "Slog je uspješno obrisan.");
  }


  @Override
  public CustomData<CustomDummy> handleMessages(InsuranceType insuranceType, MessagesPoint messagesPoint)
  {
    return this.handleMessages(insuranceType.unpivot(), messagesPoint);
  }


  @Override
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsuranceType, MessagesPoint messagesPoint)
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 300);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_300",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedInsuranceType.getValueAsString("name") == null || "".equals(unpivotedInsuranceType.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU300",
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

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup300_1300",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("gsm"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Opis");
        if (unpivotedInsuranceType.getValueAsString("description") == null || "".equals(unpivotedInsuranceType.getValueAsString("description").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU1300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Opis")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup1300_2400",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedInsuranceType.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2400",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup2400_2500",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedInsuranceType.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2500",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup2500_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 200);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_200",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BU200: NOTFOUND-ERROR-STOP-ROLLBACK  => [{locale:hr-hr,definition:Slog sa id InsuranceTypeServiceImpl.java InsuranceTypeService.java ext više ne postoji u bazi podataka.},{locale:en-us,definition:Record with id InsuranceTypeServiceImpl.java InsuranceTypeService.java ext doesnt exists in a database.}]
      // {dbName:klijent_id,javaName:insuranceTypeID,javaType:Long,modificators:[],constraints:[BU200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("insuranceTypeID"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Slog sa id * više ne postoji u bazi podataka.", unpivotedInsuranceType.getValueAsLong("insuranceTypeID"));
        if (unpivotedInsuranceType.getValueAsLong("insuranceTypeID") != null)
        {
          CustomData<List<InsuranceType>> insuranceTypesList = this.insuranceTypeDAO.select(0, 1, "insuranceTypeID_" + unpivotedInsuranceType.getValueAsLong("insuranceTypeID"), null, new ArrayList<String>(), new ArrayList<String>());
          if (insuranceTypesList.getData().size() == 0)
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
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BU200",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter(unpivotedInsuranceType.getValueAsLong("insuranceTypeID"))
          );
          messagesHandler.setProceed(false);
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 200, 300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup200_300",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedInsuranceType.getValueAsString("name") == null || "".equals(unpivotedInsuranceType.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU300",
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

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup3000_1300",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("opis"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Opis");
        if (unpivotedInsuranceType.getValueAsString("description") == null || "".equals(unpivotedInsuranceType.getValueAsString("description").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU1300",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter("Opis")
          );
          checkPassed = false;
        }
        if (messagesHandler.proceed() && checkPassed)
        {
          LOGGER.info("BIU1300: PASSED, messsage: " + preparedMessage.toString());
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup1300_2400",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedInsuranceType.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2400",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup2400_2500",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedInsuranceType.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedInsuranceType.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.BIU2500",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup2500_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 5000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
      
      
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 5000, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_5000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AI2500",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup5000_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AU2500",
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
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }      
      
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.messageGroup0_10000",
          this.insuranceTypeServiceExt.handleMessages(unpivotedInsuranceType, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.AD2500",
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
  public CustomData<InsuranceType> get(Long id)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<InsuranceType>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.get.messagesSelect",
          (fetchedRows = this.insuranceTypeDAO.select(0, 1, "insuranceTypeID_" + Long.toString(id), null, new ArrayList<String>(), new ArrayList<String>())));
    }

    CustomData<InsuranceType> returnRow = new CustomData<InsuranceType>();

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
  public CustomData<List<InsuranceType>> get(
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
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.get.messagesBeforeSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.BeforeSelect));
    }

    CustomData<List<InsuranceType>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.get.messagesSelect",
          (fetchedRows = this.insuranceTypeDAO.select(pageNumber, rowPerPage, filter, sort, includeColumns, excludeColumns)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.get.messagesAfterSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.AfterSelect));
    }

    CustomData<List<InsuranceType>> returnRows = new CustomData<List<InsuranceType>>();

    if (fetchedRows != null)
    {
 	    returnRows.setMetadata(fetchedRows.getMetadata());
    	returnRows.setData(fetchedRows.getData());
    }

    returnRows.appendMessages(messagesHandler);

    return returnRows;
  }


  @Override
  public CustomData<InsuranceType> post(InsuranceType insuranceType)
  {
    return this.post(insuranceType, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsuranceType> post(InsuranceType insuranceType, HandleTransaction handleTransaction)
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
       		"com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.post.messagesBeforeInsert",
      		this.handleMessages(insuranceType, MessagesPoint.BeforeInsert));
    }

    CustomData<Long> lastId = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.post.messagesGetLastId",
          (lastId = this.insuranceTypeDAO.getLastId()));
      insuranceType.setInsuranceTypeID(lastId.getData() + 1);
    }

    CustomData<InsuranceType> insertedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.post.messagesInsert",
          (insertedRowStatus = this.insuranceTypeDAO.insert(insuranceType)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.services.InsuranceTypeServiceImpl.post.messagesAfterInsert",
      		this.handleMessages(insuranceType, MessagesPoint.AfterInsert));
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
	    insertedRowStatus = new CustomData<InsuranceType>();
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<InsuranceType> put(InsuranceType insuranceType)
  {
    return this.put(insuranceType, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsuranceType> put(InsuranceType insuranceType, HandleTransaction handleTransaction)
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
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.put.messagesBeforeUpdate",
      		this.handleMessages(insuranceType, MessagesPoint.BeforeUpdate));
    }

    CustomData<InsuranceType> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.put.messagesUpdate",
          (updatedRowStatus = this.insuranceTypeDAO.update(insuranceType.unpivot())));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.put.messagesAfterUpdate",
      		this.handleMessages(insuranceType, MessagesPoint.AfterUpdate));
    }

    CustomData<InsuranceType> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.put.messagesRefetchRow",
      		(refetchedRow = this.get(insuranceType.getInsuranceTypeID())));
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

    CustomData<InsuranceType> returnRow = new CustomData<InsuranceType>();

    if (refetchedRow != null) returnRow.setData(refetchedRow.getData());
    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<InsuranceType> patch(Long id, JsonPatch jsonPatch)
  {
    return this.patch(id, jsonPatch, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsuranceType> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction)
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

    UnpivotedData unpivotedData = new UnpivotedData(id, jsonPatch, this.insuranceTypeDAO.getDataColumnDefinitions());

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.patch.messagesBeforeUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.BeforeUpdate));
    }

    CustomData<InsuranceType> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.patch.messagesUpdate",
          (updatedRowStatus = this.insuranceTypeDAO.update(unpivotedData)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.patch.messagesAfterUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.AfterUpdate));
    }

    CustomData<InsuranceType> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.patch.messagesRefetchRow",
      		(refetchedRow = this.get(id)));
    }

    CustomData<InsuranceType> returnRow = new CustomData<InsuranceType>();

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
    unpivotedData.add("insuranceTypeID", "Long", id);

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.delete.messagesBeforeDelete",
          this.handleMessages(unpivotedData, MessagesPoint.BeforeDelete));
    }

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.delete.messagesDelete",
          (deletedRowStatus = this.insuranceTypeDAO.delete(id)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.delete.messagesAfterDelete",
          this.handleMessages(unpivotedData, MessagesPoint.AfterDelete));
    }

    CustomData<CustomDummy> returnRow = new CustomData<CustomDummy>();

    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<List<InsuranceType>> deleteAndGet(CustomData<InsuranceType> requestData)
  {
    return this.deleteAndGet(requestData, HandleTransaction.NO);
  }


  @Override
  public CustomData<List<InsuranceType>> deleteAndGet(CustomData<InsuranceType> requestData, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("insuranceTypeID", "Long", requestData.getData().getInsuranceTypeID());

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.deleteAndGet.messagesDelete",
          (deletedRowStatus = this.delete(requestData.getData().getInsuranceTypeID())));
    }

    CustomData<List<InsuranceType>> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insuranceType.InsuranceTypeServiceImpl.deleteAndGet.messagesDelete",
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

    CustomData<List<InsuranceType >> returnRow = new CustomData<List<InsuranceType>>();

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
