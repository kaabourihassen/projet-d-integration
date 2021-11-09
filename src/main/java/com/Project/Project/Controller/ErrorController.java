package com.Project.Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class ErrorController {

	@RequestMapping("403")
	public String get403() {
		return "error/403";
	}
}
