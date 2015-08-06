package com.v3pm_prototype.view.controller;

import java.io.IOException;
import java.io.WriteAbortedException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import org.controlsfx.control.Notifications;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.springbox.SpringBox;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.Viewer.ThreadingModel;

import com.v3pm_prototype.analysis.CompleteRobustnessAnalysis;
import com.v3pm_prototype.calculation.Calculator;
import com.v3pm_prototype.calculation.ConstraintSet;
import com.v3pm_prototype.calculation.Process;
import com.v3pm_prototype.calculation.Project;
import com.v3pm_prototype.database.DBConnection;
import com.v3pm_prototype.database.DBConstraint;
import com.v3pm_prototype.database.DBProcess;
import com.v3pm_prototype.database.DBScenario;
import com.v3pm_prototype.main.MainApp;
import com.v3pm_prototype.rmgeneration.RMGenerator;
import com.v3pm_prototype.rmgeneration.RoadMap;
import com.v3pm_prototype.rmgeneration.RunConfiguration;
import com.v3pm_prototype.tools.Colorpalette;
import com.v3pm_prototype.tools.TableViewSnapshot;

public class TabScenarioCalculationController {
	@FXML
	private Label lblNPV;
	
	@FXML
	private Label lblRobustness;
	@FXML
	private Label lblRobustnessText;
	@FXML
	private ProgressIndicator piRobustness;

	@FXML
	private Label lblAmountRoadmaps;
	
	@FXML
	private TableView<RoadMap> tvRoadmap;
	private ObservableList<RoadMap> olRoadmap = FXCollections
			.observableArrayList();
	private ChangeListener rmListChangeListener;
	private List<RoadMap> rmList;

	@FXML
	VBox boxRMSolution;
	
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
	private LineChart<String, Number> lcProcessOOP;
	@FXML
	private LineChart<String, Number> lcProcessFC;
	@FXML
	private BarChart<String, Number> bcRBroken;

	@FXML
	private ListView lvRestrictions;
	
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
	
	private javafx.scene.Node snapshotNode;

	public TabScenarioCalculationController() {

	}

	@FXML
	public void initialize() {
		chartBugfix();
		initRobustnessAnalysis();
		initTVRoadmaps();
		initTVProcesses();
		bootGraphStream();
		setupSnapshots();
	}
			
