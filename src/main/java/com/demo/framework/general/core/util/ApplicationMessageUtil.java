package com.demo.framework.general.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.framework.general.core.configuration.GeneralConfiguration;
import com.demo.framework.general.core.model.ApplicationMessage;
import com.demo.framework.general.core.services.ApplicationMessageService;

@Component
public class ApplicationMessageUtil 
{
	private static GeneralConfiguration generalConfiguration;
	private static ApplicationMessageService applicationMessageService;
	
	@Autowired 
	public GeneralConfiguration generalConfigurationTemp; 

	@Autowired
	private ApplicationMessageService applicationMessageServiceTemp;	


	@PostConstruct     
	private void initStaticDao () 
	{
	  generalConfiguration = this.generalConfigurationTemp;
	  applicationMessageService = this.applicationMessageServiceTemp;
	}

	
	public static FatalMessageReplacementInfo findFatalMessageReplacementInfo()
	{
		FatalMessageReplacementInfo replacementInfo = new FatalMessageReplacementInfo();
		replacementInfo.setReplaceFatalMessage(generalConfiguration.isReplaceFatalMessage());
		//System.out.println("to replace fatal message " + generalConfiguration.isReplaceFatalMessage());
		
		if (replacementInfo.isReplaceFatalMessage())
		{
			String[] classFullNameParts = Thread.currentThread().getStackTrace()[3].getClassName().split("\\.");
			String className = classFullNameParts[classFullNameParts.length - 1];
			//System.out.println("klasa " + classFullNameParts[classFullNameParts.length - 1]);

			if (className.indexOf("ApplicationMessage") > -1)
			{
				//System.out.println("detektiran application message");
				ApplicationMessage applicationMessage = new ApplicationMessage();
				applicationMessage.setMessageText(generalConfiguration.getReplaceFatalMessageText());
				replacementInfo.setReplacementMessage(applicationMessage);
			}
			else
			{
				//System.out.println("prije applicationMessageService.get " + generalConfiguration.getReplaceFatalMessageId());
				CustomData<ApplicationMessage> fatalApplicationMessage = applicationMessageService.get(generalConfiguration.getReplaceFatalMessageId());
				//System.out.println("replacement message found " + fatalApplicationMessage.getData() != null);
				if (fatalApplicationMessage.getData() != null)
				{
					replacementInfo.setReplacementMessage(fatalApplicationMessage.getData());
				}
				else 
				{
					ApplicationMessage applicationMessage = new ApplicationMessage();
					applicationMessage.setMessageText(generalConfiguration.getReplaceFatalMessageText());
					replacementInfo.setReplacementMessage(applicationMessage);
				}
			}
		}
		//System.out.println("vraćanje replacment info " + Boolean.toString(replacementInfo != null));
		return replacementInfo;
	}
	
	
	
	public static void addMessage(List<ApplicationMessage> applicationMessages, String messageName, String locale, String messageText)
	{
		if (applicationMessages != null)
		{
	  	ApplicationMessage applicationMessage = null;
	  	
	  	applicationMessage = new ApplicationMessage();
	  	applicationMessage.setName(messageName);
	  	applicationMessage.setLocale(locale);
	  	applicationMessage.setMessageText(messageText);
	  	
	  	applicationMessages.add(applicationMessage);
		}
	}
	
	
	public static List<LocalizedMessage> composeMessage(List<ApplicationMessage> applicationMessages, String messageName, Object...parameters)
	{
		List<LocalizedMessage> localizedMessages = new ArrayList<LocalizedMessage>();
		
		boolean exists = false;
		if (applicationMessages != null)
		{
			for (int i = 0; i < applicationMessages.size(); i++)
			{
				if (messageName.equals(applicationMessages.get(i).getName()))
				{
					exists = true;
					LocalizedMessage localizedMessage = new LocalizedMessage();
					localizedMessage.setLocale(applicationMessages.get(i).getLocale());
					localizedMessage.setMessage(applicationMessages.get(i).getMessageText());
				}
			}
			if (!exists)
			{
				LocalizedMessage localizedMessage = new LocalizedMessage();
				localizedMessage.setLocale("hr-hr");
				localizedMessage.setMessage("opećenita poruka");
			}
		}
		
		return localizedMessages;
	}
	
	
	public static StringBuilder prepareMessage(String parametrizedMessage, Object...parameters)
	{
		return prepareMessage(parametrizedMessage, Arrays.asList(parameters));
	}
	
	
	public static StringBuilder prepareMessage(String parametrizedMessage, List<Object> parameters)
	{
		StringBuilder sb = new StringBuilder();
		
    List<String> escapedStrings = new ArrayList<String>();
 //   List<Integer> pozicijeZamjena = new ArrayList<Integer>();

		String tmpMessage = parametrizedMessage;
		
	//	if (parameters.length > 0) 		System.out.println("param 0: " + parameters[0]);
		
		/*if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				System.out.println("parameter "+ i + ": " + parameters[i]);
			}
		}*/
		
    while(true)
    {
    	int indexof = tmpMessage.indexOf('\\');
    	if (indexof < 0) break;
    //	System.out.println("$ na poziciji: " +  indexof + "  " + tmpFilter.substring(indexof, indexof + 2));
    	escapedStrings.add(tmpMessage.substring(indexof + 1, indexof + 2));
    	String noviString = tmpMessage.substring(0, indexof) +  "#" + tmpMessage.substring(indexof + 2);
    //	System.out.println("novi filter:  " + noviString);
    	tmpMessage = noviString;
    }

    if ((tmpMessage.substring(tmpMessage.length() - 1)).equals("*"))
    {
    	tmpMessage = tmpMessage + " ";
    }

    List<String> messageParts = Arrays.asList(tmpMessage.split("\\*"));
    
    for (int i = 0; i < messageParts.size(); i++)
    {
    	String messagePart = messageParts.get(i);
    	while(true)
    	{
    		int indexof = messagePart.indexOf("#");
    		if (indexof < 0) break;
    		messagePart = messagePart.substring(0, indexof) + (escapedStrings != null && escapedStrings.size() > 0 ? escapedStrings.get(0) : "?") + messagePart.substring(indexof + 1);
    		if (escapedStrings != null && escapedStrings.size() > 0) escapedStrings.remove(0);    		
    	}
    	messageParts.set(i, messagePart);
    //	System.out.println(i + ": " + messagePart);
  		sb.append(messageParts.get(i));
    	//if (i > 0)
    	//{
  	//	System.out.println("parameters: " + parameters.length);
  		if (parameters != null && parameters.size() > i && messageParts.size() - 1 > i )
  		{
  		//	System.out.println("parameter: " + parameters[0]);
  			sb.append(parameters.get(i));
  		}
    	//}
  	//	System.out.println("poruka: " + sb.toString());
    }
    
    
    
  //  System.out.println(messageParts);
    

   // System.out.println(tmpMessage);
		
	//	sb.append(parametrizedMessage);
    
   // System.out.println(sb.toString());
		
		return sb;
	}
	
}
