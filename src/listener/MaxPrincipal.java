package listener;

import javax.servlet.ServletRequestAttributeEvent;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;


/**
 * Application Lifecycle Listener implementation class MaxPrincip
 *
 */

@WebListener
public class MaxPrincipal implements HttpSessionAttributeListener {
	
	private double maxPrincipal;
	
    /**
     * Default constructor. 
     */
    public MaxPrincipal() {
        // TODO Auto-generated constructor stub
    	maxPrincipal = 0.0;
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    	if (event.getName().equals("principal")) {
			 handleEvent(event);
		}
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event)  { 
         // TODO Auto-generated method stub
    	if (event.getName().equals("principal")) {
			 handleEvent(event);
		}
    }
    
    public void handleEvent(HttpSessionBindingEvent event) {
		double princip = Double.parseDouble(event.getValue().toString());  // gets the principal input from UI.jspx
		
		if (princip > maxPrincipal) {
			maxPrincipal = princip;
		}
//		event.getSession().getServletContext().setAttribute("maxPrincipal", maxPrincipal);
	}	
}
