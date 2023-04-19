package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

//My imports
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;


/** Tests for MarkovChain */
public class MarkovChainTest {

    private MarkovChain mc;

    @BeforeEach
    public void setUp() {
        mc = new MarkovChain();
    }

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    @Test
    public void testAddBigramMultipleTimes() {
        mc.addBigram("1", "2");
        mc.addBigram("1", "2");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(2, pd.count("2"));
    }

    /* ***** ****** ***** ***** TRAIN TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testTrain() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainMultipleSentences() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "I like cats";
        String sentence2 = "I like dogs";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        assertEquals(4, mc.chain.size());

        ProbabilityDistribution<String> pdI = mc.chain.get("I");
        assertTrue(pdI.getRecords().containsKey("like"));
        assertEquals(2, pdI.count("like"));

        ProbabilityDistribution<String> pdLike = mc.chain.get("like");
        assertTrue(pdLike.getRecords().containsKey("cats"));
        assertTrue(pdLike.getRecords().containsKey("dogs"));
        assertEquals(1, pdLike.count("cats"));
        assertEquals(1, pdLike.count("dogs"));

        ProbabilityDistribution<String> pdCats = mc.chain.get("cats");
        assertTrue(pdCats.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pdCats.count(MarkovChain.END_TOKEN));

        ProbabilityDistribution<String> pdDogs = mc.chain.get("dogs");
        assertTrue(pdDogs.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pdDogs.count(MarkovChain.END_TOKEN));
    }


    @Test
    public void trainTestEmpty() {
        MarkovChain mc = new MarkovChain();
        String sentence = "    ";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        mc.reset();
        assertEquals(0, mc.chain.size());
        assertFalse(mc.hasNext());
        sentence = "1";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(mc.startWords.count("1"), 1);
        mc.reset();
        assertTrue(mc.hasNext());
    }



    @Test
    public void trainOneWord() {
        MarkovChain mc = new MarkovChain();
        String sentence = "monkey";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        mc.reset();
        assertEquals(mc.next(), "monkey");
        assertFalse(mc.hasNext());
    }





    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testWalk() {
        /*
         * Using the sentences "CIS 1200 rocks" and "CIS 1200 beats CIS 1600",
         * we're going to put some bigrams into the Markov Chain.
         *
         * While in the real world, we want the sentence we output to be random,
         * we don't want this in testing. For testing, we want to modify our
         * ProbabilityDistribution such that it will output a predictable chain
         * of words.
         *
         * Luckily, we've provided a `fixDistribution` method that will do this
         * for you! By calling `fixDistribution` with a list of words that you
         * expect to be output, the ProbabilityDistributions will be modified to
         * output your words in that order.
         *
         * See our below test for an example of how to use this.
         */

        String[] expectedWords = {"CIS", "1200", "beats", "CIS", "1200", "rocks"};
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        mc.reset("CIS"); // we start with "CIS" since that's the word our desired walk starts with
        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));

        for (int i = 0; i < expectedWords.length; i++) {
            assertTrue(mc.hasNext());
            assertEquals(expectedWords[i], mc.next());
        }


    }


    @Test
    public void testSingleWordInput() {
        MarkovChain mc = new MarkovChain();
        String singleWord = "hello";
        mc.train(Arrays.stream(singleWord.split(" ")).iterator());

        // Check if the chain has only one key
        assertEquals(1, mc.chain.size());

        // Test if the single word links to the END_TOKEN
        ProbabilityDistribution<String> pd = mc.chain.get("hello");
        assertTrue(pd.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd.count(MarkovChain.END_TOKEN));
    }







}