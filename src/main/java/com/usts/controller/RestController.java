package com.usts.controller;
import java.util.*;

import com.usts.lib.model.NodeInformation;
import com.usts.service.ServiceUtil;
import com.usts.tools.ChangToNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest")
public class RestController {
	ServiceUtil service = new ServiceUtil();


	@RequestMapping(value = "/nonAddress", produces = "application/json; charset=utf-8")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ResponseBody
	public Map matchAddress(@RequestBody Map map) {
	    Map resultMap = new HashMap();
	    resultMap.put("requestService",new Date().getTime());
//	    if (map.get("nonAddress")!=null){
//
//        }
		String nonAddress = map.get("nonAddress").toString();
		System.out.println(nonAddress);
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<NodeInformation> node = new ArrayList<>();
		try {
			if (nonAddress.length() > 1 && nonAddress.length() < 100) {
				list = service.getRight(nonAddress);
				node = ChangToNode.convertNodeInformations(list);
			} else {
				list.add("传入的是无效的值");
			}
		} catch (Exception e) {
			list.add("发生错误...");
		}
		resultMap.put("data",node);
        resultMap.put("responseService",new Date().getTime());
		return resultMap;
	}

    @RequestMapping(value = "/test", produces = "application/json; charset=utf-8")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ResponseBody
	public Map test() {
	    Map map1 = new HashMap();
	    map1.put("hello","HelloWorld");
		return map1;
	}
}