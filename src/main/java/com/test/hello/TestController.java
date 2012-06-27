package com.test.hello;

import com.mtt.api.client.TestAPI;
import com.mtt.api.client.exception.ApiException;
import com.mtt.api.client.exception.ValidationException;
import com.mtt.api.client.impl.ExceptionTranslator;
import com.mtt.api.model.APITask;
import com.mtt.service.request.CreateTaskRequest;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import sun.security.validator.ValidatorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private TestAPI testAPI;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public ModelAndView getTest(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("In the Test Controller");
        Map<String, Object> model = new HashMap<String, Object>();

        //retrieve a task that exists
        APITask task = testAPI.taskApi().getTask(1L);

        //try and retrieve a task which does not exist
        try {
            task = testAPI.taskApi().getTask(3L);
        } catch (ClientResponseFailure failure) {
            //--->>>NOTE: this contains the error code, the exception (i.e. ValidationException), error messages
            //This needs to be translated etc
            try {
                throw exceptionTranslator.translateResponseFailure(failure);
            } catch (ApiException ex) {
                ex.getStatusCode();
            }
        }

        //try and update an task which fails due to validation
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("<html>");
        createTaskRequest.setUserId(1L);

        try {
            task = testAPI.taskApi().updateTask(1L, createTaskRequest);
        } catch (ClientResponseFailure failure) {

            try {
                throw exceptionTranslator.translateResponseFailure(failure);
            } catch (ValidationException ex) {
                //the error codes need to be translated to english for each field
                ex.getErrors();
            }
        }

        model.put("testTask", task);

        return new ModelAndView("test",model);
    }
}
