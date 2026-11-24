<img src="src/main/resources/assets/fontshield/icon.png" width="96" align="right" alt="">

# FontShield

[![Build](https://github.com/Keviro/FontShield/actions/workflows/build.yml/badge.svg)](https://github.com/Keviro/FontShield/actions/workflows/build.yml)

Client-side Fabric mod for Minecraft 26.3 that mitigates **minecraft2shell**, the remote code execution via crafted fonts in server resource packs.

FontShield makes FreeType load every glyph without hinting, so TrueType bytecode from a font is never executed. That is where the exploit starts.

## Install

Put the jar from [Releases](https://github.com/Keviro/FontShield/releases) into your `mods` folder. Requires Fabric Loader, no Fabric API needed.

If the patch cannot be applied, the game refuses to start instead of running unprotected.

## Build

```
./gradlew build
```

Requires JDK 25. The jar ends up in `build/libs`.

---

Fonts from resource packs may look slightly softer. Remove the mod once Mojang ships a fixed version.
