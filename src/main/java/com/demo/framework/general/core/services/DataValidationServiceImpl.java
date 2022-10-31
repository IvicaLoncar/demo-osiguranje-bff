package com.demo.framework.general.core.services;

import com.demo.framework.general.core.DAO.DataValidationDAO;
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
import com.demo.framework.general.core.util.DataValidationUtil;
import com.demo.framework.general.core.util.HandleTransaction;
import com.demo.framework.general.core.util.MessagesHandler;
import com.demo.framework.general.core.util.UnpivotedData;
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


@Service
public class DataValidationServiceImpl implements DataValidationService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DataValidationServiceImpl.class);


  private List<ApplicationMessage> applicationMessages;


  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private DataValidationExtService dataValidationExtService;

  @Autowired
  private DataValidationDAO dataValidationDAO;



  public DataValidationServiceImpl()
  {
    if (this.applicationMessages == null) this.applicationMessages = new ArrayList<ApplicationMessage>();

  }


  @Override
  public CustomData<CustomDummy> handleMessages(DataValidation dataValidation, MessagesPoint messagesPoint)
  {
    return this.handleMessages(dataValidation.unpivot(), messagesPoint);
  }


  @Override
  public CustomData<CustomDummy> handleMessages(UnpivotedData unpivotedDataValidation, MessagesPoint messagesPoint)
  {
    CustomData<CustomDummy> messagesStatus = new CustomData<CustomDummy>();

   /* MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<DataValidation>> dataValidations = this.get(1, 1000, null, null);
    List<DataValidation> dataValidationBetweenPoints = null;

    StringBuilder preparedMessage = null;

    boolean checkPassed = true;

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.BeforeDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterSelect)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterInsert)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterUpdate)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    if (messagesHandler.proceed() && messagesPoint == MessagesPoint.AfterDelete)
    {
      dataValidationBetweenPoints = DataValidationUtil.extract(dataValidations.getData(), messagesPoint, 0, 10000);
      if (dataValidationBetweenPoints.size() > 0)
      {
        messagesHandler.addMessages(
          "com.example.demo.general.framework.services.DataValidationServiceImpl.messageGroup0_10000",
          this.dataValidationExtService.handleExtMessages(unpivotedDataValidation, dataValidationBetweenPoints, messagesPoint));
      }
    }

    messagesStatus.appendMessages(messagesHandler);*/

    return messagesStatus;
  }


  @Override
  public CustomData<DataValidation> get(Long id)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    CustomData<List<DataValidation>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.get.messagesSelect",
          (fetchedRows = this.dataValidationDAO.select(1, 1, "dataValidationID_" + Long.toString(id), null)));
    }

    CustomData<DataValidation> returnRow = new CustomData<DataValidation>();

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
  public CustomData<List<DataValidation>> get(Integer pageNumber, Integer rowPerPage, String filter, String sort)
  {
    MessagesHandler messagesHandler = new MessagesHandler();

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.get.messagesBeforeSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.BeforeSelect));
    }

    CustomData<List<DataValidation>> fetchedRows = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.get.messagesSelect",
          (fetchedRows = this.dataValidationDAO.select(pageNumber, rowPerPage, filter, sort)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.get.messagesAfterSelect",
      		this.handleMessages(new UnpivotedData(), MessagesPoint.AfterSelect));
    }

    CustomData<List<DataValidation>> returnRows = new CustomData<List<DataValidation>>();

    if (fetchedRows != null) returnRows.setData(fetchedRows.getData());

    returnRows.appendMessages(messagesHandler);

    return returnRows;
  }


  @Override
  public CustomData<DataValidation> post(DataValidation dataValidation)
  {
    return this.post(dataValidation, HandleTransaction.NO);
  }


  @Override
  public CustomData<DataValidation> post(DataValidation dataValidation, HandleTransaction handleTransaction)
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
       		"com.example.demo.general.framework.services.DataValidationServiceImpl.post.messagesBeforeInsert",
      		this.handleMessages(dataValidation, MessagesPoint.BeforeInsert));
    }

    CustomData<Long> lastId = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.services.DataValidationServiceImpl.post.messagesGetLastId",
          (lastId = this.dataValidationDAO.getLastId()));
      dataValidation.setDataValidationId(lastId.getData() + 1);
    }

    CustomData<DataValidation> insertedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.services.DataValidationServiceImpl.post.messagesInsert",
          (insertedRowStatus = this.dataValidationDAO.insert(dataValidation)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.services.DataValidationServiceImpl.post.messagesAfterInsert",
      		this.handleMessages(dataValidation, MessagesPoint.AfterInsert));
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
	    insertedRowStatus = new CustomData<DataValidation>();
    }

    insertedRowStatus.appendMessages(messagesHandler);

    return insertedRowStatus;
  }


  @Override
  public CustomData<DataValidation> put(DataValidation dataValidation)
  {
    return this.put(dataValidation, HandleTransaction.NO);
  }


  @Override
  public CustomData<DataValidation> put(DataValidation dataValidation, HandleTransaction handleTransaction)
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
       		"com.example.demo.general.framework.DataValidationServiceImpl.put.messagesBeforeUpdate",
      		this.handleMessages(dataValidation, MessagesPoint.BeforeUpdate));
    }

    CustomData<DataValidation> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.put.messagesUpdate",
          (updatedRowStatus = this.dataValidationDAO.update(dataValidation.unpivot())));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.put.messagesAfterUpdate",
      		this.handleMessages(dataValidation, MessagesPoint.AfterUpdate));
    }

    CustomData<DataValidation> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.put.messagesRefetchRow",
      		(refetchedRow = this.get(dataValidation.getDataValidationId())));
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

    CustomData<DataValidation> returnRow = new CustomData<DataValidation>();

    if (refetchedRow != null) returnRow.setData(refetchedRow.getData());
    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


  @Override
  public CustomData<DataValidation> patch(Long id, JsonPatch jsonPatch)
  {
    return this.patch(id, jsonPatch, HandleTransaction.NO);
  }


  @Override
  public CustomData<DataValidation> patch(Long id, JsonPatch jsonPatch, HandleTransaction handleTransaction)
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

    UnpivotedData unpivotedData = new UnpivotedData(id, jsonPatch, this.dataValidationDAO.getDataColumnDefinitions());

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.patch.messagesBeforeUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.BeforeUpdate));
    }

    CustomData<DataValidation> updatedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.patch.messagesUpdate",
          (updatedRowStatus = this.dataValidationDAO.update(unpivotedData)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.patch.messagesAfterUpdate",
      		this.handleMessages(unpivotedData, MessagesPoint.AfterUpdate));
    }

    CustomData<DataValidation> refetchedRow = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
       		"com.example.demo.general.framework.DataValidationServiceImpl.patch.messagesRefetchRow",
      		(refetchedRow = this.get(id)));
    }

    CustomData<DataValidation> returnRow = new CustomData<DataValidation>();

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
    unpivotedData.add("dataValidationId", "Long", id);

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.example.demo.general.framework.DataValidationServiceImpl.delete.messagesBeforeDelete",
          this.handleMessages(unpivotedData, MessagesPoint.BeforeDelete));
    }

    CustomData<CustomDummy> deletedRowStatus = null;

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.example.demo.general.framework.DataValidationServiceImpl.delete.messagesDelete",
          (deletedRowStatus = this.dataValidationDAO.delete(id)));
    }

    if (messagesHandler.proceed() && messagesHandler.markedForRollback() == false)
    {
      messagesHandler.addMessages(
          "com.example.demo.general.framework.DataValidationServiceImpl.delete.messagesAfterDelete",
          this.handleMessages(unpivotedData, MessagesPoint.AfterDelete));
    }

    CustomData<CustomDummy> returnRow = new CustomData<CustomDummy>();

    returnRow.appendMessages(messagesHandler);

    return returnRow;
  }


}
