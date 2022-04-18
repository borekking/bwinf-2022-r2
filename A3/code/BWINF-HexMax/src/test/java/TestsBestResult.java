import de.flo.hexMax.AbstractHexMaxSolver;
import de.flo.hexMax.HexMaxSolver;
import de.flo.hexMax.RealChangesCreater;
import de.flo.hexMax.ResultChangesCreater;
import de.flo.hexMax.changingTrack.SSDSetChangingRow;
import de.flo.hexMax.digit.HexDigit;
import de.flo.hexMax.digit.HexDigitUtils;
import de.flo.hexMax.util.JavaUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TestsBestResult {

    // TODO Delete

    @Test
    public void test0() throws FileNotFoundException {
        HexDigit[] expected = HexDigitUtils.getDigits("EE4");
        int changesLeft = 0;
        this.test("../../inputs/hexmax0.txt", expected, changesLeft);
    }

    @Test
    public void test1() throws FileNotFoundException {
        HexDigit[] expected = HexDigitUtils.getDigits("FFFEA97B55");
        int changesLeft = 0;
        this.test("../../inputs/hexmax1.txt", expected, changesLeft);
    }

    @Test
    public void test2() throws FileNotFoundException {
        HexDigit[] expected = HexDigitUtils.getDigits("FFFFFFFFFFFFFFFFD9A9BEAEE8EDA8BDA989D9F8");
        int changesLeft = 0;
        this.test("../../inputs/hexmax2.txt", expected, changesLeft);
    }

    @Test
    public void test3() throws FileNotFoundException {
        HexDigit[] expected = HexDigitUtils.getDigits("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAA98BB8B9DFAFEAE888DD888AD8BA8EA8888");
        int changesLeft = 0;
        this.test("../../inputs/hexmax3.txt", expected, changesLeft);
    }

    @Test
    public void test4() throws FileNotFoundException {
        HexDigit[] expected = HexDigitUtils.getDigits("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEB8DE88BAA8ADD888898E9BA88AD98988F898AB7AF7BDA8A61BA7D4AD8F888");
        int changesLeft = 0;
        this.test("../../inputs/hexmax4.txt", expected, changesLeft);
    }

    @Test
    public void test5() throws FileNotFoundException {
        //                                          EF50AA77ECAD25F5E11A307B713EAAEC55215E7E640FD263FA529BBB48DC8FAFE14D5B02EBF792B5CCBBE9FA1330B867E330A6412870DD2BA6ED0DBCAE553115C9A31FF350C5DF993824886DB5111A83E773F23AD7FA81A845C11E22C4C45005D192ADE68AA9AA57406EB0E7C9CA13AD03888F6ABEDF1475FE9832C66BFDC28964B7022BDD969E5533EA4F2E4EABA75B5DC11972824896786BD1E4A7A7748FDF1452A5079E0F9E6005F040594185EA03B5A869B109A283797AB31394941BFE4D38392AD12186FF6D233585D8C820F197FBA9F6F063A0877A912CCBDCB14BEECBAEC0ED061CFF60BD517B6879B72B9EFE977A9D3259632C718FBF45156A16576AA7F9A4FAD40AD8BC87EC569F9C1364A63B1623A5AD559AAF6252052782BF9A46104E443A3932D25AAE8F8C59F10875FAD3CBD885CE68665F2C826B1E1735EE2FDF0A1965149DF353EE0BE81F3EC133922EF43EBC09EF755FBD740C8E4D024B033F0E8F3449C94102902E143433262CDA1925A2B7FD01BEF26CD51A1FC22EDD49623EE9DEB14C138A7A6C47B677F033BDEB849738C3AE5935A2F54B99237912F2958FDFB82217C175448AA8230FDCB3B3869824A826635B538D47D847D8479A88F350E24B31787DFD60DE5E260B265829E036BE340FFC0D8C05555E75092226E7D54DEB42E1BB2CA9661A882FB718E7AA53F1E606
        HexDigit[] expected = HexDigitUtils.getDigits("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF88EFA9EBE89EFA99FBDAA8E8EAD88AB899F8E8F9AA9E9AD88988EDA9A99888EDAD989A8BAFD8A888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");
        int changesLeft = 0;
        this.test("../../inputs/hexmax5.txt", expected, changesLeft);
    }

    public void test(String filename, HexDigit[] expectedResult, int expectedChangesLeft) throws FileNotFoundException {
        List<String> file = JavaUtils.readFile(new File(filename));

        HexDigit[] digits = HexDigitUtils.getDigits(file.get(0));
        int maxChanges = Integer.parseInt(file.get(1));

        this.test(digits, maxChanges, expectedResult, expectedChangesLeft);
    }

    public void test(HexDigit[] start, int changes, HexDigit[] expectedResult, int expectedChangesLeft) {
        AbstractHexMaxSolver solver = new HexMaxSolver(start, changes);
        HexDigit[] result = solver.solve();

        Deque<ResultChangesCreater.Swap> swaps = new ResultChangesCreater(start, result).getSwaps();
        SSDSetChangingRow changingRow = new RealChangesCreater(swaps, start).getChangingRow();

        BigDecimal decimalValueResultExpected = HexDigitUtils.getDecimalValue(expectedResult);
        BigDecimal decimalValueResultActual = HexDigitUtils.getDecimalValue(result);

        if (decimalValueResultActual.compareTo(decimalValueResultExpected) > 0) {
            System.err.println("WTFFFFFFFFFFFFFFFFFFFFF");
        } else if (decimalValueResultActual.compareTo(decimalValueResultExpected) == 0) {
            System.err.println("Normal");
        } else if (decimalValueResultActual.compareTo(decimalValueResultExpected) < 0) {
            System.err.println("Smaller: " + HexDigitUtils.getStringByDigits(result));
        }

        Assert.assertArrayEquals("Digits weren't equal!", expectedResult, result);
        Assert.assertEquals("ChangesLeft weren't equal!", changes - expectedChangesLeft, changingRow.getSize());
    }
}
