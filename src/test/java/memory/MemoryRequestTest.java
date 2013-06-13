package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MemoryRequestTest {

    @BeforeMethod
    public void setup() {
        Memory.minBlockSize = Memory.convertToPowerOfTwo(8);
    }

    @DataProvider(name = "testBlockSizes")
    public Object[][] createTestCases() {
        return new Object[][]{{1, 3}, {4, 3}, {8, 3}, {14, 4}, {25, 5}};
    }

    @Test(dataProvider = "createTestCases")
    public void testGetSize(int requestSize, int outcomeSize) {
        MemoryRequest request = new MemoryRequest(1, requestSize);

        assertEquals(request.getSize(), outcomeSize);
    }
}
