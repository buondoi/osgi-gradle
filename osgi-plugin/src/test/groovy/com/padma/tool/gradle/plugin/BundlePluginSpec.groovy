package com.padma.tool.gradle.plugin

import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder


/**
 * Created on 5/9/16.*/
class BundlePluginSpec extends BaseSpec
{
	def 'apply plugin and check of availability'()
	{
		setup:
		def project = ProjectBuilder.builder().withProjectDir(rootPath().toFile()).build()
		project.apply plugin: 'java'
		project.apply plugin: 'war'

		when:
		project.apply plugin: 'bundle'

		then:
		noExceptionThrown()

		and:
		def manifest = null
		project.tasks.withType(Jar) {
			manifest = it.getManifest()
		}

		then:
		manifest != null
		manifest instanceof BundleManifest
	}
}
