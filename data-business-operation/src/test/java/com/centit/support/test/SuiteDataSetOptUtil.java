package com.centit.support.test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestCrossTabulation.class,TestStatDataset.class,TestCompareTabulation.class})
public class SuiteDataSetOptUtil extends TestSuite {

}
