package com.h3c.bigdata.zhgx.common.utils;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

public class ReadWSDLUtils {

    /**
     * 根据wsdl文件获取TargetNamespace
     *
     * @param wsdlUri
     * @return
     */
    public static String getTargetNamespace(String wsdlUri) {
        String tns = null;
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", false);
            reader.setFeature("javax.wsdl.importDocuments", false);
            Definition def = reader.readWSDL(wsdlUri);
            //命名空间
            tns = def.getTargetNamespace();
        } catch (WSDLException e) {
            e.printStackTrace();
        }
        return tns;
    }
}
