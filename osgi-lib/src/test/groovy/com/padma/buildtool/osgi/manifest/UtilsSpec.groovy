package com.padma.buildtool.osgi.manifest

import spock.lang.Specification


/**
 * Created on 5/9/16.*/
class UtilsSpec extends Specification
{
	def 'convert list to string'()
	{
		setup:
		def list = ['package1', 'package2'] as List

		expect:
		'package1,package2'.equals(Utils.toString(list))
	}
}
