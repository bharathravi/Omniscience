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

	private List<SerializableCalendar> calendarList = new ArrayList<SerializableCalendar>();
	
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
		statusBarPanel.add(new Label("Hello " + username + "!"));
		statusBarPanel.add(new Anchor("Logout", logoutUrl));
				
		createAddCalendarForm();

		Label addDescription = new Label("Add a new calendar:");
		addDescription.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		addCalendarPanel.add(addDescription);
		addCalendarPanel.add(addCalendarMessageLabel);
		addCalendarPanel.add(addCalendarForm);
		
		Label listDescription = new Label("Your calendars:");
		listDescription.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		calendarListPanel.add(listDescription);
		calendarListPanel.add(tableMessageLabel);		
		calendarListPanel.add(calendarsTable);
		
		contentPanel.add(addCalendarPanel);
		contentPanel.add(calendarListPanel);
		
		mainPanel.add(statusBarPanel);
		mainPanel.add(contentPanel);
		
		RootPanel.get("calendarList").add(mainPanel);
		
		
	
		
		

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

	private void createAddCalendarForm() {
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
				newCalendarUrlTextBox.setText("");
				newLatitudeTextBox.setText("");
				newLongitudeTextBox.setText("");
				newAltitudeTextBox.setText("");
			}
		});
	}
	
	public void refreshCalendarList() {
		tableMessageLabel.setText("Refreshing Table...");
		calendarListPanel.remove(calendarsTable);
		getCalendarsService.getCalendarsForCurrentUser(
				new AsyncCallback<List<SerializableCalendar>>() {
					@Override
					public void onFailure(Throwable caught) {
						globalMessageLabel.setText("Error fetching cals");						
					}

					@Override
					public void onSuccess(List<SerializableCalendar> result) {
						for (SerializableCalendar cal : result) {
							if (!calendarList.contains(cal)) {
								calendarList.add(cal);
							}
						}
						
						tableMessageLabel.setText("Refreshing Table...");				
						
						// TODO set "refreshing"  icon
						calendarsTable.removeAllRows();		
						calendarsTable.setText(0, 0, "Calendar Name");
						calendarsTable.setText(0, 1, "Edit");
						calendarsTable.setText(0, 2, "Remove");														
						
						for(final SerializableCalendar cal : calendarList) {
							final int row = calendarsTable.getRowCount();
							calendarsTable.setWidget(row, 0, new CalendarWidget(cal));
							
							Button editButton = new Button("Edit");
							calendarsTable.setWidget(row, 1, editButton);
							editButton.addClickHandler(new ClickHandler() {								
								@Override
								public void onClick(ClickEvent event) {
									EditCalendarDialog dialog = 
											new EditCalendarDialog(cal, editCalendarService, me);
									dialog.center();									
								}
							});
							
							final Button removeButton = new Button("Delete");
							calendarsTable.setWidget(row, 2, removeButton);
							removeButton.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									removeButton.setEnabled(false);
									deleteCalendarService.deleteCalendar(cal.getUrl(), 
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(
														Throwable caught) {
													tableMessageLabel.setText("Error deleting caledar:" 
														+ caught.getMessage());
													tableMessageLabel.setStyleName("errorLabel");
													removeButton.setEnabled(true);
												}

												@Override
												public void onSuccess(
														Void result) {
													calendarList.remove(cal);
													calendarsTable.removeRow(row);
													tableMessageLabel.setText("Deleted entry!");
													tableMessageLabel.setStyleName("successLabel");
													removeButton.setEnabled(true);
												}
											});
								}
								
							});
						}
						tableMessageLabel.setText("");
						calendarListPanel.add(calendarsTable);						
					}										
				});		
			}		
}
