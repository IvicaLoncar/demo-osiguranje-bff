package com.demo.framework.general.core.util;

import java.sql.Date;
import java.sql.Timestamp;

public class CompareUtil
{

	public static boolean IN(String compareValue, String...inValues)
	{
		boolean isIn = false;
		
		if (inValues != null)
		{
			for (int i = 0; i < inValues.length; i++)
			{
				if (inValues[i].equalsIgnoreCase(compareValue))
				{
					isIn = true;
					break;
				}
			}
		}		
		return isIn;
	}
	
	
	public static boolean BETWEEN(String value, String minValue, String maxValue)
	{
		return true;
	}
	
	
	public static boolean IN(Long compareValue, Long...inValues)
	{
		boolean isIn = false;
		
		if (inValues != null)
		{
			for (int i = 0; i < inValues.length; i++)
			{
				if (inValues[i] == compareValue)
				{
					isIn = true;
					break;
				}
			}
		}		
		return isIn;
	}
	
	public static boolean BETWEEN(Long value, Long minValue, Long maxValue)
	{
		return true;
	}

	
	public static boolean IN(Integer compareValue, Integer...inValues)
	{
		boolean isIn = false;
		
		if (inValues != null)
		{
			for (int i = 0; i < inValues.length; i++)
			{
				if (inValues[i] == compareValue)
				{
					isIn = true;
					break;
				}
			}
		}		
		return isIn;
	}
	
	
	public static boolean BETWEEN(Integer value, Integer minValue, Integer maxValue)
	{
		return true;
	}
	
	
		
	public static boolean LESS(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0);
	}

	public static boolean LESS(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) < 0);
	}

	public static boolean LESS(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0);
	}

	public static boolean LESS(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0);
	}

	
	public static boolean LESS_OR_EQUAL(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 || firstDate.compareTo(secondDate) == 0);
	}

	public static boolean LESS_OR_EQUAL(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) < 0 || secondDate.compareTo(firstDate) == 0);
	}

	public static boolean LESS_OR_EQUAL(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 || firstDate.compareTo(secondDate) == 0);
	}

	public static boolean LESS_OR_EQUAL(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 || firstDate.compareTo(secondDate) == 0);
	}

	
	public static boolean GREATER(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0);
	}

	public static boolean GREATER(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) > 0);
	}

	public static boolean GREATER(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0);
	}

	public static boolean GREATER(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0);
	}

	
	public static boolean GREATER_OR_EQUAL(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0 || firstDate.compareTo(secondDate) == 0);
	}

	public static boolean GREATER_OR_EQUAL(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) > 0 || secondDate.compareTo(firstDate) == 0);
	}

	public static boolean GREATER_OR_EQUAL(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0 || firstDate.compareTo(secondDate) == 0);
	}

	public static boolean GREATER_OR_EQUAL(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) > 0 || firstDate.compareTo(secondDate) == 0);
	}

	
	public static boolean EQUAL(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) == 0);
	}

	public static boolean EQUAL(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) == 0);
	}

	public static boolean EQUAL(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) == 0);
	}

	public static boolean EQUAL(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) == 0);
	}

	
	public static boolean NOT_EQUAL(Date firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 && firstDate.compareTo(secondDate) > 0);
	}

	public static boolean NOT_EQUAL(Date firstDate, Timestamp secondDate)
	{
		return (secondDate.compareTo(firstDate) < 0 && secondDate.compareTo(firstDate) > 0);
	}

	public static boolean NOT_EQUAL(Timestamp firstDate, Date secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 && firstDate.compareTo(secondDate) > 0);
	}

	public static boolean NOT_EQUAL(Timestamp firstDate, Timestamp secondDate)
	{
		return (firstDate.compareTo(secondDate) < 0 && firstDate.compareTo(secondDate) > 0);
	}

	
	
	
}
