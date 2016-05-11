package com.padma.buildtool.osgi.manifest.instruction;

import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;
import com.padma.buildtool.osgi.manifest.Utils;

import java.io.IOException;
import java.util.List;

import static aQute.bnd.osgi.Constants.EXPORT_PACKAGE;


/**
 * Created on 5/11/16.
 */
public class ExportPackageInstruction implements InstructionFunc
{
	public static final ExportPackageInstruction EXPORT_PACKAGE_INSTRUCTION = new ExportPackageInstruction();

	@Override
	public Context apply(final Context context, final Instructions instructions) throws IOException
	{
		final List<String> exports = instructions.getExports();
		if (!exports.isEmpty())
		{
			context.append(EXPORT_PACKAGE, Utils.toString(exports));
		}
		return context;
	}
}
