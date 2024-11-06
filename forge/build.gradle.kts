import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)

	forge {
		convertAccessWideners.set(true)
		extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

		mixinConfig("${properties["mod_id"]}-common.mixins.json")
		mixinConfig("${properties["mod_id"]}.mixins.json")
	}
}

configurations {
	getByName("developmentForge").extendsFrom(configurations["common"])
	// Required for embedding libraries into the jar because Forge is weird.
	getByName("forgeRuntimeLibrary").extendsFrom(configurations["jarShadow"])
}

dependencies {
	forge("net.minecraftforge:forge:${properties["forge_version"]}")

	modImplementation("me.pandamods:pandalib-forge:${properties["deps_pandalib_version"]}")
	modApi("dev.architectury:architectury-forge:${properties["deps_architectury_version"]}")

	modCompileOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-forge,${properties["deps_ht_treechop_mc_version"]}")
//	modRuntimeOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-forge,${properties["deps_ht_treechop_mc_version"]}")

	modCompileOnly("maven.modrinth:jade:${properties["deps_jade_version"]}-forge,${properties["deps_jade_mc_version"]}")
//	modLocalRuntime("maven.modrinth:jade:${properties["deps_jade_version"]}-forge,${properties["deps_jade_mc_version"]}")


	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionForge"))
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}