package com.cnzk.controller;

import com.alibaba.fastjson.JSON;

import com.cnzk.aoplog.Log;
import com.cnzk.pojo.*;

import com.cnzk.service.AdminService;
import com.cnzk.service.MenuService;
import com.cnzk.service.RoleServeice;
import com.cnzk.utils.MD5;
import com.cnzk.utils.ResponseUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/AdminController")
public class AdminController
{
	@Resource
	private AdminService adminService;
	@Resource
	private MenuService menuService;
	@Resource
	private RoleServeice roleServeice;
	private char[] codeSequence = {
			'A', '1', 'B', 'C', '2', 'D', '3', 'E', '4', 'F', '5', 'G', '6', 'H', '7', 'I', '8', 'J', 'K', '9', 'L', '1', 'M', '2', 'N', 'P', '3', 'Q', '4', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	//登陆l
	@RequestMapping(value = "/adminlogin", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String adminLogin(@RequestParam Map<String, Object> param, HttpSession session)
	{
		System.out.println("===============================管理员登陆=============================");
		String vcode = session.getAttribute("vcode").toString();//获取session上的验证码
		System.out.println("验证码：" + vcode);
		if(vcode.equalsIgnoreCase(param.get("adminvcode").toString())){
		return adminService.adminlogin(param, session);//获取service层返回的信息
		}
		return "验证码错误";
	}
	//验证码

	/**
	 * 获取验证码图片
	 *
	 * @param session
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/CheckCodeServlet")
	public void CheckCodeServlet(HttpSession session, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("=====================获取验证码=======================");
		int width = 75;
		int height = 37;
		Random random = new Random();
		//设置response头信息
		//禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		//生成缓冲区image类
		BufferedImage image = new BufferedImage(width, height, 1);
		//产生image类的Graphics用于绘制操作
		Graphics g = image.getGraphics();
		//Graphics类的样式
		g.setColor(this.getColor(200, 250));
		g.setFont(new Font("Times New Roman", 0, 28));
		g.fillRect(0, 0, width, height);
		//绘制干扰线
		for (int i = 0; i < 40; i++)
		{
			g.setColor(this.getColor(130, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}

		//绘制字符
		String strCode = "";
		for (int i = 0; i < 4; i++)
		{
			String rand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			strCode = strCode + rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 15 * i + 6, 28);
		}
		//将字符保存到session中用于前端的验证
		session.setAttribute("vcode", strCode.toLowerCase());
		g.dispose();

		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.getOutputStream().flush();
	}

	public Color getColor(int fc, int bc)
	{
		Random random = new Random();
		if (fc > 255)
		{
			fc = 255;
		}
		if (bc > 255)
		{
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	//获取管理员列表
	@ResponseBody
	@RequestMapping("queryAdmin")
	public Object queryAdmin(String msg, String page, String limit)
	{
		System.out.println("带条件获取管理员列表");
		Admin admin = new Admin();
		if (msg != null && !"".equals(msg.trim()))
		{
			admin = JSON.parseObject(msg, Admin.class);
		}
		//获取页码;
		int startPage = Integer.parseInt(page);
		//每页数量
		int pageSize = Integer.parseInt(limit);
		//计算出起始查询位置
		int start = (startPage - 1) * pageSize;
		LayuiData layuiData = adminService.queryAdmin(admin, start, pageSize);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	//	添加管理员
	@RequestMapping("/addAdmin")
	@ResponseBody
	@Log(operationThing = "添加管理员", operationType = "添加")
	public Object addAdmin(String msg)
	{
		Admin admin = JSON.parseObject(msg, Admin.class);
		admin.setAdminPass(MD5.machining(admin.getAdminPass()));
		boolean bool = false;
		int num = adminService.addAdmin(admin);
		if (num > 0)
		{
			System.out.println("管理员添加成功");
			bool = true;
		}
		return bool;
	}

	//	删除管理员
	@RequestMapping("/deleteAdmin")
	@ResponseBody
	@Log(operationThing = "删除管理员信息", operationType = "删除")
	public Object deleteAdmin(String delList, String adminId)
	{
		int num = 0;
		boolean bool = false;
		if (adminId != null && !"".equals(adminId.trim()))
		{
			System.out.println("删除管理员adminId = " + adminId);
			int[] arr = {Integer.parseInt(adminId)};
			num = adminService.deleteAdmin(arr);
		}
		else
		{
			System.out.println("delList = " + delList);
			String[] strArr = delList.split(",");
			int[] intArr = new int[strArr.length];
			for (int i = 0; i < strArr.length; i++)
			{
				intArr[i] = Integer.parseInt(strArr[i]);
			}
			num = adminService.deleteAdmin(intArr);
		}
		if (num > 0)
		{
			System.out.println("删除成功");
			bool = true;

		}
		return bool;
	}

	//	更新管理员信息
	@RequestMapping("/updateAdmin")
	@Log(operationThing = "更新管理员信息", operationType = "修改")
	public @ResponseBody
	String updateAdmin(String msg)
	{
		Admin admin = null;
		boolean bool = false;
		System.out.println("更新msg = " + msg);
		if (msg != null && !"".equals(msg))
		{
			admin = JSON.parseObject(msg, Admin.class);
		}
		int num = adminService.updateAdmin(admin);
		if (num > 0)
		{
			System.out.println("成功");
			bool = true;
		}
		return bool + "";
	}

	//	上传头像
	@RequestMapping("/upload")
	@ResponseBody
	@Log(operationThing = "上传管理员头像", operationType = "添加")
	public Object fileUpload(String msg, MultipartFile file, HttpServletRequest request) throws IOException
	{
		//获取上传文件名 : file.getOriginalFilename();
		String uploadFileName = file.getOriginalFilename();
		System.out.println("msg = " + msg);
		String savePath = request.getServletContext().getRealPath("/upload/");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(new Date());
//		要保存的文件路径和名称
		String fileName = str + uploadFileName;
		String projectPath = savePath + fileName;
		File files = new File(projectPath);
		if (!files.getParentFile().exists())
		{
			files.getParentFile().mkdirs();
		}
		file.transferTo(files);//将接收的文件保存到指定文件中
		System.out.println("文件保存路径================" + projectPath);

		LayuiData layuiData = new LayuiData();
		int num = adminService.uploadAdminImg(projectPath, msg);
		if (num > 0)
		{
			layuiData.setCode(0);
			layuiData.setMsg("上传成功");
			System.out.println("--------------------上传成功");
		}
		return layuiData;
	}

	//更改管理员状态
	@ResponseBody
	@RequestMapping("updateState")
	public void updateState(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("doPost");

		Admin admin = new Admin();

		admin.setAdminName(request.getParameter("adminName"));
		admin.setAdminState(request.getParameter("adminState"));

		int i = adminService.updateState(admin);
		if (i > 0)
		{
			response.getWriter().write("true");
		}
		else
		{
			response.getWriter().write("false");
		}
	}

	//查角色列表
	@ResponseBody
	@RequestMapping("/queryRolesList")
	public Object queryRolesList(String page, String limit, String roleName, HttpServletRequest request, HttpServletResponse response)
	{
		Admin admin = (Admin) request.getSession().getAttribute("tbAdmin");
		Integer roleId = admin.getRoleId();
		//存带有值得条件
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("roleId", roleId);
		//获取页码
		int startPage;
		//每页数量
		int pageSize;
		//计算出起始查询位置
		int start;
		if (null != limit && !"".equals(limit.trim()) && !"0".equals(limit))
		{
			pageSize = Integer.valueOf(limit);
			condition.put("pageSize", pageSize);
		}
		else
		{
			pageSize = 10;
			condition.put("pageSize", pageSize);
		}
		if (null != page && !"".equals(page.trim()) && !"0".equals(page))
		{
			startPage = Integer.parseInt(page);
			start = (startPage - 1) * pageSize;
			condition.put("start", start);
		}
		else
		{
			start = 0;
			condition.put("start", start);
		}
		condition.put("roleName", roleName);
		LayuiData layuiData = roleServeice.queryRolesList(condition);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	//  查权限
	@RequestMapping("/queryMenuTree")
	@ResponseBody
	public void queryMenuTree(TbRole tbRole, HttpServletRequest request, HttpServletResponse response)
	{
		List<MenuVo> treeDataList = menuService.menuAuthority(Integer.valueOf(tbRole.getRoleId() + ""));
		if (treeDataList.size() != 0)
		{
			ResponseUtils.outJson(response, treeDataList);
		}
		else
		{
			ResponseUtils.outHtml(response, "error");
		}
	}

	//  修改权限
	@RequestMapping("/updateMenuTree")
	@ResponseBody
	@Log(operationThing = "修改权限", operationType = "修改")
	public void updateMenuTree(HttpServletRequest request, HttpServletResponse response) {
		Integer rolesid = Integer.valueOf(request.getParameter("roleId"));
		String treeStr = request.getParameter("checkData");
		System.out.println("updateMenuTree_rolesid:"+rolesid);
		System.out.println("updateMenuTree_treeStr:"+treeStr);
		Integer row = roleServeice.updateMenuTree(treeStr, rolesid);
		if (row != 0) {
			ResponseUtils.outHtml(response, "success");
		}
		else {
			ResponseUtils.outHtml(response, "error");
		}
	}

	//查计费规则列表
	@ResponseBody
	@RequestMapping("/queryRatesList")
	public Object queryRatesList(String page, String limit)
	{
		System.out.println("--------queryRatesList-------");
		//获取页码
		int startPage = Integer.parseInt(page);
		//每页数量
		int pageSize = Integer.valueOf(limit);
		;
		//计算出起始查询位置
		int start = (startPage - 1) * pageSize;
		//存带有值得条件
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("pageSize", pageSize);
		condition.put("start", start);

		LayuiData layuiData = adminService.queryRatesList(condition);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	//查月缴产品列表
	@ResponseBody
	@RequestMapping("/queryComboList")
	public Object queryComboList(String page, String limit)
	{
		System.out.println("--------queryComboList-------");
		//获取页码
		int startPage = Integer.parseInt(page);
		//每页数量
		int pageSize = Integer.valueOf(limit);
		;
		//计算出起始查询位置
		int start = (startPage - 1) * pageSize;
		//存带有值得条件
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("pageSize", pageSize);
		condition.put("start", start);

		LayuiData layuiData = adminService.queryComboList(condition);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	//  修改权限
	@RequestMapping("/editRates")
	@Transactional
	@ResponseBody
	@Log(operationThing = "修改收费金额", operationType = "修改")
	public void editRates(HttpServletRequest request, HttpServletResponse response, TbRates tbRates)
	{
		System.out.println("--------editRates-------");
		System.out.println(tbRates);
		Integer row = adminService.editRates(tbRates);
		if (row != 0)
		{
			ResponseUtils.outHtml(response, "success");
		}
		else
		{
			ResponseUtils.outHtml(response, "error");
		}
	}

	//	查询计费规则
	@ResponseBody
	@RequestMapping("/queryPrice")
	public Object queryPrice()
	{
		System.out.println("--------queryRatesList-------");
		TbRates tbRates = adminService.queryPrice();
		System.out.println("tbRates = " + JSON.toJSONString(tbRates));
		return tbRates;
	}

	//    新增月缴状态
	@ResponseBody
	@RequestMapping("/addCombo")
	@Log(operationThing = "添加月缴产品", operationType = "添加")
	public Object addCombo(TbCombo tbCombo)
	{
		tbCombo.setComboState("启用");
		if (adminService.addCombo(tbCombo) != 0)
		{
			System.out.println("添加成功");
			return "true";
		}
		else
		{
			System.out.println("删除失败");
			return "false";
		}

	}

	//    修改月缴状态
	@ResponseBody
	@RequestMapping("/updataCombo")
	@Log(operationThing = "修改月缴产品状态", operationType = "修改")
	public Object updataCombo(TbCombo tbCombo)
	{
		System.out.println("+++++++++++++++");
		System.out.println(tbCombo.toString());
		if (tbCombo.getComboState().equals("启用"))
		{
			tbCombo.setComboState("禁用");
		}
		else
		{
			tbCombo.setComboState("启用");
		}
		if (adminService.updataCombo(tbCombo) != 0)
		{
			System.out.println("修改成功");
			return "true";
		}
		else
		{
			System.out.println("修改失败");
			return "false";
		}

	}

	//    修改月缴产品
	@ResponseBody
	@RequestMapping("/editCombo")
	@Log(operationThing = "修改月缴产品", operationType = "修改")
	public Object editCombo(TbCombo tbCombo)
	{
		if (adminService.editCombo(tbCombo) != 0)
		{
			System.out.println("修改成功");
			return "true";
		}
		else
		{
			System.out.println("修改失败");
			return "false";
		}
	}

	//	查收支明细
	@ResponseBody
	@RequestMapping("queryBill")
	public Object queryBill(String page, String limit, String billNum, String billTime)
	{
		LayuiData layuiData = adminService.queryBill(page, limit, billNum, billTime);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	@ResponseBody
	@RequestMapping("queryParam")
	public Object queryParam(String page, String limit)
	{
		int startPage = Integer.parseInt(page);//获取页码;
		int pageSize = Integer.parseInt(limit);//每页数量
		int start = (startPage - 1) * pageSize;//计算出起始查询位置
		LayuiData layuiData = adminService.queryParam(start, pageSize);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	@ResponseBody
	@RequestMapping("editParam")
	public Object editParam(TbParam tbParam)
	{
		if (adminService.editParam(tbParam) != 0)
		{
			System.out.println("修改成功");
			return "true";
		}
		else
		{
			System.out.println("修改失败");
			return "false";
		}
	}


	//用户统计
	@RequestMapping("/showBillStatistics")
	@ResponseBody
	public void showBillStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
	{
		System.out.println("showBillStatistics");
		String mystatic = request.getParameter("mystatic");
		System.out.println(mystatic);
		//存带有值得条件
		HashMap<String, Object> condition = new HashMap<>();
		if (null != mystatic && !"".equals(mystatic.trim()))
		{
			condition.put("mystatic", mystatic);
		}
		HashMap<String, Object> statisticsMap = adminService.showBillStatistics(condition);
		StatisticsData statisticsData = new StatisticsData(statisticsMap);
		if (null != statisticsMap)
		{
			ResponseUtils.outJson(response, statisticsData);
		}
		else
		{
			response.getWriter().print("error");
		}
	}

	//	月缴统计
	@RequestMapping("/showPieComboStatistics")
	@ResponseBody
	public void showPieComboStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		System.out.println("showPieComboStatistics");
		HashMap<String, Object> statisticsMap = adminService.showPieComboStatistics();
		StatisticsData statisticsData = new StatisticsData(statisticsMap);
		if (null != statisticsMap)
		{
			ResponseUtils.outJson(response, statisticsData);
		}
		else
		{
			response.getWriter().print("error");
		}
	}

	//		退出登录
	@RequestMapping("/outLogin")
	@ResponseBody
	@Log(operationThing = "退出登录", operationType = "退出")
	public String outLogin(HttpServletRequest request)
	{
		request.getSession().invalidate();
//		Boolean out = true;
		return "success";
	}

	@RequestMapping("/querySlideShow")
	@ResponseBody
	public Object querySlideShow(String page,String limit){
		System.out.println("--------querySlideShow-------");
		//获取页码
		int startPage = Integer.parseInt(page);
		//每页数量
		int pageSize = Integer.valueOf(limit);;
		//计算出起始查询位置
		int start = (startPage - 1) * pageSize;
		//存带有值得条件
		LayuiData layuiData=adminService.querySlideShow(start,pageSize);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

	@RequestMapping(value = "/slideshowupload")
	@ResponseBody
	public Object upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String fileName) {

		System.out.println("fileName=" + fileName);
		try {
			//获取文件名
			String originalName = file.getOriginalFilename();
			//扩展名
			String prefix = originalName.substring(originalName.lastIndexOf(".") + 1);
			Date date = new Date();
			//使用UUID+后缀名保存文件名，防止中文乱码问题
			String uuid = UUID.randomUUID() + "";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = simpleDateFormat.format(date);
			String savePath = request.getSession().getServletContext().getRealPath("/upload/");
			//要保存的问题件路径和名称
			String projectPath = savePath + dateStr + File.separator + uuid + "." + prefix;
			System.out.println("savePath==" + savePath);
			System.out.println("dateStr==" + dateStr);
			System.out.println("projectPath==" + projectPath);
			File files = new File(projectPath);
			//打印查看上传路径
			if (!files.getParentFile().exists()) {//判断目录是否存在
				System.out.println("files11111=" + files.getPath());
				files.getParentFile().mkdirs();
			}
			file.transferTo(files); // 将接收的文件保存到指定文件中
			System.out.println(projectPath);
			LayuiData layuiData = new LayuiData();
			layuiData.setCode(0);
			layuiData.setMsg("上传成功");
//            projectPath="http://localhost:8080/parkinglot/upload/"+ dateStr + "/"+ uuid + "." + prefix;
			projectPath="http://39.102.35.36:8080/parkinglot/upload/"+ dateStr + "/"+ uuid + "." + prefix;
//			TabAdmin admin = (TabAdmin) request.getSession().getAttribute("admin");
			layuiData.setFiles(projectPath);

			return JSON.toJSONString(layuiData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@ResponseBody
	@RequestMapping("/addSlideShow")
	@Log(operationThing = "添加轮播图",operationType = "添加")
	public Object addSlideShow(TbSlideshow tbSlideshow){
		if (adminService.addSlideShow(tbSlideshow)!=0){
			System.out.println("添加成功");
			return "true";
		}else {
			System.out.println("添加失败");
			return "false";
		}

	}
	@ResponseBody
	@RequestMapping("/deleteSlideShow")
	@Log(operationThing = "删除轮播图",operationType = "删除")
	public Object deleteSlideShow(TbSlideshow tbSlideshow){
		if (adminService.deleteSlideShow(tbSlideshow)!=0){
			System.out.println("删除成功");
			return "true";
		}else {
			System.out.println("删除失败");
			return "false";
		}

	}

	@ResponseBody
	@RequestMapping("/editSlideShow")
	@Log(operationThing = "删除轮播图",operationType = "删除")
	public Object editSlideShow(TbSlideshow tbSlideshow){
		if (adminService.editSlideShow(tbSlideshow)!=0){
			System.out.println("删除成功");
			return "true";
		}else {
			System.out.println("删除失败");
			return "false";
		}

	}

	@RequestMapping("/queryFeedback")
	@ResponseBody
	public Object queryFeedback(String page,String limit){
		System.out.println("--------queryFeedback-------");
		//获取页码
		int startPage = Integer.parseInt(page);
		//每页数量
		int pageSize = Integer.valueOf(limit);;
		//计算出起始查询位置
		int start = (startPage - 1) * pageSize;
		//存带有值得条件
		LayuiData layuiData=adminService.queryfeedback(start,pageSize);
		System.out.println("layuiData = " + JSON.toJSONString(layuiData));
		return layuiData;
	}

}
