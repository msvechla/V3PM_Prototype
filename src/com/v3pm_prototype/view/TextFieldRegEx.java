package com.v3pm_prototype.view;

import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class TextFieldRegEx extends TextField {
	//Property Setup
	private StringProperty type = new SimpleStringProperty();
	public final String getType(){return type.get();}
	public final void setType(String value){type.set(value);}
	public StringProperty typeProperty() {return type;}
	
	private InnerShadow errorEffect;
	
	public TextFieldRegEx(){
		super();
		errorEffect = new InnerShadow();
		errorEffect.setChoke(0.57);
		errorEffect.setWidth(18.04);
		errorEffect.setColor(Color.valueOf("#ff000040"));
		
	}
	
	@Override
	public void replaceText(int start, int end, String text) {
		String newText = getText().substring(0, start)+text+getText().substring(end);
        
		Pattern regEx = null; 
		
		switch(type.getValue()){
			case "absrel": 
				regEx = Pattern.compile("([*|\\-|+])[0-9]*\\.?[0-9]+");
				break;
		}
		
		//Change the TextField Style if RegEx is not met
		if (!regEx.matcher(newText).matches()) {
           this.setEffect(errorEffect);
           System.out.println("WRONG");
        }else{
        	this.setEffect(null);
        	System.out.println("RIGHT");
        }
		super.replaceText(start, end, text);
	}
		
	
}
