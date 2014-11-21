package net.glowstone.io;

import java.io.IOException;
import java.util.Map;

import net.glowstone.generator.structures.GlowStructure;

/**
 * Provider of I/O for structures data.
 */
public interface StructureDataService {

    Map<Integer, GlowStructure> readStructuresData() throws IOException;

    void writeStructuresData(Map<Integer, GlowStructure> structures) throws IOException;
}
