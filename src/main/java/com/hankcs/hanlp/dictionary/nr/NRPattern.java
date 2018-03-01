
package com.hankcs.hanlp.dictionary.nr;

/**
 * 人名识别模式串
http://www.hankcs.com/nlp/segment/ictclas-the-hmm-name-recognition.html
 */
public enum NRPattern
{
    BBCD,  //姓+姓+名1+名2;
    BBE,	//姓+姓+单名;
    BBZ,
    BCD,	//姓+名1+名2
    BEE,
    BE,
    BC,
    BEC,
    BG,
    DG,
    EG,
    BXD,
    BZ,
    EE,
    FE,
    FC,
    FB,
    FG,
    Y,
    XD,
}
