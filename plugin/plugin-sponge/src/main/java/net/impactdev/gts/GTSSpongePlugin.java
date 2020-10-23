package net.impactdev.gts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.impactdev.gts.commands.GTSCommandManager;
import net.impactdev.gts.listeners.PingListener;
import net.impactdev.gts.listings.SpongeItemEntry;
import net.impactdev.gts.listings.data.SpongeItemManager;
import net.impactdev.gts.listings.legacy.SpongeLegacyItemStorable;
import net.impactdev.gts.messaging.interpreters.SpongeBINInterpreters;
import net.impactdev.impactor.api.Impactor;
import net.impactdev.impactor.api.configuration.Config;
import net.impactdev.impactor.api.dependencies.Dependency;
import net.impactdev.impactor.api.plugin.PluginMetadata;
import net.impactdev.impactor.api.storage.StorageType;
import net.impactdev.impactor.sponge.configuration.SpongeConfig;
import net.impactdev.impactor.sponge.configuration.SpongeConfigAdapter;
import net.impactdev.impactor.sponge.plugin.AbstractSpongePlugin;
import net.impactdev.gts.api.GTSService;
import net.impactdev.gts.api.blacklist.Blacklist;
import net.impactdev.gts.api.exceptions.LackingServiceException;
import net.impactdev.gts.api.extension.ExtensionManager;
import net.impactdev.gts.api.listings.auctions.Auction;
import net.impactdev.gts.api.listings.buyitnow.BuyItNow;
import net.impactdev.gts.api.stashes.Stash;
import net.impactdev.gts.api.storage.GTSStorage;
import net.impactdev.gts.common.api.ApiRegistrationUtil;
import net.impactdev.gts.common.api.GTSAPIProvider;
import net.impactdev.gts.common.blacklist.BlacklistImpl;
import net.impactdev.gts.common.config.updated.ConfigKeys;
import net.impactdev.gts.common.config.MsgConfigKeys;
import net.impactdev.gts.common.data.ResourceManagerImpl;
import net.impactdev.gts.common.extension.SimpleExtensionManager;
import net.impactdev.gts.common.messaging.InternalMessagingService;
import net.impactdev.gts.common.messaging.MessagingFactory;
import net.impactdev.gts.common.plugin.GTSPlugin;
import net.impactdev.gts.common.storage.StorageFactory;
import net.impactdev.gts.sponge.manager.SpongeListingManager;
import net.impactdev.gts.messaging.SpongeMessagingFactory;
import net.impactdev.gts.messaging.interpreters.SpongePingPongInterpreter;
import net.impactdev.gts.sponge.listings.SpongeAuction;
import net.impactdev.gts.sponge.listings.SpongeBuyItNow;
import net.impactdev.gts.sponge.pricing.provided.MonetaryPrice;
import net.impactdev.gts.sponge.stash.SpongeStash;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class GTSSpongePlugin extends AbstractSpongePlugin implements GTSPlugin {

	private final GTSSpongeBootstrap bootstrap;

	private Config config;
	private Config msgConfig;

	private GTSStorage storage;

	private InternalMessagingService messagingService;

	private SimpleExtensionManager extensionManager;

	public GTSSpongePlugin(GTSSpongeBootstrap bootstrap, org.slf4j.Logger fallback) {
		super(PluginMetadata.builder()
				.id("gts")
				.name("GTS")
				.version("@version@")
				.description("@gts_description@")
				.build(),
				fallback
		);
		this.bootstrap = bootstrap;
	}

	public void preInit() {
		ApiRegistrationUtil.register(new GTSAPIProvider());
		Impactor.getInstance().getRegistry().register(GTSPlugin.class, this);
		Impactor.getInstance().getRegistry().register(Blacklist.class, new BlacklistImpl());

		this.displayBanner();

		ApiRegistrationUtil.register(new GTSAPIProvider());
		Sponge.getServiceManager().setProvider(this.bootstrap, GTSService.class, GTSService.getInstance());
		this.supplyBuilders();

		Impactor.getInstance().getRegistry().register(SpongeListingManager.class, new SpongeListingManager());

		GTSService.getInstance().getGTSComponentManager().registerListingResourceManager(BuyItNow.class, new ResourceManagerImpl<>("BIN", "minecraft:emerald", SpongeBuyItNow::deserialize));
		GTSService.getInstance().getGTSComponentManager().registerListingResourceManager(Auction.class, new ResourceManagerImpl<>("Auctions", "minecraft:gold_ingot", SpongeAuction::deserialize));
		GTSService.getInstance().getGTSComponentManager().registerEntryManager(SpongeItemEntry.class, new SpongeItemManager());
		GTSService.getInstance().getGTSComponentManager().registerPriceManager(MonetaryPrice.class, new MonetaryPrice.MonetaryPriceManager());
		GTSService.getInstance().getGTSComponentManager().registerLegacyEntryDeserializer("item", new SpongeLegacyItemStorable());

		this.config = new SpongeConfig(new SpongeConfigAdapter(this, new File(this.getConfigDir().toFile(), "main.conf")), new ConfigKeys());
		this.msgConfig = new SpongeConfig(new SpongeConfigAdapter(this, new File(this.getConfigDir().toFile(), "lang/en_us.conf")), new MsgConfigKeys());

		this.extensionManager = new SimpleExtensionManager(this);
		this.extensionManager.loadExtensions(this.getBootstrap().getConfigDirectory().resolve("extensions"));
	}

	public void init() {
		this.applyMessagingServiceSettings();

		Impactor.getInstance().getEventBus().subscribe(new PingListener());
		new GTSCommandManager(this.bootstrap.getContainer()).register();

		this.storage = new StorageFactory(this).getInstance(StorageType.H2);

		this.extensionManager.enableExtensions();
	}

	public void started() {
		if(!Sponge.getServiceManager().isRegistered(EconomyService.class)) {
			throw new LackingServiceException(EconomyService.class);
		}
		MonetaryPrice.setEconomy(this.getEconomy());
	}

	public MessagingFactory<?> getMessagingFactory() {
		return new SpongeMessagingFactory(this);
	}

	public EconomyService getEconomy() {
		return Sponge.getServiceManager().provideUnchecked(EconomyService.class);
	}

	public PluginContainer getPluginContainer() {
		return this.bootstrap.getContainer();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends GTSPlugin> T as(Class<T> type) {
		if(!type.isAssignableFrom(this.getClass())) {
			throw new RuntimeException("Invalid plugin typing");
		}
		return (T) this;
	}

	@Override
	public GTSSpongeBootstrap getBootstrap() {
		return this.bootstrap;
	}

	@Override
	public Gson getGson() {
		return new GsonBuilder().create();
	}

	@Override
	public GTSStorage getStorage() {
		return this.storage;
	}

	@Override
	public ExtensionManager getExtensionManager() {
		return this.extensionManager;
	}

	@Override
	public InternalMessagingService getMessagingService() {
		return this.messagingService;
	}

	@Override
	public Path getConfigDir() {
		return this.bootstrap.getConfigDirectory();
	}

	@Override
	public Config getConfiguration() {
		return this.config;
	}

	@Override
	public List<Dependency> getAllDependencies() {
		return Lists.newArrayList(
				Dependency.KYORI_TEXT,
				Dependency.KYORI_TEXT_SERIALIZER_LEGACY,
				Dependency.KYORI_TEXT_SERIALIZER_GSON,
				Dependency.KYORI_TEXT_ADAPTER_SPONGEAPI,
				Dependency.CAFFEINE,
				Dependency.ACF_SPONGE
		);
	}

	@Override
	public List<StorageType> getStorageRequirements() {
		return Lists.newArrayList();
	}

	@Override
	public boolean inDebugMode() {
		return this.getConfiguration().get(ConfigKeys.DEBUG_ENABLED);
	}

	@Override
	public Config getMsgConfig() {
		return this.msgConfig;
	}

	private void displayBanner() {
		List<String> output = Lists.newArrayList(
				"",
				"&3     _________________",
				"&3    / ____/_  __/ ___/       &aGTS " + this.getMetadata().getVersion(),
				"&3   / / __  / /  \\__ \\        &aRunning on: &e" + Sponge.getGame().getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName() + " " + Sponge.getGame().getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getVersion().orElse(""),
				"&3  / /_/ / / /  ___/ /        &aAuthor: &3NickImpact",
				"&3  \\____/ /_/  /____/",
				""
		);

		GTSPlugin.getInstance().getPluginLogger().noTag(output);
	}

	private void applyMessagingServiceSettings() {
		this.messagingService = this.getMessagingFactory().getInstance();
		new SpongePingPongInterpreter().register(this);
		new SpongeBINInterpreters().register(this);
	}

	private void supplyBuilders() {
		Impactor.getInstance().getRegistry().registerBuilderSupplier(Auction.AuctionBuilder.class, SpongeAuction.SpongeAuctionBuilder::new);
		Impactor.getInstance().getRegistry().registerBuilderSupplier(BuyItNow.BuyItNowBuilder.class, SpongeBuyItNow.SpongeBuyItNowBuilder::new);
		Impactor.getInstance().getRegistry().registerBuilderSupplier(Stash.StashBuilder.class, SpongeStash.SpongeStashBuilder::new);
	}

	@Override
	public ImmutableList<StorageType> getMultiServerCompatibleStorageOptions() {
		return ImmutableList.copyOf(Lists.newArrayList(
				StorageType.MARIADB,
				StorageType.MYSQL,
				StorageType.MONGODB,
				StorageType.POSTGRESQL
		));
	}

}
