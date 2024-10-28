architectury {
	common(properties["supported_mod_loaders"].toString().split(","))
}

loom.accessWidenerPath.set(file("src/main/resources/${properties["mod_id"]}.accesswidener"))

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")

	modApi("dev.architectury:architectury:${properties["deps_architectury_version"]}")

	if (properties["MC_VER"].toString().toInt() <= properties["MC_1_20"].toString().toInt())
		modImplementation("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-fabric,${properties["deps_ht_treechop_mc_version"]}")

	if (properties["MC_VER"].toString().toInt() > properties["MC_1_19_2"].toString().toInt()) {
		modImplementation("maven.modrinth:jade:${properties["deps_jade_version"]}+fabric-fabric,${properties["deps_jade_mc_version"]}")
	} else {
		modImplementation("maven.modrinth:jade:${properties["deps_jade_version"]}-fabric,${properties["deps_jade_mc_version"]}")
	}
}