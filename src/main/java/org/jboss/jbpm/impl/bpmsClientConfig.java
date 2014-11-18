package org.jboss.jbpm.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class bpmsClientConfig {

    public    String server = "http://localhost:8080/business-central/";
    public    String username = "erics";
    public    String password = "bpmsuite1!";

    public   long MAX_TASK_DELAY;
    public   String BooleanChoice[] = {"False","False","True"}; // 66% False, 33% True
    public   String NameChoice[] = new String[100];
    public   long MAX_PRICE;
    public   String NofTChoice[] = new String[10];


    private static final String DefaultFilePath = "/home/aubbiali/IdeaProjects/jbpm6-process-client/classes/artifacts/bpmsClient.json";

    public bpmsClientConfig () {

    }

    public void parseJSON (String filePath) {

    try {
              if (filePath==null)
                  filePath = DefaultFilePath;
                else
                   if (filePath == "")
                        filePath = DefaultFilePath;

              // read the json file
              FileReader reader = new FileReader(filePath);

              JSONParser jsonParser = new JSONParser();
              JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

              // get a String from the JSON object
              server = (String) jsonObject.get("url");
              System.out.println("Server: " + server);
              server = (String) jsonObject.get("password");
              System.out.println("password: " + password);
              server = (String) jsonObject.get("username");
              System.out.println("username: " + username);



              MAX_TASK_DELAY  =  (Long) jsonObject.get("max_task_delay");
              System.out.println("The max task delay  is: " + MAX_TASK_DELAY);

              MAX_PRICE  =  (Long) jsonObject.get("price");
              System.out.println("The max price  is: " + MAX_PRICE);


              JSONArray arr = (JSONArray) jsonObject.get("names");

             for (int i = 0; i < arr.size(); i++) {
                  NameChoice[i]  = (String) arr.get(i).toString();
                  System.out.println(NameChoice[i]);

              }
              arr = (JSONArray) jsonObject.get("review");
            for (int i = 0; i < arr.size(); i++) {
                BooleanChoice[i] = (String) arr.get(i).toString();
                System.out.println(BooleanChoice[i]);
            }
            arr = (JSONArray) jsonObject.get("review");
            for (int i = 0; i < arr.size(); i++) {
                NofTChoice[i] = (String) arr.get(i).toString();
                System.out.println(NofTChoice[i]);
            }


/*
              // get an array from the JSON object
              JSONArray lang= (JSONArray) jsonObject.get("languages");

              // take the elements of the json array
              for(int i=0; i<lang.size(); i++){
                      System.out.println("The " + i + " element of the array: "+lang.get(i));
                  }
              Iterator i = lang.iterator();


                  // take each value from the json array separately
                  while (i.hasNext()) {
                      JSONObject innerObj = (JSONObject) i.next();
                      System.out.println("language "+ innerObj.get("lang") +
                              " with level " + innerObj.get("knowledge"));
                  }
                  // handle a structure into the json object
                  JSONObject structure = (JSONObject) jsonObject.get("job");
                  System.out.println("Into job structure, name: " + structure.get("name"));
*/
              } catch (FileNotFoundException ex) {
                  ex.printStackTrace();
              } catch (IOException ex) {
                  ex.printStackTrace();
              } catch (ParseException ex) {
                  ex.printStackTrace();
              } catch (NullPointerException ex) {
                  ex.printStackTrace();
              }
    }
}