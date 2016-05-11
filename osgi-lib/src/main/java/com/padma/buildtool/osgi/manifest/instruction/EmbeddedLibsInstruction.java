package com.padma.buildtool.osgi.manifest.instruction;

import com.padma.buildtool.osgi.manifest.InstructionFunc;
import com.padma.buildtool.osgi.manifest.Instructions;
import com.padma.buildtool.osgi.manifest.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static aQute.bnd.osgi.Constants.BUNDLE_CLASSPATH;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.newDirectoryStream;


/**
 * Created on 5/11/16.
 */
public class EmbeddedLibsInstruction implements InstructionFunc
{
	public static final EmbeddedLibsInstruction EMBEDDED_LIBS_INSTRUCTION = new EmbeddedLibsInstruction();

	@Override
	public Context apply(final Context context, final Instructions instructions) throws IOException
	{
		final List<String> embeddedLibs = instructions.getEmbeddedLibs();
		if (!embeddedLibs.isEmpty())
		{
			final Path root = context.root();
			final List<Path> libPaths = new ArrayList<>(embeddedLibs.size());
			embeddedLibs.forEach(lib -> {
				final Path path = root.resolve(lib);
				if (Files.exists(path))
				{
					libPaths.add(path);
				}
			});

			if (!libPaths.isEmpty())
			{
				final List<String> paths = new ArrayList<>(libPaths.size());
				for (Path libPath : libPaths)
				{
					if (isDirectory(libPath))
					{
						newDirectoryStream(libPath, "*.{jar}").forEach(jarPath -> paths.add(root.relativize(jarPath).toString()));
					}
					else
					{
						paths.add(root.relativize(libPath).toString());
					}
				}
				context.append(BUNDLE_CLASSPATH, ".," + Utils.toString(paths));
			}
		}
		return context;
	}
}
