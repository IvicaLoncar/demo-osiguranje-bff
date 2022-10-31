package com.demo.osiguranje.general.group.model;


import java.time.Instant;
import java.util.List;

import com.demo.framework.general.core.util.UnpivotedData;
import com.demo.framework.general.core.util.UnpivotedValue;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceType
{
  private Long insuranceTypeID;
  private String group;
  private String name;
  private String description;
  private Instant dateFrom;
  private Instant dateTo;
  private Boolean active;
  private Instant createDate;
  private String createUser;
  private Instant updateDate;
  private String updateUser;


  public InsuranceType(UnpivotedData unpivotedData)
  {
    List<UnpivotedValue> unpivotedValues = null;
    if (unpivotedData != null && (unpivotedValues = unpivotedData.getValues()) != null) 
    {
      for (int i = 0; i < unpivotedValues.size(); i++)
      {
        UnpivotedValue unpivotedValue = unpivotedValues.get(i);
        while(unpivotedValue != null)
        {
          if (unpivotedValue.match("insuranceTypeID", "Long")) { this.insuranceTypeID = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("group", "String")) { this.group = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("name", "String")) { this.name = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("description", "String")) { this.description = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("dateFrom", "Instant")) { this.dateFrom = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("dateTo", "Instant")) { this.dateTo = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("active", "Boolean")) { this.active = unpivotedValue.getValueAsBoolean(); break;}
          if (unpivotedValue.match("createDate", "Instant")) { this.createDate = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("createUser", "String")) { this.createUser = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("updateDate", "Instant")) { this.updateDate = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("updateUser", "String")) { this.updateUser = unpivotedValue.getValueAsString(); break;}
          break;
        }
      }
    }
  }


  public UnpivotedData unpivot()
  {
    UnpivotedData unpivotedData = new UnpivotedData();
    unpivotedData.add("insuranceTypeID", "Long", this.insuranceTypeID);
    unpivotedData.add("group", "String", this.group);
    unpivotedData.add("name", "String", this.name);
    unpivotedData.add("description", "String", this.description);
    unpivotedData.add("dateFrom", "Date", this.dateFrom);
    unpivotedData.add("dateTo", "Date", this.dateTo);
    unpivotedData.add("active", "Boolean", this.active);
    unpivotedData.add("createDate", "Timestamp", this.createDate);
    unpivotedData.add("createUser", "String", this.createUser);
    unpivotedData.add("updateDate", "Timestamp", this.updateDate);
    unpivotedData.add("updateUser", "String", this.updateUser);
    return unpivotedData;
  }
}
