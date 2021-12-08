package cpen221.mp3;
import cpen221.mp3.fsftbuffer.Bufferable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import cpen221.mp3.fsftbuffer.FSFTBuffer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Tests {

    private static FSFTBuffer buffer1;
    private static FSFTBuffer buffer2;
    private static FSFTBuffer buffer3;
    private static FSFTBuffer buffer4;
    private static FSFTBuffer buffer5;


    @BeforeAll
    public static void setupTests() {
        buffer1 = new FSFTBuffer(10, 5);
        buffer2 = new FSFTBuffer(4, 5);
        buffer3 = new FSFTBuffer(4, 10);
        buffer4 = new FSFTBuffer(10, 2);
        buffer5 = new FSFTBuffer(1, 5);

        /**
         * Task 1 Tests
         */

    }

    /**
     * test 1
     * check put
     */
    @Test
    public static void test(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer2.put(str1));
        Assertions.assertEquals(true, buffer2.put(str2));
        Assertions.assertEquals(true, buffer2.put(str3));
        Assertions.assertEquals(false, buffer2.put(null));
    }
    @Test
    public static void test1_1(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer1.put(str1));
        Assertions.assertEquals(true, buffer1.put(str2));
        Assertions.assertEquals(true, buffer1.put(str3));
        Assertions.assertEquals(false, buffer1.put(null));
    }
    @Test
    public static void test1_2(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer3.put(str1));
        Assertions.assertEquals(true, buffer3.put(str2));
        Assertions.assertEquals(true, buffer3.put(str3));
        Assertions.assertEquals(false, buffer3.put(null));
    }
    @Test
    public static void test1_3(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer4.put(str1));
        Assertions.assertEquals(true, buffer4.put(str2));
        Assertions.assertEquals(true, buffer4.put(str3));
        Assertions.assertEquals(false, buffer4.put(null));
    }
    @Test
    public static void test1_4(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer5.put(str1));
        Assertions.assertEquals(true, buffer5.put(str2));
        Assertions.assertEquals(true, buffer5.put(str3));
        Assertions.assertEquals(false, buffer5.put(null));
    }
    /**
     * test 2
     * check get
     */
    @Test
    public static void test2(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer2.put(str1));
        Assertions.assertEquals(true, buffer2.put(str2));
        Assertions.assertEquals(true, buffer2.put(str3));
        Assertions.assertEquals(true, buffer2.get("We love 221"));
        Assertions.assertEquals(true, buffer2.get("We are warriors"));
        Assertions.assertEquals(true, buffer2.get("we will pass 281!"));
        Assertions.assertEquals(false, buffer2.get("shouldn't exist"));
        Assertions.assertEquals(false, buffer2.put(null));
    }
    @Test
    public static void test2_1(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer1.put(str1));
        Assertions.assertEquals(true, buffer1.put(str2));
        Assertions.assertEquals(true, buffer1.put(str3));
        Assertions.assertEquals(true, buffer1.get("We love 221"));
        Assertions.assertEquals(true, buffer1.get("We are warriors"));
        Assertions.assertEquals(true, buffer1.get("we will pass 281!"));
        Assertions.assertEquals(false, buffer1.get("shouldn't exist"));
        Assertions.assertEquals(false, buffer1.put(null));
    }
    @Test
    public static void test2_2(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer3.put(str1));
        Assertions.assertEquals(true, buffer3.put(str2));
        Assertions.assertEquals(true, buffer3.put(str3));
        Assertions.assertEquals(true, buffer3.get("We love 221"));
        Assertions.assertEquals(true, buffer3.get("We are warriors"));
        Assertions.assertEquals(true, buffer3.get("we will pass 281!"));
        Assertions.assertEquals(false, buffer3.get("shouldn't exist"));
        Assertions.assertEquals(false, buffer3.put(null));
    }@Test
    public static void test2_3(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer4.put(str1));
        Assertions.assertEquals(true, buffer4.put(str2));
        Assertions.assertEquals(true, buffer4.put(str3));
        Assertions.assertEquals(true, buffer4.get("We love 221"));
        Assertions.assertEquals(true, buffer4.get("We are warriors"));
        Assertions.assertEquals(true, buffer4.get("we will pass 281!"));
        Assertions.assertEquals(false, buffer4.get("shouldn't exist"));
        Assertions.assertEquals(false, buffer4.put(null));
    }
    @Test
    public static void test2_4(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer5.put(str1));
        Assertions.assertEquals(true, buffer5.put(str2));
        Assertions.assertEquals(true, buffer5.put(str3));
        Assertions.assertEquals(true, buffer5.get("We love 221"));
        Assertions.assertEquals(true, buffer5.get("We are warriors"));
        Assertions.assertEquals(true, buffer5.get("we will pass 281!"));
        Assertions.assertEquals(false, buffer5.get("shouldn't exist"));
        Assertions.assertEquals(false, buffer5.put(null));
    }

    /**
     *  Test 3
     *  get updates
     */
    @Test
    public static void test3(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer2.put(str1));
        Assertions.assertEquals(true, buffer2.put(str2));
        Assertions.assertEquals(true, buffer2.put(str3));
        Assertions.assertEquals(true, buffer2.update(str1));
        Assertions.assertEquals(true, buffer2.update(str2));
        Assertions.assertEquals(true, buffer2.update(str3));
        Assertions.assertEquals(false, buffer2.update(str4));
        Assertions.assertEquals(false, buffer2.update(null));
    }
    @Test
    public static void test3_1(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer1.put(str1));
        Assertions.assertEquals(true, buffer1.put(str2));
        Assertions.assertEquals(true, buffer1.put(str3));
        Assertions.assertEquals(true, buffer1.update(str1));
        Assertions.assertEquals(true, buffer1.update(str2));
        Assertions.assertEquals(true, buffer1.update(str3));
        Assertions.assertEquals(false, buffer1.update(str4));
        Assertions.assertEquals(false, buffer1.update(null));
    }
    @Test
    public static void test3_2(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer3.put(str1));
        Assertions.assertEquals(true, buffer3.put(str2));
        Assertions.assertEquals(true, buffer3.put(str3));
        Assertions.assertEquals(true, buffer3.update(str1));
        Assertions.assertEquals(true, buffer3.update(str2));
        Assertions.assertEquals(true, buffer3.update(str3));
        Assertions.assertEquals(false, buffer3.update(str4));
        Assertions.assertEquals(false, buffer3.update(null));
    }
    @Test
    public static void test3_3(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer4.put(str1));
        Assertions.assertEquals(true, buffer4.put(str2));
        Assertions.assertEquals(true, buffer4.put(str3));
        Assertions.assertEquals(true, buffer4.update(str1));
        Assertions.assertEquals(true, buffer4.update(str2));
        Assertions.assertEquals(true, buffer4.update(str3));
        Assertions.assertEquals(false, buffer4.update(str4));
        Assertions.assertEquals(false, buffer4.update(null));
    }@Test
    public static void test3_4(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer5.put(str1));
        Assertions.assertEquals(true, buffer5.put(str2));
        Assertions.assertEquals(true, buffer5.put(str3));
        Assertions.assertEquals(true, buffer5.update(str1));
        Assertions.assertEquals(true, buffer5.update(str2));
        Assertions.assertEquals(true, buffer5.update(str3));
        Assertions.assertEquals(false, buffer5.update(str4));
        Assertions.assertEquals(false, buffer5.update(null));
    }

    /**
     *  Test 4
     *  get touches
     */
    @Test
    public static void test4(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer2.put(str1));
        Assertions.assertEquals(true, buffer2.put(str2));
        Assertions.assertEquals(true, buffer2.put(str3));
        Assertions.assertEquals(true, buffer2.touch("We love 221"));
        Assertions.assertEquals(true, buffer2.touch("We are warriors"));
        Assertions.assertEquals(true, buffer2.touch("we will pass 281!"));
        Assertions.assertEquals(false, buffer2.touch("built a snowman last night, a tiny one"));
        Assertions.assertEquals(false, buffer2.touch(null));
    }
    @Test
    public static void test4_1(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer1.put(str1));
        Assertions.assertEquals(true, buffer1.put(str2));
        Assertions.assertEquals(true, buffer1.put(str3));
        Assertions.assertEquals(true, buffer1.touch("We love 221"));
        Assertions.assertEquals(true, buffer1.touch("We are warriors"));
        Assertions.assertEquals(true, buffer1.touch("we will pass 281!"));
        Assertions.assertEquals(false, buffer1.touch("built a snowman last night, a tiny one"));
        Assertions.assertEquals(false, buffer1.touch(null));
    }
    @Test
    public static void test4_2(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer3.put(str1));
        Assertions.assertEquals(true, buffer3.put(str2));
        Assertions.assertEquals(true, buffer3.put(str3));
        Assertions.assertEquals(true, buffer3.touch("We love 221"));
        Assertions.assertEquals(true, buffer3.touch("We are warriors"));
        Assertions.assertEquals(true, buffer3.touch("we will pass 281!"));
        Assertions.assertEquals(false, buffer3.touch("built a snowman last night, a tiny one"));
        Assertions.assertEquals(false, buffer3.touch(null));
    }
    @Test
    public static void test4_3(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer4.put(str1));
        Assertions.assertEquals(true, buffer4.put(str2));
        Assertions.assertEquals(true, buffer4.put(str3));
        Assertions.assertEquals(true, buffer4.touch("We love 221"));
        Assertions.assertEquals(true, buffer4.touch("We are warriors"));
        Assertions.assertEquals(true, buffer4.touch("we will pass 281!"));
        Assertions.assertEquals(false, buffer4.touch("built a snowman last night, a tiny one"));
        Assertions.assertEquals(false, buffer4.touch(null));
    }@Test
    public static void test4_4(){
        TTT str1 = new TTT("We love 221");
        TTT str2 = new TTT("We are warriors");
        TTT str3 = new TTT("we will pass 281!");
        TTT str4 = new TTT("built a snowman last night, a tiny one");
        Assertions.assertEquals(true, buffer5.put(str1));
        Assertions.assertEquals(true, buffer5.put(str2));
        Assertions.assertEquals(true, buffer5.put(str3));
        Assertions.assertEquals(true, buffer5.touch("We love 221"));
        Assertions.assertEquals(true, buffer5.touch("We are warriors"));
        Assertions.assertEquals(true, buffer5.touch("we will pass 281!"));
        Assertions.assertEquals(false, buffer5.touch("built a snowman last night, a tiny one"));
        Assertions.assertEquals(false, buffer5.touch(null));
    }

    // TTT is a data type
    public static class TTT implements Bufferable{
        String str1;
        public TTT(String xxx){
            str1 = xxx;
        }
        public String id(){
            return str1;
        }
    }
}