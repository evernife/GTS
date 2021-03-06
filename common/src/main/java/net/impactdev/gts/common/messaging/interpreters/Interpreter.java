package net.impactdev.gts.common.messaging.interpreters;

import net.impactdev.gts.common.plugin.GTSPlugin;

public interface Interpreter {

    void register(GTSPlugin plugin);

    void getDecoders(GTSPlugin plugin);

    void getInterpreters(GTSPlugin plugin);

}
