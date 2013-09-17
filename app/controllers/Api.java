package controllers;

import play.*;
import play.mvc.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import models.*;


@With(Secure.class)
public class Api extends Controller {
	
	@Before
	static void initSession() throws Throwable {
		
	    if(Security.isConnected()) {
	    	renderArgs.put("user", Security.connected());
	    	
	    	Account account = Account.find("username = ?", Security.connected()).first();
	            
	        if(account == null && Account.count() == 0) {
	        	account = new Account("admin", "admin", "admin@test.com", true, null);
	        	account.save();
	        }
	           
	        renderArgs.put("agencyId", account.agencyId);
        }
        else {
        	Secure.login();
        }
    }
	

    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();

    private static String toJson(Object pojo, boolean prettyPrint)
            throws JsonMappingException, JsonGenerationException, IOException {
                StringWriter sw = new StringWriter();
                JsonGenerator jg = jf.createJsonGenerator(sw);
                if (prettyPrint) {
                    jg.useDefaultPrettyPrinter();
                }
                mapper.writeValue(jg, pojo);
                return sw.toString();
            }

    // **** alert controllers ****

    public static void getAlert(Long id) {
        try {
            if(id != null) {
                Alert alert = Alert.findById(id);
                if(alert != null)
                    renderJSON(Api.toJson(alert, false));
                else
                    notFound();
            }
            else {
                renderJSON(Api.toJson(Alert.all().fetch(), false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }

    }

    public static void createAlert() {
        Alert alert;
        
        

        try {
            alert = mapper.readValue(params.get("body"), Alert.class);
            
            // security check
            if(!alert.securityCheck((String)renderArgs.get("agencyId")))
            	badRequest();
            	
            alert.save();

            renderJSON(Api.toJson(alert, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    
    }


    public static void updateAlert() {
        Alert alert;

        try {
            alert = mapper.readValue(params.get("body"), Alert.class);

            // security check
            if(!alert.securityCheck((String)renderArgs.get("agencyId")))
            	badRequest();
            
            if(alert.id == null || Alert.findById(alert.id) == null)
                badRequest();

            Alert updatedAlert = Alert.em().merge(alert);
            updatedAlert.save();

            renderJSON(Api.toJson(updatedAlert, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    }

    public static void deleteAlert(Long id) {
        if(id == null)
            badRequest();

        Alert alert = Alert.findById(id);

        if(alert == null)
            badRequest();
        
        // security check
        if(!alert.securityCheck((String)renderArgs.get("agencyId")))
        	badRequest();

        alert.delete();

        ok();
    }
    
    // **** InformedEntity controllers ****
    

    public static void getInformedEntity(Long id) {
        try {
            if(id != null) {
            	InformedEntity ie = Alert.findById(id);
                if(ie != null)
                    renderJSON(Api.toJson(ie, false));
                else
                    notFound();
            }
            else {
                renderJSON(Api.toJson(InformedEntity.all().fetch(), false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }

    }

    public static void createInformedEntity() {
    	InformedEntity ie;

        try {
        	ie = mapper.readValue(params.get("body"), InformedEntity.class);
            ie.save();

            renderJSON(Api.toJson(ie, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    }

    public static void updateInformedEntity() {
    	InformedEntity ie;

        try {
        	ie = mapper.readValue(params.get("body"), InformedEntity.class);

            if(ie.id == null || Alert.findById(ie.id) == null)
                badRequest();

            InformedEntity updatedInformedEntity = InformedEntity.em().merge(ie);
            updatedInformedEntity.save();

            renderJSON(Api.toJson(updatedInformedEntity, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    }

    public static void deleteInformedEntity(Long id) {
        if(id == null)
            badRequest();

        InformedEntity ie = InformedEntity.findById(id);

        if(ie == null)
            badRequest();

        ie.delete();

        ok();
    }

    // **** TimeRange controllers ****

    public static void getTimeRange(Long id) {
        try {
            if(id != null) {
            	TimeRange tr = TimeRange.findById(id);
                if(tr != null)
                    renderJSON(Api.toJson(tr, false));
                else
                    notFound();
            }
            else {
                renderJSON(Api.toJson(TimeRange.all().fetch(), false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }

    }

    public static void createTimeRange() {
    	TimeRange tr;

        try {
        	tr = mapper.readValue(params.get("body"), TimeRange.class);
        	tr.save();

            renderJSON(Api.toJson(tr, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    }


    public static void updateTimeRange() {
    	TimeRange tr;

        try {
        	tr = mapper.readValue(params.get("body"), TimeRange.class);

            if(tr.id == null || TimeRange.findById(tr.id) == null)
                badRequest();

            TimeRange updatedTimeRange = TimeRange.em().merge(tr);
            updatedTimeRange.save();

            renderJSON(Api.toJson(updatedTimeRange, false));
        } catch (Exception e) {
            e.printStackTrace();
            badRequest();
        }
    }

    public static void deleteTimeRange(Long id) {
        if(id == null)
            badRequest();

        TimeRange tr = TimeRange.findById(id);

        if(tr == null)
            badRequest();

        tr.delete();

        ok();
    }
    

}