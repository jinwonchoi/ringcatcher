package com.eclues.ringcatcher.ctrl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eclues.ringcatcher.dao.CustomerDAO;
import com.eclues.ringcatcher.dao.EnvironmentBean;
import com.eclues.ringcatcher.dao.UserInfoDAO;
import com.eclues.ringcatcher.obj.Customer;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
//	@Autowired
//	private UserInfoDAO userInfoDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private EnvironmentBean environmentBean;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public ModelAndView home(Locale locale, ModelAndView model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		logger.info("dafsdfa");
//		logger.info("["+environmentBean.getDomainName()+"]");
//		logger.info("["+environmentBean.getDomainIp()+"]");
//
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addObject("serverTime", formattedDate );
//	
//		List<Customer> listCustomer = customerDAO.list();
//		System.out.println("ListCustomer");
//		model.addObject("listCustomer", listCustomer);
//		model.setViewName("home");
//		return model;
////		return "home";
//	}
//	
//	@RequestMapping(value = "/upfile", method = RequestMethod.GET)
//	public ModelAndView upfile(Locale locale, ModelAndView model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		model.setViewName("upload");
//		return model;
////		return "home";
//	}
//
//	@RequestMapping(value = "/ringfileup", method = RequestMethod.GET)
//	public ModelAndView ringfileup(Locale locale, ModelAndView model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		model.setViewName("ringfileup");
//		return model;
////		return "home";
//	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model) {
		String greetings = "Greetings, This is Ringcatcher!";
		model.addAttribute("message",greetings);
		return "test";
	}
//	
//	@RequestMapping(value="/register", method=RequestMethod.GET)
//	public ModelAndView register(ModelAndView model) {
//		model.setViewName("register");
//		return model;
//	}
//
//	@RequestMapping(value="/testjquery", method=RequestMethod.GET)
//	public ModelAndView testjquery(ModelAndView model) {
//		model.setViewName("testjquery");
//		return model;
//	}
//	
//
//	@RequestMapping(value="/")
//	public ModelAndView listCustomer(ModelAndView model) throws IOException {
//		List<Customer> listCustomer = customerDAO.list();
//		System.out.println("ListCustomer");
//		model.addObject("listCustomer", listCustomer);
//		model.setViewName("home");
//		return model;
//	}
//	
//	@RequestMapping(value="/newCustomer", method=RequestMethod.GET)
//	public ModelAndView newCustomer(ModelAndView model) {
//		Customer newCustomer = new Customer();
//		model.addObject("customer",newCustomer);
//		model.setViewName("CustomerForm");
//		return model;
//	}
//	
//	@RequestMapping(value="/saveCustomer", method= RequestMethod.POST)
//	public ModelAndView saveCustomer(@ModelAttribute Customer customer) {
//		System.out.println("saveCustomer");
//		customerDAO.saveOrUpdate(customer);
//		return new ModelAndView("redirect:/");
//	}
//	
//	@RequestMapping(value="/deleteCustomer", method=RequestMethod.GET) 
//	public ModelAndView deleteCustomer(HttpServletRequest request) {
//		String customerId = request.getParameter("id");
//		customerDAO.delete(customerId);
//		return new ModelAndView("redirect:/");
//	}
//	
//	@RequestMapping(value="/editCustomer", method=RequestMethod.GET)
//	public ModelAndView editCustomer(HttpServletRequest request) {
//		String customerId = request.getParameter("id");
//		Customer customer = customerDAO.get(customerId);
//		ModelAndView model = new ModelAndView("CustomerForm");
//		model.addObject("customer",customer);
//		return model;
//	}
}
