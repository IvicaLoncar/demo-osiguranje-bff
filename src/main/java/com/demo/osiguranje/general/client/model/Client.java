package com.demo.osiguranje.general.client.model;


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
public class Client
{
  private Long clientID;
  private String clientType;
  private String name;
  private String gsm;
  private String email;
  private String oib;
  private String mbr;
  private Instant dateFrom;
  private Instant dateTo;
  private Boolean active;
  private Instant createDate;
  private String createUser;
  private Instant updateDate;
  private String updateUser;


  public Client(UnpivotedData unpivotedData)
  {
    List<UnpivotedValue> unpivotedValues = null;
    if (unpivotedData != null && (unpivotedValues = unpivotedData.getValues()) != null) 
    {
      for (int i = 0; i < unpivotedValues.size(); i++)
      {
        UnpivotedValue unpivotedValue = unpivotedValues.get(i);
        while(unpivotedValue != null)
        {
          if (unpivotedValue.match("clientID", "Long")) { this.clientID = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("clientType", "String")) { this.clientType = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("name", "String")) { this.name = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("gsm", "String")) { this.gsm = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("email", "String")) { this.email = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("oib", "String")) { this.oib = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("mbr", "String")) { this.mbr = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("dateFrom", "Date")) { this.dateFrom = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("dateTo", "Date")) { this.dateTo = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("active", "Boolean")) { this.active = unpivotedValue.getValueAsBoolean(); break;}
          if (unpivotedValue.match("createDate", "Timestamp")) { this.createDate = unpivotedValue.getValueAsInstant(); break;}
          if (unpivotedValue.match("createUser", "String")) { this.createUser = unpivotedValue.getValueAsString(); break;}
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
    unpivotedData.add("clientID", "Long", this.clientID);
    unpivotedData.add("clientType", "String", this.clientType);
    unpivotedData.add("name", "String", this.name);
    unpivotedData.add("gsm", "String", this.gsm);
    unpivotedData.add("email", "String", this.email);
    unpivotedData.add("oib", "String", this.oib);
    unpivotedData.add("mbr", "String", this.mbr);
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
