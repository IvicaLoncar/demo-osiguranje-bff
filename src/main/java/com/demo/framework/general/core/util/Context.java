package com.demo.framework.general.core.util;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Context 
{
	private UserInfo userInfo = new UserInfo();
	private String sessionId = UUID.randomUUID().toString();
}
