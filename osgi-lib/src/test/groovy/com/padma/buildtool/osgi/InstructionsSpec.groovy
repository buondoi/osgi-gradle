package com.padma.buildtool.osgi

import spock.lang.Specification


/**
 * Created on 5/9/16.*/
class InstructionsSpec extends Specification
{
	def 'create bundle manifest from manifest.yml file'()
	{
		setup:
		def url = InstructionsSpec.class.getResource('/META-INF/manifest.yml')

		when:
		def manifest = Instructions.fromYml(url)

		then:
		noExceptionThrown()
		manifest != null
		manifest.exports.contains('com.padma')
		manifest.requires.contains('javax.sql')
	}
}
