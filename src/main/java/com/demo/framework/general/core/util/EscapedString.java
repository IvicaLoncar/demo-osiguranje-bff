package com.demo.framework.general.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EscapedString
{
	private int position;
	private String replaced;
}
