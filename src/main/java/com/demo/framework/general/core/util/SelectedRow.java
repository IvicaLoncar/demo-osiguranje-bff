package com.demo.framework.general.core.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SelectedRow 
{
	private Integer direction;  // 0 - from start, 1 - from end
	private Integer offset;
	private List<DataFieldValue> fields;
}
