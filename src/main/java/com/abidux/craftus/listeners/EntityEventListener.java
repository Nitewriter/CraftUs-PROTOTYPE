package com.abidux.craftus.listeners;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.events.GameMatchPlayerAttackedEvent;
import com.abidux.craftus.game.GameMatch;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.repository.PlayerRepository;
import com.abidux.craftus.utils.PlayerEvaluation;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Singleton
public class EntityEventListener implements Listener {

    private final CraftUs plugin;
    private final PlayerRepository playerRepository;

    @Inject
    public EntityEventListener(CraftUs plugin, PlayerRepository playerRepository) {
        this.plugin = plugin;
        this.playerRepository = playerRepository;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!PlayerEvaluation.isPlayer(event.getEntity())) {
            return;
        }
        if (!PlayerEvaluation.isPlayer(event.getDamager())) {
            return;
        }

        CraftUsPlayer victim = playerRepository.getPlayerForId(event.getEntity().getUniqueId());
        CraftUsPlayer attacker = playerRepository.getPlayerForId(event.getDamager().getUniqueId());
        if (victim == null || attacker == null) {
            return;
        }
        if (victim.isNotPlayingMatch() || attacker.isNotPlayingMatch()) {
            return;
        }
        if (victim.getCurrentMatch() != attacker.getCurrentMatch()) {
            return;
        }

        event.setCancelled(true);
        GameMatch gameMatch = victim.getCurrentMatch();
        GameMatchPlayerAttackedEvent attackedPlayerEvent = new GameMatchPlayerAttackedEvent(
                attacker,
                victim,
                gameMatch
        );
        Bukkit.getPluginManager().callEvent(attackedPlayerEvent);
    }
}
