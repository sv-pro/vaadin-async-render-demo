package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.components.WhiteBoard;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

// @Push
@Route("/main-view") 
public class MainView extends VerticalLayout { 

  @Autowired
  private WhiteBoard buffer;


  private TextArea board;
  private TextField status;
  private TextField clock;

  ScheduledExecutorService executorService = Executors
  .newSingleThreadScheduledExecutor();
  private Logger logger = LogManager.getLogger();


  public MainView() {
    // VerticalLayout todosList = new VerticalLayout(); 
    TextField taskField = new TextField(); 
    Button addButton = new Button("Add");
    this.board = new TextArea("WhiteBoard");
    this.status = new TextField("Status");
    this.clock = new TextField("Clock");

    addButton.addClickListener(click -> { 
      String line = taskField.getValue();
      Checkbox checkbox = new Checkbox(line);
      // todosList.add(checkbox);
      buffer.append(line);
      renderBoard();
    });
    addButton.addClickShortcut(Key.ENTER); 

    add( 
      new H1("Vaadin Todo"),
      // todosList,
      new HorizontalLayout(
        taskField,
        addButton,
        clock
      ),
      new VerticalLayout(status, board)
    );

    executorService.scheduleAtFixedRate(this::render, 0, 3, TimeUnit.SECONDS);
  }

  private void renderBoard() {
    Pair<String, String> head_tail = this.buffer.read();
    this.status.setValue(head_tail.getValue0());
    this.board.setValue(head_tail.getValue1());
  }

  private void renderClock() {
    String now = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    this.clock.setValue(now);
  }
  
  private void renderImpl(){
    renderBoard();
    renderClock();
  }

  private void render(){

    UI ui = null;

    try {
      
      ui = UI.getCurrent();
      
    } catch(Exception ex){
      this.logger.debug(ex);
      
    }


    if (ui == null)
    {
      try {
      
        ui = this.getUI().get();
        
      } catch(Exception ex1){
        this.logger.debug(ex1);
      }
    }

    if(ui != null){
      ui.access(() -> renderImpl());
    }

  }
}