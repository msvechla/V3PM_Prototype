package com.v3pm_prototype.view.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.springbox.SpringBox;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.Viewer.ThreadingModel;

import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.calculation.RobustnessAnalysis;
import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;

public class TabScenarioCalculationController {
	@FXML
	private Label lblNPV;

	@FXML
	private TableView<RoadMap> tvRoadmap;
	private ObservableList<RoadMap> olRoadmap = FXCollections
			.observableArrayList();
	private ChangeListener rmListChangeListener;
	private List<RoadMap> rmList;

	@FXML
	private VBox roadmapContainer;

	@FXML
	private TableColumn<RoadMap, String> clmRoadmap;
	@FXML
	private TableColumn<RoadMap, Double> clmNPV;
	@FXML
	private ChoiceBox<Project> cbProject;
	private ObservableList<Project> olProjects = FXCollections
			.observableArrayList();

	// TVProcesses
	@FXML
	private TableView<Process> tvProcesses;
	private ObservableList<Process> olProcesses = FXCollections
			.observableArrayList();
	@FXML
	private TableColumn<Process, String> clmProcessesName;
	@FXML
	private TableColumn<Process, Double> clmProcessesQ;
	@FXML
	private TableColumn<Process, Double> clmProcessesQDelta;
	@FXML
	private TableColumn<Process, Double> clmProcessesT;
	@FXML
	private TableColumn<Process, Double> clmProcessesTDelta;
	@FXML
	private TableColumn<Process, Double> clmProcessesOOp;
	@FXML
	private TableColumn<Process, Double> clmProcessesOOpDelta;
	@FXML
	private TableColumn<Process, Double> clmProcessesFC;
	@FXML
	private TableColumn<Process, Double> clmProcessesFCDelta;

	// Charts
	@FXML
	private LineChart<String, Number> lcProcessQuality;
	@FXML
	private LineChart<String, Number> lcProcessTime;

	@FXML
	private SwingNode swingNode;
	private Graph graph;
	private Viewer viewer;
	private View view;
	SpringBox graphstreamLayout;
	private HashSet<Project> oldProjectList = new HashSet<Project>();

	private MainApp mainApp;
	private DBScenario scenario;
	private RunConfiguration config;
	private Tab tab;

	public TabScenarioCalculationController() {

	}

	@FXML
	public void initialize() {
		lcProcessQuality.setPrefWidth(lcProcessTime.getWidth() / 2);
		initTVRoadmaps();
		initTVProcesses();
		bootGraphStream();
	}

