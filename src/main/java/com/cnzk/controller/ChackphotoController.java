package com.cnzk.controller;

import com.cnzk.service.ChackphotoService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/ChackphotoController")
public class ChackphotoController
{
	@Autowired
	private ChackphotoService chackphotoService;

	@RequestMapping("/File")
	@ResponseBody
	public String file(@RequestParam("file")MultipartFile file){

		String carnum = chackphotoService.file(file);
		System.out.println("车牌-----------:"+carnum);

		SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
		String starttime = sdf.format(new Date());
		//查车找人
		String username=chackphotoService.finduser(carnum);
		System.out.println("入场用户-----------"+username);
        //入场插入数据
		chackphotoService.caraddenter(carnum,starttime);
		String state;
		//车辆情况查询 是临时还是有身份
		if ("临时用户".equals(username)){
			state = "临时车";
		} else {
			state=chackphotoService.findcarvip(carnum);
		}
		System.out.println("车辆情况---------"+state);
		//车位查询
		String ps=null;



		//		Object obj = new Gson().toJson()
		return carnum+","+username+","+state+","+starttime+","+ps;

	}

	@RequestMapping("/File2")
	@ResponseBody
	public String file2(@RequestParam("file2")MultipartFile file2){

		String carnum = chackphotoService.file2(file2);
		System.out.println("车牌-----------:"+carnum);

		SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
		String exittime = sdf.format(new Date());//查车找人

		String username=chackphotoService.finduser(carnum);
		System.out.println("入场用户-----------"+username);
		//出场插入数据
		chackphotoService.caraddexit(carnum,exittime);
		//车辆情况查询 是临时还是有身份
		String state;
		if ("临时用户".equals(username)){
			state = "临时车";
		} else {
			state=chackphotoService.findcarvip(carnum);
		}
		System.out.println("车辆情况---------"+state);
		//查询入场时间
		String entertime=chackphotoService.findentertime(carnum);
		System.out.println("查询入场时间"+entertime);
		//车位查询
		String ps=null;
		String time=null;
		String money=null;
		String date= carnum+","+username+","+state+","+ps+","+entertime+","+exittime+","+time+","+money;
		System.out.println("车牌："+carnum+"用户名："+username+"车状态："+state+"车位："+ps+"进场时间："+entertime+"出场时间："+exittime+"总时长："+time+"收费："+money);
		//		Object obj = new Gson().toJson()
		return date;

	}

}
