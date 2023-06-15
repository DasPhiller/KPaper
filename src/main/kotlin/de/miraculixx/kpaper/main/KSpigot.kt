package de.miraculixx.kpaper.main

import de.miraculixx.kpaper.gui.GUIHolder
import de.miraculixx.kpaper.extensions.kotlin.closeIfInitialized
import de.miraculixx.kpaper.runnables.KRunnableHolder
import org.bukkit.plugin.java.JavaPlugin

/**
 * The main plugin instance. Available with public visibility.
 */
val KSpigotMainInstance: KSpigot get() = PluginInstance

/**
 * The main plugin instance. Less complicated name for internal usage.
 */
@PublishedApi
internal lateinit var PluginInstance: KSpigot
    private set

/**
 * This is the main instance of KSpigot.
 *
 * This class replaces (and inherits from) the
 * JavaPlugin class. Your main plugin class should
 * inherit from this abstract class.
 *
 * **Instead** of overriding [onLoad()], [onEnable()]
 * and [onDisable()] **override**:
 * - [load()] (called first)
 * - [startup()]  (called second)
 * - [shutdown()] (called in the "end")
 */
abstract class KSpigot : JavaPlugin() {
    // lazy properties
    private val kRunnableHolderProperty = lazy { KRunnableHolder }
    private val guiHolderProperty = lazy { GUIHolder }
    internal val kRunnableHolder by kRunnableHolderProperty
    internal val guiHolder by guiHolderProperty

    /**
     * Called when the plugin was loaded
     */
    open fun load() {}

    /**
     * Called when the plugin was enabled
     */
    open fun startup() {}

    /**
     * Called when the plugin gets disabled
     */
    open fun shutdown() {}

    final override fun onLoad() {
        if (::PluginInstance.isInitialized) {
            server.logger.warning("The main instance of KPaper has been modified, even though it has already been set by another plugin!")
        }
        PluginInstance = this
        load()
    }

    final override fun onEnable() {
        startup()
    }

    final override fun onDisable() {
        shutdown()
        // avoid unnecessary load of lazy properties
        kRunnableHolderProperty.closeIfInitialized()
        guiHolderProperty.closeIfInitialized()
    }
}