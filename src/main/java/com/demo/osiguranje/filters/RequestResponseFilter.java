package com.demo.osiguranje.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;



@Component
public class RequestResponseFilter extends AbstractRequestLoggingFilter 
{

    @Value("${requestResponse.logging.shouldLog:false}")
    private boolean shouldLog;

    @Value("${requestResponse.logging.includeClientInfo:true}")
    private boolean includeClientInfo;

    @Value("${requestResponse.logging.includeHeaders:true}")
    private boolean includeHeaders;

    @Value("#{'${requestResponse.logging.excludedHeaders:}'.split(',')}")
    private String[] excludedHeaders;

    @Value("${requestResponse.logging.includePayload:true}")
    private boolean includePayload;

    @Value("${requestResponse.logging.includeQueryString:true}")
    private boolean includeQueryString;

    @Value("${requestResponse.logging.maxPayloadLength:64000}")
    private int maxPayloadLength;

    @Value("${requestResponse.logging.indent:4}")
    private int indent;

    @Value("${requestResponse.logging.beforeMessagePrefix:REQUEST started }")
    private String beforeMessagePrefix;

    @Value("${requestResponse.logging.afterMessagePrefix:REQUEST ended }")
    private String afterMessagePrefix;

    @Value("${requestResponse.logging.requestMessagePrefix:REQUEST [ }")
    private String requestMessagePrefix;

    @Value("${requestResponse.logging.requestMessageSuffix:] }")
    private String requestMessageSuffix;

    @Value("${requestResponse.logging.responseMessagePrefix:RESPONSE [ }")
    private String responseMessagePrefix;

