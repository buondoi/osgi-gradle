package com.padma.buildtool.osgi.manifest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;


/**
 * Created on 5/11/16.
 */
@FunctionalInterface
public interface InstructionFunc
{
	InstructionFunc IDENTITY = (c, i) -> c;

	Context apply(Context context, Instructions instructions) throws IOException;

	static InstructionFunc compose(InstructionFunc... resources)
	{
		return Stream.of(resources).reduce(IDENTITY, InstructionFunc::andThen);
	}

	default InstructionFunc andThen(InstructionFunc func)
	{
		return (c, i) -> func.apply(InstructionFunc.this.apply(c, i), i);
	}

	interface Context
	{
		Path root();

		void append(String prop, String value);

		boolean hasProperty(String name);

		String getProperty(String name);
	}
}