package com.padma.buildtool.osgi.manifest.instruction;

import aQute.bnd.osgi.Constants;
import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;

import java.io.IOException;


/**
 * Created on 6/5/16.
 */
public class WebBundleInstruction implements InstructionFunc
{
	public static final WebBundleInstruction WEB_BUNDLE_INSTRUCTION = new WebBundleInstruction();

	private WebBundleInstruction()
	{
		// empty
	}

	@Override
	public Context apply(Context context, Instructions instructions) throws IOException
	{
		if (instructions.isWebModule())
		{
			context.append(Constants.BUNDLE_CLASSPATH, "WEB-INF/classes");
		}
		return context;
	}
}
