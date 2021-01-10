/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.offer;

import bisq.network.p2p.storage.payload.ExpirablePayload;
import bisq.network.p2p.storage.payload.ProtectedStoragePayload;
import bisq.network.p2p.storage.payload.RequiresOwnerIsOnlinePayload;

import bisq.common.proto.ProtoUtil;

import java.util.concurrent.TimeUnit;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class OfferPayload implements ProtectedStoragePayload, ExpirablePayload, RequiresOwnerIsOnlinePayload {
    public static final long TTL = TimeUnit.MINUTES.toMillis(9);

    // Keys for extra map
    // Only set for fiat offers
    public static final String ACCOUNT_AGE_WITNESS_HASH = "accountAgeWitnessHash";
    public static final String REFERRAL_ID = "referralId";
    // Only used in payment method F2F
    public static final String F2F_CITY = "f2fCity";
    public static final String F2F_EXTRA_INFO = "f2fExtraInfo";
    // Comma separated list of ordinal of a bisq.common.app.Capability. E.g. ordinal of
    // Capability.SIGNED_ACCOUNT_AGE_WITNESS is 11 and Capability.MEDIATION is 12 so if we want to signal that maker
    // of the offer supports both capabilities we add "11, 12" to capabilities.
    public static final String CAPABILITIES = "capabilities";
    // If maker is seller and has xmrAutoConf enabled it is set to "1" otherwise it is not set
    public static final String XMR_AUTO_CONF = "xmrAutoConf";
    public static final String XMR_AUTO_CONF_ENABLED_VALUE = "1";

    public enum Direction {
        BUY,
        SELL;

        public static OfferPayload.Direction fromProto(protobuf.OfferPayload.Direction direction) {
            return ProtoUtil.enumFromProto(OfferPayload.Direction.class, direction.name());
        }

        public static protobuf.OfferPayload.Direction toProtoMessage(Direction direction) {
            return protobuf.OfferPayload.Direction.valueOf(direction.name());
        }
    }
}
