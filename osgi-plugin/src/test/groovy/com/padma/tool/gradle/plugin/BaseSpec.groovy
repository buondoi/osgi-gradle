package com.padma.tool.gradle.plugin

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created on 5/9/16.
 */
class BaseSpec extends Specification
{
    @Rule
    TemporaryFolder temp = new TemporaryFolder()

    Path rootPath()
    {
        Paths.get(temp.getRoot().toURI())
    }
}
