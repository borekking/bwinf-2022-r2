import de.flo.hexMax.AbstractHexMaxSolver;
import de.flo.hexMax.HexMaxSolver;
import de.flo.hexMax.RealChangesCreater;
import de.flo.hexMax.ResultChangesCreater;
import de.flo.hexMax.changingTrack.SSDSetChangingRow;
import de.flo.hexMax.digit.HexDigit;
import de.flo.hexMax.digit.HexDigitUtils;
import de.flo.hexMax.digit.display.SSDSet;
import de.flo.hexMax.digit.display.SevenSegmentDisplay;
import de.flo.hexMax.util.JavaUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class Tests {

    /*
     * Test all input (from bwinf.de website) to be valid (0-5) and own tests (6, 7).
     *
     */

    @Test
    public void hexMax0() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax0.txt");
    }

    @Test
    public void hexMax1() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax1.txt");
    }

    @Test
    public void hexMax2() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax2.txt");
    }

    @Test
    public void hexMax3() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax3.txt");
    }

    @Test
    public void hexMax4() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax4.txt");
    }

    @Test
    public void hexMax6() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax6.txt");
    }

    @Test
    public void hexMax7() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax7.txt");
    }

    @Test
    public void hexMax5() throws FileNotFoundException {
        this.testFile("../../inputs/hexmax5.txt");
    }

    private void testFile(String fileName) throws FileNotFoundException {
        List<String> file = JavaUtils.readFile(new File(fileName));

        HexDigit[] digits = HexDigitUtils.getDigits(file.get(0));
        int maxChanges = Integer.parseInt(file.get(1));

        this.testNumber(digits, maxChanges);
    }

    // Methode to test a given hex-number with given amount of changes
    private void testNumber(HexDigit[] digits, int changes) {
        AbstractHexMaxSolver solver = new HexMaxSolver(digits, changes);

        HexDigit[] result = solver.solve();
        Deque<ResultChangesCreater.Swap> swaps = new ResultChangesCreater(digits, result).getSwaps();
        SSDSetChangingRow changingRow = new RealChangesCreater(swaps, digits).getChangingRow();

        this.testResults(digits, result, changingRow, changes);
    }

    private void testResults(HexDigit[] given, HexDigit[] result, SSDSetChangingRow changingRow, int changes) {
        this.testResult(given, result);

        this.testChangingRow(given, changingRow, changes);
    }

    private void testChangingRow(HexDigit[] given, SSDSetChangingRow changingRow, int changes) {
        // ChangingRow‘s size = used changes <= changes
        Assert.assertTrue("ChangingRow's size is bigger than changes!", changingRow.getSize() <= changes);

        // No SSD in changes with no enabled elements
        for (SSDSet set : changingRow) {
            SevenSegmentDisplay[] displays = set.getDisplays();

            for (SevenSegmentDisplay display : displays)
                Assert.assertFalse("Display can not be empty!", display.isEmpty());
        }

        // Every element of changingRow has the same amount of elements
        int len = given.length;

        for (SSDSet set : changingRow) {
            Assert.assertEquals("All elements of ChangingRow have to be same size!", len, set.getDisplays().length);
        }

        // -- One Change per ChangingRow elements --
        Iterator<SSDSet> iterator = changingRow.iterator();

        // Init last element to SSDSet of given
        SSDSet last = new SSDSet(given);

        while (iterator.hasNext()) {
            // Get current SSDSet
            SSDSet current = iterator.next();

            // Get SSD-Arrays from current and last SSDSet
            SevenSegmentDisplay[] currentDisplays = current.getDisplays();
            SevenSegmentDisplay[] lastDisplays = last.getDisplays();

            // Count changes from last to current
            int changesCounter = 0;

            // Segment values of changes (have to be false and true or true and false)
            boolean[] changesValues = new boolean[2];

            // Go through all SSDs of last and current in same loop
            // (they should have same length as checked)
            for (int digitIndex = 0; digitIndex < len; digitIndex++) {
                // Get current displays from last and current arrays
                SevenSegmentDisplay currentDisplay = currentDisplays[digitIndex];
                SevenSegmentDisplay lastDisplay = lastDisplays[digitIndex];

                // Go through all segments of both
                for (int segmentIndex = 0; segmentIndex < SevenSegmentDisplay.SEGMENT_AMOUNT; segmentIndex++) {
                    // Increase changes counter if the current segment is not equal
                    if (currentDisplay.getSegment(segmentIndex) != lastDisplay.getSegment(segmentIndex)) {
                        // Not more than 2 changes allowed because then these to could be swapped, s.t.
                        // a "swap" ("Umlegung") could(!) be done
                        Assert.assertNotEquals("More than 2 changes in an element of ChangeRow!", 2, changesCounter);

                        changesValues[changesCounter] = currentDisplay.getSegment(segmentIndex);

                        changesCounter++;
                    }
                }
            }

            // Check if exactly 2 changes where found (s.t. a "swap" ("Umlegung") could(!) be done)
            Assert.assertEquals("ChangeRow has not exactly 2 changes in one element: " + changesCounter, 2, changesCounter);

            // Check if found changes are false and true or true and false, s.t.
            // the "swap" ("Umlegung") can in fact be done; using xor operator.
            Assert.assertTrue("Changes can not have been swapped: " + changesValues[0] + ", " + changesValues[1], changesValues[0] ^ changesValues[1]);

            // Update last element
            last = current;
        }
        // ---
    }

    private void testResult(HexDigit[] given, HexDigit[] result) {
        // result >= given
        BigDecimal givenNum = HexDigitUtils.getDecimalValue(given);
        BigDecimal resultNum = HexDigitUtils.getDecimalValue(result);

        BigDecimal dif = resultNum.subtract(givenNum);
        Assert.assertTrue("Result is not >= given number: " + resultNum + " - " + givenNum + " = " + dif, dif.compareTo(new BigDecimal(0)) >= 0);

        // Digit amount if equal
        Assert.assertEquals("Result and given number have to have the same amount if digits!", given.length, result.length);

        // result's digit amount is equal to given's digit amount
        long segmentsGiven = this.getSegmentAmount(given);
        long segmentsResult = this.getSegmentAmount(result);
        Assert.assertEquals("Segment amount has to be equal!", segmentsGiven, segmentsResult);
    }

    private long getSegmentAmount(HexDigit[] digits) {
        long amount = 0L;

        for (HexDigit d : digits)
            amount += d.getEnabledSegments();

        return amount;
    }
}
