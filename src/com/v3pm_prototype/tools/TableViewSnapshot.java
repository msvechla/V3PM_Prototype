package com.v3pm_prototype.tools;

import java.text.DecimalFormat;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.database.DBProcess;

/**
 * Class for copying tables to the clipboard in the MSExcel format
 * @author Marius Svechla
 *
 */
public class TableViewSnapshot implements EventHandler<ActionEvent> {

	private TableView<Object> tableView;

	public TableViewSnapshot(TableView tableView) {
		this.tableView = tableView;
	}
	
	@Override
	public void handle(ActionEvent event) {
		int old_r = -1;
		StringBuilder clipboardString = new StringBuilder();
		for (TableColumn tc : tableView.getColumns()) {
			clipboardString.append(tc.getText() + "\t");
		}
		clipboardString.append("\n");
		for (int row = 0; row < tableView.getItems().size(); row++) {
			for (int column = 0; column < tableView.getColumns().size(); column++) {
				Object cell = tableView.getColumns().get(column)
						.getCellData(row);
				if (cell == null) {
					clipboardString.append("");
				} else {
					if (column == 0) {
						clipboardString.append(cell + "\t");
					} else {
						if(cell instanceof String || cell instanceof DBProcess){
							clipboardString.append(cell + "\t");
						}else{
							DecimalFormat formatter = new DecimalFormat("#0.00");
							clipboardString.append(formatter.format(cell) + "\t");
						}
						
					}
				}
			}
			clipboardString.append("\n");
		}

		final ClipboardContent content = new ClipboardContent();
		content.putString(clipboardString.toString());
		Clipboard.getSystemClipboard().setContent(content);
		
		Notifications.create()
        .title("Snapshot Taken")
        .text("A snapshot of the component has been taken and is available in your clipboard.")
        .showInformation();
	}
}
