package com.padma.buildtool.osgi.manifest;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Packages;
import aQute.bnd.osgi.Processor;
import com.padma.buildtool.osgi.manifest.InstructionFunc.Context;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.jar.Manifest;

import static aQute.bnd.osgi.Constants.EXPORT_PACKAGE;
import static com.padma.buildtool.osgi.manifest.InstructionFunc.compose;
import static com.padma.buildtool.osgi.manifest.instruction.EmbeddedLibsInstruction.EMBEDDED_LIBS_INSTRUCTION;
import static com.padma.buildtool.osgi.manifest.instruction.ExportPackageInstruction.EXPORT_PACKAGE_INSTRUCTION;
import static com.padma.buildtool.osgi.manifest.instruction.ImportPackageInstruction.IMPORT_PACKAGE_INSTRUCTION;
import static com.padma.buildtool.osgi.manifest.instruction.ModuleInstruction.MODULE_INSTRUCTION;
import static com.padma.buildtool.osgi.manifest.instruction.RootBundleClassPathInstruction.ROOT_BUNDLE_CLASS_PATH_INSTRUCTION;
import static com.padma.buildtool.osgi.manifest.instruction.WebBundleInstruction.WEB_BUNDLE_INSTRUCTION;


/**
 * Created on 5/9/16.
 */
public class ManifestBuilder
{
	private final Path root;
	private Instructions instructions;
	private Map<String, String> customizedProps;

	private static final InstructionFunc INSTRUCTION_FUNC = compose(EMBEDDED_LIBS_INSTRUCTION, IMPORT_PACKAGE_INSTRUCTION,
			EXPORT_PACKAGE_INSTRUCTION, MODULE_INSTRUCTION, WEB_BUNDLE_INSTRUCTION, ROOT_BUNDLE_CLASS_PATH_INSTRUCTION);

	public ManifestBuilder(Path root)
	{
		this.root = root;
	}

	public ManifestBuilder instructions(Instructions instructions)
	{
		this.instructions = instructions;
		return this;
	}

	public ManifestBuilder headers(Map<String, String> customizedProps)
	{
		this.customizedProps = customizedProps;
		return this;
	}

	public final Manifest build() throws Exception
	{
		return build(customizedProps == null ? Collections.emptyMap() : customizedProps);
	}

	/**
	 * Build {@link Manifest} with customized properties.
	 *
	 * @param customizedProps properties
	 * @return {@link Manifest}. Return a null value if there is an error.
	 * @throws Exception
	 */
	private Manifest build(Map<String, String> customizedProps) throws Exception
	{
		final Jar jar = new Jar(root.toFile());
		final Analyzer analyzer = new Analyzer();
		analyzer.setJar(jar);

		// update customized properties before processing of instructions.
		customizedProps.entrySet().forEach(entry -> analyzer.setProperty(entry.getKey(), entry.getValue()));

		// process instructions.
		if (instructions != null)
		{
			INSTRUCTION_FUNC.apply(new BndContext(root, analyzer), instructions);
		}

		/**
		 * Calculate export packages if they're not specified by default.
		 */
		if (analyzer.getProperty(EXPORT_PACKAGE) == null)
		{
			final Packages packages = getPackages(analyzer, jar);
			append(analyzer, EXPORT_PACKAGE, Processor.printClauses(packages));
		}

		/**
		 * Calculate manifest.
		 */
		final Manifest manifest = analyzer.calcManifest();
		if (analyzer.isOk())
		{
			return manifest;
		}
		return null;
	}

	final class BndContext implements Context
	{
		private final Path root;
		private final Analyzer analyzer;

		BndContext(final Path root, final Analyzer analyzer)
		{
			this.root = root;
			this.analyzer = analyzer;
		}

		@Override
		public Path root()
		{
			return root;
		}

		@Override
		public void append(final String prop, final String value)
		{
			ManifestBuilder.append(analyzer, prop, value);
		}

		@Override
		public boolean hasProperty(final String name)
		{
			return analyzer.getProperty(name) != null;
		}

		@Override
		public String getProperty(final String name)
		{
			return analyzer.getProperty(name);
		}
	}

	private static void append(Analyzer analyzer, String property, String value)
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
