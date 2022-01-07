package edu.kh.fin.common;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class Util {
	
	public static void swalSetMessage(String title, String text, String icon, String button, RedirectAttributes ra) {
		ra.addFlashAttribute("title", title);
		ra.addFlashAttribute("text", text);
		ra.addFlashAttribute("icon", icon);
		ra.addFlashAttribute("button", button);
	}
}
