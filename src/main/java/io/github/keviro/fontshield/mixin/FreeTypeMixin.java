package io.github.keviro.fontshield.mixin;

import io.github.keviro.fontshield.FontShieldMarker;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FTC_ImageType;
import org.lwjgl.util.freetype.FreeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Forces {@code FT_LOAD_NO_HINTING | FT_LOAD_NO_AUTOHINT} on every glyph load that goes
 * through LWJGL's FreeType bindings, so FreeType never executes a font's TrueType
 * bytecode (fpgm, prep or glyph instructions). That interpreter is the entry point
 * of minecraft2shell.
 *
 * <p>The low-level {@code n*} methods are patched because every public overload
 * delegates to them.
 *
 * <p>Both flags are required: for so-called "tricky" fonts FreeType ignores
 * {@code FT_LOAD_NO_HINTING} on its own and hints anyway.
 *
 * <p>Parameter positions verified against LWJGL 3.4.3.
 */
@Mixin(value = FreeType.class, remap = false)
public abstract class FreeTypeMixin implements FontShieldMarker {

    @Unique
    private static final int FONTSHIELD_FLAGS = FreeType.FT_LOAD_NO_HINTING | FreeType.FT_LOAD_NO_AUTOHINT;

    // nFT_Load_Glyph(long face, int glyph_index, int load_flags)
    // nFT_Get_Advance(long face, int gindex, int load_flags, long padvance)
    @ModifyVariable(method = {"nFT_Load_Glyph", "nFT_Get_Advance"}, at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private static int fontshield$glyphFlags(int loadFlags) {
        return loadFlags | FONTSHIELD_FLAGS;
    }

    // nFT_Load_Char(long face, long char_code, int load_flags)
    @ModifyVariable(method = "nFT_Load_Char", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static int fontshield$charFlags(int loadFlags) {
        return loadFlags | FONTSHIELD_FLAGS;
    }

    // nFT_Get_Advances(long face, int start, int count, int load_flags, long padvances)
    @ModifyVariable(method = "nFT_Get_Advances", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private static int fontshield$advancesFlags(int loadFlags) {
        return loadFlags | FONTSHIELD_FLAGS;
    }

    // nFTC_ImageCache_LookupScaler / nFTC_SBitCache_LookupScaler
    //   (long cache, long scaler, long load_flags, int gindex, long out, long anode)
    @ModifyVariable(
        method = {"nFTC_ImageCache_LookupScaler", "nFTC_SBitCache_LookupScaler"},
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 2
    )
    private static long fontshield$scalerFlags(long loadFlags) {
        return loadFlags | FONTSHIELD_FLAGS;
    }

    // nFTC_ImageCache_Lookup / nFTC_SBitCache_Lookup
    //   (long cache, long type, int gindex, long out, long anode)
    // The load flags live in the FTC_ImageType struct that "type" points to.
    @ModifyVariable(
        method = {"nFTC_ImageCache_Lookup", "nFTC_SBitCache_Lookup"},
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 1
    )
    private static long fontshield$imageTypeFlags(long type) {
        if (type != MemoryUtil.NULL) {
            long flagsAddress = type + FTC_ImageType.FLAGS;
            MemoryUtil.memPutInt(flagsAddress, MemoryUtil.memGetInt(flagsAddress) | FONTSHIELD_FLAGS);
        }
        return type;
    }
}
