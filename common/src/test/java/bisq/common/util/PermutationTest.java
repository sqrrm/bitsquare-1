/*
 * This file is part of Bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PermutationTest {

    @Test
    public void testFindMatchingPermutation() {
        int limit = 100000;
        List<String> result;
        List<String> list;
        List<String> expected;
        BiFunction<String, List<String>, Boolean> predicate = (target, variationList) -> {
            System.out.println("variationList " + variationList);
            return variationList.toString().equals(target);
        };

        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("0");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, limit);
        assertEquals(expected.toString(), result.toString());

        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("1", "3");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, limit);
        assertEquals(expected.toString(), result.toString());

        list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("0", "1");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, limit);
        assertEquals(expected.toString(), result.toString());

        // algorithm does not support changed order
        list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("1", "0");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, limit);
        assertNotEquals(expected.toString(), result.toString());

        // limit hit before finding result
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        expected = new ArrayList<>();
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, 1);
        assertEquals(expected.toString(), result.toString());


        // needs 1023 predicate tests (2 ^ 10 = 1024, we don't use empty list as result as that's the 'not found' case)
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("0");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, 1023);
        assertEquals(expected.toString(), result.toString());

        // 2 ^ 20 = 1048576
        // commented out to avoid test slowdown...
       /* list = new ArrayList<>();
        long ts = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }
        expected = Arrays.asList("0");
        result = PermutationUtil.findMatchingPermutation(expected.toString(), list, predicate, 1048575);
        assertTrue(expected.toString().equals(result.toString()));
        // took: 8663 ms -> out test predicate is fast, so that duration test does not mean much...
        System.out.println("took: " + (System.currentTimeMillis() - ts) + " ms");*/
    }
}
