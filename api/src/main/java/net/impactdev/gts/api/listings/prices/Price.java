package net.impactdev.gts.api.listings.prices;

import net.impactdev.gts.api.data.Storable;
import net.impactdev.gts.api.listings.makeup.Display;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents a value which a player will pay to purchase (or bid) on a listing.
 *
 * @param <P> The element that is controlled by this interface
 * @param <S> A potentially populated field indicating payment source from the player
 * @param <I> The display output type for this price
 */
public interface Price<P, S, I> extends Storable {

	/**
	 * The instance being setup as the price. So this might be a double or BigDecimal to represent some form of monetary
	 * value, or another instance to represent something more.
	 *
	 * @return The instance being used as the mark for the price
	 */
	P getPrice();

	/**
	 * Represents the output of the price as it is to be displayed to the user querying the listing. In more general terms,
	 * this would represent the value, so for a dollar based currency, this would be something like "$500"
	 *
	 * @return The textual representation of the price
	 */
	TextComponent getText();

	/**
	 * Represents the look of a price should it be held in reserve for a player to accept or log back in, depending
	 * on the mode selected by the server configuration.
	 *
	 * @return The displayable representation of a price
	 */
	Display<I> getDisplay();

	/**
	 * Determines whether or not the user paying for the listing can actually pay the price. If they can, this call will
	 * return true. Otherwise, this call will return false to mark that any further action should be cancelled.
	 *
	 * @param payer The user paying for the listing
	 * @return True if the user can pay, false otherwise
	 */
	boolean canPay(UUID payer);

	/**
	 * Processes the payment for the user paying for the listing. This call should be made after verifying the user
	 * can actually complete the payment, via {@link #canPay(UUID)}.
	 *
	 * @param payer The user paying for the listing
	 */
	void pay(UUID payer, @Nullable Object source);

	/**
	 * Processes the receiving end of a payment. Sometimes, a price may be unable to be completed due to offline
	 * restrictions, so this call may temporarily store the payment as a later receivable for the user intended
	 * to receive the payment.
	 *
	 * @param recipient The user receiving the payment
	 */
	boolean reward(UUID recipient);

	/**
	 * Represents the typing of the source that should be applied to a listings price
	 *
	 * @return A type token wrapping the source type
	 */
	Class<S> getSourceType();

	/**
	 * Calculates and returns the fee a user should pay for selecting this price
	 *
	 * @param listingType <code>true</code> if BIN, <code>false</code> if Auction
	 * @return The fee a user is to pay for this price
	 */
	long calculateFee(boolean listingType);

}
