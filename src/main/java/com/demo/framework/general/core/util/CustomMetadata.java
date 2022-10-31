package com.demo.framework.general.core.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class CustomMetadata
{
	private Integer pageNumber;
	private Integer rowsPerPage;
	private Long numberOfRows;
	private String filter;
	private String sort;
	private List<String> includeColumns;
	private List<String> excludeColumns;	
	private String domain;
}
