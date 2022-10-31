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
public class ApplicationMessage
{
  private Long applicationMessageId;
  private String group;
  private String name;
  private String locale;
  private String messageText;
  private Instant dateFrom;
  private Instant dateTo;
  private String active;
  private Instant createDate;
  private String crateUser;
  private Instant updateDate;
  private String updateUser;


  public ApplicationMessage(UnpivotedData unpivotedData)
  {
    List<UnpivotedValue> unpivotedValues = null;
    if (unpivotedData != null && (unpivotedValues = unpivotedData.getValues()) != null) 
    {
      for (int i = 0; i < unpivotedValues.size(); i++)
      {
        UnpivotedValue unpivotedValue = unpivotedValues.get(i);
        while(unpivotedValue != null)
        {
          if (unpivotedValue.match("applicationMessageId", "Long")) { this.applicationMessageId = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("group", "String")) { this.group = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("name", "String")) { this.name = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("locale", "String")) { this.locale = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("messageText", "String")) { this.messageText = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("dateFrom", "Date")) { this.dateFrom = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("dateTo", "Date")) { this.dateTo = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("active", "String")) { this.active = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("createDate", "Timestamp")) { this.createDate = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("crateUser", "String")) { this.crateUser = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("updateDate", "Timestamp")) { this.updateDate = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("updateUser", "String")) { this.updateUser = unpivotedValue.getValueAsString(); break;}
          break;
        }
      }
    }
  }


  public UnpivotedData unpivot()
  {
    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("applicationMessageId", "Long", this.applicationMessageId);
    unpivotedData.add("group", "String", this.group);
    unpivotedData.add("name", "String", this.name);
    unpivotedData.add("locale", "String", this.locale);
    unpivotedData.add("messageText", "String", this.messageText);
    unpivotedData.add("dateFrom", "Date", this.dateFrom);
    unpivotedData.add("dateTo", "Date", this.dateTo);
    unpivotedData.add("active", "String", this.active);
    unpivotedData.add("createDate", "Timestamp", this.createDate);
    unpivotedData.add("crateUser", "String", this.crateUser);
    unpivotedData.add("updateDate", "Timestamp", this.updateDate);
    unpivotedData.add("updateUser", "String", this.updateUser);
    return unpivotedData;
  }
}
