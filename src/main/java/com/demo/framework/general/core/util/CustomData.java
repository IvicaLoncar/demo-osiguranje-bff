package com.demo.framework.general.core.util;

import java.util.ArrayList;
import java.util.List;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.enums.OperationStatus;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomData<T> 
{
	private CustomMetadata metadata;
	private T data;
	private List<CustomMessage> messages = new ArrayList<CustomMessage>();
	
	@JsonIgnore
	private boolean markedForRollback = false;
	
	@JsonIgnore
	private boolean internalServerError = false;
	
	@JsonIgnore
	private boolean resourceNotFound = false;
	
	@JsonIgnore
	private boolean patchContentMissing = false;
	

	
	/*public void addMessage(Context ctx, CustomMessageLevel level, String message, Object...parameters)
	{
		//this.addMessage(ctx, level, message, null);
	}*/
	
	
	public void addMessage(CustomMessageLevel level, Exception exception)
	{
		FatalMessageReplacementInfo replacementInfo = null;
		
		//System.out.println("prije dohvata replacement info ");
		if (level == CustomMessageLevel.Fatal) replacementInfo = ApplicationMessageUtil.findFatalMessageReplacementInfo();
		
		//System.out.println("poslije dohvata replacement info: " + Boolean.toString(replacementInfo != null));
		
		CustomMessage customMessage = new CustomMessage();
		customMessage.setLevel(level);

		LocalizedMessage localizedMessage = new LocalizedMessage();
		localizedMessage.setLocale("hr-hr");
		localizedMessage.setMessage(level == CustomMessageLevel.Fatal && replacementInfo.isReplaceFatalMessage() ? replacementInfo.getReplacementMessage().getMessageText() : exception.getMessage());
		
		customMessage.setMessages(new ArrayList<LocalizedMessage>());
		customMessage.getMessages().add(localizedMessage);
		
		this.messages.add(customMessage);
		
		//System.out.println("broj poruka " + this.messages.size());
				
		if (LogUtil.logActivated && 
			((LogUtil.level == CustomMessageLevel.Fatal && 
			  (level == CustomMessageLevel.Fatal)) ||
  			 (LogUtil.level == CustomMessageLevel.Error && 
			  (level == CustomMessageLevel.Error ||
			   level == CustomMessageLevel.Fatal)) ||
  			 (LogUtil.level == CustomMessageLevel.Warning && 
			  (level == CustomMessageLevel.Warning ||
			   level == CustomMessageLevel.Error ||
			   level == CustomMessageLevel.Fatal)) ||
  			 (LogUtil.level == CustomMessageLevel.Debug && 
			  (level == CustomMessageLevel.Debug ||
			   level == CustomMessageLevel.Warning ||
			   level == CustomMessageLevel.Error ||
			   level == CustomMessageLevel.Fatal))))
		{			
			LogUtil.Log(level, null, exception);
		}
	}
	
	
	public void appendMessages(List<CustomMessage> messages)
	{
		if (messages != null && messages.size() > 0)
		{
			for (int i = 0; i < messages.size(); i++)
			{
				this.messages.add(messages.get(i));
			}
			messages.clear();
		}
	}
	
	
	public void appendMessages(MessagesHandler messagesHandler)
	{
		if (messagesHandler != null)
		{
			this.appendMessages(messagesHandler.getMessages());
			this.markedForRollback = messagesHandler.markedForRollback();
			this.internalServerError = messagesHandler.isInternalServerError();
			this.resourceNotFound = messagesHandler.isResourceNotFound();
			this.patchContentMissing = messagesHandler.isPatchContetMissing();
		}
	}

	
	public void addMessage(CustomMessageLevel level, MarkForRollback markForRollback, List<ApplicationMessage> applicationMessages, String applicationGroup, Object[] parameters)
	{
  	//localizedMessages = ApplicationMessageUtil.composeMessage(this.applicationMessages, "com.example.demo.general.proba.ClientTypeServiceImpl.BIU300");

		
		//System.out.println("dohvat poruke iz baze -> application messsages DAO "+  Boolean.toString(this.applicationMessageDAO != null));
		//this.addMessage(ctx, level, code, message, null);
		//GeneralConfiguration gc = new GeneralConfiguration();
	//	System.out.println("general configuration " + Boolean.toString(gc.isMaskFatalMessages()));
	}

	
	public void addMessage(CustomMessageLevel level, MarkForRollback markForRollback, List<ApplicationMessage> applicationMessages, String applicationGroup, String applicationMessage, Object...parameters)
	{
  	//localizedMessages = ApplicationMessageUtil.composeMessage(this.applicationMessages, "com.example.demo.general.proba.ClientTypeServiceImpl.BIU300");

		
		//System.out.println("dohvat poruke iz baze -> application messsages DAO "+  Boolean.toString(this.applicationMessageDAO != null));
		//this.addMessage(ctx, level, code, message, null);
		//GeneralConfiguration gc = new GeneralConfiguration();
	//	System.out.println("general configuration " + Boolean.toString(gc.isMaskFatalMessages()));
	}


	public void addMessage(CustomMessageLevel level, OnFatal onFatal, MarkForRollback markForRollback, List<ApplicationMessage> applicationMessages, String applicationGroup, String applicationMessage, Object...parameters)
	{
  	//localizedMessages = ApplicationMessageUtil.composeMessage(this.applicationMessages, "com.example.demo.general.proba.ClientTypeServiceImpl.BIU300");

		
		//System.out.println("dohvat poruke iz baze -> application messsages DAO "+  Boolean.toString(this.applicationMessageDAO != null));
		//this.addMessage(ctx, level, code, message, null);
		//GeneralConfiguration gc = new GeneralConfiguration();
	//	System.out.println("general configuration " + Boolean.toString(gc.isMaskFatalMessages()));
	}

	
}
