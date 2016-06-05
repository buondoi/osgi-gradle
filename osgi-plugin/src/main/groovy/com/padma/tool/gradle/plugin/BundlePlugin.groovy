package com.padma.tool.gradle.plugin

import com.padma.buildtool.osgi.manifest.Instructions
import com.padma.buildtool.osgi.manifest.ManifestBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.bundling.War

import java.nio.file.Path

import static java.nio.file.Files.exists
import static java.nio.file.Paths.get


/**
 * Created on 5/9/16.*/
class BundlePlugin implements Plugin<ProjectInternal>
{
	public static final String MANIFEST_DEF_FILE = 'META-INF/manifest.yml'

	@Override
	void apply(final ProjectInternal project)
	{
		// if it's web module.
		boolean isWebModule = isWebProject(project)

		project.logger.info('Project [{}] {} web module.', project.name, isWebModule ? 'is' : 'is not')

		// apply required plugins.
		project.plugins.apply(JavaPlugin)

		// setup task.
		project.plugins.withType(JavaPlugin) {
			def main = project.convention.getPlugin(JavaPluginConvention).sourceSets.main

			Path root = get(main.output.classesDir.toURI())
			def builder = new ManifestBuilder(root)

			Map<String, String> headers = new HashMap<>(3)
			headers.put("Bundle-SymbolicName", project.name)
			headers.put("Bundle-Name", project.name)
			headers.put("Bundle-Version", project.version.toString())

			// configure customized headers.
			builder.headers(headers)

			// configure customized instructions.
			Instructions instructions = prepareInstructions(project)

			project.logger.info("***** Instructions = ${instructions.toString()}")

			builder.instructions(instructions)

			if (isWebModule)
			{
				project.tasks.withType(War) {
					it.setManifest(new BundleManifest(project.fileResolver, builder))
				}
			}
			else
			{
				project.tasks.withType(Jar) {
					it.setManifest(new BundleManifest(project.fileResolver, builder))
				}
			}
		}
	}

	static def prepareInstructions(Project project)
	{
		def instructions = loadInstructions(project)
		if (instructions == null)
		{
			instructions = Instructions.newBuilder().build()
		}
		if (isWebProject(project))
		{
			def builder = Instructions.from(instructions)
			builder.setIsWebModule(true)

			instructions = builder.build()
		}
		return instructions
	}

	static Instructions loadInstructions(Project project)
	{
		def main = project.convention.getPlugin(JavaPluginConvention).sourceSets.main

		Set<File> dirs = main.resources.srcDirs
		for (File dir : dirs)
		{
			def manifestPath = get(dir.toURI()).resolve(MANIFEST_DEF_FILE)
			if (exists(manifestPath))
			{
				project.logger.info("******loadInstructions() Found $MANIFEST_DEF_FILE in project ${project.name}")
				return Instructions.fromYml(manifestPath.toUri().toURL())
			}
		}
		return null
	}

	static def isWebProject(Project project)
	{
		return exists(project.projectDir.toPath().resolve('src/main/webapp'))
	}
}
