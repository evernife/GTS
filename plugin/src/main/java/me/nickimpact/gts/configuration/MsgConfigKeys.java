/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.nickimpact.gts.configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.nickimpact.impactor.api.configuration.ConfigKey;
import com.nickimpact.impactor.api.configuration.IConfigKeys;
import com.nickimpact.impactor.api.configuration.keys.ListKey;
import com.nickimpact.impactor.api.configuration.keys.StringKey;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MsgConfigKeys implements IConfigKeys {

	// Plugin chat prefix (replacement option for {{gts_prefix}}
	public static final ConfigKey<String> PREFIX = StringKey.of("general.gts-prefix", "&eGTS &7\u00bb");
	public static final ConfigKey<String> ERROR_PREFIX = StringKey.of("general.gts-prefix-error", "&eGTS &7(&cERROR&7)");

	// Generic messages for the program
	// Best to support lists of text here, as a server may decide to go heavy on text formatting
	public static final ConfigKey<List<String>> MAX_LISTINGS = ListKey.of("general.max-listings", Lists.newArrayList(
			"{{gts_prefix}} &cUnfortunately, you can't deposit another listing, since you already have {{max_listings}} deposited..."
	));
	public static final ConfigKey<List<String>> ADD_TEMPLATE = ListKey.of("general.addition-to-seller", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7has been added to the market!"
	));
	public static final ConfigKey<List<String>> TAX_APPLICATION = ListKey.of("general.taxes.applied", Lists.newArrayList(
			"&c&l- {{tax}} &7(&aTaxes&7)"
	));
	public static final ConfigKey<List<String>> TAX_INVALID = ListKey.of("general.taxes.invalid", Lists.newArrayList(
			"{{gts_prefix}} &cUnable to afford the tax of &e{{tax}} &cfor this listing..."
	));
	public static final ConfigKey<List<String>> ADD_BROADCAST = ListKey.of("general.addition-broadcast", Lists.newArrayList(
			"{{gts_prefix}} &c{{player}} &7has added a &a{{listing_specifics}} &7to the GTS for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> PURCHASE_PAY = ListKey.of("general.prices.pay", Lists.newArrayList(
			"{{gts_prefix}} &7You have purchased a &a{{listing_specifics}} &7for &e{{price}}&7!"
	));
	public static final ConfigKey<List<String>> PURCHASE_RECEIVE = ListKey.of("general.prices.receive", Lists.newArrayList(
			"{{gts_prefix}} &a{{buyer}} &7purchased your &a{{listing_name}} &7listing for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_BID_BROADCAST = ListKey.of("general.auctions.bid", Lists.newArrayList(
			"{{gts_prefix}} &e{{player}} &7has placed a bid on the &a{{listing_specifics}}!"
	));
	public static final ConfigKey<List<String>> AUCTION_BID = ListKey.of("general.auctions.bid-personal", Lists.newArrayList(
			"{{gts_prefix}} &7Your bid has been placed! If you win, you will pay &e{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_WIN_BROADCAST = ListKey.of("general.auctions.win", Lists.newArrayList(
			"{{gts_prefix}} &e{{player}} &7has won the auction for the &a{{listing_specifics}}!"
	));
	public static final ConfigKey<List<String>> AUCTION_WIN = ListKey.of("general.auctions.win-personal", Lists.newArrayList(
			"{{gts_prefix}} &7Congrats! You've won the auction on the &e{{listing_specifics}} &7for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_SOLD = ListKey.of("general.auctions.sold", Lists.newArrayList(
			"{{gts_prefix}} &7Your &e{{listing_specifics}} &7auction was sold to &e{{high_bidder}} &7for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_IS_HIGH_BIDDER = ListKey.of("general.auctions.is-high-bidder", Lists.newArrayList(
			"{{gts_prefix}} &cHold off! You wouldn't want to bid against yourself!"
	));
	public static final ConfigKey<List<String>> REMOVAL_CHOICE = ListKey.of("general.removal.choice", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7listing has been returned!"
	));
	public static final ConfigKey<List<String>> REMOVAL_EXPIRES = ListKey.of("general.removal.expires", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7listing has expired, and has thus been returned!"
	));
	public static final ConfigKey<List<String>> MIN_PRICE_ERROR = ListKey.of("general.prices.min-price.invalid", Lists.newArrayList(
			"{{gts_error}} &7In order to sell your &a{{listing_name}}&7, you need to list it for the price of &e{{min_price}}&7..."
	));

	// Items
	public static final ConfigKey<String> UI_ITEMS_PLAYER_LISTINGS_TITLE = StringKey.of("item-displays.player-listings.title", "&eYour Listings");
	public static final ConfigKey<List<String>> UI_ITEMS_PLAYER_LISTINGS_LORE = ListKey.of("item-displays.player-listings.lore", Lists.newArrayList());

	// Entries
	public static final ConfigKey<List<String>> ENTRY_INFO = ListKey.of("entries.base-info", Lists.newArrayList(
			"",
			"&7Price: &e{{price}}",
			"&7Time Left: &e{{time_left}}"
	));
	public static final ConfigKey<List<String>> AUCTION_INFO = ListKey.of("entries.auction-info", Lists.newArrayList(
			"",
			"&7High Bidder: &e{{high_bidder}}",
			"&7Current Price: &e{{auc_price}}",
			"&7Increment: &e{{increment}}",
			"&7Time Left: &e{{time_left}}"
	));

	// Error messages
	public static final ConfigKey<List<String>> NOT_ENOUGH_FUNDS = ListKey.of("general.purchase.not-enough-funds", Lists.newArrayList("&cUnfortunately, you were unable to afford the price of {{price}}"));
	public static final ConfigKey<List<String>> ALREADY_CLAIMED = ListKey.of("general.purchase.already-claimed", Lists.newArrayList("&cUnfortunately, this listing has already been claimed..."));
	public static final ConfigKey<List<String>> ITEM_ENTRY_BASE_LORE = ListKey.of("entries.item.base.lore", Lists.newArrayList(
			"&7Seller: &e{{seller}}"
	));
	public static final ConfigKey<List<String>> ITEM_ENTRY_CONFIRM_LORE = ListKey.of("entries.item.confirm.lore", Lists.newArrayList(
			"&7Seller: &e{{seller}}"
	));
	public static final ConfigKey<String> ITEM_ENTRY_CONFIRM_TITLE = StringKey.of("entries.item.confirm.title", "&ePurchase {{item_title}}?");
	public static final ConfigKey<String> ITEM_ENTRY_BASE_TITLE = StringKey.of("entries.item.base.title", "{{item_title}}");
	public static final ConfigKey<String> ITEM_ENTRY_SPEC_TEMPLATE = StringKey.of("entries.item.spec-template", "{{item_title}}");

	public static final ConfigKey<String> DISCORD_PURCHASE = StringKey.of("discord.purchase", "{{buyer}} just purchased a {{listing_specifics}} from {{seller}} for {{price}}");
	public static final ConfigKey<String> DISCORD_REMOVE = StringKey.of("discord.purchase", "{{player}} has removed their {{listing_specifics}} from the GTS!");

	public static final ConfigKey<String> REFRESH_ICON = StringKey.of("item-displays.refresh-icon.title", "&eRefresh Listings");

	// -----------------------------------------------------------------------------
	// As of 4.1.4
	// -----------------------------------------------------------------------------
	public static final ConfigKey<String> FILTER_TITLE = StringKey.of("ui.main.filters.title", "&eShow only {{gts_entry_classification}}?");
	public static final ConfigKey<String> FILTER_STATUS_ENABLED = StringKey.of("ui.main.filters.status.enabled", "&7Status: &aEnabled");
	public static final ConfigKey<String> FILTER_STATUS_DISABLED = StringKey.of("ui.main.filters.status.disabled", "&7Status: &cDisabled");

	public static final ConfigKey<List<String>> FILTER_NOTES = ListKey.of("ui.main.filters.notes", Lists.newArrayList(
			"",
			"&bControls:",
			"&7Left Click: &aApply action",
			"&7Right Click: &aSwitch filter",
			"",
			"&bNOTE:",
			"&7This option will be overridden by",
			"&7the &eYour Listings &7option",
			"&7if it is enabled."
	));

	public static final ConfigKey<String> UI_TITLES_MAIN = StringKey.of("ui.main.title", "&cGTS &7\u00bb &3Listings");
	public static final ConfigKey<String> UI_TITLES_ITEMS = StringKey.of("ui.items.title", "&cGTS &7(&3Items&7)");
	public static final ConfigKey<String> UI_TITLES_CONFIRMATION = StringKey.of("ui.confirm.title", "&cGTS &7\u00bb &3Confirmation");
	public static final ConfigKey<String> UI_MAIN_NO_ENTRIES_AVAILABLE = StringKey.of("ui.main.no-entries-available", "&cNo Listing Types Available");

	public static final ConfigKey<String> TRANSLATIONS_YES = StringKey.of("translations.yes", "Yes");
	public static final ConfigKey<String> TRANSLATIONS_NO = StringKey.of("translations.no", "No");

	public static final ConfigKey<String> NO_PERMISSION = StringKey.of("general.errors.no-permission", "{{gts_error}} You don't have permission to use this!");
	public static final ConfigKey<String> PRICE_NOT_POSITIVE = StringKey.of("general.errors.non-positive-price", "{{gts_error}} Invalid price! Value must be positive!");
	public static final ConfigKey<String> PRICE_MAX_INVALID = StringKey.of("general.errors.max-price.invalid", "{{gts_error}} Your request is above the max amount of &e{{gts_max_price}}&7!");
	public static final ConfigKey<String> ERROR_BLACKLISTED = StringKey.of("general.errors.blacklisted", "{{gts_error}} Sorry, but &e{{gts_entry}} &7has been blacklisted from the GTS...");

	public static final ConfigKey<String> BUTTONS_INCREASE_CURRENCY_TITLE = StringKey.of("buttons.currency.increase.title", "&aIncrease Price Requested");
	public static final ConfigKey<List<String>> BUTTONS_INCREASE_CURRENCY_LORE = ListKey.of("buttons.currency.increase.lore", Lists.newArrayList(
			"&7Left Click: &b+{{gts_button_currency_left_click}}",
			"&7Right Click: &b+{{gts_button_currency_right_click}}",
			"&7Shift + Left Click: &b+{{gts_button_currency_shift_left_click}}",
			"&7Shift + Right Click: &b+{{gts_button_currency_shift_right_click}}"
	));

	public static final ConfigKey<String> BUTTONS_DECREASE_CURRENCY_TITLE = StringKey.of("buttons.currency.decrease.title", "&cDecrease Price Requested");
	public static final ConfigKey<List<String>> BUTTONS_DECREASE_CURRENCY_LORE = ListKey.of("buttons.currency.decrease.lore", Lists.newArrayList(
			"&7Left Click: &c-{{gts_button_currency_left_click}}",
			"&7Right Click: &c-{{gts_button_currency_right_click}}",
			"&7Shift + Left Click: &c-{{gts_button_currency_shift_left_click}}",
			"&7Shift + Right Click: &c-{{gts_button_currency_shift_right_click}}"
	));

	public static final ConfigKey<String> BUTTONS_INCREASE_TIME_TITLE = StringKey.of("buttons.time.increase.title", "&aIncrease Time");
	public static final ConfigKey<List<String>> BUTTONS_INCREASE_TIME_LORE = ListKey.of("buttons.time.increase.lore", Lists.newArrayList(
			"&7Left Click: &b+{{gts_button_time_left_click}}",
			"&7Right Click: &b+{{gts_button_time_right_click}}",
			"&7Shift + Left Click: &b+{{gts_button_time_shift_left_click}}",
			"&7Shift + Right Click: &b+{{gts_button_time_shift_right_click}}"
	));

	public static final ConfigKey<String> BUTTONS_DECREASE_TIME_TITLE = StringKey.of("buttons.time.decrease.title", "&cDecrease Time");
	public static final ConfigKey<List<String>> BUTTONS_DECREASE_TIME_LORE = ListKey.of("buttons.time.decrease.lore", Lists.newArrayList(
			"&7Left Click: &c-{{gts_button_time_left_click}}",
			"&7Right Click: &c-{{gts_button_time_right_click}}",
			"&7Shift + Left Click: &c-{{gts_button_time_shift_left_click}}",
			"&7Shift + Right Click: &c-{{gts_button_time_shift_right_click}}"
	));

	public static final ConfigKey<String> TIME_DISPLAY_TITLE = StringKey.of("buttons.time.display.title", "&eListing Time");
	public static final ConfigKey<List<String>> TIME_DISPLAY_LORE = ListKey.of("buttons.time.display.lore", Lists.newArrayList(
			"&7Target Time: &a{{gts_time}}",
			"",
			"&7Min Time: &a{{gts_min_time}}",
			"&7Max Time: &a{{gts_max_time}}"
	));

	public static final ConfigKey<String> PRICE_DISPLAY_TITLE = StringKey.of("buttons.currency.display.title", "&eListing Price");
	public static final ConfigKey<List<String>> PRICE_DISPLAY_LORE = ListKey.of("buttons.currency.display.lore", Lists.newArrayList(
			"&7Target Price: &a{{gts_price}}",
			"",
			"&7Min Price: &a{{gts_min_price}}",
			"&7Max Price: &a{{gts_max_price}}"
	));

	public static final ConfigKey<String> COMMANDS_ERROR_TIMEARG_IMPROPER = StringKey.of("commands.time.argument.improper", "The specified time is of an incorrect format, or breaches time constraints...");

	public static final ConfigKey<String> ITEMS_NONE_IN_HAND = StringKey.of("entries.items.command.none-in-hand", "{{gts_error}} Your hand has no item in it!");
	public static final ConfigKey<String> ITEMS_NO_CUSTOM_NAMES = StringKey.of("entries.items.generic.custom-name-restricted", "{{gts_error}} Your can't sell items with custom names!");
	public static final ConfigKey<String> ITEMS_INVENTORY_FULL = StringKey.of("entries.items.generic.inventory-full", "{{gts_error}} Your inventory is full, so we'll hold onto this item for you!");

	public static final ConfigKey<List<String>> DISCORD_PUBLISH_TEMPLATE = ListKey.of("discord.templates.publish", Lists.newArrayList(
			"Publisher: {{gts_publisher}}",
			"Publisher Identifier: {{gts_publisher_id}}",
			"",
			"Published Item: {{gts_published_item}}",
			"Item Details: {{gts_published_item_details}}",
			"Requested Price: {{gts_publishing_price}}",
			"Expiration Time: {{gts_publishing_expiration}}"
	));
	public static final ConfigKey<List<String>> DISCORD_PURCHASE_TEMPLATE = ListKey.of("discord.templates.purchase", Lists.newArrayList(
			"Buyer: {{gts_buyer}}",
			"Buyer Identifier: {{gts_buyer_id}}",
			"",
			"Seller: {{gts_seller}}",
			"Seller Identifier: {{gts_seller_id}}",
			"",
			"Item: {{gts_published_item}}",
			"Item Details: {{gts_published_item_details}}",
			"Price: {{gts_publishing_price}}"
	));
	public static final ConfigKey<List<String>> DISCORD_EXPIRATION_TEMPLATE = ListKey.of("discord.templates.expiration", Lists.newArrayList(
			"Publisher: {{gts_publisher}}",
			"Publisher Identifier: {{gts_publisher_id}}",
			"",
			"Item: {{gts_published_item}}",
			"Price: {{gts_publishing_price}}"
	));
	public static final ConfigKey<List<String>> DISCORD_REMOVAL_TEMPLATE = ListKey.of("discord.templates.removal", Lists.newArrayList(
			"Publisher: {{gts_publisher}}",
			"Publisher Identifier: {{gts_publisher_id}}",
			"",
			"Item: {{gts_published_item}}",
			"Item Details: {{gts_published_item_details}}"
	));

	private static Map<String, ConfigKey<?>> KEYS = null;
	@Override
	public synchronized Map<String, ConfigKey<?>> getAllKeys() {
		if(KEYS == null) {
			Map<String, ConfigKey<?>> keys = new LinkedHashMap<>();

			try {
				Field[] values = MsgConfigKeys.class.getFields();
				for(Field f : values) {
					if(!Modifier.isStatic(f.getModifiers()))
						continue;

					Object val = f.get(null);
					if(val instanceof ConfigKey<?>)
						keys.put(f.getName(), (ConfigKey<?>) val);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			KEYS = ImmutableMap.copyOf(keys);
		}

		return KEYS;
	}
}
