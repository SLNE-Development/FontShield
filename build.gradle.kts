plugins {
    id("net.fabricmc.fabric-loom") version "1.19-SNAPSHOT"
}

val minecraftVersion = providers.gradleProperty("minecraft_version").get()
val loaderVersion = providers.gradleProperty("loader_version").get()
val modVersion = providers.gradleProperty("mod_version").get()

group = providers.gradleProperty("maven_group").get()
version = "$modVersion+$minecraftVersion"

base {
    archivesName = "fontshield"
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_fontshield" }
    }
}
