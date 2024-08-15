// gradle.properties
val modId: String by project
val supportedModLoaders: String by project

val minecraftVersion: String by project
val fabricLoaderVersion: String by project

val jadeVersion: String by project
val jadeMinecraftVersion: String by project

val htsTreechopVersion: String by project
val htsTreechopMinecraftVersion: String by project

val MC_VER: String by project
val MC_1_19_2: String by project
val MC_1_20: String by project

architectury {
	common(supportedModLoaders.split(","))
}

loom.accessWidenerPath.set(file("src/main/resources/${modId}.accesswidener"))

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")

	if (MC_VER > MC_1_19_2) {
		modImplementation("maven.modrinth:jade:${jadeVersion}+fabric-fabric,${jadeMinecraftVersion}")
	} else {
		modImplementation("maven.modrinth:jade:${jadeVersion}-fabric,${jadeMinecraftVersion}")
	}

	if (MC_VER <= MC_1_20)
        modImplementation("maven.modrinth:treechop:${htsTreechopVersion}-fabric,${htsTreechopMinecraftVersion}")
}