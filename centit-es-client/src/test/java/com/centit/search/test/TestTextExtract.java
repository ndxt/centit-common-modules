package com.centit.search.test;

import com.centit.search.utils.ObjectTextExtractor;
import com.centit.search.utils.TikaTextExtractor;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import lombok.Data;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by codefan on 17-6-22.
 */
public class TestTextExtract {



    @Data
    public static class T1{
        int a;
        String b;
        Map<String, Object> map;
        //List<T2> t2List;
    }

    @Data
    public static class T2{
        float f;
        Date date;
        byte[] c;
        Set<T1> t1List;
    }

    public static void main(String[] args) {

        try {
            System.out.println(
                TikaTextExtractor.extractFileText("/D/WorkDoc/烽火科技/广州616/南大先腾17.3.13.doc"));
        } catch (IOException | TikaException |SAXException e) {
            e.printStackTrace();
        }
     /*
        T1 t11 = new T1();
        t11.setA(1);
        t11.setB("T11");

        T1 t12 = new T1();
        t12.setA(2);
        t12.setB("T12");

        T2 t21 = new T2();
        t21.setF(1.2345f);
        t21.setC(new byte[]{'1','2','3','4','5'});
        t21.setDate(DatetimeOpt.currentUtilDate());
        T2 t22 = new T2();
        t22.setF(5.4321f);
        t22.setC(new byte[]{'a','b','c','d','e'});
        t22.setDate(DatetimeOpt.currentUtilDate());

        t22.setT1List(CollectionsOpt.createHashSet(t11,t12));

        t11.setMap(CollectionsOpt.createHashMap("t12",t12,"t21",t21,"t22",t22,"t11",t11));

        System.out.println(ObjectTextExtractor.extractText(t11));*/
    }

}
