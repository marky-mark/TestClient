package com.test.hello;

import com.mtt.api.client.TestAPI;
import com.mtt.api.model.APITask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private TestAPI testAPI;

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public ModelAndView getTest(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("In the Test Controller");
        Map<String, Object> model = new HashMap<String, Object>();

        APITask task = testAPI.taskApi().getTask(1L);
        model.put("testTask", task);

        return new ModelAndView("test",model);
    }
}
