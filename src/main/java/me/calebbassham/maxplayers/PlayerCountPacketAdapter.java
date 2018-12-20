package me.calebbassham.maxplayers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PlayerCountPacketAdapter extends PacketAdapter {

    private MaxPlayers plugin;

    private PlayerCountPacketAdapter(MaxPlayers plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        var packet = event.getPacket().getServerPings().read(0);
        packet.setPlayersMaximum(plugin.getMaxPlayers());
    }

    public static PlayerCountPacketAdapter register(MaxPlayers plugin) {
        var instance = new PlayerCountPacketAdapter(plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(instance);
        return instance;
    }
}
