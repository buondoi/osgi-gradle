package com.padma.buildtool.osgi.manifest.instruction;

import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;
import com.padma.buildtool.osgi.manifest.Utils;

import java.io.IOException;
import java.util.List;


/**
 * Process <strong>modules</strong> instructions.
 * Created on 5/11/16.
 */
public class ModuleInstruction implements InstructionFunc
{
	public static final ModuleInstruction MODULE_INSTRUCTION = new ModuleInstruction();
	public static final String MODULES_HEADER = "Modules";

	@Override
	public Context apply(final Context context, final Instructions instructions) throws IOException
	{
		final List<String> modules = instructions.getModules();
		if (!modules.isEmpty())
		{
			context.append(MODULES_HEADER, Utils.toString(modules));
		}
		return context;
	}
}
