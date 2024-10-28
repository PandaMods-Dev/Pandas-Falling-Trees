architectury {
	platformSetupLoomIde()
	neoForge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

dependencies {
	neoForge("net.neoforged:neoforge:${properties["neoforge_version"]}")

	modApi("dev.architectury:architectury-neoforge:${properties["deps_architectury_version"]}")

	if (properties["MC_VER"].toString().toInt() > properties["MC_1_19_2"].toString().toInt()) {
		"modCompileOnly"("maven.modrinth:jade:${properties["deps_jade_version"]}+neoforge-neoforge,${properties["deps_jade_mc_version"]}")
//		"modLocalRuntime"("maven.modrinth:jade:${properties["deps_jade_version"]}+neoforge-neoforge,${properties["deps_jade_mc_version"]}")
	} else {
		"modCompileOnly"("maven.modrinth:jade:${properties["deps_jade_version"]}-neoforge,${properties["deps_jade_mc_version"]}")
//		"modLocalRuntime"("maven.modrinth:jade:${properties["deps_jade_version"]}-neoforge,${properties["deps_jade_mc_version"]}")
	}

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionNeoForge"))
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}