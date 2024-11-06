import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

architectury {
	common(properties["supported_mod_loaders"].toString().split(","))
}

loom.accessWidenerPath.set(file("src/main/resources/${properties["mod_id"]}.accesswidener"))

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")

	modImplementation("me.pandamods:pandalib-common:${properties["deps_pandalib_version"]}")
	modApi("dev.architectury:architectury:${properties["deps_architectury_version"]}")

	modImplementation("maven.modrinth:treechop:${properties["deps_ht_treechop_version"]}-fabric,${properties["deps_ht_treechop_mc_version"]}")

	modImplementation("maven.modrinth:jade:${properties["deps_jade_version"]}-fabric,${properties["deps_jade_mc_version"]}")
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}