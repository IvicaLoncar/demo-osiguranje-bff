package com.demo.framework.general.core.model;


import java.time.Instant;
import java.util.List;

import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.framework.general.core.util.UnpivotedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataValidation
{
  private Long dataValidationId;
  private String fullClassName;
  private String methodName;
  private String messagePoint;
  private Integer itemPosition;
  private String validationName;
  private String applicationMessageLevel;
  private String applicationMessageGroup;
  private String applicationMessageName;
  private String rollback;
  private Instant dateFrom;
  private Instant dateTo;
  private String active;
  private Instant createTime;
  private String crateUser;
  private Instant updateTime;
  private String updateUser;


  public DataValidation(UnpivotedData unpivotedData)
  {
    List<UnpivotedValue> unpivotedValues = null;
    if (unpivotedData != null && (unpivotedValues = unpivotedData.getValues()) != null) 
    {
      for (int i = 0; i < unpivotedValues.size(); i++)
      {
        UnpivotedValue unpivotedValue = unpivotedValues.get(i);
        while(unpivotedValue != null)
        {
          if (unpivotedValue.match("dataValidationId", "Long")) { this.dataValidationId = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("fullClassName", "String")) { this.fullClassName = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("methodName", "String")) { this.methodName = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("messagePoint", "String")) { this.messagePoint = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("itemPosition", "Integer")) { this.itemPosition = unpivotedValue.getValueAsInteger(); break;}
          if (unpivotedValue.match("validationName", "String")) { this.validationName = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("applicationMessageLevel", "String")) { this.applicationMessageLevel = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("applicationMessageGroup", "String")) { this.applicationMessageGroup = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("applicationMessageName", "String")) { this.applicationMessageName = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("rollback", "String")) { this.rollback = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("dateFrom", "Date")) { this.dateFrom = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("dateTo", "Date")) { this.dateTo = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("active", "String")) { this.active = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("createTime", "Timestamp")) { this.createTime = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("crateUser", "String")) { this.crateUser = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("updateTime", "Timestamp")) { this.updateTime = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("updateUser", "String")) { this.updateUser = unpivotedValue.getValueAsString(); break;}
          break;
        }
      }
    }
  }


  public UnpivotedData unpivot()
  {
    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("dataValidationId", "Long", this.dataValidationId);
    unpivotedData.add("fullClassName", "String", this.fullClassName);
    unpivotedData.add("methodName", "String", this.methodName);
    unpivotedData.add("messagePoint", "String", this.messagePoint);
    unpivotedData.add("itemPosition", "Integer", this.itemPosition);
    unpivotedData.add("validationName", "String", this.validationName);
    unpivotedData.add("applicationMessageLevel", "String", this.applicationMessageLevel);
    unpivotedData.add("applicationMessageGroup", "String", this.applicationMessageGroup);
    unpivotedData.add("applicationMessageName", "String", this.applicationMessageName);
    unpivotedData.add("rollback", "String", this.rollback);
    unpivotedData.add("dateFrom", "Date", this.dateFrom);
    unpivotedData.add("dateTo", "Date", this.dateTo);
    unpivotedData.add("active", "String", this.active);
    unpivotedData.add("createTime", "Timestamp", this.createTime);
    unpivotedData.add("crateUser", "String", this.crateUser);
    unpivotedData.add("updateTime", "Timestamp", this.updateTime);
    unpivotedData.add("updateUser", "String", this.updateUser);
    return unpivotedData;
  }
}
