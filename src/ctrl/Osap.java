package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Loan;

/**
 * Servlet implementation class Osap
 */
@WebServlet({ "/Osap", "/Osap/*"  })						// * means -> all url with osap  
public class Osap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String default_principal = "1000.0";		// hard coded value, used instead of going into web.xml
	private static String default_period =  "24.0";
	private static String default_interest =  "5.0";
	private static String gracePeriod = "6.0";
	double graceInterest = 0, payment = 0;					// check this out this is the new  computation system
	Loan ln =  new Loan();  								// method call for loan
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Osap() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();

		String principal = context.getInitParameter("principal");
		String userInterest = context.getInitParameter("interest");
		String period = context.getInitParameter("period");

		// Instantiate Loan object
		context.setAttribute("mLoan", new Loan());
	}
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext context = this.getServletContext();
		String appName = this.getServletContext().getInitParameter("applicationName"); // application name
		
		 //user input values,
		String principal = request.getParameter("principal");
		String userInterest = request.getParameter("interest");
		String period = request.getParameter("period"); 				// grace period
		

		// input from query String [these are the user input values]
		if (request.getParameterMap().isEmpty()) {
			principal = default_principal; // the default values
			period = default_period;
			userInterest = default_interest;
		} else {
			principal = principal; // the user values
			period = period;
			userInterest = userInterest;
		}
		
		try {
			if (request.getParameter("inputGrace") == null) {
				graceInterest  = 0.0;
			} else {
				graceInterest = ln.computeGraceInterest(principal, gracePeriod, userInterest, default_interest);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (request.getParameter("inputGrace") != null) {
				payment = ln.computePayment(principal, userInterest, userInterest, period, gracePeriod, default_interest) + 
						((graceInterest) / Double.parseDouble(gracePeriod));
			} else {
				payment = ln.computePayment(principal, userInterest, userInterest, period, gracePeriod, default_interest);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getServletContext().setAttribute("GI", graceInterest);
		request.getSession().setAttribute("GI", graceInterest);
		
		request.getServletContext().setAttribute("PAY", payment);
		request.getSession().setAttribute("PAY", payment);
		
		
		// synchronous buttons
		if (request.getParameter("calculate") == null &&  !(request.getRequestURI().equals("/OsapCalc-v4/Osap/Ajax/")) ||
				(request.getParameter("restart") != null && request.getParameter("restart").equals("true"))){			
			setErrorResponse(request);
			request.getRequestDispatcher("/UI.jspx").forward(request, response);  // send to UI page 
			request.getSession().setAttribute("errorMessage","hello");

		} else if (request.getParameter("calculate") == null || request.getParameter("ajax") !=  null) { 				
			response.getWriter().append("Served at: ").append(request.getContextPath());
			response.setContentType("text/plain");
			Writer resOut = response.getWriter();
			
		
			// task E : save session, how do you get the data from servlet into the results page
			HttpSession session = request.getSession();
			request.getServletContext().setAttribute("APPNAME", appName);
			request.getSession().setAttribute("APPNAME", appName);
			
			request.getServletContext().setAttribute("PRINCIPAL", principal);
			request.getSession().setAttribute("PRINCIPAL", principal);
			
			request.getServletContext().setAttribute("ANNUALINTEREST",userInterest);
			request.getSession().setAttribute("ANNUALINTEREST",userInterest);

			request.getServletContext().setAttribute("PAYMENTPERIOD", period );
			request.getSession().setAttribute("PAYMENTPERIOD", period );
			
			request.getServletContext().setAttribute("GI", graceInterest);
			request.getSession().setAttribute("GI", graceInterest);
			
			request.getServletContext().setAttribute("PAY", payment);
			request.getSession().setAttribute("PAY", payment);

			//check
			session.getServletContext().setAttribute("PRINCIPAL", principal);
			session.getServletContext().setAttribute("ANNUALINTEREST",userInterest);
			session.getServletContext().setAttribute("PAYMENTPERIOD", period );
			session.getServletContext().setAttribute("GI", graceInterest);
			session.getServletContext().setAttribute("PAY", payment);
			
			request.getRequestDispatcher("/Results.jspx").forward(request, response);		// send to results page 
		
		} 
		else if (request.getParameter("ajax") != null) {
			setErrorResponse(request); // ajax button
			request.getRequestDispatcher("/UI.jspx").forward(request, response); // send to UI page
			request.getSession().setAttribute("errorMessage", "hello");
		} 
		else if (request.getParameter("ajax") == null) {
			
			
			response.getWriter().write("Grace Period Interest: " + graceInterest + "</br>" + "Monthly Payments: " + String.format("%.2f",payment));
			response.getWriter().write(" ");
			System.out.println("submit clicked");
			System.out.println("ajax clicked");
			System.out.println("-----------------graceInterestHandler------------------------");
			System.out.println("default_principal = " + default_principal);
			System.out.println("default_period =  " + default_period);
			System.out.println("default_interest =  " + default_interest);
			System.out.println("------------------------------------------");
			System.out.println("principal " + principal);
			System.out.println("period " + period);
			System.out.println("user interest " + userInterest);

		} 
	}
	

	/*
	 * error handler message
	 */
	private void setErrorResponse(HttpServletRequest request) {
		if (request.getParameter("principal") != null && (request.getParameter("period") != null)
				&& (request.getParameter("interest") != null)) {

			if (Double.parseDouble(request.getParameter("principal")) <= 0) {
				request.getServletContext().setAttribute("errorMessage",  "Principal muse be greater than 0.");
//				request.getSession().setAttribute("errorMessage", "Principal muse be greater than 0.");
			}
			if (Double.parseDouble(request.getParameter("interest")) <= 0) {
				request.getServletContext().setAttribute("errorMessage", "Annual Interest must be greater than 0.");
//				request.getSession().setAttribute("errorMessage", "Interest must be greater than 0.");
			}
			if (Double.parseDouble(request.getParameter("period")) <= 0) {
				request.getServletContext().setAttribute("errorMessage", "Payment Period must be greater than 0.");
//				request.getSession().setAttribute("errorMessage", "Period must be greater than 0.");
			}
		}
	}
	
	/*
	 * exception handler
	 */
	private boolean isExceptionHandler(HttpServletRequest request) {
		if (Double.parseDouble(request.getParameter("principal")) <= 0
				|| Double.parseDouble(request.getParameter("interest")) <= 0
				|| Double.parseDouble(request.getParameter("period")) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
