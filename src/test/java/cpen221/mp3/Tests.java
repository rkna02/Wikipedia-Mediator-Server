package cpen221.mp3;
import cpen221.mp3.fsftbuffer.Bufferable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import cpen221.mp3.fsftbuffer.FSFTBuffer;

import java.util.Arrays;
import java.util.List;

public class Tests {

    private static FSFTBuffer<Bufferable> buffer1;
    private static FSFTBuffer<Bufferable> buffer2;
    private static FSFTBuffer<Bufferable> buffer3;
    private static FSFTBuffer<Bufferable> buffer4;
    private static FSFTBuffer<Bufferable> buffer5;

    @BeforeAll
    public static void setupTests() {
        buffer1 = new FSFTBuffer<Bufferable>(20, 5);
        buffer2 = new FSFTBuffer<Bufferable>(20, 5);
        buffer3 = new FSFTBuffer<Bufferable>(20, 5);
        buffer4 = new FSFTBuffer<Bufferable>(20, 5);
        buffer5 = new FSFTBuffer<Bufferable>(1, 5);

        /**
         * Task 1 Tests
         */

    }


}
