package com.songoda.epicbuckets.listener;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SourceBlockBreakListener implements Listener {

    private EpicBuckets epicBuckets;

    public SourceBlockBreakListener() {
        epicBuckets = EpicBuckets.getInstance();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        epicBuckets.getGenbucketManager().getActiveGens().forEach((uuid, genbuckets) -> {
            if (genbuckets.size() > 0) {
                genbuckets.forEach(genbucket -> {
                    if (genbucket.getSourceBlock().getLocation() == e.getBlock().getLocation()) {
                        genbucket.getGeneration().cancel();
                        return;
                    }
                });
            }
        });
    }

}
