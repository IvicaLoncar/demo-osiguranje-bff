package com.demo.framework.general.core.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestCallSelection 
{
	private String selectionUUID;
	private List<DataFieldValue> selectionFields;
	private List<DataSelectionCondition> orConditions;	
	private Boolean multiRowSelect;
	private List<SelectedRow> selectedRows;
}
