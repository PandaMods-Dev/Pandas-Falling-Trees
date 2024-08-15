// gradle.properties
val neoForgeVersion: String by project

val jadeVersion: String by project
val jadeMinecraftVersion: String by project

val MC_VER: String by project
val MC_1_19_2: String by project

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

dependencies {
	neoForge("net.neoforged:neoforge:${neoForgeVersion}")

	if (MC_VER > MC_1_19_2) {
		"modCompileOnly"("maven.modrinth:jade:${jadeVersion}+neoforge-neoforge,${jadeMinecraftVersion}")
//		"modLocalRuntime"("maven.modrinth:jade:${jadeVersion}+neoforge-neoforge,${jadeMinecraftVersion}")
	} else {
		"modCompileOnly"("maven.modrinth:jade:${jadeVersion}-neoforge,${jadeMinecraftVersion}")
//		"modLocalRuntime"("maven.modrinth:jade:${jadeVersion}-neoforge,${jadeMinecraftVersion}")
	}

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionNeoForge"))
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}