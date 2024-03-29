package com.omniscience.client;

import java.util.ArrayList;
import java.util.List;

import com.omniscience.shared.Constants;
import com.omniscience.shared.FieldVerifier;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.SerializableCalendar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.Pair;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Omniscience implements EntryPoint {		
	private final AddCalendarServiceAsync addCalendarService = GWT
			.create(AddCalendarService.class);

	private final GetCalendarsServiceAsync getCalendarsService = GWT
			.create(GetCalendarsService.class);

	private final AuthenticationServiceAsync authenticationService = GWT
			.create(AuthenticationService.class);
	
	private final DeleteCalendarServiceAsync deleteCalendarService = GWT
			.create(DeleteCalendarService.class);

	private final EditCalendarServiceAsync editCalendarService = GWT
			.create(EditCalendarService.class);

	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel statusBarPanel = new HorizontalPanel();
	private HorizontalPanel contentPanel = new HorizontalPanel();
    private VerticalPanel addCalendarPanel = new VerticalPanel();
    private VerticalPanel calendarListPanel = new VerticalPanel();
    private VerticalPanel aboutPanel = new VerticalPanel();
	private FlexTable calendarsTable = new FlexTable();
	private FlexTable addCalendarForm = new FlexTable();
	//private VerticalPanel listPanel = new VerticalPanel();
	private TextBox newCalendarUrlTextBox = new TextBox();
	private TextBox newLatitudeTextBox = new TextBox();
	private TextBox newLongitudeTextBox = new TextBox();
	private TextBox newAltitudeTextBox = new TextBox();
	private Button addCalendarButton = new Button("Add");
	private Button resetFormButton = new Button("Reset Form");
	private Label tableMessageLabel = new Label("Refreshing Table...");
	private Label globalMessageLabel = new Label();
	private Omniscience me;
	
	private Label addCalendarMessageLabel = new Label();	
	
	private class CalendarWidget extends Label {
		private SerializableCalendar calendar;
		
		public SerializableCalendar getCalendar() {
			return calendar;
		}

		public void setCalendar(SerializableCalendar calendar) {
			this.calendar = calendar;
		}

		public CalendarWidget(SerializableCalendar cal) {
			super(cal.getName());
			this.calendar = cal;
		}				
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		this.me = this;
		Label loading = new Label("Loading App...");
		RootPanel.get("calendarList").add(loading);
		RootPanel.get("calendarList").add(globalMessageLabel);
		final AsyncCallback<String> loginUrlCallback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				globalMessageLabel.setStyleName("errorLabel");
				globalMessageLabel.setText("Error trying to login");
			}

			@Override
			public void onSuccess(String result) {
				Window.Location.replace(result);				
			}
		};
		
		final AsyncCallback<List<String>> logoutUrlCallback = 
				new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				globalMessageLabel.setStyleName("errorLabel");
				globalMessageLabel.setText("Error trying to login");							
			}

			@Override
			public void onSuccess(List<String> result) {
				createPage(result.get(0), result.get(1));				
			}
		};

		
		authenticationService.isUserLoggedIn(new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				globalMessageLabel.setStyleName("errorLabel");
				globalMessageLabel.setText("Error trying to login");
			}

			@Override
			public void onSuccess(Boolean result) {
				String myUrl = Window.Location.getHref();
				if (result) {
				  authenticationService.getLogoutUrl(myUrl, logoutUrlCallback);
				} else {
					// Redirect to login page
					authenticationService.getLoginUrl(myUrl, loginUrlCallback);
				}
			}
		});						
	}

	private void createPage(String username, String logoutUrl) {
		RootPanel.get("calendarList").clear();
		RootPanel.get("calendarList").add(globalMessageLabel);
		globalMessageLabel.setText("");
		
		statusBarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		statusBarPanel.setStyleName("statusBarPanel");
		
		Anchor usernameLabel = new Anchor(username);		
		usernameLabel.setStyleName("logoutLink");
		usernameLabel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("https://accounts.google.com", "", "");				
			}
		});
		statusBarPanel.add(usernameLabel);
				
		Anchor googleCalendarLink = new Anchor("Google Calendar");
		googleCalendarLink.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("https://calendar.google.com", "", "");				
			}
		});
		googleCalendarLink.setStyleName("logoutLink");
		statusBarPanel.add(googleCalendarLink);
		
		Anchor googleMapsLink = new Anchor("Google Maps");
		googleMapsLink.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("https://maps.google.com", "", "");				
			}
		});
		googleMapsLink.setStyleName("logoutLink");
		statusBarPanel.add(googleMapsLink);
					
		Anchor logoutLink = new Anchor("Logout", logoutUrl);
		logoutLink.setStyleName("logoutLink");
		statusBarPanel.add(logoutLink);						
		createAddCalendarForm();
		createAboutPage(aboutPanel);
		
		Label addDescription = new Label("Add a new calendar:");
		addDescription.setStyleName("subheader");
		addDescription.getElement().getStyle().setFontWeight(FontWeight.BOLD);		
		addCalendarPanel.add(addDescription);
		addCalendarPanel.add(addCalendarMessageLabel);
		addCalendarPanel.add(addCalendarForm);
		addCalendarPanel.setStyleName("addCalendarPanel");
		
		Label listDescription = new Label("Your calendars:");
		listDescription.setStyleName("subheader");
		listDescription.getElement().getStyle().setFontWeight(FontWeight.BOLD);		
		calendarListPanel.add(listDescription);
		calendarListPanel.add(tableMessageLabel);		
		calendarsTable.getColumnFormatter().addStyleName(0, "calendarsTableCalName");
		calendarListPanel.add(calendarsTable);
		calendarListPanel.setStyleName("calendarListPanel");
		
		
		
		contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		contentPanel.setStyleName("contentPanel");
		contentPanel.setWidth("100%");
		
		contentPanel.add(addCalendarPanel);
		contentPanel.add(calendarListPanel);
		contentPanel.add(aboutPanel);
		
		
		contentPanel.setCellWidth(aboutPanel, "30%");
		contentPanel.setCellWidth(addCalendarPanel, "30%");
		contentPanel.setCellWidth(calendarListPanel, "30%");
		mainPanel.setStyleName("mainPanel");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(statusBarPanel);
		mainPanel.add(contentPanel);
		
		
		RootPanel.get("calendarList").add(mainPanel);
		
		//alendarsTable.getRowFormatter().addStyleName(0, "calendarsTableHeader");
		calendarsTable.setStyleName("calendarsTable");
		
		// Create a handler for the sendButton and nameField
		class AddCalendarButtonHandler implements ClickHandler {			

			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendCalendarToServer();
			}
			
			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendCalendarToServer() {
  			// First, we validate the input.
				addCalendarMessageLabel.setText("Saving Calendar...");
				if(newCalendarUrlTextBox.getText().isEmpty() ||
					newLatitudeTextBox.getText().isEmpty() || 
					newLongitudeTextBox.getText().isEmpty() ||
					newAltitudeTextBox.getText().isEmpty()) {
					addCalendarMessageLabel.setText("Required fields missing!");
					return;
				}

				// Then, we send the input to the server.
				addCalendarButton.setEnabled(false);
				
				
				addCalendarService.addCalendar(newCalendarUrlTextBox.getText(), 
						newLatitudeTextBox.getText(), 
						newLongitudeTextBox.getText(), 
						newAltitudeTextBox.getText(), 
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								addCalendarMessageLabel.setText(Constants.SERVER_ERROR + caught.getMessage());
								addCalendarMessageLabel.setStyleName("errorLabel");
								addCalendarButton.setEnabled(true);
							}

							public void onSuccess(String result) {
								addCalendarMessageLabel.setText("Saved!!");
								addCalendarMessageLabel.setStyleName("successLabel");
								addCalendarButton.setEnabled(true);
								refreshCalendarList();
							}
						});						
			}
		}

		// Add a handler to send the name to the server		
		addCalendarButton.addClickHandler(new AddCalendarButtonHandler());		
		refreshCalendarList();
	}

	private void createAboutPage(VerticalPanel aboutPanel) {
		aboutPanel.setStyleName("aboutPanel");
		Label heading = new Label("What is Omniscience?");
		heading.setStyleName("subheader");
		
		HTML description = new HTML();
		description.setHTML(Constants.ABOUT);		
		aboutPanel.add(heading);
		aboutPanel.add(description);
		
	}

	private void createAddCalendarForm() {
		addCalendarForm.setStyleName("addCalendarForm");
		addCalendarForm.setText(0, 0, "Calendar URL");
		addCalendarForm.setWidget(0, 1, newCalendarUrlTextBox);
		
		addCalendarForm.setText(1, 0, "Latitude");
		addCalendarForm.setWidget(1, 1, newLatitudeTextBox);
		
		addCalendarForm.setText(2, 0, "Longitude");
		addCalendarForm.setWidget(2, 1, newLongitudeTextBox);

		addCalendarForm.setText(3, 0, "Altitude");
		addCalendarForm.setWidget(3, 1, newAltitudeTextBox);
			
		addCalendarForm.setWidget(4, 0, addCalendarButton);
		addCalendarForm.setWidget(4, 1, resetFormButton);
		
		// Add a handler to close the DialogBox
		resetFormButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCalendarMessageLabel.setText("");
				addCalendarMessageLabel.setStyleName("normalLabel");
				newCalendarUrlTextBox.setText("");
				newLatitudeTextBox.setText("");
				newLongitudeTextBox.setText("");
				newAltitudeTextBox.setText("");
			}
		});
	}
	
	public void refreshCalendarList() {
		tableMessageLabel.setText("Refreshing Table...");		
		
		// TODO set "refreshing"  icon
		calendarsTable.removeAllRows();				
	//	calendarsTable.setText(0, 0, "Calendar Name");
	//	calendarsTable.setText(0, 1, "Edit");
	//	calendarsTable.setText(0, 2, "Remove");	
	//	calendarsTable.getRowFormatter().addStyleName(0, "calendarsTableHeader");
		
		
		getCalendarsService.getCalendarsForCurrentUser(
				new AsyncCallback<List<SerializableCalendar>>() {
					@Override
					public void onFailure(Throwable caught) {
						tableMessageLabel.setText("Error fetching cals");
						tableMessageLabel.setStyleName("errorLabel");
					}

					@Override
					public void onSuccess(List<SerializableCalendar> result) {
						tableMessageLabel.setText("Refreshing Table...");				
						
						for(final SerializableCalendar cal : result) {
							final int row = calendarsTable.getRowCount();
							final CalendarWidget calWidget = new CalendarWidget(cal);
							calendarsTable.setWidget(row, 0, calWidget);
							
							Anchor editLink = new Anchor("Edit");
							editLink.setStyleName("linkStyle");
							calendarsTable.setWidget(row, 1, editLink);
							calendarsTable.getRowFormatter().setStyleName(row, "calendarsTableRow");
							editLink.addClickHandler(new ClickHandler() {								
								@Override
								public void onClick(ClickEvent event) {
									EditCalendarDialog dialog = 
											new EditCalendarDialog(cal, editCalendarService, me);
									dialog.showRelativeTo(calWidget);									
								}
							});
							
							final Anchor removeLink = new Anchor("Delete");
							removeLink.setStyleName("linkStyle");
							calendarsTable.setWidget(row, 2, removeLink);
							removeLink.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									removeLink.setEnabled(false);
									deleteCalendarService.deleteCalendar(cal.getUrl(), 
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(
														Throwable caught) {
													tableMessageLabel.setText("Error deleting caledar:" 
														+ caught.getMessage());
													tableMessageLabel.setStyleName("errorLabel");
													removeLink.setEnabled(true);
												}

												@Override
												public void onSuccess(
														Void result) {													
													calendarsTable.removeRow(row);
													tableMessageLabel.setText("Deleted entry!");
													tableMessageLabel.setStyleName("successLabel");
													removeLink.setEnabled(true);
												}
											});
								}
								
							});
						}						
						tableMessageLabel.setText("");
						tableMessageLabel.setStyleName("normalLabel");
					}										
				});		
			}		
}
