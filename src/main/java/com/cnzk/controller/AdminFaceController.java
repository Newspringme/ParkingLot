package com.cnzk.controller;



import com.alibaba.fastjson.JSONObject;
import com.cnzk.pojo.Admin;
import com.cnzk.service.FaceService;
import com.cnzk.utils.GetToken;
import com.cnzk.utils.GsonUtils;
import com.cnzk.utils.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员人脸识别控制类
 */
@Controller
@RequestMapping("/adminFace")
public class AdminFaceController {

    @Resource
    private FaceService faceService;

    /**
     * 路径跳转
     *
     * @param path
     * @return
     */
    @RequestMapping("/path/{uri}")
    public String redirect(@PathVariable(value = "uri") String path) {
        return "/administration/jsp/admin/" + path;
    }


    //添加人脸
    @RequestMapping("/addAdminFace")
    @ResponseBody
    public Map<String, Object> addAdminFace(HttpServletRequest request, HttpSession session) {

        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        Map<String, Object> mapAdd = new HashMap<String, Object>();
        String adminFace = request.getParameter("adminFace");
        System.out.println("adminFace=" + adminFace);

        Admin tbAdmin = (Admin) session.getAttribute("tbAdmin");
        System.out.println("tbadmin=" + tbAdmin.toString());
        try {
            boolean flag = faceSearch(adminFace);//判断百度中是否存在该人脸
            System.out.println("flag:"+flag);
            String result = null;
            if (flag == false) {

                Map<String, Object> map = new HashMap<>();
                map.put("image", adminFace);
                map.put("group_id", "admin_face");//分组
                map.put("user_id", tbAdmin.getAdminName());//账户
                map.put("liveness_control", "NORMAL");
                map.put("image_type", "BASE64");
                map.put("quality_control", "LOW");

                String param = GsonUtils.toJson(map);

                // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                String accessToken = GetToken.getAuth();

                result = HttpUtil.post(url, accessToken, "application/json", param);
                System.out.println("添加result=" + result);
            } else {
                System.out.println("账号名称"+ tbAdmin.getAdminName());
                System.out.println("adminFace"+ adminFace);
                result = faceUpdate(adminFace, tbAdmin.getAdminName());
                System.out.println("更新result=" + result);
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            System.out.println("jsonObject:"+jsonObject);
            JSONObject fromObject = jsonObject.getJSONObject("result");
            System.out.println("fromObject:"+fromObject);
            String face_token = (String) fromObject.get("face_token");//得到百度返回是人脸
            System.out.println("添加人脸face_token=" + face_token);

            if (face_token != null && !"".equals(face_token)) {
                mapAdd.put("msg", "1");

            } else {
                mapAdd.put("msg", "2");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapAdd;
    }

    //人脸登录
    @RequestMapping("/adminFaceLogin")
    @ResponseBody
    public String adminFaceLogin(HttpServletRequest request, HttpServletResponse response) {

        String adminFace = request.getParameter("adminFace");

        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", adminFace);
            map.put("liveness_control", "NORMAL");
            map.put("group_id_list", "admin_face");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = GetToken.getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);

            JSONObject fromObject = JSONObject.parseObject(result);
            JSONObject jsonObject = fromObject.getJSONObject("result");
            // 此时需要加个判断
            if (jsonObject.isEmpty()) {
                System.out.println("jsonObject 为空");
                return "无法识别面部!";
            }
            JSONObject jsonArray = jsonObject.getJSONArray("user_list").getJSONObject(0);
            System.out.println("jsonArray"+jsonArray);
            String adminAccount = (String) jsonArray.get("user_id");

            System.out.println("adminAccount=" + adminAccount);
            Admin tbAdmin = faceService.findAdminByAccount(adminAccount);
            if (tbAdmin != null) {
                if (tbAdmin.getAdminState().equals("启用")) {
                    request.getSession().setAttribute("tbAdmin", tbAdmin);//将管理员信息放到session
                    return "验证成功";
                } else if (tbAdmin.getAdminState().equals("禁用")) {
                    return "该账户已禁用";
//                } else if (tbAdmin.getAdminState() == 2) {
//                    return "该管理员已离职";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "无法识别面部,请用账户密码登录!";
    }

    /**
     * 人脸搜索
     * 查看百度中是否存在当前人脸
     */
    public boolean faceSearch(String image) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("liveness_control", "NORMAL");
            map.put("group_id_list", "admin_face");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = GetToken.getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println("人脸搜索result=" + result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String error_msg = (String) jsonObject.get("error_msg");
            if (error_msg.equals("SUCCESS") && !"".equals(error_msg)){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 人脸更新
     */
    public String faceUpdate(String image, String adminAccount) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("group_id", "admin_face");
            map.put("user_id", adminAccount);
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = GetToken.getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println("人脸更新方法result=" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
