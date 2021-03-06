package me.arasple.mc.trmenu

import io.izzel.taboolib.TabooLib
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.locale.TLocaleLoader
import me.arasple.mc.trmenu.modules.command.registerable.RegisterCommands
import me.arasple.mc.trmenu.modules.conf.MenuLoader
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.function.Shortcuts
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import me.arasple.mc.trmenu.util.Watchers
import me.clip.placeholderapi.PlaceholderAPIPlugin
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2020/2/29 17:27
 */
class TrMenuLoader {

    fun init() {
        TrMenu.SETTINGS.listener { register() }
        if (!TrMenu.SETTINGS.getBoolean("Options.Hide-Logo", false)) {
            printLogo()
            TLocale.sendToConsole("PLUGIN.LOADING", Version.getBukkitVersion())
        }
        register()
    }

    fun active() {
        HookInstance.init()
        TLocale.sendToConsole("PLUGIN.LOADED", TrMenu.plugin.description.version)
        MenuLoader.loadMenus()
    }

    fun cancel() {
        Menu.getAllMenus().flatMap { it.value }.forEach { it.close(true) }
        PlaceholderAPIPlugin.getInstance().localExpansionManager.findExpansionByIdentifier("trmenu").ifPresent {
            it.unregister()
        }
        Watchers.watcher.unregisterAll()
    }

    private fun register() {
        RegisterCommands.load()
        Shortcuts.load()
    }

    private fun printLogo() = arrayOf(
            "§8  ___________         _____                        ________",
            "§8    \\__    ___/______  /     \\   ____   ____  __ __  \\_____  \\",
            "§8    |    |  \\_  __ \\/  \\ /  \\_/ __ \\ /    \\|  |  \\  /  ____/",
            "§8    |    |   |  | \\/    Y    \\  ___/|   |  \\  |  / /       \\",
            "§8    |____|   |__|  \\____|__  /\\___  >___|  /____/  \\_______ \\",
            "§8    \\/     \\/     \\/                \\/",
            "                                                      "
    ).let {
        it.forEachIndexed { index, raw ->
            if (raw.isNotBlank()) {
                val line = raw.toCharArray()
                val width = (3..6).random()
                var randomIndex: Int
                do {
                    randomIndex = (2..line.size - width).random()
                } while (String(line.copyOfRange(randomIndex, randomIndex + width)).isBlank())
                val replace = String(line.copyOfRange(randomIndex, randomIndex + width))
                it[index] = String(line).replaceFirst(replace, "§${arrayOf('9', 'b', '3').random()}$replace§8")
            }
        }
        Bukkit.getConsoleSender().sendMessage(it)
    }


}