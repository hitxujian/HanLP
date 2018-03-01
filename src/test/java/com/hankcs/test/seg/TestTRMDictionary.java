
package com.hankcs.test.seg;

import com.hankcs.hanlp.corpus.tag.NR;
import com.hankcs.hanlp.dictionary.TransformMatrixDictionary;
import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestTRMDictionary extends TestCase
{
    public void testLoad() throws Exception
    {
        TransformMatrixDictionary<NR> nrTransformMatrixDictionary = new TransformMatrixDictionary<NR>(NR.class);
        nrTransformMatrixDictionary.load("data/dictionary/person/nr.tr.txt");
        System.out.println(nrTransformMatrixDictionary.getFrequency(NR.A, NR.A));
        System.out.println(nrTransformMatrixDictionary.getFrequency("A", "A"));
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency());
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency(NR.Z));
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency(NR.A));
    }
}
