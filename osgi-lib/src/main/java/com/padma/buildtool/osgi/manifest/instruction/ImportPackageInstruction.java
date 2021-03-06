package com.padma.buildtool.osgi.manifest.instruction;

import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;
import com.padma.buildtool.osgi.manifest.Utils;

import java.io.IOException;
import java.util.List;

import static aQute.bnd.osgi.Constants.IMPORT_PACKAGE;


/**
 * Created on 5/11/16.
 */
public class ImportPackageInstruction implements InstructionFunc
{
	public static final ImportPackageInstruction IMPORT_PACKAGE_INSTRUCTION = new ImportPackageInstruction();

	@Override
	public Context apply(final Context context, final Instructions instructions) throws IOException
	{
		final List<String> requires = instructions.getRequires();
		if (!requires.isEmpty())
		{
			context.append(IMPORT_PACKAGE, Utils.toString(requires));
		}
		return context;
	}
}
