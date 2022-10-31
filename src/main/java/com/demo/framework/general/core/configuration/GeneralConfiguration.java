package com.demo.framework.general.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Configuration
public class GeneralConfiguration 
{
	@Value("${replaceFatalMessage}")
	private boolean replaceFatalMessage;

	@Value("${replaceFatalMessageId}")
	private Long replaceFatalMessageId;

	@Value("${replaceFatalMessageText}")
	private String replaceFatalMessageText;

}
