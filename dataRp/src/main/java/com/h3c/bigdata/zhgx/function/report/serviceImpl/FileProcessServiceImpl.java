package com.h3c.bigdata.zhgx.function.report.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.IOUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.report.config.ResponseHelper;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.dao.DataAnnexMapper;
import com.h3c.bigdata.zhgx.function.report.dao.TemplateEntityMapper;
import com.h3c.bigdata.zhgx.function.report.entity.DataAnnexEntity;
import com.h3c.bigdata.zhgx.function.report.service.FileProcessService;
import com.h3c.bigdata.zhgx.function.report.util.EsUtil;
import com.h3c.bigdata.zhgx.function.report.util.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
@Slf4j
@Service
public class FileProcessServiceImpl extends BaseService implements FileProcessService {

    @Resource
    TemplateEntityMapper templateEntityMapper;

    @Value("${uploadPath}")
    String uploadPath;

    /**
     * 机要文件日期字段，固定
     */
    @Value("${date_word}")
    String jydate;
    /**
     * 机要文件期数字段，固定
     */
    @Value("${volume_word}")
    String volume;

    @Autowired
    DataAnnexMapper annexMapper;

    /**
     * 上传文件
     *                map.put("oldname",oldName);//文件原名称
     *                 map.put("ext",ext);
     *                 map.put("size",sizeString);
     *                 map.put("name",newFileName);//文件新名称
     *                 map.put("url",url);
     * @param file
     * @return
     */
    @Override
    public ResponseModel uploadFile(MultipartFile file, HttpServletRequest request) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.OK.value());
        MultipartFile[] files = {file};
        Map<String, String> result = new HashMap<>();
        DataAnnexEntity annexEntity = new DataAnnexEntity();
        try {
            List<HashMap<String, Object>> fileList =  FileUtil.mutlUpload(files, uploadPath);
            if (fileList == null || fileList.size() == 0) {
                new Exception("上传文件列表返回信息为空");
            }
            /***1 本方法适配单文件上传,因此直接取值第一个元素*/
            annexEntity.setFilePath(fileList.get(0).get("url").toString());
            annexEntity.setFileSize(fileList.get(0).get("size").toString());
            annexEntity.setFileType(fileList.get(0).get("ext").toString());
            annexEntity.setId(UUIDUtil.get());
            annexEntity.setNewName(fileList.get(0).get("name").toString());
            annexEntity.setOldName(fileList.get(0).get("oldname").toString());
            annexEntity.setMd5(fileList.get(0).get("md5").toString());
            annexEntity.setUrl(fileList.get(0).get("net_url").toString());


            /**2 入库 */
            annexMapper.insert(annexEntity);
            result.put("id", annexEntity.getId());
        }catch (Exception e) {
            e.printStackTrace();
            response.setResult(annexEntity.getId());
            response.setCode(Const.kCode_Fail);
            return response;
        }
        return ResponseHelper.buildResponseModel(result);
    }

    /**
     * 文件列表
     *
     * @param annexIds
     * @param httpRequest
     * @return
     */
    @Override
    public List<Map> getAnnexList(String  annexIds, HttpServletRequest httpRequest) {
        List<Map> result = new ArrayList<>();

        if (annexIds.isEmpty()){
            return null;
        }
        String[]  ids  = annexIds.split("@");

        for (String id : ids){
            DataAnnexEntity annexEntity = annexMapper.selectById(id);
            Map<String, String> data  = new HashMap<>();
            if (annexEntity == null){
                continue;
            }
            data.put("id", annexEntity.getId());
            data.put("fileName", annexEntity.getOldName());
            result.add(data);
        }
        return result;
    }

    /**
     * 下载文件列表
     *
     * @param annexIds
     * @param httpRequest
     * @return
     */
    @Override
    public void downLoadAnnexByGroupId(String annexIds, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        if (annexIds.isEmpty()){
            return;
        }
        String[]  ids  = annexIds.split("@");
        List<DataAnnexEntity> annexList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        //附件下载压缩包内，文件名使用oldName
        List<File> oldFileList = new ArrayList<>();
        for (String id : ids){
            DataAnnexEntity entity = annexMapper.selectById(id);
            annexList.add(entity);
            File file = new File(entity.getFilePath());
            String uid = UUIDUtil.get();
            File dir = new File(uploadPath+"/zip/"+ uid +"/");
            dir.mkdirs();
            File oldFile = new File(uploadPath+"/zip/"+ uid +"/" +entity.getOldName());
            oldFile.createNewFile();

            InputStream stream = new FileInputStream(new File(entity.getFilePath()));
            FileUtils.copyInputStreamToFile(stream, oldFile);

            fileList.add(file);
            oldFileList.add(oldFile);
        }

        String[] strs;
        if (fileList.size() == 1) {
            strs = annexList.get(0).getOldName().split("\\.");
            File file = new File(annexList.get(0).getFilePath());
            try{
                IOUtil.fetchFileToResponse(httpRequest,response, strs[0], annexList.get(0).getFileType().replace(".", ""), file);
            }catch (Exception e){
                e.printStackTrace();
                response.setStatus(500);
            }

        }else {
            File zipFile = IOUtil.getZipFile(oldFileList, uploadPath, "fujian.zip");
            try{
                IOUtil.fetchFileToResponse(httpRequest,response, "附件", "zip", zipFile);
            }catch (Exception e){
                e.printStackTrace();
                response.setStatus(500);
            }
        }
        return;
    }

    /**
     * 删除文件
     *
     * @param annexIds
     * @return
     */
    @Override
    public String deleteAnnex(String annexIds) {
        if (annexIds.isEmpty()){
            return null;
        }
        String[]  ids  = annexIds.split("@");
        for (String id : ids){
            DataAnnexEntity dataAnnexEntity = annexMapper.selectById(id);
            int result = annexMapper.deleteById(id);
            if (result == 1 && dataAnnexEntity != null){
                File file = new File(dataAnnexEntity.getFilePath());
                file.delete();
            }
        }
        return "删除成功";
    }


    /**
     * 删除一组文件
     *
     * @param groupId
     * @param httpRequest
     * @return
     */
    @Override
    @Deprecated
    public String deleteAnnexByGroupId(String groupId, HttpServletRequest httpRequest) {
        List<String> annexIds = annexMapper.selectIdsByGroupId(groupId);

        for(String annexId : annexIds) {
            DataAnnexEntity dataAnnexEntity = annexMapper.selectById(annexId);
            int result = annexMapper.deleteById(annexId);
            if (result == 1 && dataAnnexEntity != null){
                File file = new File(dataAnnexEntity.getFilePath());
                file.delete();
            }
        }
        return "删除成功";
    }


    /**
     * test
     *
     * @param annexIds
     * @return
     */
    @Override
    public String delUpdate(String number, String dataId, String annexIds, String tableName, String key) throws Exception{
        String table = number + "." + tableName;
        //1取出原纪录
        Map<String, Object> old = annexMapper.getOneDataById(table, dataId);
        //2取出annexIds
        String annexIdsOld = old.get(key).toString();
        //3去重

        ArrayList objArray = new ArrayList();
        ArrayList objArray2 = new ArrayList();
        String[] n = annexIds.split("@");
        String[] o = annexIdsOld.split("@");

        for (int k = 0; k < n.length; k++) {
            objArray.add(k, n[k]);
        }
        for (int k = 0; k < o.length; k++) {
            objArray2.add(k, o[k]);
        }
        Boolean r = objArray2.removeAll(objArray);

        if (!r) {
            log.info("去重失败");
            return "去重失败";
        }
        log.info("去重后：{}" , objArray2);
        //更新数据
        StringBuilder sb = new StringBuilder();
        for (Object id : objArray2){
            sb.append(id.toString()).append("@");
        }
        //删除最后一个分号
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }
        old.put(key, sb.toString());
        //更新ES
        EsUtil.updataData(tableName, number, dataId, old);
        log.info("ES 更新完成");
        //更新数据库
        annexMapper.updateOneData(table, dataId, key, sb.toString());
        log.info("DB 更新完成");
        //删除文件
        deleteAnnex(annexIds);
        log.info("文件删除完成");
        return null;
    }


    @Override
    public ApiResult getAnnexListByTempalteName(String tempalteName, int page, int pageSize) {
        //1根据模板名称获取附件字段
        List<Map<String, String>> annexWordList = templateEntityMapper.annexWordList(tempalteName);
        if (annexWordList == null || annexWordList.size() == 0){
            log.info("根据模板名称获取附件字段---为空");
            return ApiResult.fail("暂无数据");
        }
        //2约定展示仅有一个附件字段，取第一个
        String annexWord = annexWordList.get(0).get("item");
        String table = annexWordList.get(0).get("number");
        //数据存放所在库名
        String sorceName = annexWordList.get(0).get("template_source_name");

        //3根据字段取所有附件id列表
        List<String> annexList = annexMapper.getAnnexFileIdList(sorceName + "." + table, annexWord);
        List<String> annexIdList = new ArrayList<>();
        for (String id : annexList){
            if (id == null) {
                continue;
            }
            String[] ids = id.split("@");
            for (String iid : ids) {
                annexIdList.add(iid);
            }
        }
        //增加分页
        List<DataAnnexEntity> annexEntityList2 = annexMapper.getAnnexListByIdList(annexIdList);
        startPage(page, pageSize,null,null);
        List<DataAnnexEntity> annexEntityList = annexMapper.getAnnexListByIdList(annexIdList);
        PageResult result2 = getDataList(annexEntityList);

        List<Map<String, String>> result = new ArrayList<>();
        if (annexEntityList ==null || annexEntityList.size() == 0){
            return ApiResult.fail("暂无数据");
        }
        //期数和日期
        Map<String, String> data = new HashMap<>();
        for(DataAnnexEntity annexEntity : (List<DataAnnexEntity>)result2.getData()){
            Map<String, String> tmp = new HashMap<>();
            tmp.put("id", annexEntity.getId());

            /*** 附加期数和日期start*/
            data = getExtraInfoByAnnexId(sorceName + "." + table, annexEntity.getId(), jydate, volume, annexWord);
            if (data == null ) {
                tmp.put("jydate", "暂无日期数据");
                tmp.put("volume", "暂无期数数据");
            }else {
                tmp.put("jydate", data.get(jydate));
                tmp.put("volume", data.get(volume));
            }
            /*** 附加期数和日期end*/

            tmp.put("oldName", annexEntity.getOldName());
            tmp.put("url", annexEntity.getUrl());
            tmp.put("md5", annexEntity.getMd5());
            tmp.put("importDate", annexEntity.getImportDate().toString());
            result.add(tmp);
        }
        Map<String, Object> r = new HashMap<>();
        r.put("data", result);
        r.put("total", annexEntityList2.size());
        return ApiResult.success("附件获取成功",r);
    }

    /**
     * 根据附件ID获取包含此附件的期数和日期
     * @param table
     * @param annexId
     * @return   @Param("table") String table, @Param("annexId") String annexId,@Param("jydate") String jydate,@Param("volume") String volume, @Param("annexWord") String annexWord);
     */
    private Map<String, String> getExtraInfoByAnnexId(String table, String annexId, String jydate, String volume, String annexWord){
        Map<String, String> result = annexMapper.getExtraInfoByAnnexId(table, annexId, jydate, volume, annexWord);
        return result;
    }
}
