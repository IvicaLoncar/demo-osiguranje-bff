package com.demo.osiguranje.general.policy.model;


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
public class InsurancePolicy
{
  private Long insurancePolicyID;
  private Long insuranceTypeID;
  private String insuranceTypeName;
  private Long clientIDPolicyHolder;
  private String clientNamePolicyHolder;
  private Long clientIDInsured;
  private String clientNameInsured;
  private Instant dateFrom;
  private Instant dateTo;
  private Boolean active;
  private Instant createDate;
  private String createUser;
  private Instant updateDate;
  private String updateUser;


  public InsurancePolicy(UnpivotedData unpivotedData)
  {
    List<UnpivotedValue> unpivotedValues = null;
    if (unpivotedData != null && (unpivotedValues = unpivotedData.getValues()) != null) 
    {
      for (int i = 0; i < unpivotedValues.size(); i++)
      {
        UnpivotedValue unpivotedValue = unpivotedValues.get(i);
        while(unpivotedValue != null)
        {
          if (unpivotedValue.match("insurancePolicyID", "Long")) { this.insurancePolicyID = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("insuranceTypeID", "Long")) { this.insuranceTypeID = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("insuranceTypeName", "String")) { this.insuranceTypeName = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("clientIDPolicyHolder", "Long")) { this.clientIDPolicyHolder = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("clientNamePolicyHolder", "String")) { this.clientNamePolicyHolder = unpivotedValue.getValueAsString(); break;}
          if (unpivotedValue.match("clientIDInsured", "Long")) { this.clientIDInsured = unpivotedValue.getValueAsLong(); break;}
          if (unpivotedValue.match("clientNameInsured", "String")) { this.clientNameInsured = unpivotedValue.getValueAsString(); break;}
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
    unpivotedData.add("insurancePolicyID", "Long", this.insurancePolicyID);
    unpivotedData.add("insuranceTypeID", "Long", this.insuranceTypeID);
    unpivotedData.add("insuranceTypeName", "String", this.insuranceTypeName);
    unpivotedData.add("clientIDPolicyHolder", "Long", this.clientIDPolicyHolder);
    unpivotedData.add("clientNamePolicyHolder", "String", this.clientNamePolicyHolder);
    unpivotedData.add("clientIDInsured", "Long", this.clientIDInsured);
    unpivotedData.add("clientNameInsured", "String", this.clientNameInsured);
    unpivotedData.add("dateFrom", "Instant", this.dateFrom);
    unpivotedData.add("dateTo", "Instant", this.dateTo);
    unpivotedData.add("active", "Boolean", this.active);
    unpivotedData.add("createDate", "Instant", this.createDate);
    unpivotedData.add("createUser", "String", this.createUser);
    unpivotedData.add("updateDate", "Instant", this.updateDate);
    unpivotedData.add("updateUser", "String", this.updateUser);
    return unpivotedData;
  }
}
