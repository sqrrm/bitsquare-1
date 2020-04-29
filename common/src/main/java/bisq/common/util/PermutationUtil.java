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

package bisq.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PermutationUtil {

    /**
     *
     * Finds a permutation of the list which matches the given predicate. The list is ordered and the algorithm does
     * not (need to) support changes in the order.
     *
     * Example of all iterations for a 5 element list
     * [0, 1, 2, 3, 4]
     * [1, 2, 3, 4]
     * [0, 2, 3, 4]
     * [0, 1, 3, 4]
     * [0, 1, 2, 4]
     * [0, 1, 2, 3]
     * [2, 3, 4]
     * [1, 3, 4]
     * [1, 2, 4]
     * [1, 2, 3]
     * [0, 3, 4]
     * [0, 2, 4]
     * [0, 2, 3]
     * [0, 1, 4]
     * [0, 1, 3]
     * [0, 1, 2]
     * [3, 4]
     * [2, 4]
     * [2, 3]
     * [1, 4]
     * [1, 3]
     * [1, 2]
     * [0, 4]
     * [0, 3]
     * [0, 2]
     * [0, 1]
     * [4]
     * [3]
     * [2]
     * [1]
     * [0]
     * []
     *
     * @param targetValue           Expected value for testing predicate
     * @param list                  List from which we want to find a permutation matching our predicate condition
     * @param predicate             Predicate used to determine our matching criteria
     * @param maxNumPredicateTests  Max. number of predicate tests
     * @param <T>                   Type of list items
     * @param <R>                   Type of predicate targetValue
     * @return Permutated list which matches our predicate. If nothing found or the limit is
     *                              reached we return an empty Array
     */
    public static <T, R> List<T> findMatchingPermutation(R targetValue,
                                                         List<T> list,
                                                         BiFunction<R, List<T>, Boolean> predicate,
                                                         int maxNumPredicateTests) {
        AtomicInteger numRemainingTests = new AtomicInteger(maxNumPredicateTests);
        numRemainingTests.getAndDecrement();
        if (predicate.apply(targetValue, list)) {
            return list;
        } else {
            return findMatchingPermutation(targetValue,
                    list,
                    predicate,
                    maxNumPredicateTests,
                    numRemainingTests);
        }
    }

    private static <T, R> List<T> findMatchingPermutation(R targetValue,
                                                          List<T> list,
                                                          BiFunction<R, List<T>, Boolean> predicate,
                                                          int maxNumPredicateTests,
                                                          AtomicInteger numRemainingTests) {
        for (int level = 0; level < list.size(); level++) {
            // Test one level at a time
            var result = checkLevel(targetValue, list, predicate, level, 0, numRemainingTests);
            if (!result.isEmpty()) {
                return result;
            }

            if (numRemainingTests.get() <= 0) {
                log.warn("We hit the limit of predicate tests of {}", maxNumPredicateTests);
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }

    @NonNull
    private static <T, R> List<T> checkLevel(R targetValue,
                                             List<T> previousLevel,
                                             BiFunction<R, List<T>, Boolean> predicate,
                                             int level,
                                             int permutationIndex,
                                             AtomicInteger numRemainingTests) {
        if (previousLevel.size() == 1) {
            return new ArrayList<>();
        }
        for (int i = permutationIndex; i < previousLevel.size(); i++) {
            if (numRemainingTests.get() <= 0) {
                return new ArrayList<>();
            }
            List<T> newList = new ArrayList<>(previousLevel);
            newList.remove(i);
            if (level == 0) {
                numRemainingTests.decrementAndGet();
                // Check all permutations on this level
                if (predicate.apply(targetValue, newList)) {
                    return newList;
                }
            } else {
                // Test next level
                var result = checkLevel(targetValue, newList, predicate, level - 1, i, numRemainingTests);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return new ArrayList<>();
    }
}
