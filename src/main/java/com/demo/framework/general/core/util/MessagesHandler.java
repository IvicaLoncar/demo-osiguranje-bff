package com.demo.framework.general.core.util;

import java.util.ArrayList;
import java.util.List;

import com.demo.framework.general.core.enums.CustomMessageLevel;

import lombok.ToString;


@ToString
public class MessagesHandler
{
	private List<CustomMessage> messages = new ArrayList<CustomMessage>();
	
	private boolean proceed = true;
	private MarkForRollback markForRollback = MarkForRollback.No;
	
	private boolean internalServerError = false;
	
	private boolean resourceNotFound = false;
	
	private boolean patchContentMissing = false;
	
	
	
	public void setMarkForRollback(MarkForRollback markForRollback)
	{
		this.markForRollback = markForRollback;
	}
	
	
	public MarkForRollback getMarkForRollback()
	{
		return this.markForRollback;
	}
	
	
	public boolean markedForRollback()
	{
		return this.markForRollback == MarkForRollback.Yes;
	}
	
	
	public boolean isInternalServerError()
	{
		return this.internalServerError;
	}
	
	
	public boolean isResourceNotFound()
	{
		return this.resourceNotFound;
	}
	
	
	public void setResourceNotFound(boolean resourceNotFound)
	{
		this.resourceNotFound = resourceNotFound;
	}
	
	
	public boolean isPatchContetMissing()
	{
		return this.patchContentMissing;
	}
	
	
	public void setPatchContentMissing(boolean patchContentMissing)
	{
		this.patchContentMissing = patchContentMissing;
	}
	
	
	public boolean proceed()
	{
		return this.proceed;
	}
	
	
	public void setProceed(boolean proceed)
	{
		this.proceed = proceed;
	}

	
/*	public void addMessage(String group, CustomMessage message)
	{
		
	}*/
	
	public void addMessage(String messageName, CustomMessage message)
	{
		System.out.println("add message ");
		if (this.proceed && message != null)
		{
			if (message.getMarkForRollback() == MarkForRollback.Yes) 
			{
				this.markForRollback = MarkForRollback.Yes;
			}
			if (message.getLevel() == CustomMessageLevel.Fatal && message.getOnFatal() == OnFatal.Stop)
			{
				this.internalServerError = true;
				this.proceed = false;
				this.messages.clear();
			}
			if (message.getMessages().size() == 0)
			{
				message.prepareMessages(messageName);
			}
			this.messages.add(message);
		}
	}

	
	/*public void addMessages(String group, List<CustomMessage> messages)
	{
		if (messages != null)
		{
			for (int i = 0; i < messages.size(); i++)
			{
				this.addMessage(group, messages.get(i));
			}
		}
	}
*/
	public void addMessages(String group, CustomData<?> data)
	{
		List<CustomMessage> messages = null;
		
		if (data != null && (messages = data.getMessages()) != null)
		{			
			for (int i = 0; i < messages.size(); i++)
			{
				this.addMessage(group, messages.get(i));
			}
		}
		if (data.isMarkedForRollback()) this.markForRollback = MarkForRollback.Yes;
		if (data.isResourceNotFound()) this.resourceNotFound = true;
		if (data.isPatchContentMissing()) this.patchContentMissing = true;
	}

	
	public List<CustomMessage> getMessages()
	{
		return this.messages;
	}

}
