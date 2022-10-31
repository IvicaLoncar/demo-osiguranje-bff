package com.demo.osiguranje.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingFilterConfig 
{

	/*@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
	    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	    //loggingFilter.setIncludeClientInfo(true);
	    loggingFilter.setIncludeQueryString(true);
	    //loggingFilter.setIncludePayload(true);
	    //loggingFilter.setIncludeHeaders(true);
	    loggingFilter.setMaxPayloadLength(64000);
	    return loggingFilter;
	}*/
	
	
	@Bean
	public RequestResponseFilter requestResponseLoggingFilter() 
	{
	    return new RequestResponseFilter();
	}
	
}