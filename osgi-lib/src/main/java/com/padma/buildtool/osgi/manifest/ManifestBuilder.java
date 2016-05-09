package com.padma.buildtool.osgi.manifest;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Packages;
import aQute.bnd.osgi.Processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import static aQute.bnd.osgi.Constants.*;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.newDirectoryStream;


/**
 * Created on 5/9/16.
 */
public class ManifestBuilder
{
	private final Path root;
	private Instructions instructions;

	public ManifestBuilder(Path root)
	{
		this.root = root;
	}

	public ManifestBuilder with(Instructions instructions)
	{
		this.instructions = instructions;
		return this;
	}

	public final Manifest build() throws Exception
	{
		return build(Collections.emptyMap());
	}

	/**
	 * Build {@link Manifest} with customized properties.
	 *
	 * @param customizedProps properties
	 * @return {@link Manifest}. Return a null value if there is an error.
	 * @throws Exception
	 */
	public Manifest build(Map<String, String> customizedProps) throws Exception
	{
		final Jar jar = new Jar(root.toFile());
		final Analyzer analyzer = new Analyzer();
		analyzer.setJar(jar);

		// update customized properties before processing of instructions.
		customizedProps.entrySet().forEach(entry -> analyzer.setProperty(entry.getKey(), entry.getValue()));

		// process instructions.
		if (instructions != null)
		{
			/**
			 * bundle classpath instruction
			 */
			final List<String> embeddedLibs = instructions.getEmbeddedLibs();
			if (!embeddedLibs.isEmpty())
			{
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
					final List<String> libStrPaths = new ArrayList<>(libPaths.size());
					for (Path libPath : libPaths)
					{
						if (isDirectory(libPath))
						{
							newDirectoryStream(libPath, "*.{jar}")
									.forEach(jarPath -> libStrPaths.add(root.relativize(jarPath).toString()));
						}
						else
						{
							libStrPaths.add(root.relativize(libPath).toString());
						}
					}
					append(analyzer, BUNDLE_CLASSPATH, ".," + Utils.toString(libStrPaths));
				}
			}

			/**
			 * export package instruction.
			 * TODO if bundle-classpath is specified then we need to handle export-package in a different way
			 */
			final Packages packages = getPackages(analyzer, jar);
			append(analyzer, EXPORT_PACKAGE, Processor.printClauses(packages));

			/**
			 * import package instruction
			 */
			final List<String> requires = instructions.getRequires();
			if (!requires.isEmpty())
			{
				append(analyzer, IMPORT_PACKAGE, Utils.toString(requires));
			}
		}

		final Manifest manifest = analyzer.calcManifest();
		if (analyzer.isOk())
		{
			return manifest;
		}
		return null;
	}

	private void append(Analyzer analyzer, String property, String value)
	{
		final String propVal = analyzer.getProperty(property);
		if (propVal != null)
		{
			analyzer.setProperty(property, propVal + ',' + value);
		}
		else
		{
			analyzer.setProperty(property, value);
		}
	}

	private Packages getPackages(Analyzer analyzer, Jar jar)
	{
		final Packages packages = new Packages();
		jar.getPackages().forEach(p -> {
			if (!p.startsWith("META-INF") && !p.startsWith("GEN-INF"))
			{
				packages.put(analyzer.getPackageRef(p));
			}
		});
		return packages;
	}
}
