package com.padma.tool.gradle.osgi

import org.gradle.testfixtures.ProjectBuilder

/**
 * Created on 5/9/16.
 */
class BundlePluginSpec extends BaseSpec
{
    def 'apply plugin and check of availability'()
    {
        setup:
        def project = ProjectBuilder.builder().withProjectDir(rootPath().toFile()).build()

        when:
        project.apply plugin: 'bundle'

        then:
        noExceptionThrown()
    }
}
