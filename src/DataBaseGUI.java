import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ... (existing imports)

public class DataBaseGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private static final Map<String, String[]> ATTRIBUTES_MAP = new HashMap<>();
	{
		ATTRIBUTES_MAP.put("ADDRESS", new String[] { "id", "building", "street", "city", "country" });
		ATTRIBUTES_MAP.put("CAR", new String[] { "name", "model", "year", "made" });
		ATTRIBUTES_MAP.put("CAR_PART", new String[] { "car", "part" });
		ATTRIBUTES_MAP.put("CUSTOMER", new String[] { "id", "f_name", "l_name", "address", "job" });
		ATTRIBUTES_MAP.put("DEVICE", new String[] { "no", "name", "price", "weight", "made" });
		ATTRIBUTES_MAP.put("MANUFACTURE", new String[] { "name", "type", "city", "country" });
		ATTRIBUTES_MAP.put("ORDERS", new String[] { "id", "date", "customer", "car" });
	}
	private String[] tableNames = { "address", "car", "car_part", "customer", "device", "manufacture", "orders" };
	private TableView<ObservableList<String>>[] tableViews = new TableView[tableNames.length];
	private ObservableList<ObservableList<String>>[] data = new ObservableList[tableNames.length];
	private VBox vBox;
	TabPane tabPane = new TabPane();
	private TextField[] textfields;
	private TextField deleteTextField;
	private TextField updateTextField;
	private TextField updateTextField1;
	private Connection c;
	private ComboBox<String> foreignKeysCombo = new ComboBox<>();
	private ComboBox<String> foreignKeysCombo2 = null;
	private int columnCount = 0;
	private ComboBox<String> deleteComboBox = new ComboBox<>();
	private ComboBox<String> updateComboBox = new ComboBox<>();
	private ComboBox<String> updateForeignBox = null;
	private ComboBox<String> updateComboBox1 = new ComboBox<>();
	private java.sql.DatabaseMetaData metaData;
	private List<String> foreignKeysList = new ArrayList<>();
	private List<String> foreignKeysTableList = new ArrayList<>();

	public void start(Stage primaryStage) {
		buildData();
		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.setTitle("CARS DATABASE APP");
		primaryStage.show();
	}

	public void buildData() {
		try {
			Tab tablesTab = new Tab("TABLES");
			tablesTab.setId("tablesTab");
			Tab search = new Tab("SEARCH");
			Tab insert = new Tab("INSERT");
			Tab delete = new Tab("DELETE");
			Tab update = new Tab("UPDATE");
			Tab about = new Tab("ABOUT US");
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars", "root", "");
			tablesTab.setContent(Tables());
			metaData = c.getMetaData();
			tabPane.getTabs().add(tablesTab);
			tabPane.setStyle("-fx-tab-min-height: 50px;" + "-fx-tab-min-width: 200px;" + "-fx-font-size: 16px;");
			//////////// ***********************SEARCH STARTS FROM
			//////////// HERE**********************///////////
			ComboBox<String> searchTables = new ComboBox<>();
			VBox searchBox = new VBox();
			ExecuteSQL(searchTables, searchBox, "SEARCH");
			search.setContent(searchBox);
			tabPane.getTabs().add(search);
			//////////// ***********************INSERT STARTS FROM
			//////////// HERE**********************////////////
			ComboBox<String> insertTables = new ComboBox<>();
			VBox insertBox = new VBox();
			ExecuteSQL(insertTables, insertBox, "INSERT");
			insert.setContent(insertBox);
			tabPane.getTabs().add(insert);
			//////////// ***********************DELETE STARTS FROM
			//////////// HERE**********************////////////
			ComboBox<String> deleteTables = new ComboBox<>();
			VBox deleteBox = new VBox();
			ExecuteSQL(deleteTables, deleteBox, "DELETE");
			delete.setContent(deleteBox);
			tabPane.getTabs().add(delete);
			//////////// ***********************UPDATE STARTS FROM
			//////////// HERE**********************////////////
			ComboBox<String> updateTables = new ComboBox<>();
			VBox updateBox = new VBox();
			ExecuteSQL(updateTables, updateBox, "UPDATE");
			update.setContent(updateBox);
			tabPane.getTabs().add(update);
			// tabPane.getTabs().add(about);
			vBox = new VBox(tabPane);
			vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ExecuteSQL(ComboBox<String> comboBox, VBox vbox, String btnValue) {
		Label label = makeLabel(btnValue);
		vbox.getChildren().add(label);
		FadeTransition fade = new FadeTransition(Duration.seconds(2.00), label);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		fade.setAutoReverse(true);
		fade.setCycleCount(FadeTransition.INDEFINITE);
		fade.play();
		comboBox.setStyle("-fx-font-family: 'Arial';" + "-fx-font-size: 25;" + "-fx-text-fill: blue;"
				+ "-fx-background-color: white;" + "-fx-alignment: CENTER;");
		comboBox.setMaxWidth(500);
		vbox.setPadding(new Insets(10, 0, 0, 0)); // Top, Right, Bottom, Left padding
		StackPane paddedComboBox = new StackPane(comboBox);
		comboBox.setPromptText("Select Table");
		vbox.setSpacing(100);
		StackPane pane = new StackPane();
		HBox hbox = new HBox(pane);
		hbox.setSpacing(125);
		hbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(paddedComboBox, hbox);
		comboBox.setItems(FXCollections.observableArrayList("Address", "Car", "Car Part", "Customer", "Device",
				"Manufacture", "Orders"));
		VBox box1 = new VBox();
		Button btn = new Button(btnValue);
		box1.setAlignment(Pos.CENTER);
		btn.setMaxWidth(300);
		btn.setPrefWidth(300);
		comboBox.setOnAction(e -> {
			hbox.getChildren().removeIf(node -> node instanceof TableView && "table".equals(node.getId()));
			vbox.getChildren().remove(btn);
			String value = comboBox.getValue();
			for (int i = 0; i < tableNames.length; i++) {
				pane.getChildren().clear();
				if (value.equalsIgnoreCase(comboBox.getItems().get(i))) {
					String SQL = "SELECT * FROM " + tableNames[i];
					ResultSet rs = null;
					try {
						rs = c.createStatement().executeQuery(SQL);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					TableView<ObservableList<String>> tableViewInner = new TableView<>();
					ObservableList<ObservableList<String>> Innerdata = FXCollections.observableArrayList();
					createTableView(rs, tableViewInner, Innerdata);
					tableViewInner.setId("tableViewInner");
					pane.getChildren().add(tableViewInner);
					HBox[] box = new HBox[tableViewInner.getColumns().size()];
					textfields = new TextField[tableViewInner.getColumns().size()];
					box1.getChildren().clear();
					if (value.equalsIgnoreCase("Car Part")) {
						value = "car_part";
					}
					createFields(btnValue, tableViewInner, box, box1, value);
					box1.setSpacing(20);
					break;
				}
			}
			box1.getChildren().add(btn);
		});
		hbox.getChildren().add(box1);
		btn.setOnAction(e -> {
			String value = comboBox.getValue();
			if (value.equalsIgnoreCase("CAR PART")) {
				value = "car_part";
			}
			String[] attributes = ATTRIBUTES_MAP.get(value.toUpperCase());
			StringBuilder SQL = new StringBuilder();
			StringBuilder values = new StringBuilder();
			if (btnValue.equalsIgnoreCase("SEARCH")) {
				if (ATTRIBUTES_MAP.containsKey(value.toUpperCase())) {
					SQL.append("SELECT * FROM ").append(value.toLowerCase());
					for (int i = 0; i < textfields.length; i++) {
						if (!textfields[i].getText().isEmpty()) {
							if (SQL.indexOf("WHERE") == -1) {
								SQL.append(" WHERE ");
							} else {
								SQL.append(" AND ");
							}
							if (isNumeric(textfields[i].getText())) {
								SQL.append(attributes[i]).append(" = ").append(textfields[i].getText());
							} else {
								SQL.append(attributes[i]).append(" = '").append(textfields[i].getText()).append("'");
							}
						}
					}
					try {
						ResultSet rs = c.createStatement().executeQuery(SQL.toString());
						TableView<ObservableList<String>> view = new TableView<>();
						view.setPrefWidth(400);
						view.setMaxWidth(400);
						view.setId("table");
						createTableView(rs, view, FXCollections.observableArrayList());
						hbox.getChildren().removeIf(node -> node instanceof TableView && "table".equals(node.getId()));
						hbox.getChildren().add(view);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			} else if (btnValue.equalsIgnoreCase("INSERT")) {
				if (ATTRIBUTES_MAP.containsKey(value.toUpperCase())) {
					SQL.append("INSERT INTO ").append(value.toLowerCase()).append(" (");
					values.append(") VALUES (");
					boolean valuesAdded = false;
					boolean foreign1 = true;
					boolean foreign2 = true;
					for (int i = 0; i < textfields.length; i++) {
						boolean isForeignKey = isForeignKeyField(attributes[i]);
						boolean textFieldNotEmpty = textfields[i] != null && !textfields[i].getText().isEmpty();
						boolean foreignKeyValueSelected = isForeignKey && foreignKeysCombo != null
								&& foreignKeysCombo.getValue() != null;
						if (textFieldNotEmpty || foreignKeyValueSelected) {
							if (valuesAdded) {
								SQL.append(", ");
								values.append(", ");
							}
							SQL.append(attributes[i]);
							if (foreignKeyValueSelected && foreign1) {
								if (isNumeric(foreignKeysCombo.getSelectionModel().getSelectedItem())) {
									values.append(foreignKeysCombo.getSelectionModel().getSelectedItem());
								} else {
									values.append("'" + foreignKeysCombo.getSelectionModel().getSelectedItem() + "'");
								}
								foreign1 = false;
							} else if (foreignKeyValueSelected && foreign2) {
								if (isNumeric(foreignKeysCombo2.getSelectionModel().getSelectedItem())) {
									values.append(foreignKeysCombo2.getSelectionModel().getSelectedItem());
								} else {
									values.append("'" + foreignKeysCombo2.getSelectionModel().getSelectedItem() + "'");
								}
								foreign2 = false;
							} else if (textfields[i] != null && isNumeric(textfields[i].getText())) {
								values.append(textfields[i].getText());
							} else if (textfields[i] != null) {
								values.append("'").append(textfields[i].getText()).append("'");
							}
						}
						valuesAdded = true;
					}
					if (valuesAdded) {
						SQL.append(values).append(")");
						System.out.println(SQL);
						try {
							PreparedStatement preparedStatement = c.prepareStatement(SQL.toString());
							int rowsInserted = preparedStatement.executeUpdate();
							if (rowsInserted > 0) {
								Tab tab = null;
								foreignKeysCombo.setValue(null);
								Optional<ComboBox> opt1 = Optional.ofNullable(foreignKeysCombo2);
								if (opt1.isPresent()) {
									foreignKeysCombo2.setValue(null);
								}
								for (int i = 0; i < textfields.length; i++) {
									Optional<TextField> opt = Optional.ofNullable(textfields[i]);
									if (opt.isPresent())
										textfields[i].clear();
								}
								for (int i = 0; i < tabPane.getTabs().size(); i++) {
									if (tabPane.getTabs().get(i).getId().equalsIgnoreCase("tablesTab")) {
										tab = tabPane.getTabs().get(i);
										break;
									}
								}
								tab.setContent(Tables());
								updateTableView(value, pane);
								showMessagePopup("INSERT", "A new record was inserted successfully!");
							} else {
								showMessagePopup("INSERT", "No rows inserted.");
							}
						} catch (SQLException e1) {
							showMessagePopup("INSERT", "Please insert different ID.");
						}
					}
				}
			} else if (btnValue.equalsIgnoreCase("DELETE")) {
				String deleteTable = "";
				if (value.equalsIgnoreCase("Car Part")) {
					deleteTable = "car_part";
				} else {
					for (int i = 0; i < tableNames.length; i++) {
						if (value.equalsIgnoreCase(tableNames[i])) {
							deleteTable = tableNames[i];
							break;
						}
					}
				}
				Optional<TextField> optField = Optional.ofNullable(deleteTextField);
				if (optField.isPresent()) {
					if (isNumeric(deleteTextField.getText().trim())) {
						SQL.append("DELETE FROM " + deleteTable + " WHERE " + deleteComboBox.getValue() + " = "
								+ deleteTextField.getText().trim());
					} else {
						SQL.append("DELETE FROM " + deleteTable + " WHERE " + deleteComboBox.getValue() + " = '"
								+ deleteTextField.getText().trim() + "'");
					}
				}
				try (PreparedStatement preparedStatement = c.prepareStatement(SQL.toString())) {
					int rowsDeleted = preparedStatement.executeUpdate();
					if (rowsDeleted > 0) {
						deleteComboBox.setValue(null);
						deleteTextField.clear();
						updateTableView(value, pane);
						Tab tab = null;
						for (int i = 0; i < tabPane.getTabs().size(); i++) {
							if (tabPane.getTabs().get(i).getId().equalsIgnoreCase("tablesTab")) {
								tab = tabPane.getTabs().get(i);
								break;
							}
						}
						tab.setContent(Tables());
						showMessagePopup("DELETE", "The record was deleted successfully!");
					} else {
						if (deleteTextField.getText().isEmpty()) {
							showMessagePopup("DELETE", "Insert a value to delete.");
						} else {
							showMessagePopup("DELETE", "No rows deleted. The record might not exist.");
						}
					}
				} catch (SQLException e1) {
					showMessagePopup("DELETE", "Please choose what you want to delete.");
				}

			} else if (btnValue.equalsIgnoreCase("UPDATE")) {
				String updateTable = "";
				if (value.equalsIgnoreCase("Car Part")) {
					updateTable = "car_part";
				} else {
					for (int i = 0; i < tableNames.length; i++) {
						if (value.equalsIgnoreCase(tableNames[i])) {
							updateTable = tableNames[i];
							break;
						}
					}
				}
				Optional<TextField> optUpdate = Optional.ofNullable(updateTextField);
				Optional<ComboBox> optUpdate1 = Optional.ofNullable(updateForeignBox);
				if (optUpdate.isPresent() || optUpdate1.isPresent()) {
					if (updateTextField1 == null || updateTextField1.getText().isEmpty()) {
						if (updateTextField != null && !updateTextField.getText().isEmpty()) {
							if (isNumeric(updateTextField.getText().trim())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateTextField.getText().trim());
							} else {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateTextField.getText().trim() + "'");
							}
						} else if (updateForeignBox != null) {
							if (isNumeric(updateForeignBox.getValue())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateForeignBox.getValue());
							} else {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateForeignBox.getValue() + "'");
							}
						}
					} else if (updateTextField1 != null || !updateTextField1.getText().isEmpty()) {
						if (updateTextField != null && !updateTextField.getText().isEmpty()) {
							if (isNumeric(updateTextField1.getText().trim())
									&& isNumeric(updateTextField.getText().trim()) == false) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateTextField.getText().trim() + "' WHERE " + updateComboBox1.getValue()
										+ " = " + updateTextField1.getText().trim());
							} else if (isNumeric(updateTextField1.getText().trim()) == false
									&& isNumeric(updateTextField.getText().trim())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateTextField.getText().trim() + " WHERE " + updateComboBox1.getValue()
										+ " = '" + updateTextField1.getText().trim() + "'");
							} else if (isNumeric(updateTextField1.getText().trim())
									&& isNumeric(updateTextField.getText().trim())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateTextField.getText().trim() + " WHERE " + updateComboBox1.getValue()
										+ " = " + updateTextField1.getText().trim());
							} else {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateTextField.getText().trim() + "' WHERE " + updateComboBox1.getValue()
										+ " = '" + updateTextField1.getText().trim() + "'");
							}
						} else {
							if (isNumeric(updateTextField1.getText().trim())
									&& isNumeric(updateForeignBox.getValue()) == false) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateForeignBox.getValue() + "' WHERE " + updateComboBox1.getValue() + " = "
										+ updateTextField1.getText().trim());
							} else if (isNumeric(updateTextField1.getText().trim()) == false
									&& isNumeric(updateForeignBox.getValue())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateForeignBox.getValue() + " WHERE " + updateComboBox1.getValue() + " = '"
										+ updateTextField1.getText().trim() + "'");
							} else if (isNumeric(updateTextField1.getText().trim())
									&& isNumeric(updateForeignBox.getValue())) {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = "
										+ updateForeignBox.getValue() + " WHERE " + updateComboBox1.getValue() + " = "
										+ updateTextField1.getText().trim());
							} else {
								SQL.append("UPDATE " + updateTable + " SET " + updateComboBox.getValue() + " = '"
										+ updateForeignBox.getValue() + "' WHERE " + updateComboBox1.getValue() + " = '"
										+ updateTextField1.getText().trim() + "'");
							}
						}
					}
				}
				try (PreparedStatement preparedStatement = c.prepareStatement(SQL.toString())) {
					int rowsUpdated = preparedStatement.executeUpdate();
					if (rowsUpdated > 0) {
//						updateComboBox.setValue(null);
						Optional<TextField> op1 = Optional.ofNullable(updateTextField);
						if (op1.isPresent()) {
							updateTextField.clear();
						}
						updateComboBox1.setValue(null);
						Optional<TextField> op2 = Optional.ofNullable(updateTextField1);
						if (op2.isPresent()) {
							updateTextField1.clear();
						}
						Tab tab = null;
						for (int i = 0; i < tabPane.getTabs().size(); i++) {
							if (tabPane.getTabs().get(i).getId().equalsIgnoreCase("tablesTab")) {
								tab = tabPane.getTabs().get(i);
								break;
							}
						}
						tab.setContent(Tables());
						updateTableView(value, pane);
						showMessagePopup("UPDATE", "Updated successfully!");
					} else {
						showMessagePopup("UPDATE", "Nothing Updated. The record might not exist.");
					}
				} catch (SQLException e1) {
					if (optUpdate.isPresent() && updateTextField.getText().isEmpty()) {
						showMessagePopup("UPDATE", "Nothing Updated. Please insert a value.");
					} else
						showMessagePopup("UPDATE", "Nothing Updated. The record might not exist.");
				}
			}
		});
		AboutLabel(vbox, hbox);
	}

	public void createFields(String btnValue, TableView<ObservableList<String>> tableView, HBox[] box, VBox box1,
			String Value) {
		if (btnValue.equalsIgnoreCase("SEARCH")) {
			VBox labelVBox = new VBox();
			VBox fieldsVBox = new VBox();
			for (int j = 0; j < tableView.getColumns().size(); j++) {
				textfields[j] = new TextField();
				Label label = createLabel(tableView.getColumns().get(j).getText().toUpperCase());
				labelVBox.getChildren().add(label);
				fieldsVBox.getChildren().add(textfields[j]);
				box[j] = new HBox(labelVBox, fieldsVBox);
				box[j].setAlignment(Pos.CENTER);
				labelVBox.setSpacing(35);
				fieldsVBox.setSpacing(20);
				box[j].setSpacing(20);
				box1.getChildren().add(box[j]);
			}
		} else if (btnValue.equalsIgnoreCase("DELETE")) {
			ObservableList<String> list = FXCollections.observableArrayList();
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equalsIgnoreCase(Value)) {
					for (int j = 0; j < tableViews[i].getColumns().size(); j++) {
						list.add(tableViews[i].getColumns().get(j).getText());
					}
					deleteComboBox.setItems(list);
					break;
				}
			}
			deleteComboBox.setPromptText("Select");
			Label label = createLabel("Select Column ");
			VBox labelBox = new VBox(label);
			labelBox.setSpacing(35);
			VBox fieldBox = new VBox(deleteComboBox);
			fieldBox.setSpacing(20);
			deleteComboBox.setOnAction(e -> {
				Label deleteValueLabel = createLabel("Value: ");
				deleteValueLabel.setId("delete");
				labelBox.getChildren().removeIf(node -> node instanceof Label && node.getId() != null
						&& node.getId().equalsIgnoreCase("delete"));
				labelBox.getChildren().add(deleteValueLabel);
				deleteTextField = new TextField();
				deleteTextField.setPromptText("Value");
				deleteTextField.setId("deleteTextField");
				fieldBox.getChildren()
						.removeIf(node -> node instanceof TextField && node.getId().equals("deleteTextField"));
				fieldBox.getChildren().add(deleteTextField);
			});
			HBox deleteHbox = new HBox(labelBox, fieldBox);
			deleteHbox.setSpacing(20);
			deleteHbox.setAlignment(Pos.CENTER);
			box1.getChildren().add(deleteHbox);
		} else if (btnValue.equalsIgnoreCase("INSERT")) {
			if (Value.equalsIgnoreCase("Car Part")) {
				Value = "car_part";
			}
			try {
				java.sql.DatabaseMetaData metaData = c.getMetaData();
				for (int i = 0; i < tableNames.length; i++) {
					try (ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableNames[i])) {
						while (foreignKeys.next()) {
							columnCount++;
							foreignKeysList.add(foreignKeys.getString("FKCOLUMN_NAME"));
							foreignKeysTableList.add(foreignKeys.getString("PKTABLE_NAME"));
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			VBox labelVBox = new VBox();
			VBox fieldsVBox = new VBox();
			labelVBox.setSpacing(35);
			fieldsVBox.setSpacing(20);
			boolean foreign1 = true;
			boolean foreign2 = true;
			for (int j = 0; j < tableView.getColumns().size(); j++) {
				foreignKeysCombo2 = null;
				TableColumn<?, ?> column = tableView.getColumns().get(j);
				if (foreignKeysList.contains(column.getText().toLowerCase())) {
					int index = foreignKeysList.indexOf(column.getText().toLowerCase());
					String tableName = foreignKeysTableList.get(index);
					String query = buildQueryForTable(tableName);
					try {
						ResultSet foreignSet = c.createStatement().executeQuery(query);
						ResultSetMetaData rsMetaData = foreignSet.getMetaData();
						columnCount = rsMetaData.getColumnCount();
						if (foreign1) {
							foreignKeysCombo.setPromptText("Select " + tableName.toUpperCase());
							foreignKeysCombo.setId("combo1");
							Label label = createLabel(column.getText().toUpperCase());
							label.setId("label1");
							foreignKeysCombo.getItems().clear();
							while (foreignSet.next()) {
								foreignKeysCombo.getItems().add(foreignSet.getString(1));
							}
							labelVBox.getChildren()
									.removeIf(node -> node instanceof Label && "label1".equals(node.getId()));
							labelVBox.getChildren().add(label);
							fieldsVBox.getChildren()
									.removeIf(node -> node instanceof ComboBox && "combo1".equals(node.getId()));
							fieldsVBox.getChildren().add(foreignKeysCombo);
							box[j] = new HBox(labelVBox, fieldsVBox);
							box[j].setAlignment(Pos.CENTER);
							box[j].setSpacing(20);
							box1.getChildren().add(box[j]);
							foreign1 = false;
						} else if (foreign2 && foreignKeysCombo2 == null) {
							foreignKeysCombo2 = new ComboBox<String>();
							foreignKeysCombo2.setId("combo2");
							foreignKeysCombo2.setPromptText("Select " + tableName.toUpperCase());
							while (foreignSet.next()) {
								foreignKeysCombo2.getItems().add(foreignSet.getString(1));
							}
							foreign2 = false;
						}
						if (foreignKeysCombo2 != null) {
							Label label2 = createLabel(column.getText().toUpperCase());
							label2.setId("label2");
							labelVBox.getChildren()
									.removeIf(node -> node instanceof Label && "Label2".equals(node.getId()));
							fieldsVBox.getChildren()
									.removeIf(node -> node instanceof ComboBox && "combo2".equals(node.getId()));
							labelVBox.getChildren().add(label2);
							fieldsVBox.getChildren().add(foreignKeysCombo2);
							box[j] = new HBox(labelVBox, fieldsVBox);
							box[j].setAlignment(Pos.CENTER);
							box[j].setSpacing(20);
							box1.getChildren().add(box[j]);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					textfields[j] = new TextField();
					Label label = createLabel(column.getText().toUpperCase());
					labelVBox.getChildren().add(label);
					fieldsVBox.getChildren().add(textfields[j]);
					box[j] = new HBox(labelVBox, fieldsVBox);
					box[j].setAlignment(Pos.CENTER);
					box[j].setSpacing(20);
					box1.getChildren().add(box[j]);
				}
			}
		} else if (btnValue.equalsIgnoreCase("UPDATE")) {
			ObservableList<String> list = FXCollections.observableArrayList();
			String tableName = Value;
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equalsIgnoreCase(Value)) {
					for (int j = 0; j < tableViews[i].getColumns().size(); j++) {
						list.add(tableViews[i].getColumns().get(j).getText());
					}
					break;
				}
			}
			updateComboBox.setItems(list);
			updateComboBox.setPromptText("Select");
			Label label = createLabel("Select Column ");
			VBox labelBox = new VBox(label);
			labelBox.setSpacing(35);
			VBox fieldBox = new VBox(updateComboBox);
			fieldBox.setSpacing(20);
			updateComboBox.setOnAction(e -> {
				try {
					java.sql.DatabaseMetaData metaData = c.getMetaData();
					for (int i = 0; i < tableNames.length; i++) {
						try (ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableNames[i])) {
							while (foreignKeys.next()) {
								columnCount++;
								foreignKeysList.add(foreignKeys.getString("FKCOLUMN_NAME"));
								foreignKeysTableList.add(foreignKeys.getString("PKTABLE_NAME"));
							}
						}
					}
				} catch (SQLException s) {
					s.printStackTrace();
				}
				Label updateValueLabel = createLabel("New Value ");
				updateValueLabel.setId("updateValueLabel");
				labelBox.getChildren().removeIf(node -> node instanceof Label && node.getId() != null
						&& node.getId().equalsIgnoreCase("updateValueLabel"));
				labelBox.getChildren().add(updateValueLabel);
				for (int j = 0; j < tableView.getColumns().size(); j++) {
					TableColumn<?, ?> column = tableView.getColumns().get(j);
					if (column.getText().equalsIgnoreCase(updateComboBox.getValue())) {
						if (foreignKeysList.contains(column.getText().toLowerCase())) {
							updateForeignBox = new ComboBox();
							int index = foreignKeysList.indexOf(column.getText().toLowerCase());
							String tableName1 = foreignKeysTableList.get(index);
							String query = buildQueryForTable(tableName1);
							try {
								ResultSet foreignSet = c.createStatement().executeQuery(query);
								ResultSetMetaData rsMetaData = foreignSet.getMetaData();
								columnCount = rsMetaData.getColumnCount();
								updateForeignBox.setPromptText("Select " + tableName.toUpperCase());
								updateForeignBox.getItems().clear();
								while (foreignSet.next()) {
									updateForeignBox.getItems().add(foreignSet.getString(1));
								}
							} catch (SQLException p) {

							}
							updateForeignBox.setPromptText("Select");
							updateForeignBox.setId("updateForeignBox");
							fieldBox.getChildren().removeIf(node -> node instanceof TextField && node.getId() != null
									&& node.getId().equalsIgnoreCase("updateTextField"));
							fieldBox.getChildren().removeIf(node -> node instanceof ComboBox && node.getId() != null
									&& node.getId().equalsIgnoreCase("updateForeignBox"));
							fieldBox.getChildren().add(updateForeignBox);
							break;
						} else {
							updateTextField = new TextField();
							updateTextField.setPromptText("Value");
							updateTextField.setId("updateTextField");
							fieldBox.getChildren().removeIf(node -> node instanceof ComboBox && node.getId() != null
									&& node.getId().equalsIgnoreCase("updateForeignBox"));
							fieldBox.getChildren().removeIf(node -> node instanceof TextField && node.getId() != null
									&& node.getId().equalsIgnoreCase("updateTextField"));
							fieldBox.getChildren().add(updateTextField);
						}
					}
				}

				ObservableList<String> list1 = FXCollections.observableArrayList();
				if (updateComboBox.getValue() != null) {
					for (int i = 0; i < tableNames.length; i++) {
						if (tableNames[i] != null && tableNames[i].equalsIgnoreCase(tableName)) {
							if (tableViews[i] != null) {
								for (TableColumn<?, ?> column : tableViews[i].getColumns()) {
									if (column.getText() != null
											&& !updateComboBox.getValue().equalsIgnoreCase(column.getText())) {
										list1.add(column.getText());
									}
								}
								break;
							}
						}
					}
				}
				updateComboBox1.setItems(list1);
				updateComboBox1.setPromptText("Select");
				Label label1 = createLabel("Select Column to Update With ");
				label1.setId("label1");
				labelBox.getChildren().removeIf(node -> node instanceof Label && node.getId() != null
						&& node.getId().equalsIgnoreCase("label1"));
				labelBox.getChildren().add(label1);
				updateComboBox1.setId("updateComboBox1");
				fieldBox.getChildren().removeIf(node -> node instanceof ComboBox && node.getId() != null
						&& node.getId().equalsIgnoreCase("updateComboBox1"));
				fieldBox.getChildren().add(updateComboBox1);
				updateComboBox1.setOnAction(s -> {
					Label updateValueLabel1 = createLabel("Value ");
					updateValueLabel1.setId("updateValueLabel1");
					labelBox.getChildren().removeIf(node -> node instanceof Label && node.getId() != null
							&& node.getId().equalsIgnoreCase("updateValueLabel1"));
					labelBox.getChildren().add(updateValueLabel1);
					updateTextField1 = new TextField();
					updateTextField1.setPromptText("Value");
					updateTextField1.setId("updateTextField1");
					fieldBox.getChildren().removeIf(node -> node instanceof TextField && node.getId() != null
							&& node.getId().equalsIgnoreCase("updateTextField1"));
					fieldBox.getChildren().add(updateTextField1);
				});
			});
			HBox updateHbox = new HBox(labelBox, fieldBox);
			updateHbox.setAlignment(Pos.CENTER);
			updateHbox.setSpacing(20);
			box1.getChildren().add(updateHbox);
			foreignKeysList.clear();
			foreignKeysTableList.clear();
		}
		setFieldValue();
	}

	public void updateTableView(String value, StackPane pane) throws SQLException {
		ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + value);
		TableView<ObservableList<String>> tableViewInner = new TableView<>();
		ObservableList<ObservableList<String>> Innerdata = FXCollections.observableArrayList();
		pane.getChildren().removeIf(node -> node instanceof TableView && "tableViewInner".equals(node.getId()));
		createTableView(rs, tableViewInner, Innerdata);
		pane.getChildren().add(tableViewInner);
	}

	public Label makeLabel(String value) {
		Label label = new Label(value);
		label.setAlignment(Pos.CENTER);
		label.setStyle(
				"-fx-font-family: 'Times New Roman', sans-serif; " + "-fx-font-size: 72px; " + "-fx-font-weight: bold; "
						+ "-fx-text-fill: #333; " + "-fx-padding: 10px; " + "-fx-background-color: #FFFFFF; "
						+ "-fx-border-color: #000000; " + "-fx-border-width: 1px; " + "-fx-border-radius: 5px;");
		label.setMaxWidth(Double.MAX_VALUE);
		return label;
	}

	public boolean isForeignKeyField(String attributeName) {
		return foreignKeysList.contains(attributeName);
	}

	private String buildQueryForTable(String tableName) {
		switch (tableName.toUpperCase()) {
		case "ADDRESS":
			return "SELECT id FROM ADDRESS";
		case "CAR":
			return "SELECT name FROM CAR";
		case "CUSTOMER":
			return "SELECT id FROM CUSTOMER";
		case "DEVICE":
			return "SELECT no FROM DEVICE";
		case "MANUFACTURE":
			return "SELECT name FROM MANUFACTURE";
		case "ORDERS":
			return "SELECT id FROM ORDERS";
		default:
			return "";
		}
	}

	private VBox Tables() throws SQLException {
		for (int i = 0; i < tableNames.length; i++) {
			tableViews[i] = new TableView<>();
			data[i] = FXCollections.observableArrayList();
			String SQL = "SELECT * FROM " + tableNames[i];
			ResultSet rs = c.createStatement().executeQuery(SQL);
			createTableView(rs, tableViews[i], data[i]);
		}
		HBox tables1 = new HBox();
		HBox tables2 = new HBox();
		VBox tables = new VBox();
		tables.setSpacing(20);
		tables1.setSpacing(20);
		tables2.setSpacing(20);
		VBox vbox[] = new VBox[7];
		for (int i = 0; i < tableNames.length; i++) {
			Label label = new Label(tableNames[i].toUpperCase());
			if (tableNames[i].equalsIgnoreCase("car_part")) {
				label.setText("CAR PART");
			}
			label.setStyle("-fx-font-family: 'Times New Roman', sans-serif; " + "-fx-font-weight: bold;"
					+ "-fx-font-size: 20px; " + "-fx-text-fill: #333; " + "-fx-padding: 8px; "
					+ "-fx-background-color: #e7e7e7; " + "-fx-border-color: #c3c3c3; " + "-fx-border-width: 1; "
					+ "-fx-border-radius: 5; " + "-fx-background-radius: 5;");
			vbox[i] = new VBox(label, tableViews[i]);
			label.setAlignment(Pos.CENTER);
			label.setMaxWidth(Double.MAX_VALUE);
			vbox[i].setAlignment(Pos.CENTER);
			vbox[i].setSpacing(20);
		}
		tables1.getChildren().addAll(vbox[0], vbox[1], vbox[2], vbox[3]);
		tables2.getChildren().addAll(vbox[4], vbox[5], vbox[6]);
		tables1.setAlignment(Pos.CENTER);
		tables2.setAlignment(Pos.CENTER);
		tables.setPadding(new Insets(20, 0, 0, 0)); // Top, Right, Bottom, Left padding
		tables.getChildren().addAll(tables1, tables2);
		HBox hbox = new HBox();
		AboutLabel(tables, hbox);
		return tables;
	}

	private void setFieldValue() {
		for (int i = 0; i < textfields.length; i++) {
			Optional<TextField> opt = Optional.ofNullable(textfields[i]);
			if (opt.isPresent()) {
				textfields[i].setPromptText("Value");
			}

		}
	}

	public void AboutLabel(VBox vbox, HBox hbox) {
		Label aboutLabel = new Label("© Developed by Yousef Albandak - December 2023.");
		aboutLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size:20;");
		aboutLabel.setAlignment(Pos.BOTTOM_RIGHT);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox hbox23 = new HBox(spacer, aboutLabel);
		vbox.getChildren().addAll(new Region(), hbox23);
		VBox.setVgrow(hbox, Priority.ALWAYS);
	}

	private void showMessagePopup(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	private static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Label createLabel(String value) {
		Label label = new Label(value + ":");
		label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size:20;");
		return label;
	}

	public void createTableView(ResultSet rs, TableView<ObservableList<String>> tableView,
			ObservableList<ObservableList<String>> data) {
		try {
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int columnIndex = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(
						rs.getMetaData().getColumnName(i + 1));
				column.setCellValueFactory(param -> {
					if (param.getValue().size() > columnIndex && param.getValue().get(columnIndex) != null) {
						return new SimpleStringProperty(param.getValue().get(columnIndex));
					} else {
						return new SimpleStringProperty("");
					}
				});
				tableView.getColumns().add(column);
				column.setMinWidth(100);
			}
			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			tableView.setItems(data);
			for (TableColumn<?, ?> column : tableView.getColumns()) {
				column.setText(column.getText().toUpperCase());
			}
			tableView.setPrefWidth(450);
			tableView.setMaxWidth(450);
			tableView.setMinHeight(200);
			tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}
}