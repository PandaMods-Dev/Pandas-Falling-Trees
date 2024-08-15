// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

val modmenuVersion: String by project

val htsTreechopVersion: String by project
val htsTreechopMinecraftVersion: String by project

val jadeVersion: String by project
val jadeMinecraftVersion: String by project

val MC_VER: String by project
val MC_1_19_2: String by project
val MC_1_20: String by project

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
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

	modApi("com.terraformersmc:modmenu:${modmenuVersion}")

	if (MC_VER <= MC_1_20) {
		modCompileOnly("maven.modrinth:treechop:${htsTreechopVersion}-fabric,${htsTreechopMinecraftVersion}")
//		modRuntimeOnly("maven.modrinth:treechop:${htsTreechopVersion}-fabric,${htsTreechopMinecraftVersion}")
	}

	if (MC_VER > MC_1_19_2) {
		modCompileOnly("maven.modrinth:jade:${jadeVersion}+fabric-fabric,${jadeMinecraftVersion}")
//		modLocalRuntime("maven.modrinth:jade:${jadeVersion}+fabric-fabric,${jadeMinecraftVersion}")
	} else {
		modCompileOnly("maven.modrinth:jade:${jadeVersion}-fabric,${jadeMinecraftVersion}")
//		modLocalRuntime("maven.modrinth:jade:${jadeVersion}-fabric,${jadeMinecraftVersion}")
	}

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionFabric"))
}

tasks.remapJar {
	injectAccessWidener.set(true)
}