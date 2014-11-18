package org.jboss.jbpm;



import org.apache.http.HttpException;
import org.jboss.jbpm.api.Jbpm6Client.TasksBy;
import org.jboss.jbpm.api.Task;
import org.jboss.jbpm.impl.Jbpm6ClientImpl;
import org.jboss.jbpm.impl.Jbpm6ClientObjects;
import org.jboss.jbpm.impl.bpmsClientConfig;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static javax.xml.xpath.XPathConstants.STRING;


public class bpmsClientThread2 implements Runnable {

    Thread runner;



    private Jbpm6ClientImpl client;
    private Jbpm6ClientObjects clientObjects;


    private String processid = null;

    private List<Task> tasks = null;

    bpmsClientConfig config;

    public bpmsClientThread2() {

    }


    public  void bpmsCLientThread2 (String threadName, bpmsClientConfig config) {

        runner = new Thread(this, threadName); // (1) Create a new thread.
        System.out.println(runner.getName());
        this.config = config;
        runner.start(); // (2) Start the thread.

    }

    private String rndChoice(String [] a) {
        int idx = new Random().nextInt(a.length);
        return a[idx];
    }

    private Task taskReady (List<Task> tasks) {

        if (tasks == null) return null;



        for(Task task_i : tasks) {

            if (task_i.getStatus().contains("Ready"))  {

                return task_i;

            }

        }
        return null;
    }




    private void init() {
        client = new Jbpm6ClientImpl(config.server, config.username, config.password, true);
        clientObjects = new Jbpm6ClientObjects(config.server, config.username, config.password);
    }

    private Task executeNextTask (int retries) {

        tasks = null;
        Task task = null;
        String response;

        System.out.println(Thread.currentThread());
        System.out.println("Process "+processid+": next task");
        for (int i= 0; i<retries; i++){
            try {
                tasks = clientObjects.getTasks(TasksBy.processInstanceId, processid);

            } catch (HttpException e) {
                e.printStackTrace();
            }

            task = taskReady(tasks);

            if (task != null) break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (task == null)
                return (task);



        System.out.println("completing task " + task.getName()+ " "+task.getId()+  ": " + task.getDescription()+task.getStatus());
        try {
                response = client.claimTask(task.getId());
                response = client.startTask(task.getId());
            try {
                Thread.sleep(new Random().nextInt((int) config.MAX_TASK_DELAY));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (task.getName().contains("Price Review")) {
                    Integer price = new Random().nextInt((int) config.MAX_PRICE);
                    String priceString = price.toString() + "i";
                    response = client.completeTask(task.getId(),
                            "totalPriceOut=" + priceString
                                    + ",reviewerCommentOut=No Comments");
                }

                else if (task.getName().contains("Booking Complete")) {
                    response = client.completeTask(task.getId(),
                            "");
                }

                else if (task.getName().contains("Employee Booking")) {
                    response = client.completeTask(task.getId(),
                        "reviewRequiredOut="+ rndChoice(config.BooleanChoice)
                        +",reviewRequiredDetailsOut=No Details,bookingConfirmedOut=NO");
                }
         }
         catch (HttpException e) {
                        e.printStackTrace();
                        return null;
         }

        System.out.println("Completed " + task.getName()+ " "+task.getId()+  ": " + response);

        return task;

    }



    public void run() {

        Task task = null;

        String response = null;

        init();

        System.out.println(Thread.currentThread());

        try {


            response = client.startProcess("org.specialtripsagency:specialtripsagencyproject:2.0.0",
                    "org.specialtripsagency.specialtripsagencyprocess",
                    "applicantName="+rndChoice(config.NameChoice)+
                    ",emailAddress=myemail@email.com" +
                    ",numberOfTravelers="+rndChoice(config.NofTChoice)+
                    ",otherDetails=NO,fromDestination=London,toDestination=Edinburgh");

            System.out.println("Process started 1: " + response);

            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response.toString().getBytes()));
                System.out.println("Process  2: " + doc.getDoctype());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            XPath xpath = XPathFactory.newInstance().newXPath();


            try {
                processid = (String) xpath.evaluate("/process-instance/id", doc, STRING);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }


        } catch (HttpException e) {
            e.printStackTrace();
        }


        System.out.println("Process 3:" + processid); // Display the string.




        while (true) {



            task = executeNextTask (5);
            if (task == null)
                    break;


        }








    }
}