	/**
	 * Adds a snapshot feature to the charts
	 */
	private void setupSnapshots() {
		final ContextMenu snapshotCM = new ContextMenu();
		MenuItem miSnapshot = new MenuItem("Copy to Clipboard");
		snapshotCM.getItems().add(miSnapshot);

		miSnapshot.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WritableImage snapshot = new WritableImage((int)snapshotNode.getBoundsInLocal().getWidth(), (int)snapshotNode.getBoundsInLocal().getHeight());
				snapshotNode.snapshot(new SnapshotParameters(), snapshot);
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putImage(snapshot); 
				clipboard.clear();
				clipboard.setContent(content);
				
				Notifications.create()
	              .title("Snapshot Taken")
	              .text("A snapshot of the component has been taken and is available in your clipboard.")
	              .showInformation();
				
			}
		});
		
		EventHandler<MouseEvent> eventHandlerSnapshot = (new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				 if (MouseButton.SECONDARY.equals(event.getButton())) {
					 snapshotNode = (javafx.scene.Node) event.getSource();
				      snapshotCM.show(mainApp.getPrimaryStage(), event.getScreenX(), event.getScreenY());
				    }  
			}
		});
		
		lcProcessQuality.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		lcProcessTime.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		lcProcessFC.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		lcProcessOOP.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		bcRBroken.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		swingNode.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		boxRMSolution.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSnapshot);
		
		MenuItem item = new MenuItem("Copy to Clipboard");
		item.setOnAction(new TableViewSnapshot(tvProcesses));
		ContextMenu menu = new ContextMenu(item);
		tvProcesses.setContextMenu(menu);
		
	}

	private void chartBugfix() {
		// Add blank data and clear afterwards for bug workaraound
		Series<String, Number> series1 = new XYChart.Series<String, Number>();
	
		for (int i = 0; i < 8; i++) {
			series1.getData().add(new XYChart.Data<String, Number>("", 0));
		}
		
		bcRBroken.getData().add(series1);
	}
	
	public void openSAProjectSuccessTab() {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/SAProjectSuccess.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			SAProjectSuccessController saPSController = loader
					.getController();
			saPSController.setMainApp(this.mainApp);
			saPSController.setTsc(this);
			saPSController.setRoadmap(tvRoadmap.getSelectionModel().getSelectedItem());
			
			Tab tabSAPS = new Tab(mainApp.getV3pmGUIController().getTpMain().getSelectionModel().getSelectedItem().getText()+" Project Success Analysis");
			tabSAPS.setContent(root);
			tabSAPS.setClosable(true);
			mainApp.getV3pmGUIController().getTpMain().getTabs().add(tabSAPS);
			mainApp.getV3pmGUIController().getTpMain().getSelectionModel()
			.select(tabSAPS);
			
			mainApp.getV3pmGUIController().getTpMain()
					.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void openRobustnessAnalysisTab() {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/RobustnessAnalysis.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			RobustnessAnalysisController raController = loader
					.getController();
			raController.setMainApp(this.mainApp);
			raController.setTsc(this);

			Tab tabRA = new Tab(mainApp.getV3pmGUIController().getTpMain().getSelectionModel().getSelectedItem().getText()+" Robustness Analysis");
			tabRA.setContent(root);
			tabRA.setClosable(true);
			mainApp.getV3pmGUIController().getTpMain().getTabs().add(tabRA);
			mainApp.getV3pmGUIController().getTpMain().getSelectionModel()
			.select(tabRA);
			
			mainApp.getV3pmGUIController().getTpMain()
					.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
			
			raController.setScenario(scenario);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void openRoadmapComparisonTab() {
		if( tvRoadmap.getSelectionModel().getSelectedItems().size() == 2){
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("/com/v3pm_prototype/view/RoadmapComparison.fxml"));
			VBox root;
			try {
				root = (VBox) loader.load();
				RoadmapComparisonController rcController = loader
						.getController();
				rcController.setMainApp(this.mainApp);
				rcController.setRoadmaps(config, tvRoadmap.getSelectionModel().getSelectedItems().get(0), tvRoadmap.getSelectionModel().getSelectedItems().get(1));
				
				Tab tabSAPS = new Tab("Roadmap Comparison");
				tabSAPS.setContent(root);
				tabSAPS.setClosable(true);
				mainApp.getV3pmGUIController().getTpMain().getTabs().add(tabSAPS);
				mainApp.getV3pmGUIController().getTpMain().getSelectionModel()
				.select(tabSAPS);
				
				mainApp.getV3pmGUIController().getTpMain()
						.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startCompleteRobustnessAnalysis(){
		CompleteRobustnessAnalysis cra = new CompleteRobustnessAnalysis(config, rmList){

			@Override
			protected void succeeded() {
				super.succeeded();
				initRoadmapContainer(this);
				DecimalFormat df = new DecimalFormat("#.#%");
				lblRobustness.setText(df.format(this.getPercentage()));
				lblRobustnessText.setText("All set parameters have been tested in a 2% radius. "
						+ "Projects scored a robustness of "+df.format(this.getpProjectsAll())+". Discount rate scored a robustness of "+df.format(this.getpGeneral())+".");
				System.out.println(df.format(this.getPercentage()));
				lblRobustness.setVisible(true);
				lblRobustness.setManaged(true);
				piRobustness.setVisible(false);
				piRobustness.setManaged(false);
			}
			
		};

		Thread t = new Thread(cra);
		t.setDaemon(false);
		t.start();
	}
	
	private void initRobustnessAnalysis(){
		lblRobustness.setVisible(false);
		lblRobustness.setManaged(false);
	}
	
	private void initRoadmapContainer(CompleteRobustnessAnalysis cra) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("/com/v3pm_prototype/view/RoadmapBox.fxml"));
		VBox root;
		try {
			root = (VBox) loader.load();
			RoadmapBoxController rmbController = loader.getController();
			rmbController.generate(this.rmList.get(0), config, cra);
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
		swingNode.setContent(view);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
		
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
	
	private void initLVRestrictions(){
		ObservableList olRestrictions = FXCollections.observableArrayList();
		olRestrictions.addAll(config.getConstraintSet().getLstConstraints());
		lvRestrictions.setItems(olRestrictions);
	}

	private void initTVRoadmaps() {
		
		tvRoadmap.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
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
		
		// -------------------- DOUBLECLICK: START SAProjectSuccess
		// -------------------

		tvRoadmap.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					openSAProjectSuccessTab();
				}
			}
		});

	}

	private void initLineCharts() {
		lcProcessQuality.getData().clear();
		lcProcessTime.getData().clear();
		lcProcessOOP.getData().clear();
		lcProcessFC.getData().clear();

		// Add Data to the charts for each process
		if (tvRoadmap.getSelectionModel().getSelectedItem() != null) {
			if(tvRoadmap.getSelectionModel().getSelectedItem()
					.getLstProcessCalculated() != null){
				
				for (Process p : tvRoadmap.getSelectionModel().getSelectedItem()
						.getLstProcessCalculated()) {

					// Create a series for each process and factor (q/t/OOP/FC)
					Series<String, Number> seriesQ = new XYChart.Series<String, Number>();
					seriesQ.setName(p.getName());

					Series<String, Number> seriesT = new XYChart.Series<String, Number>();
					seriesT.setName(p.getName());
					
					Series<String, Number> seriesOOP = new XYChart.Series<String, Number>();
					seriesT.setName(p.getName());
					
					Series<String, Number> seriesFC = new XYChart.Series<String, Number>();
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
						
						seriesOOP.getData().add(
								new XYChart.Data<String, Number>("Period "
										+ String.valueOf(period), p
										.getOopPerPeriod(config)[period]));
						
						seriesFC.getData().add(
								new XYChart.Data<String, Number>("Period "
										+ String.valueOf(period), p
										.getFixedCostsPerPeriod(config)[period]));
					}

					lcProcessQuality.getData().add(seriesQ);
					lcProcessTime.getData().add(seriesT);
					lcProcessOOP.getData().add(seriesOOP);
					lcProcessFC.getData().add(seriesFC);
				}
				
			}
			

		}

	}
	
	private void initBarChartRBroken(){
		bcRBroken.getData().clear();
		
		//Add data to the chart for each restriction type
		ConstraintSet cs = config.getConstraintSet();
		List<String> lstTypeAlreadyCounted = new ArrayList<String>();
		
		Series<String, Number> series = new XYChart.Series<String, Number>();
		
		for(DBConstraint dbConstraint : config.getConstraintSet().getLstConstraints()){
			int countBrokenAll = 0;
			
			// Get all constraints of the same type
			if(!lstTypeAlreadyCounted.contains(dbConstraint.getType())){
				for(DBConstraint dbc : config.getConstraintSet().getLstConstraints()){
					if(dbConstraint.getType().equals(dbc.getType())){
						countBrokenAll = countBrokenAll + dbc.getCountBroken();
					}
				}
			
				// Dont count constraints of this type again
				lstTypeAlreadyCounted.add(dbConstraint.getType());
				System.out.println(dbConstraint.getType() + " countBroken: "+countBrokenAll);
				// Add a Bar of the type to the chart
				series.getData().add(new XYChart.Data<String, Number>(dbConstraint.getType(),countBrokenAll));
			}	
		}
		
		bcRBroken.getData().add(series);
		
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
						lblAmountRoadmaps.setText(rmList.size() +" Roadmaps have been generated.");
						System.out.println(config);
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
						initRoadmapContainer(null);
						updateTVProcesses();
						startCompleteRobustnessAnalysis();
						initBarChartRBroken();
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
		initLVRestrictions();
		startInitialCalculations();
	}

	/**
	 * Make sure that the GraphStream Thread is closed on exit
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		//TODO Cycle through tabs and closs all viewers
		this.mainApp = mainApp;
		this.mainApp.getPrimaryStage().setOnCloseRequest(
				new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						if(viewer != null){
							viewer.close();
						}
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

	public List<RoadMap> getRmList() {
		return rmList;
	}

	public RunConfiguration getConfig() {
		return config;
	}
	
	

}
