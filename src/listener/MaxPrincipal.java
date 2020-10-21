package listener;

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
    	if (event.equals("principal")) {
    		double principal = Double.parseDouble(event.getValue().toString());
			
			if (principal > maxPrincipal) {
				this.maxPrincipal = principal;
			}
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
    		double principal = Double.parseDouble(event.getValue().toString());
    		
    		if (principal > maxPrincipal) {
				this.maxPrincipal = principal;
			}
//			handleEvent(arg0);
		}
    }
	
}
