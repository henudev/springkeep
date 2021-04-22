package com.h3c.bigdata.zhgx.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Mingchao.Ji
 * @Date 2018/5/30
 * @Version 1.0
 */
public class FlowTypeTrsanfer {

    /**
     * 项目阶段类型转化-申请阶段.
     * @param type 项目阶段类型.
     * @return 转化结果.
     */
    public static Map<String,String> typeTransfer4Apply(String type){

        Map<String,String> typeMap = new HashMap<>();

        //支撑类型：1-测试；2-演示；3-调研；4-提前交付；5-合约交付
        switch (type){
            case "1":
                typeMap.put("currentType", "F3_TEST_01");
                typeMap.put("nextType", "F3_TEST_02");
                return  typeMap;
            case "2":
                typeMap.put("currentType", "F2_DISP_01");
                typeMap.put("nextType", "F2_DISP_02");
                return  typeMap;
            case "3":
                typeMap.put("currentType", "F4_SUVY_01");
                typeMap.put("nextType", "F4_SUVY_02");
                return  typeMap;
            case "4":
                typeMap.put("currentType", "F5_PDLV_01");
                typeMap.put("nextType", "F5_PDLV_02");
                return  typeMap;
            case "5":
                typeMap.put("currentType", "F7_DLVR_01");
                typeMap.put("nextType", "F7_DLVR_02");
                return  typeMap;
            default:
                typeMap.put("currentType", "F3_TEST_01");
                typeMap.put("nextType", "F3_TEST_02");
                return  typeMap;
        }
    }

    /**
     * 项目阶段类型转化-审批阶段.
     * @param type 项目阶段类型.
     * @return 转化结果.
     */
    public static Map<String,String> typeTransfer4Approval(String type){

        Map<String,String> typeMap = new HashMap<>();

        //支撑类型：1-测试；2-演示；3-调研；4-提前交付；5-合约交付
        switch (type){
            case "1":
                typeMap.put("currentType","F3_TEST_02");
                typeMap.put("nextType","F3_TEST_03");
                return  typeMap;
            case "2":
                typeMap.put("currentType","F2_DISP_02");
                typeMap.put("nextType","F2_DISP_03");
                return  typeMap;
            case "3":
                typeMap.put("currentType","F4_SUVY_02");
                typeMap.put("nextType","F4_SUVY_03");
                return  typeMap;
            case "4":
                typeMap.put("currentType","F5_PDLV_02");
                typeMap.put("nextType","F5_PDLV_03");
                return  typeMap;
            case "5":
                typeMap.put("currentType","F7_DLVR_02");
                typeMap.put("nextType","F7_DLVR_03");
                return  typeMap;
            default:
                typeMap.put("currentType","F3_TEST_02");
                typeMap.put("nextType","F3_TEST_03");
                return  typeMap;
        }
    }

    /**
     * 项目阶段类型转化-支撑填报阶段.
     * @param type 项目阶段类型.
     * @return 转化结果.
     */
    public static Map<String,String> typeTransfer4Support(String type){

        Map<String,String> typeMap = new HashMap<>();

        //支撑类型：1-测试；2-演示；3-调研；4-提前交付；5-合约交付
        switch (type){
            case "1":
                typeMap.put("currentType", "F3_TEST_03");
                return  typeMap;
            case "2":
                typeMap.put("currentType", "F2_DISP_03");
                return  typeMap;
            case "3":
                typeMap.put("currentType", "F4_SUVY_03");
                return  typeMap;
            default:
                typeMap.put("currentType", "F3_TEST_03");
                return  typeMap;
        }
    }

    /**
     * 项目阶段类型转化-支撑记录查询阶段.
     * @param type 项目阶段类型.
     * @return 转化结果.
     */
    public static String applyTypeTransfer4Support(String type){


        //支撑类型：1-测试；2-演示；3-调研；4-提前交付；5-合约交付
        switch (type){
            case "1":
                return  "测试";
            case "2":
                return  "演示";
            case "3":
                return  "调研";
            case "4":
                return  "提前交付";
            case "5":
                return  "合约交付";
            default:
                return  "测试";
        }
    }
}
