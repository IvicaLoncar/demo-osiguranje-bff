package com.demo.framework.general.core.util;


import com.demo.framework.general.core.model.ApplicationMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FatalMessageReplacementInfo
{
	private ApplicationMessage replacementMessage;
	private boolean replaceFatalMessage;
}
