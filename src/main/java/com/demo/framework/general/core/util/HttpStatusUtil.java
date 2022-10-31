package com.demo.framework.general.core.util;

import org.springframework.http.HttpStatus;

import com.demo.framework.general.core.enums.OperationStatus;

public class HttpStatusUtil 
{


	public static HttpStatus resolveGet(CustomData<?> data)
	{
		HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
		
		if (data != null)
		{
	  	httpStatus 
				= (data.isInternalServerError() 
					? httpStatus = HttpStatus.INTERNAL_SERVER_ERROR 
					: (data.isResourceNotFound() 
							? HttpStatus.NOT_FOUND
							: HttpStatus.OK));
		}
		return httpStatus;
	}

	
	public static HttpStatus resolvePost(CustomData<?> data)
	{
		HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
		
		if (data != null)
		{
	  	httpStatus 
				= (data.isInternalServerError() 
					? httpStatus = HttpStatus.INTERNAL_SERVER_ERROR 
					: (data.getData() == null 
							? HttpStatus.CONFLICT
							: HttpStatus.CREATED));
		}
		return httpStatus;
	}

	
	public static HttpStatus resolvePut(CustomData<?> data)
	{
		HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
		
		if (data != null)
		{
	  	httpStatus 
				= (data.isInternalServerError() 
					? httpStatus = HttpStatus.INTERNAL_SERVER_ERROR 
					: (data.isResourceNotFound() 
							? HttpStatus.NOT_FOUND
							: (data.getData() == null
							    ? HttpStatus.CONFLICT 
							    : HttpStatus.OK)));
		}
		return httpStatus;
	}

	
	public static HttpStatus resolvePatch(CustomData<?> data)
	{
		HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
		
		if (data != null)
		{
	  	httpStatus 
				= (data.isInternalServerError() 
					? httpStatus = HttpStatus.INTERNAL_SERVER_ERROR 
					: (data.isResourceNotFound() 
							? HttpStatus.NOT_FOUND
							: (data.isPatchContentMissing()
									? HttpStatus.BAD_REQUEST
									: (data.getData() == null
									    ? HttpStatus.CONFLICT 
									    : HttpStatus.OK))));
		}
		return httpStatus;
	}

	
	public static HttpStatus resolveDelete(CustomData<?> data)
	{
		HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
		
		if (data != null)
		{
	  	httpStatus 
				= (data.isInternalServerError() 
					? httpStatus = HttpStatus.INTERNAL_SERVER_ERROR 
					: (data.isResourceNotFound() 
							? HttpStatus.NOT_FOUND
							: HttpStatus.OK));
		}
		return httpStatus;
	}

	
}
