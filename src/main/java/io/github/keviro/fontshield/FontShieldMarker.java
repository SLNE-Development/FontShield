package io.github.keviro.fontshield;

/**
 * Empty marker interface that the mixin adds to {@code org.lwjgl.util.freetype.FreeType}.
 * {@link FontShield} uses it at startup to verify that the patch was actually applied.
 */
public interface FontShieldMarker {
}
