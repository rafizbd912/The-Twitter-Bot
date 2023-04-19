package org.cis1200;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

//My imports
import java.util.NoSuchElementException;


/** Tests for FileLineIterator */
public class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void testEmptyFile() {
        String emptyContent = "";
        StringReader sr = new StringReader(emptyContent);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertFalse(li.hasNext());
    }

    @Test
    public void testOneLineFile() {
        String content = "Only one line in the file";
        StringReader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("Only one line in the file", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testMultipleLinesFile() {
        String content = "First line\nSecond line\nThird line";
        StringReader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("First line", li.next());
        assertTrue(li.hasNext());
        assertEquals("Second line", li.next());
        assertTrue(li.hasNext());
        assertEquals("Third line", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testNoSuchElementException() {
        String content = "One line";
        StringReader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("One line", li.next());
        assertFalse(li.hasNext());

        assertThrows(NoSuchElementException.class, () -> {
            li.next();
        });
    }

    @Test
    public void testNullBufferedReader() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FileLineIterator((BufferedReader) null);
        });
    }

    @Test
    public void testNullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FileLineIterator((String) null);
        });
    }

    @Test
    public void testNonExistentFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FileLineIterator("non_existent_file.txt");
        });
    }


}
