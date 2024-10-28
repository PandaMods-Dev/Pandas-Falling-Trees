architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
	modApi("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

	modApi("dev.architectury:architectury-fabric:${properties["deps_architectury_version"]}")
	modApi("com.terraformersmc:modmenu:${properties["deps_modmenu_version"]}")

	if (properties["MC_VER"].toString().toInt() <= properties["MC_1_20"].toString().toInt()) {
		modCompileOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-fabric,${properties["deps_ht_treechop_mc_version"]}")
//		modRuntimeOnly("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-fabric,${properties["deps_ht_treechop_mc_version"]}")
	}

	if (properties["MC_VER"].toString().toInt() > properties["MC_1_19_2"].toString().toInt()) {
		modCompileOnly("maven.modrinth:jade:${properties["deps_jade_version"]}+fabric-fabric,${properties["deps_jade_mc_version"]}")
//		modLocalRuntime("maven.modrinth:jade:${properties["deps_jade_version"]}+fabric-fabric,${properties["deps_jade_mc_version"]}")
	} else {
		modCompileOnly("maven.modrinth:jade:${properties["deps_jade_version"]}-fabric,${properties["deps_jade_mc_version"]}")
//		modLocalRuntime("maven.modrinth:jade:${properties["deps_jade_version"]}-fabric,${properties["deps_jade_mc_version"]}")
	}

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionFabric"))
}

tasks.remapJar {
	injectAccessWidener.set(true)
}