import de.flo.hexMax.AbstractHexMaxSolver;
import de.flo.hexMax.HexMaxSolver;
import de.flo.hexMax.ResultChangesCreater;
import de.flo.hexMax.changingTrack.SSDSetChangingRow;
import de.flo.hexMax.digit.HexDigit;
import de.flo.hexMax.digit.HexDigitUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTests {

    /*
     * Tests where input equals output.
     *
     */

    @Test
    public void testA() {
        // Test 1's only
        HexDigit[] given = HexDigitUtils.getDigits("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        this.test(given);
    }

    @Test
    public void testB() {
        // Test 8's only
        HexDigit[] given = HexDigitUtils.getDigits("88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");

        this.test(given);
    }

    @Test
    public void testC() {
        // Test F's only
        HexDigit[] given = HexDigitUtils.getDigits("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

        this.test(given);
    }

    private void test(HexDigit[] digits) {
        int changes = 100;

        AbstractHexMaxSolver solver = new HexMaxSolver(digits, changes);
        HexDigit[] result = solver.solve();

        Deque<ResultChangesCreater.Swap> swaps = new ResultChangesCreater(digits, result).getSwaps();

        Assert.assertArrayEquals(digits, result);
        Assert.assertEquals(0, swaps.size());
    }
}
