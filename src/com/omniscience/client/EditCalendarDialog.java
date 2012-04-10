package com.omniscience.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.omniscience.shared.jdo.SerializableCalendar;

public class EditCalendarDialog extends DialogBox {	
	
	private TextBox newCalendarUrlTextBox = new TextBox();
	private FlexTable editCalendarForm = new FlexTable();
	private TextBox newLatitudeTextBox = new TextBox();
	private TextBox newLongitudeTextBox = new TextBox();
	private TextBox newAltitudeTextBox = new TextBox();
	private Button saveCalendarButton = new Button("Save");
	private Button cancelButton = new Button("Close/Cancel");
	private DialogBox dialogBox;
	private VerticalPanel dialogVPanel = new VerticalPanel();
	private HorizontalPanel titlePanel = new HorizontalPanel();
	private Label messageLabel = new Label();
	private final EditCalendarServiceAsync editCalendarService;
	
	public EditCalendarDialog(SerializableCalendar cal, 
			EditCalendarServiceAsync service, final Omniscience parent) {
		// Create the popup dialog box
		super();
		this.editCalendarService = service;
		dialogBox = this;
		
		dialogBox.setText("Edit Calendar");
		this.setAnimationEnabled(true);		
		
		
		createEditCalendarForm(cal);				
		saveCalendarButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			  saveCalendarButton.setEnabled(false);
			  editCalendarService.editCalendar(newCalendarUrlTextBox.getText(), 
					  newLatitudeTextBox.getText(), 
					  newLongitudeTextBox.getText(), 
					  newAltitudeTextBox.getText(), 
					  new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							messageLabel.setText("Saved!");
							messageLabel.setStyleName("successLabel");
							saveCalendarButton.setEnabled(true);
							parent.refreshCalendarList();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							messageLabel.setText("Error:" + caught.getMessage());
							messageLabel.setStyleName("errorLabel");
							saveCalendarButton.setEnabled(true);
						}
					});	
				
			}
		});
		
		dialogVPanel.add(titlePanel);
		dialogVPanel.add(messageLabel);
		dialogVPanel.add(editCalendarForm);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setModal(true);
	}
	
	private void createEditCalendarForm(SerializableCalendar cal) {
		editCalendarForm.setText(0, 0, "Calendar Name");
		editCalendarForm.setText(0, 1, cal.getName());
		
		editCalendarForm.setText(1, 0, "Calendar Description");
		editCalendarForm.setText(1, 1, cal.getDescription());
				
		editCalendarForm.setText(2, 0, "Calendar URL");
		editCalendarForm.setWidget(2, 1, newCalendarUrlTextBox);
		newCalendarUrlTextBox.setText(cal.getUrl());
		
		editCalendarForm.setText(3, 0, "Latitude");
		editCalendarForm.setWidget(3, 1, newLatitudeTextBox);
		newLatitudeTextBox.setText(cal.getLatitude());
		
		editCalendarForm.setText(4, 0, "Longitude");
		editCalendarForm.setWidget(4, 1, newLongitudeTextBox);
		newLongitudeTextBox.setText(cal.getLongitude());

		editCalendarForm.setText(5, 0, "Altitude");
		editCalendarForm.setWidget(5, 1, newAltitudeTextBox);
		newAltitudeTextBox.setText(cal.getAltitude());
			
		editCalendarForm.setWidget(6, 0, saveCalendarButton);
		editCalendarForm.setWidget(6, 1, cancelButton);
		
		// Add a handler to close the DialogBox
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
	}
}
