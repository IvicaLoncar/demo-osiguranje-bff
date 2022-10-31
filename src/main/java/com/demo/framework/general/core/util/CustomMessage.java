package com.demo.framework.general.core.util;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomMessage
{
	private CustomMessageLevel level;
	private String code;
	//private String messageNameHash;
	private List<LocalizedMessage> messages = new ArrayList<LocalizedMessage>();

	@JsonIgnore
	private String messageName;

	@JsonIgnore
	private Exception exception = null;
	
	@JsonIgnore
	private OnFatal onFatal = OnFatal.Stop;
	
	@JsonIgnore
	private MarkForRollback markForRollback = MarkForRollback.No;
	
	@JsonIgnore
	private List<Object> parameters = new ArrayList<Object>();
	
	@JsonIgnore
	private List<ApplicationMessage> applicationMessages = null;
	
	
	public CustomMessage exception(Exception exception)
	{
		this.exception = exception;
		return this;
	}
	
	public CustomMessage applicationMessages(List<ApplicationMessage> applicationMessages)
	{
		this.applicationMessages = applicationMessages;
		return this;
	}
	
	public CustomMessage customMessageLevel(CustomMessageLevel level)
	{
		this.level = level;
		return this;
	}
	
	public CustomMessage code(String code)
	{
		this.code = code;
		return this;
	}
	
	/*public CustomMessage hash(String hash)
	{
		this.messageNameHash = hash;
		return this;
	}*/

	public CustomMessage onFatalContinue(boolean onFatal)
	{
		this.onFatal = (onFatal ? OnFatal.Continue : OnFatal.Stop);
		return this;
	}

	public CustomMessage markForRollback(boolean markForRollback)
	{
		this.markForRollback = (markForRollback ? MarkForRollback.Yes : MarkForRollback.No);
		return this;
	}

	public CustomMessage parameter(Object parameter)
	{
		this.parameters.add(parameter);
		return this;
	}
	
	
	public void prepareMessages(String messageName)
	{
		this.messageName = messageName;
		//this.messageNameHash = DigestUtils.sha1Hex(messageName);
		//System.out.println("došao u preparedMessages " + this.applicationMessages.size() + "  " + this.messages.size());
		if (this.applicationMessages != null && this.applicationMessages.size() > 0 && this.messages.size() == 0)
		{
			this.messages.clear();
			
			for (int i = 0; i < this.applicationMessages.size(); i++)
			{
				if (this.applicationMessages.get(i).getName().equals(messageName))
				{
					LocalizedMessage localizedMessage = new LocalizedMessage();
					localizedMessage.setLocale(this.applicationMessages.get(i).getLocale());
					PreparedMessage preparedMessage = new PreparedMessage();
					preparedMessage.unformattedMessage(ApplicationMessageUtil.prepareMessage(this.applicationMessages.get(i).getMessageText(), this.parameters).toString());
					if (this.parameters != null)
					{
						for (int j = 0; j < this.parameters.size(); j++)
						{
							preparedMessage.parameter(this.parameters.get(j));
						}
					}						
					localizedMessage.setMessage(preparedMessage.getPreparedMessage());
					this.messages.add(localizedMessage);
				}
			}
			//System.out.println("preparedMessages " + this.messages.size());
			this.applicationMessages = null;
		}		
		if (this.exception != null)
		{
			LocalizedMessage localizedMessage = new LocalizedMessage();
			localizedMessage.setLocale("hr-hr");
			localizedMessage.setMessage(this.level == CustomMessageLevel.Fatal ? this.exception.toString() : this.exception.getMessage());			
			this.messages.add(localizedMessage);
		}
	}
	
	
	
}
