package com.demo.framework.general.core.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataSelectionCondition 
{	
	private Integer order;
	private String operator;
	private String senderUUID;
	private String senderStoreName;
	private String senderDomain;
	private String senderFieldName;
	private String recieverUUID;
	private String receiverStoreName;
	private String receiverDomain;
	private String recieverFieldName;
	private String fieldValue;
	private String subNodesOperator;
	private List<DataSelectionCondition> subNodes;
	private Boolean editable;
	private Integer selectionMethod;  // 0 - from start, 1 - from end
	private Integer selectionOffset;
	private String dataContextSelectionLocalUUID;
}
