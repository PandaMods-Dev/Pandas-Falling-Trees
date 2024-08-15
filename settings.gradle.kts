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

val minecraftVersion: String by settings

fun loadProperties() {
	val defaultVersion = "1.21"

	val availableVersions: MutableList<String> = fileTree("versionProperties").files.map { file -> file.name }.toMutableList()
	for ((index, s) in availableVersions.withIndex()) {
		availableVersions[index] = s.replace(".properties", "")
	}

	availableVersions.sort()
	println("Available Minecraft versions: ${availableVersions}")

	var selectedVersion = minecraftVersion
	var versionIndex = availableVersions.indexOf(minecraftVersion)
	if (versionIndex == -1) {
		println("No 'minecraftVersion' set or the set 'minecraftVersion' is invalid! Defaulting to ${defaultVersion}.")
		selectedVersion = defaultVersion
		versionIndex = availableVersions.indexOf(defaultVersion)
	}

	println("Loading properties file at ${selectedVersion}.properties")
	val properties = Properties()
	properties.load(FileInputStream("${rootDir}/versionProperties/${selectedVersion}.properties"))

	for (property in properties) {
		gradle.extra.set(property.key.toString(), property.value.toString())
	}
	gradle.extra.set("availableVersions", availableVersions)
	gradle.extra.set("versionIndex", versionIndex)
}
loadProperties()

rootProject.name = "PandaLib"

include("common")
gradle.extra.properties["supportedModLoaders"].toString().split(",").forEach {
	println("Adding loader ${it}")
	include(it)
}
