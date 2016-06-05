package com.padma.buildtool.osgi.manifest.instruction;

import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;

import java.io.IOException;

import static aQute.bnd.osgi.Constants.BUNDLE_CLASSPATH;


/**
 * Created on 6/5/16.
 */
public class RootBundleClassPathInstruction implements InstructionFunc
{
	public static final RootBundleClassPathInstruction ROOT_BUNDLE_CLASS_PATH_INSTRUCTION = new RootBundleClassPathInstruction();

	private RootBundleClassPathInstruction()
	{
		// empty
	}

	@Override
	public Context apply(Context context, Instructions instructions) throws IOException
	{
		if (context.hasProperty(BUNDLE_CLASSPATH))
		{
			context.append(BUNDLE_CLASSPATH, ".");
		}
		return context;
	}
}
