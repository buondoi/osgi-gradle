package com.padma.tool.gradle.plugin

import com.padma.buildtool.osgi.manifest.Instructions
import com.padma.buildtool.osgi.manifest.ManifestBuilder
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.bundling.Jar

import java.nio.file.Path

import static java.nio.file.Files.exists
import static java.nio.file.Paths.get

/**
 * Created on 5/9/16.
 */
class BundlePlugin implements Plugin<ProjectInternal>
{
    @Override
    void apply(final ProjectInternal project)
    {
        project.plugins.apply(JavaPlugin)
        project.plugins.withType(JavaPlugin) {
            def main = project.convention.getPlugin(JavaPluginConvention).sourceSets.main

            Path root = get(main.output.classesDir.toURI())
            def builder = new ManifestBuilder(root)

            // configure customized headers.
            def props = ['Bundle-SymbolicName': "${project.name}", 'Bundle-Version': "${project.version.toString()}", 'Bundle-Name': "${project.name}"]
            builder.withProps(props)

            // configure customized instructions.
            main.resources.srcDirs.each { dir ->
                def manifestPath = get(dir.toURI()).resolve('META-INF/manifest.yml')
                if (exists(manifestPath))
                {
                    def instructions = Instructions.fromYml(manifestPath.toUri().toURL())
                    builder.with(instructions)
                }
            }

            project.tasks.withType(Jar) {
                it.setManifest(new BundleManifest(project.fileResolver, builder))
            }
        }
    }
}
