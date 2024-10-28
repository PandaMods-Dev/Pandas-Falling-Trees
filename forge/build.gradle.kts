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

	modApi("dev.architectury:architectury-forge:${properties["deps_architectury_version"]}")

	if (properties["MC_VER"].toString().toInt() <= properties["MC_1_20"].toString().toInt()) {
		modCompileOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-forge,${properties["deps_ht_treechop_mc_version"]}")
//		modRuntimeOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-forge,${properties["deps_ht_treechop_mc_version"]}")
	}

	if (properties["MC_VER"].toString().toInt() > properties["MC_1_19_2"].toString().toInt()) {
		modCompileOnly("maven.modrinth:jade:${properties["deps_jade_version"]}+forge-forge,${properties["deps_jade_mc_version"]}")
//        	modLocalRuntime("maven.modrinth:jade:${properties["deps_jade_version"]}+forge-forge,${properties["deps_jade_mc_version"]}")
	} else {
		modCompileOnly("maven.modrinth:jade:${properties["deps_jade_version"]}-forge,${properties["deps_jade_mc_version"]}")
//        	modLocalRuntime("maven.modrinth:jade:${properties["deps_jade_version"]}-forge,${properties["deps_jade_mc_version"]}")
	}

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionForge"))
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}