    @Value("${requestResponse.logging.responseMessageSuffix:] }")
    private String responseMessageSuffix;
    

    
    public RequestResponseFilter()
    {
    }

    
    @PostConstruct
    public void init() 
    {
        setIncludeClientInfo(this.includeClientInfo);
        setIncludeHeaders(this.includeHeaders);
        setIncludePayload(this.includePayload);
        setIncludeQueryString(this.includeQueryString);
        setMaxPayloadLength(this.maxPayloadLength);
        setBeforeMessagePrefix(this.beforeMessagePrefix);
        setAfterMessagePrefix(this.afterMessagePrefix);
    }
    
    
    @Override
    protected boolean shouldLog(HttpServletRequest request)
    {
        return this.shouldLog;
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
    {    	
    	if (request.getRequestURI().startsWith("/api"))
    	{
	        boolean flag = !isAsyncDispatch(request);
	        boolean shouldLog = this.shouldLog;
	        
	        HttpServletRequest cachedRequest = request;
	
	        if (flag && !(request instanceof CachedBodyHttpServletRequest)) 
	        {
	        	cachedRequest = new CachedBodyHttpServletRequest(request);
	        
		        shouldLog = this.shouldLog(cachedRequest);
		        if (shouldLog && flag) 
		        {
		            logger.info(prepareRequestMessage(cachedRequest));
		        }
	        }
	        
	        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse(response);
	        
	        filterChain.doFilter(cachedRequest, cachedResponse);
	
	        if (shouldLog && !isAsyncStarted(cachedRequest) && !(response instanceof CachedBodyHttpServletResponse)) 
	        {
	            logger.info(prepareResponseMessage(cachedResponse));
	        }
    	}
    	else
    	{
    		filterChain.doFilter(request, response);
    	}
    }
    
    
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) 
    {
        logger.info(message);
    }

    
    @Override
    protected void afterRequest(HttpServletRequest request, String message) 
    {
        logger.info(message);
    }
      
    
    protected String prepareRequestMessage(HttpServletRequest request) 
    {
        String indent = " ".repeat(this.indent);

        StringBuilder sb = new StringBuilder();
        sb.append('\n').append('\n').append(this.requestMessagePrefix).append('\n');

        sb.append(indent).append("method = ").append(request.getMethod()).append(",").append('\n');
        sb.append(indent).append("uri = ").append(request.getRequestURI()).append('\n');

        if (this.isIncludeQueryString()) 
        {
        	String queryString = request.getQueryString();
        	if (queryString != null && !queryString.trim().equals(""))
        	{
	            String[] queryStringParts = queryString.split("&");
	            if (queryStringParts != null && queryStringParts.length > 0) 
	            {
	            	sb.append(indent).append("query string = [ ").append('\n');
	            	for (int i = 0; i < queryStringParts.length; i++)
	            	{
	                    sb.append(indent).append(indent).append(queryStringParts[i]).append('\n');            		
	            	}
	            	sb.append(indent).append("] ").append('\n');
	            }
        	}
        }

        if (isIncludeClientInfo()) 
        {
            String client = request.getRemoteAddr();
            if (StringUtils.hasLength(client)) {
                sb.append(indent).append("client = ").append(client).append('\n');
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                sb.append(indent).append(";session=").append(session.getId()).append('\n');
            }
            String user = request.getRemoteUser();
            if (user != null) {
                sb.append(indent).append(";user=").append(user).append('\n');
            }
        }

        boolean isApplicationJson = false;
        if (isIncludeHeaders()) 
        {
        	Enumeration<String> headers = request.getHeaderNames();
            sb.append(indent).append("headers = ").append("[").append('\n');
            while (headers.hasMoreElements())
            {
            	String key = headers.nextElement();
            	String value = request.getHeader(key);
            	sb.append(indent).append(indent).append(key).append(": ").append(value).append('\n');
            	if (key.equalsIgnoreCase("content-type") && value.toLowerCase().contains("application/json".toLowerCase()))
            	{
            		isApplicationJson = true;
            	}
            }
            sb.append(indent).append("]").append('\n');
        }

        if (isIncludePayload()) 
        {
        	CachedBodyHttpServletRequest cachedRequest = WebUtils.getNativeRequest(request, CachedBodyHttpServletRequest.class);

        	if (cachedRequest != null) 
        	{
                byte[] buffer = cachedRequest.getContentAsByteArray();
                if (buffer.length > 0) 
                {
                    int length = Math.min(buffer.length, getMaxPayloadLength());
                    String requestBody;
                    try 
                    {
                        requestBody = new String(buffer, 0, length, cachedRequest.getCharacterEncoding());
                    } 
                    catch (UnsupportedEncodingException ex) 
                    {
                        requestBody = "[unknown]";
                    }
                    if (isApplicationJson)
                    {
                    	sb.append(indent).append("payload = ").append('\n');
                    	
                    	Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                    	JsonElement je = JsonParser.parseString(requestBody);
                    	String prettyJsonString = gson.toJson(je).replace("\n", '\n' + indent);
                    	prettyJsonString = indent + prettyJsonString;
                    	sb.append(prettyJsonString).append('\n');
                    }
                    else
                    {
                    	sb.append(indent).append("payload = ").append(requestBody).append('\n');
                    }
                }
            }
        }

        sb.append(this.requestMessageSuffix).append('\n');
        return sb.toString();
    }

    
    protected String prepareResponseMessage(HttpServletResponse response) 
    {
        String indent = " ".repeat(this.indent);

        StringBuilder sb = new StringBuilder();
        sb.append('\n').append('\n').append(this.responseMessagePrefix).append('\n');

        sb.append(indent).append("statusCode = ").append(response.getStatus()).append('\n');


        String contentType = response.getContentType();
        boolean isApplicationJson = false;

        if (contentType != null) 
        {
            sb.append(indent).append("contentType = ").append(contentType).append('\n');
      		isApplicationJson = contentType.toLowerCase().contains("application/json".toLowerCase());
        }

        boolean isIncludePayload = isIncludePayload();
        
        isIncludePayload 
        	= response != null && response.getContentType() != null && 
        	  !(response.getContentType().equalsIgnoreCase("application/octet-stream") ||
        		  response.getContentType().equalsIgnoreCase("image/gif"));
        
        if (isIncludePayload) 
        {
        	CachedBodyHttpServletResponse cachedResponse = WebUtils.getNativeResponse(response, CachedBodyHttpServletResponse.class);
        	
            if (cachedResponse != null) 
            {
                byte[] buffer = cachedResponse.getContentAsByteArray();
                if (buffer.length > 0) 
                {
                    int length = Math.min(buffer.length, getMaxPayloadLength());
                    String payload;
                    try 
                    {
                        payload = new String(buffer, 0, length, cachedResponse.getCharacterEncoding());
                    } 
                    catch (UnsupportedEncodingException ex) 
                    {
                        payload = "[unknown]";
                    }
                    if (isApplicationJson)
                    {
                    	sb.append(indent).append("payload = ").append('\n');
 
                    	Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                    	JsonElement je = JsonParser.parseString(payload);
                    	String prettyJsonString = gson.toJson(je).replace("\n", '\n' + indent);
                    	prettyJsonString = indent + prettyJsonString;
                    	sb.append(prettyJsonString).append('\n');
                    }
                    else
                    {
                    	sb.append(indent).append("payload = ").append(payload).append('\n');
                    }
                }
            }
        }

        sb.append(this.responseMessageSuffix).append('\n');
        return sb.toString();
    }
}
