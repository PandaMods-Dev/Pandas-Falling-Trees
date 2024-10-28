import java.io.FileInputStream
import java.util.*

pluginManagement.repositories {
	maven {
		name = "Architectury"
		url = uri("https://maven.architectury.dev/")
	}
	maven {
		name = "Fabric"
		url = uri("https://maven.fabricmc.net/")
	}
	maven {
		name = "Forge"
		url = uri("https://maven.minecraftforge.net/")
	}
	maven {
		name = "NeoForge"
		url = uri("https://maven.neoforged.net/releases/")
	}
	gradlePluginPortal()
}

val minecraft_version: String by settings

fun loadProperties() {
	val defaultVersion = "1.21"

	val availableVersions: MutableList<String> = fileTree("versionProperties").files.map { file -> file.name }.toMutableList()
	for ((index, s) in availableVersions.withIndex()) {
		availableVersions[index] = s.replace(".properties", "")
	}

	availableVersions.sort()
	println("Available Minecraft versions: ${availableVersions}")

	var selectedVersion = minecraft_version
	var versionIndex = availableVersions.indexOf(minecraft_version)
	if (versionIndex == -1) {
		println("No 'minecraft_version' set or the set 'minecraft_Version' is invalid! Defaulting to ${defaultVersion}.")
		selectedVersion = defaultVersion
		versionIndex = availableVersions.indexOf(defaultVersion)
	}

	println("Loading properties file at ${selectedVersion}.properties")
	val properties = Properties()
	properties.load(FileInputStream("${rootDir}/versionProperties/${selectedVersion}.properties"))

	for (property in properties) {
		gradle.extra.set(property.key.toString(), property.value.toString())
	}
	gradle.extra.set("available_versions", availableVersions)
	gradle.extra.set("version_index", versionIndex)
}
loadProperties()

rootProject.name = "Falling Trees"

include("common")
gradle.extra.properties["supported_mod_loaders"].toString().split(",").forEach {
	println("Adding loader ${it}")
	include(it)
}