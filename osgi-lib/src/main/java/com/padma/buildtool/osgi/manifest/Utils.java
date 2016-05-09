package com.padma.buildtool.osgi.manifest;

import java.util.Collection;


/**
 * Created on 5/9/16.
 */
public final class Utils
{
	private Utils()
	{

	}

	public static String toString(Collection<String> list)
	{
		final StringBuilder strVal = new StringBuilder();
		list.forEach(str -> {
			if (strVal.length() > 0)
			{
				strVal.append(',');
			}
			strVal.append(str);
		});
		return strVal.toString();
	}
}
