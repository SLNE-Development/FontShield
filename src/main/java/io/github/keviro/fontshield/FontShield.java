package io.github.keviro.fontshield;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.util.freetype.FreeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FontShield implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("FontShield");

    @Override
    public void onInitializeClient() {
        // Fail closed: if the loaded FreeType class does not carry the marker,
        // the mixin did not apply and the client would run unprotected.
        if (!FontShieldMarker.class.isAssignableFrom(FreeType.class)) {
            throw new IllegalStateException(
                "FontShield could not patch org.lwjgl.util.freetype.FreeType. "
                    + "Protection is NOT active, refusing to start."
            );
        }
        LOGGER.info("FontShield active: FreeType hinting disabled, minecraft2shell entry point blocked.");
    }
}
