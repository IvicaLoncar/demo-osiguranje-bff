package com.demo.osiguranje.general.policy.services;

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
import com.demo.osiguranje.general.policy.DAO.InsurancePolicyDAO;
import com.demo.osiguranje.general.policy.model.InsurancePolicy;
import com.demo.osiguranje.general.policy.services.ext.InsurancePolicyServiceExt;


@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyServiceImpl.class);


  private List<ApplicationMessage> applicationMessages;


  @Autowired
  private DataValidationService dataValidationService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private InsurancePolicyServiceExt insurancePolicyServiceExt;

  @Autowired
  private InsurancePolicyDAO insurancePolicyDAO;



  public InsurancePolicyServiceImpl()
  {
    if (this.applicationMessages == null) this.applicationMessages = new ArrayList<ApplicationMessage>();

    // insurancePolicyID
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BU200",
        "hr-hr",
        "Slog sa id * više ne postoji u bazi podataka.");

    // insurancePolicyID
 /*   ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BU200",
        "en-us",
        "Record with id * doesnt exists in a database.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU300",
        "hr-hr",
        "* je obavezan podatak.");

    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU300",
        "en-us",
        "* is mandatory.");
*/
    // name
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU1200",
        "en-us",
        "* already exists.");

      // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2400",
        "hr-hr",
        "* je obavezan.");

    // dateFrom
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2400",
        "en-us",
        "* is mandatory.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2500",
        "hr-hr",
        "Aktivno je obavezno.");

    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2500",
        "en-us",
        "Active is mandatory.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AI2500",
        "hr-hr",
        "Slog je uspješno kreiran.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AU2500",
        "hr-hr",
        "Slog je uspješno promijenjen.");
    
    // active
    ApplicationMessageUtil.addMessage(
        this.applicationMessages,
        "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AD2500",
        "hr-hr",
        "Slog je uspješno obrisan.");
  }


  @Override
  public CustomData<CustomDummy> handleMessages(InsurancePolicy insurancePolicy, MessagesPoint messagesPoint)
  {
    return this.handleMessages(insurancePolicy.unpivot(), messagesPoint);
  }


  @Override
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedInsurancePolicy, MessagesPoint messagesPoint)
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 300);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_300",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
     /* if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedInsurancePolicy.getValueAsString("name") == null || "".equals(unpivotedInsurancePolicy.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU300",
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
      }*/

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup300_1300",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
    /*  if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("gsm"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Opis");
        if (unpivotedInsurancePolicy.getValueAsString("description") == null || "".equals(unpivotedInsurancePolicy.getValueAsString("description").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU1300",
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
      }*/

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup1300_2400",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedInsurancePolicy.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2400",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup2400_2500",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedInsurancePolicy.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2500",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup2500_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 200);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_200",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BU200: NOTFOUND-ERROR-STOP-ROLLBACK  => [{locale:hr-hr,definition:Slog sa id InsurancePolicyServiceImpl.java InsurancePolicyService.java ext više ne postoji u bazi podataka.},{locale:en-us,definition:Record with id InsurancePolicyServiceImpl.java InsurancePolicyService.java ext doesnt exists in a database.}]
      // {dbName:klijent_id,javaName:insurancePolicyID,javaType:Long,modificators:[],constraints:[BU200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("insurancePolicyID"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Slog sa id * više ne postoji u bazi podataka.", unpivotedInsurancePolicy.getValueAsLong("insurancePolicyID"));
        if (unpivotedInsurancePolicy.getValueAsLong("insurancePolicyID") != null)
        {
          CustomData<List<InsurancePolicy>> insurancePolicysList = this.insurancePolicyDAO.select(0, 1, "insurancePolicyID_" + unpivotedInsurancePolicy.getValueAsLong("insurancePolicyID"), null, null, null);
          if (insurancePolicysList.getData().size() == 0)
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
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BU200",
            new CustomMessage()
              .applicationMessages(this.applicationMessages)
              .customMessageLevel(CustomMessageLevel.Error)
              .markForRollback(true)
              .parameter(unpivotedInsurancePolicy.getValueAsLong("insurancePolicyID"))
          );
          messagesHandler.setProceed(false);
        }
      }

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 200, 300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup200_300",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:naziv,javaName:name,javaType:String,modificators:[],constraints:[BIU300,BIU1200],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
    /*  if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("name"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Naziv");
        if (unpivotedInsurancePolicy.getValueAsString("name") == null || "".equals(unpivotedInsurancePolicy.getValueAsString("name").trim()))
        {
          LOGGER.info("BIU300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU300",
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
      }*/

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 300, 1300);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup3000_1300",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU1300: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan podatak.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:mobitel,javaName:gsm,javaType:String,modificators:[],constraints:[BIU1300],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      /*if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("opis"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan podatak.", "Opis");
        if (unpivotedInsurancePolicy.getValueAsString("description") == null || "".equals(unpivotedInsurancePolicy.getValueAsString("description").trim()))
        {
          LOGGER.info("BIU1300: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU1300",
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
      }*/

      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 1300, 2400);
      if (messagesHandler.proceed() && dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup1300_2400",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2400: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:* je obavezan.},{locale:en-us,definition:* is mandatory.}]
      // {dbName:datum_od,javaName:dateFrom,javaType:Date,modificators:[],constraints:[BIU2400],dbTypeMapper:{functionName:,toJavaType:,sqlToJavaMappings:[]}}
      if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("dateFrom"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("* je obavezan.", "Datum od");
        if (unpivotedInsurancePolicy.getValueAsInstant("dateFrom") == null)
        {
          LOGGER.info("BIU2400: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2400",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup2400_2500",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }

      // BIU2500: MANDATORY-ERROR-CONTINUE-ROLLBACK  => [{locale:hr-hr,definition:Aktivno je obavezno.},{locale:en-us,definition:Active is mandatory.}]
      // {dbName:aktivno,javaName:active,javaType:Boolean,modificators:[C01],constraints:[BIU2500],dbTypeMapper:{functionName:CONVERT_STRING_TO_BOOLEAN,toJavaType:String,sqlToJavaMappings:[{javaValue:true,sqlValue:Y},{javaValue:false,sqlValue:N}]}}
      if (messagesHandler.proceed() && unpivotedInsurancePolicy.exists("active"))
      {
        checkPassed = true;
        preparedMessage = ApplicationMessageUtil.prepareMessage("Aktivno je obavezno.");
        if (unpivotedInsurancePolicy.getValueAsBoolean("active") == null)
        {
          LOGGER.info("BIU2500: FAILED, level: ERROR, rollback: YES, stop: NO, messsage: " + preparedMessage.toString());
          messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.BIU2500",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup2500_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 5000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
      
      
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 5000, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_5000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AI2500",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup5000_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AU2500",
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
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }      
      
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.messageGroup0_10000",
          this.insurancePolicyServiceExt.handleMessages(unpivotedInsurancePolicy, dataValidationBetweenPoints, messagesPoint));
      }
      
      if (messagesHandler.proceed())
      {
        messagesHandler.addMessage(
            "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.AD2500",
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
  public CustomData<InsurancePolicy> get(Long id)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<InsurancePolicy>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.get.messagesSelect",
          (fetchedRows = this.insurancePolicyDAO.select(0, 1, "insurancePolicyID_" + Long.toString(id), null, null, null)));
    }

    CustomData<InsurancePolicy> returnRow = new CustomData<InsurancePolicy>();

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
  public CustomData<List<InsurancePolicy>> get(
  		Integer pageNumber, 
  		Integer rowsPerPage, 
  		String filter, 
  		String sort,
      List<String> includeColumns,
      List<String> excludeColumns)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.get.messagesBeforeSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.BeforeSelect));
    }

    CustomData<List<InsurancePolicy>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.get.messagesSelect",
          (fetchedRows = this.insurancePolicyDAO.select(
          		pageNumber, 
          		rowsPerPage, 
          		filter, 
          		sort,
          		includeColumns,
          		excludeColumns)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.get.messagesAfterSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.AfterSelect));
    }

    CustomData<List<InsurancePolicy>> returnRows = new CustomData<List<InsurancePolicy>>();

    if (fetchedRows != null)
    {
 	    returnRows.setMetadata(fetchedRows.getMetadata());
    	returnRows.setData(fetchedRows.getData());
    }

    returnRows.appendMessages(messagesHandler);

    return returnRows;
  }


  @Override
  public CustomData<InsurancePolicy> post(InsurancePolicy insurancePolicy)
  {
    return this.post(insurancePolicy, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsurancePolicy> post(InsurancePolicy insurancePolicy, HandleTransaction handleTransaction)
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
       		"com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.post.messagesBeforeInsert",
      		this.handleMessages(insurancePolicy, MessagesPoint.BeforeInsert));
    }

    CustomData<Long> lastId = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.post.messagesGetLastId",
          (lastId = this.insurancePolicyDAO.getLastId()));
      insurancePolicy.setInsurancePolicyID(lastId.getData() + 1);
    }

    CustomData<InsurancePolicy> insertedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.post.messagesInsert",
          (insertedRowStatus = this.insurancePolicyDAO.insert(insurancePolicy)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.services.InsurancePolicyServiceImpl.post.messagesAfterInsert",
      		this.handleMessages(insurancePolicy, MessagesPoint.AfterInsert));
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
	    insertedRowStatus = new CustomData<InsurancePolicy>();
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<InsurancePolicy> put(InsurancePolicy insurancePolicy)
  {
    return this.put(insurancePolicy, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsurancePolicy> put(InsurancePolicy insurancePolicy, HandleTransaction handleTransaction)
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
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.put.messagesBeforeUpdate",
      		this.handleMessages(insurancePolicy, MessagesPoint.BeforeUpdate));
    }

    CustomData<InsurancePolicy> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.put.messagesUpdate",
          (updatedRowStatus = this.insurancePolicyDAO.update(insurancePolicy.unpivot())));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.put.messagesAfterUpdate",
      		this.handleMessages(insurancePolicy, MessagesPoint.AfterUpdate));
    }

    CustomData<InsurancePolicy> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.put.messagesRefetchRow",
      		(refetchedRow = this.get(insurancePolicy.getInsurancePolicyID())));
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

    CustomData<InsurancePolicy> returnRow = new CustomData<InsurancePolicy>();

    if (refetchedRow != null) returnRow.setData(refetchedRow.getData());
    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<InsurancePolicy> patch(Long id, JsonPatch jsonPatch)
  {
    return this.patch(id, jsonPatch, HandleTransaction.NO);
  }


  @Override
  public CustomData<InsurancePolicy> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction)
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

    UnpivotedData unpivotedData = new UnpivotedData(id, jsonPatch, this.insurancePolicyDAO.getDataColumnDefinitions());

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.patch.messagesBeforeUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.BeforeUpdate));
    }

    CustomData<InsurancePolicy> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.patch.messagesUpdate",
          (updatedRowStatus = this.insurancePolicyDAO.update(unpivotedData)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.patch.messagesAfterUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.AfterUpdate));
    }

    CustomData<InsurancePolicy> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.patch.messagesRefetchRow",
      		(refetchedRow = this.get(id)));
    }

    CustomData<InsurancePolicy> returnRow = new CustomData<InsurancePolicy>();

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
    unpivotedData.add("insurancePolicyID", "Long", id);

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.delete.messagesBeforeDelete",
          this.handleMessages(unpivotedData, MessagesPoint.BeforeDelete));
    }

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.delete.messagesDelete",
          (deletedRowStatus = this.insurancePolicyDAO.delete(id)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.delete.messagesAfterDelete",
          this.handleMessages(unpivotedData, MessagesPoint.AfterDelete));
    }

    CustomData<CustomDummy> returnRow = new CustomData<CustomDummy>();

    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<List<InsurancePolicy>> deleteAndGet(CustomData<InsurancePolicy> requestData)
  {
    return this.deleteAndGet(requestData, HandleTransaction.NO);
  }


  @Override
  public CustomData<List<InsurancePolicy>> deleteAndGet(CustomData<InsurancePolicy> requestData, HandleTransaction handleTransaction)
  {
    TransactionStatus transactionStatus = null;

    MessagesHandler messagesHandler = new MessagesHandler();

    if (handleTransaction == HandleTransaction.YES)
    {
      DefaultTransactionDefinition dtt = new DefaultTransactionDefinition();
      transactionStatus = this.transactionManager.getTransaction(dtt);
    }

    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("insurancePolicyID", "Long", requestData.getData().getInsurancePolicyID());

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.deleteAndGet.messagesDelete",
          (deletedRowStatus = this.delete(requestData.getData().getInsurancePolicyID())));
    }

    CustomData<List<InsurancePolicy>> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.demo.osiguranje.general.insurancePolicy.InsurancePolicyServiceImpl.deleteAndGet.messagesDelete",
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

    CustomData<List<InsurancePolicy >> returnRow = new CustomData<List<InsurancePolicy>>();

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
