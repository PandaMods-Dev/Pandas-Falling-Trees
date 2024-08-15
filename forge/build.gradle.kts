// gradle.properties
val modId: String by project

val forgeVersion: String by project

val htsTreechopVersion: String by project
val htsTreechopMinecraftVersion: String by project

val jadeVersion: String by project
val jadeMinecraftVersion: String by project

val MC_VER: String by project
val MC_1_19_2: String by project

architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)

	forge {
		convertAccessWideners.set(true)
		extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

		// Fixes Mixin Patcher issue with Forge
		useCustomMixin.set(false)

		mixinConfig("${modId}-common.mixins.json")
		mixinConfig("${modId}.mixins.json")
	}
}

configurations {
	getByName("developmentForge").extendsFrom(configurations["common"])
}

dependencies {
	forge("net.minecraftforge:forge:${forgeVersion}")

	if (MC_VER > MC_1_19_2) {
		"modCompileOnly"("maven.modrinth:jade:${jadeVersion}+forge-forge,${jadeMinecraftVersion}")
//        	"modLocalRuntime"("maven.modrinth:jade:${jadeVersion}+forge-forge,${jadeMinecraftVersion}")
	} else {
		"modCompileOnly"("maven.modrinth:jade:${jadeVersion}-forge,${jadeMinecraftVersion}")
//        	"modLocalRuntime"("maven.modrinth:jade:${jadeVersion}-forge,${jadeMinecraftVersion}")
	}

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionForge"))
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}