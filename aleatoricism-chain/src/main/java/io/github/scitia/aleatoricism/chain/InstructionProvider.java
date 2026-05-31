package io.github.scitia.aleatoricism.chain;

/**
 * Provides instructions for the Aleatoric HTTP router from markdown file.
 * Instructions define how different HTTP methods and paths should be handled.
 */
public interface InstructionProvider {

    /**
     * Get instructions from markdown file.
     * @return instructions as string
     */
    String getInstructions();

}