	private void initRoadmapContainer() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/RoadmapBox.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			RoadmapBoxController rmbController = loader.getController();
			rmbController.generate(this.rmList.get(0), config);
			roadmapContainer.getChildren().clear();
			roadmapContainer.getChildren().add(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initTVProcesses() {
		tvProcesses.setItems(olProcesses);
		clmProcessesName
				.setCellValueFactory(new PropertyValueFactory<Process, String>(
						"Name"));
		clmProcessesOOp
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"Oop"));
		clmProcessesOOpDelta
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"oopDelta"));
		clmProcessesFC
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"fixedCosts"));
		clmProcessesFCDelta
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"fixedCostsDelta"));
		clmProcessesQ
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"q"));
		clmProcessesQDelta
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"qDelta"));
		clmProcessesT
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"t"));
		clmProcessesTDelta
				.setCellValueFactory(new PropertyValueFactory<Process, Double>(
						"tDelta"));

		// Setup Colors for Process names
		clmProcessesName
				.setCellFactory(new Callback<TableColumn<Process, String>, TableCell<Process, String>>() {

					@Override
					public TableCell<Process, String> call(
							TableColumn<Process, String> param) {
						return new TableCell<Process, String>() {

							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);

								for (Process p : tvRoadmap.getSelectionModel()
										.getSelectedItem()
										.getLstProcessCalculated()) {
									if (p.getName().equals(item)) {
										if (config.getColorID(p) < Colorpalette.PROCESS.length) {
											this.setTextFill(Color
													.valueOf(Colorpalette.PROCESS[config
															.getColorID(p)]));
										}
									}
								}
								setText(item);
							}

						};
					}
				});

		Callback<TableColumn<Process, Double>, TableCell<Process, Double>> cellFactoryDelta = new Callback<TableColumn<Process, Double>, TableCell<Process, Double>>() {

			@Override
			public TableCell<Process, Double> call(
					TableColumn<Process, Double> param) {
				return new TableCell<Process, Double>() {

					@Override
					protected void updateItem(Double item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							if (this.getTableColumn()
									.equals(clmProcessesQDelta)) {

								if (item > 0) {
									this.setTextFill(Color
											.valueOf(Colorpalette.DELTA_GREEN));
									setText("+"
											+ String.valueOf(Math
													.round(item * 100) / 100.0));
								}

								if (item < 0) {
									this.setTextFill(Color
											.valueOf(Colorpalette.DELTA_RED));
									setText(String.valueOf(Math
											.round(item * 100) / 100.0));
								}
							}

							if (this.getTableColumn()
									.equals(clmProcessesTDelta)
									|| this.getTableColumn().equals(
											clmProcessesOOpDelta)
									|| this.getTableColumn().equals(
											clmProcessesFCDelta)) {

								if (item > 0) {
									this.setTextFill(Color
											.valueOf(Colorpalette.DELTA_RED));
									setText("+"
											+ String.valueOf(Math
													.round(item * 100) / 100.0));
								}

								if (item < 0) {
									this.setTextFill(Color
											.valueOf(Colorpalette.DELTA_GREEN));
									setText(String.valueOf(Math
											.round(item * 100) / 100.0));
								}

								if (item == 0) {
									this.setTextFill(Color.BLACK);
									setText(String.valueOf(item));
								}
							}
						}

					}

				};
			}
		};

		clmProcessesQDelta.setCellFactory(cellFactoryDelta);
		clmProcessesTDelta.setCellFactory(cellFactoryDelta);
		clmProcessesOOpDelta.setCellFactory(cellFactoryDelta);
		clmProcessesFCDelta.setCellFactory(cellFactoryDelta);

	}

	private void updateTVProcesses() {
		if (tvRoadmap.getSelectionModel().getSelectedItem() != null) {
			olProcesses.clear();
			olProcesses.addAll(tvRoadmap.getSelectionModel().getSelectedItem()
					.getLstProcessCalculated());
		}

	}

	private void bootGraphStream() {
		System.setProperty("gs.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph = new SingleGraph("Scenario");
		graph.addAttribute("ui.stylesheet", Colorpalette.graphStreamCSS);
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");

		viewer = new Viewer(graph, ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

		graphstreamLayout = new SpringBox(false);
		viewer.enableAutoLayout(graphstreamLayout);
		view = viewer.addDefaultView(false);
		viewer.enableAutoLayout();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
		swingNode.setContent(view);
	}

	private void initGraphStream() {
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {

				RoadMap roadmap = tvRoadmap.getSelectionModel()
						.getSelectedItem();
				HashSet<Project> proejctsInScenario = new HashSet<Project>();

				for (int id : roadmap.implementedProjectIDs) {
					for (Project p : config.getLstProjects()) {
						if (p.getId() == id) {
							proejctsInScenario.add(p);
							break;
						}
					}
				}

				if (!oldProjectList.equals(proejctsInScenario)) {

					// Clear graph contents
					while (graph.getEdgeIterator().hasNext()) {
						Edge e = graph.getEdgeIterator().next();
						e.clearAttributes();
						graph.removeEdge(e);
					}

					while (graph.getNodeIterator().hasNext()) {
						Node n = graph.getNodeIterator().next();
						n.clearAttributes();
						graph.removeNode(n);
					}

					// Add all nodes
					for (Process process : config.getLstProcesses()) {
						Node n = graph.addNode(process.getNodeID());
						n.addAttribute("ui.label", process.getName());
						n.addAttribute(
								"ui.class",
								"pc"
										+ String.valueOf(config
												.getColorID(process)));
						// Thread.sleep(300);
					}

					for (Project project : proejctsInScenario) {
						// Thread.sleep(600);
						Node n = graph.addNode(project.getNodeID());
						n.addAttribute("ui.label", project.getName());
						n.addAttribute("ui.class",
								"pj" + config.getColorID(project));

						if (project.getI() == DBProcess.ID_ALLPROCESSES) {
							n.addAttribute("layout.weight", "0.1");
							for (Process p : config.getLstProcesses()) {
								Edge e = graph.addEdge(
										project.getNodeID() + p.getNodeID(),
										project.getNodeID(), p.getNodeID());
							}
						} else {

							// get the affected Process
							for (Process p : config.getLstProcesses()) {
								if (p.getId() == project.getI()) {
									Edge e = graph.addEdge(project.getNodeID()
											+ p.getNodeID(),
											project.getNodeID(), p.getNodeID());
									break;
								}
							}
						}

					}

					oldProjectList.clear();
					oldProjectList.addAll(proejctsInScenario);
				} else {
					graphstreamLayout.shake();
				}
				return 0;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

	}

	/**
	 * Generates the initial Roadmaps and calculates their NPVs
	 */
	private void startInitialCalculations() {
		Service<List<RoadMap>> svRMGen = initialRMGenService();

		svRMGen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Service<List<RoadMap>> svNPVCalc = initialNPVCalcService((List<RoadMap>) event
						.getSource().getValue());
				svNPVCalc.start();
			}
		});

		svRMGen.start();
		initCBProjects();

	}

	private void initCBProjects() {
		// Add all Projects to the Project filter ComboBox
		cbProject.setItems(olProjects);
		olProjects.add(new Project(-1, "ALL", 0, "", 0, 0, 0, 0, 0, 0, 0, 0,
				"", "", ""));
		olProjects.addAll(config.getLstProjects());
		cbProject.setValue(olProjects.get(0));

		// Listen for selection changes and update the table / charts
		cbProject.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Project>() {
					@Override
					public void changed(
							ObservableValue<? extends Project> observable,
							Project oldValue, Project newValue) {
						olRoadmap.clear();
						if (newValue.toString().equals("ALL")) {
							olRoadmap.addAll(rmList);
						} else {
							for (RoadMap rm : rmList) {
								if (rm.implementedProjectIDs.contains(newValue
										.getId())) {
									olRoadmap.add(rm);
								}
							}
						}
						tvRoadmap.getSelectionModel().select(olRoadmap.get(0));
						initLineCharts();
					}
				});
	}

	private void initTVRoadmaps() {
		// Set the Data for the Roadmap Table
		this.tvRoadmap.setItems(olRoadmap);
		clmRoadmap
				.setCellValueFactory(new PropertyValueFactory<RoadMap, String>(
						"displayText"));
		clmNPV.setCellValueFactory(new PropertyValueFactory<RoadMap, Double>(
				"NPVString"));

		// Table sorting
		clmNPV.setSortType(SortType.DESCENDING);
		tvRoadmap.getSortOrder().add(clmNPV);
		tvRoadmap.sort();

		// Setup the change listener. Listener is set after RMCalculation
		rmListChangeListener = new ChangeListener<RoadMap>() {

			@Override
			public void changed(ObservableValue<? extends RoadMap> observable,
					RoadMap oldValue, RoadMap newValue) {
				// listen for selection changes and update the charts
				initLineCharts();
				initGraphStream();
				updateTVProcesses();
			}

		};
	}

	private void initLineCharts() {
		lcProcessQuality.getData().clear();
		lcProcessTime.getData().clear();

		// Add Data to the charts for each process
		if (tvRoadmap.getSelectionModel().getSelectedItem() != null) {
			for (Process p : tvRoadmap.getSelectionModel().getSelectedItem()
					.getLstProcessCalculated()) {

				// Create a series for each process and factor (q/t)
				Series<String, Number> seriesQ = new XYChart.Series<String, Number>();
				seriesQ.setName(p.getName());

				Series<String, Number> seriesT = new XYChart.Series<String, Number>();
				seriesT.setName(p.getName());

				// Add data to each series for each period
				for (int period = 0; period < config.getPeriods(); period++) {
					seriesQ.getData().add(
							new XYChart.Data<String, Number>("Period "
									+ String.valueOf(period), p
									.getqPerPeriod(config)[period]));

					seriesT.getData().add(
							new XYChart.Data<String, Number>("Period "
									+ String.valueOf(period), p
									.gettPerPeriod(config)[period]));
				}

				lcProcessQuality.getData().add(seriesQ);
				lcProcessTime.getData().add(seriesT);
			}

		}

	}

	/**
	 * Generates the initial Roadmaps and updates the UI
	 * 
	 * @return
	 */
	private Service<List<RoadMap>> initialRMGenService() {
		// Update Statusbar
		this.mainApp.getV3pmGUIController().setProgress(-1);
		this.mainApp.getV3pmGUIController().setStatus("Generating Roadmaps...");

		// Create a Service that executes the RMGenerator Task and start it
		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
			@Override
			protected Task<List<RoadMap>> createTask() {
				return new RMGenerator(config) {

					@Override
					protected void succeeded() {
						mainApp.getV3pmGUIController().setProgress(0);
						mainApp.getV3pmGUIController().setStatus(
								"Roadmaps generated.");
						rmList = (List<RoadMap>) getValue();
						olRoadmap.addAll(rmList);
						super.succeeded();
					}

				};
			}
		};
		return service;
	}

	/**
	 * Calculates the NPV of the initial Roadmaps and updates the UI
	 * 
	 * @param r
	 * @return
	 */
	private Service<List<RoadMap>> initialNPVCalcService(
			final List<RoadMap> generatedRoadmaps) { // Update Statusbar
		this.mainApp.getV3pmGUIController().setProgress(-1);
		this.mainApp.getV3pmGUIController().setStatus("Calculating NPVs...");

		// Create a Service that executes the RMGenerator Task and start it
		Service<List<RoadMap>> service = new Service<List<RoadMap>>() {
			@Override
			protected Task<List<RoadMap>> createTask() {

				return new Task<List<RoadMap>>() {

					@Override
					protected List<RoadMap> call() throws Exception {
						Calculator c = new Calculator(generatedRoadmaps, config);
						return c.start();
					}

					@Override
					protected void succeeded() {
						super.succeeded();
						mainApp.getV3pmGUIController().setProgress(0);
						mainApp.getV3pmGUIController().setStatus(
								"NPVs calculated.");
						// Display the NPV
						scenario.setNpv(this.getValue().get(0).getNpv());
						lblNPV.setText(scenario.getNPVString());

						try {
							writeDBNPV();
						} catch (SQLException e) {
							System.err
									.println("[SQL ERROR] writing NPV to Database");
						}

						// Add items to the Roadmap table, select first item and
						// update charts
						olRoadmap.clear();
						rmList = getValue();
						olRoadmap.addAll(rmList);
						tvRoadmap.getSelectionModel().select(rmList.get(0));
						tvRoadmap.getSelectionModel().selectedItemProperty()
								.addListener(rmListChangeListener);
						initLineCharts();
						initGraphStream();
						initRoadmapContainer();
						updateTVProcesses();

						RobustnessAnalysis ra = new RobustnessAnalysis(rmList,
								config, RobustnessAnalysis.MODE_PLUS,config.getProject(9),"a");

						Thread t = new Thread(ra);
						t.setDaemon(false);
						t.start();
					}

				};

			}
		};
		return service;
	}

	private synchronized void writeDBNPV() throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE Scenario SET npv = " + scenario.getNpv()
				+ " WHERE ID = " + this.scenario.getId());
	}

	public void setScenario(DBScenario newScenario) {
		this.scenario = newScenario;
		this.config = newScenario.generateRunConfiguration();
		startInitialCalculations();

	}

	/**
	 * Make sure that the GraphStream Thread is closed on exit
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.mainApp.getPrimaryStage().setOnCloseRequest(
				new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						viewer.close();
					}
				});
	}

	/**
	 * Make sure that the GraphStream Thread is closed on exit
	 * 
	 * @param Tab
	 */
	public void setTab(Tab tab) {
		this.tab = tab;
		tab.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				viewer.close();
				mainApp.getPrimaryStage().setOnCloseRequest(null);
			}
		});
	}

}
