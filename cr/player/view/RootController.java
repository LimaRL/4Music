package cr.player.view;

import cr.player.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RootController {
	private MainApp mainApp;
	
	@FXML
	private TextField login;
	@FXML
	private TextField password;

	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	public void LoginButton() {
		this.mainApp.showLoginDialog();
	}
	
	/**
	 * Sets the person to be edited in the dialog.
	 * 
	 * @param person
	 */
	public void Login() {
		String loginText = (String) login.getText(); 
		
		for(String login : MainApp.dbUser.userDB) {
			if(loginText.equalsIgnoreCase(login)) {
				System.out.println(password.getText());
			}
		}
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/**
	 * setMainApp
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp app) {
		this.mainApp = app;
	}
}
